package uiass.gisiba.eia.java.dao.sale;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import uiass.gisiba.eia.java.dao.HQLQueryManager;
import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.dao.exceptions.InsufficientQuantityInStock;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidFilterCriteriaMapFormatException;
import uiass.gisiba.eia.java.dao.exceptions.OperationNotModifiableException;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.SaleNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Status;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.sales.Sale;
import uiass.gisiba.eia.java.entity.sales.SaleOrder;

public class SaleDao implements iSaleDao {

    private EntityManager em;
	private EntityTransaction tr;
    
    public SaleDao() {
        this.em= HibernateUtility.getEntityManger();
	    tr=em.getTransaction();
    }

    @Override
    public Sale getSaleById(int id) throws SaleNotFoundException {

        Sale sale = em.find(Sale.class, id);

        if (sale != null) return sale;

        throw new SaleNotFoundException(id);
        
    }

    public boolean saleValidator(Sale sale) throws InsufficientQuantityInStock {

        List<SaleOrder> orders = sale.getOrders();

        for (SaleOrder order : orders) {

            InventoryItem item = order.getItem();

            int quantityInStock = item.getQuantity();

            int quantityOrdered = order.getQuantity();

            if (quantityInStock < quantityOrdered) throw new InsufficientQuantityInStock(quantityInStock);

            else item.setQuantity(quantityInStock - quantityOrdered);
        }

        return true;
    }
    
    @Override
    public void addSale(Sale sale) throws InsufficientQuantityInStock {

        if (saleValidator(sale)) {

            tr.begin();

            em.persist(sale);
    
            tr.commit();
            
        }
    }

    @Override
    public void deleteSale(int id) throws SaleNotFoundException {

        Sale sale = getSaleById(id);

        tr.begin();

        em.remove(sale);

        tr.commit();
    }

    @Override
    public List<Sale> getAllSales() {

        Query query =em.createQuery("from Sale");

        return query.getResultList();
    }

    @Override
    public List<Sale> getAllSalesByContactType(String type) throws InvalidContactTypeException {

        if (HQLQueryManager.operationContactTypeValidator(type)) {
            
            String hql = "from Sale where DTYPE = :type";

            Query query = em.createQuery(hql);

            query.setParameter("type", type);

            return query.getResultList();

        }

        throw new InvalidContactTypeException(type);
    }

    @Override
    public List<Sale> getAllSalesByCustomer(String name, String customerType) {

        String hql = HQLQueryManager.selectAllOperationsByContactNameHQLQueryGenerator("Sale",customerType);

        Query query = em.createQuery(hql);

        query.setParameter("type", customerType);

        query.setParameter("fullName", name);

        return query.getResultList();
    }

    @Override
    public List<Contact> getAllCustomers(String customerType) throws InvalidContactTypeException {

        if (HQLQueryManager.contactTypeChecker(customerType)) {

            String hql = HQLQueryManager.selectAllContactsHQLQueryGenerator(customerType, "Sale");

            Query query = em.createQuery(hql);

            return query.getResultList();
        }

        throw new InvalidContactTypeException(customerType);
    }

    @Override
    public List<Sale> getAllSalesByStatus(Status status) {

        String hql = "from Sale where status = :status";

        Query query = em.createQuery(hql);

        query.setParameter("status", status);

        return query.getResultList();
    }

        public List<Sale> getAllSalesBetweenDates(Date startDate, Date endDate) {

        String hql = HQLQueryManager.selectOperationsBetweenDatesHQLQueryGenerator("Sale");

        Query query = em.createQuery(hql);

        query.setParameter("startDate", startDate);

        query.setParameter("endDate", endDate);

        return query.getResultList();
    }

    public List<Sale> getAllSalesBeforeDate(Date date) {

        String hql = "from Sale where saleDate <= :date";

        Query query = em.createQuery(hql);

        query.setParameter("date", date);

        return query.getResultList();
    }

    public List<Sale> getAllSalesAfterDate(Date date) {

        String hql = "from Sale where saleDate >= :date";

        Query query = em.createQuery(hql);

        query.setParameter("date", date);

        return query.getResultList();
    }

    @Override
    public List<Sale> salesDatesFilter(Map<String, Date> datesCriteria) throws InvalidFilterCriteriaMapFormatException {

        if (!datesCriteria.isEmpty()) {

            List<String> criterionsList = new ArrayList<>(datesCriteria.keySet());

            if (criterionsList.size() == 1) {

                if (criterionsList.contains("beforeDate")) {

                    Date beforeDate = datesCriteria.get("beforeDate");
    
                    return getAllSalesBeforeDate(beforeDate);
                }
    
                else if (criterionsList.contains("afterDate")) {
    
                    Date afterDate = datesCriteria.get("afterDate");
    
                    return getAllSalesAfterDate(afterDate);
    
                }
            }

            else if (criterionsList.size() == 2) {

                if (criterionsList.contains("startDate") && criterionsList.contains("endDate")) {

                    Date startDate = datesCriteria.get("startDate");

                    Date endDate = datesCriteria.get("endDate");
    
                    return getAllSalesBetweenDates(startDate, endDate);
                }

            }
        }

        throw new InvalidFilterCriteriaMapFormatException();
    }

    @Override
    public List<Sale> salesFilter(Map<String, Object> criteria) throws InvalidFilterCriteriaMapFormatException {

        List<Sale> sales = null;

        if (!criteria.isEmpty()) {

            sales = getAllSales();

            for (String criterion : criteria.keySet()) {

                if (criterion.equals("customer")) {

                    Map<String, String> customerCriteria = (Map<String, String>) criteria.get("customer");

                    String customerName = customerCriteria.get("customerName");

                    String customerType = customerCriteria.get("customerType");

                    List<Sale> sales_filtered_by_customer = getAllSalesByCustomer(customerName, customerType);

                    sales.retainAll(sales_filtered_by_customer);
                }

                else if (criterion.equals("status")) {

                    String status = (String) criteria.get("status");

                    List<Sale> sales_filtered_by_status = getAllSalesByStatus(Status.valueOf(status));

                    sales.retainAll(sales_filtered_by_status);
                }

                else if (criterion.equals("date")) {

                    Map<String, Date> datesCriteria = (Map<String, Date>) criteria.get("date");

                    List<Sale> sales_filtered_by_dates = salesDatesFilter(datesCriteria);
                    
                    sales.retainAll(sales_filtered_by_dates);
                }

                else throw new InvalidFilterCriteriaMapFormatException();
         
            }

        }

        return sales;
    }

    @Override
    public List<SaleOrder> getSaleOrders(int id) throws SaleNotFoundException {

        Sale sale = getSaleById(id);

        return sale.getOrders();
    }

    @Override
    public void updateSaleOrders(int id, List<SaleOrder> newOrders) throws SaleNotFoundException,
    
            OperationNotModifiableException {

        Sale sale = getSaleById(id);

        if (sale.getStatus().equals(Status.PENDING)) {

            sale.setOrders(newOrders);

            sale.setSaleDate(Date.valueOf(LocalDate.now()));
    
            tr.begin();
    
            em.persist(sale);
    
            tr.commit();
        }

        throw new OperationNotModifiableException("purchase", id);
    }

    @Override
    public void updateSaleStatus(int id, Status status) throws SaleNotFoundException {

        Sale sale = getSaleById(id);

        sale.setStatus(status);

        tr.begin();

        em.persist(sale);

        tr.commit();
    }


}
