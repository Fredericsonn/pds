package uiass.gisiba.eia.java.dao.purchase;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import uiass.gisiba.eia.java.dao.HQLQueryManager;
import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidFilterCriteriaMapFormatException;
import uiass.gisiba.eia.java.dao.exceptions.OperationNotModifiableException;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.PurchaseNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Status;
import uiass.gisiba.eia.java.entity.purchases.EnterprisePurchase;
import uiass.gisiba.eia.java.entity.purchases.PersonPurchase;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;

public class PurchaseDao implements iPurchaseDao {

    private EntityManager em;
	private EntityTransaction tr;
    
    public PurchaseDao() {
        this.em= HibernateUtility.getEntityManger();
	    tr=em.getTransaction();
    }
    
    @Override
    public Purchase getPurchaseById(int id) throws PurchaseNotFoundException {

        Purchase purchase = em.find(Purchase.class, id);

        if (purchase != null) return purchase;

        throw new PurchaseNotFoundException(id);

    }

    @Override
    public List<Purchase> getAllPurchases() {

        Query query = em.createQuery("from Purchase");

        return query.getResultList();

    }

    @Override
    public List<Purchase> getAllPurchasesByContactType(String type) throws InvalidContactTypeException {

        if (HQLQueryManager.operationContactTypeValidator(type)) {
            
            String hql = "from Purchase where DTYPE = :type";

            Query query = em.createQuery(hql);

            query.setParameter("DTYPE", type);

            return query.getResultList();

        }

        throw new InvalidContactTypeException(type);
    }

    @Override
    public List<Purchase> getAllPurchasesBySupplier(String name, String supplierType) {

        String hql = HQLQueryManager.selectAllOperationsByContactNameHQLQueryGenerator("Purchase",supplierType);

        Query query = em.createQuery(hql);

        query.setParameter("type", supplierType);

        query.setParameter("fullName", name);

        return query.getResultList();
    }

    @Override
    public List<Contact> getAllSuppliers(String supplierType) throws InvalidContactTypeException {

        if (HQLQueryManager.contactTypeChecker(supplierType)) {

            String hql = HQLQueryManager.selectAllContactsHQLQueryGenerator(supplierType, "Purchase");

            Query query = em.createQuery(hql);

            return query.getResultList();
        }

        throw new InvalidContactTypeException(supplierType);
    }

    @Override
    public List<Purchase> getAllPurchasesByStatus(Status status) {

        String hql = "from Purchase where status = :status";

        Query query = em.createQuery(hql);

        query.setParameter("status", status);

        return query.getResultList();
    }

    public List<Purchase> getAllPurchasesBetweenDates(Date startDate, Date endDate) {

        String hql = HQLQueryManager.selectOperationsBetweenDatesHQLQueryGenerator("Purchase");

        Query query = em.createQuery(hql);

        query.setParameter("startDate", startDate);

        query.setParameter("endDate", endDate);

        System.out.println(hql);

        return query.getResultList();
    }

    public List<Purchase> getAllPurchasesBeforeDate(Date date) {

        String hql = "from Purchase where purchaseDate <= :date";

        Query query = em.createQuery(hql);

        query.setParameter("date", date);

        return query.getResultList();
    }

    public List<Purchase> getAllPurchasesAfterDate(Date date) {

        String hql = "from Purchase where purchaseDate >= :date";

        Query query = em.createQuery(hql);

        query.setParameter("date", date);

        return query.getResultList();
    }
    
    @Override
    public List<Purchase> purchasesDatesFilter(Map<String,Date> datesCriteria) throws InvalidFilterCriteriaMapFormatException {

        if (!datesCriteria.isEmpty()) {

            List<String> criterionsList = new ArrayList<>(datesCriteria.keySet());

            if (criterionsList.size() == 1) {

                if (criterionsList.contains("beforeDate")) {

                    Date beforeDate = datesCriteria.get("beforeDate");
    
                    return getAllPurchasesBeforeDate(beforeDate);
                }
    
                else if (criterionsList.contains("afterDate")) {
    
                    Date afterDate = datesCriteria.get("afterDate");
    
                    return getAllPurchasesAfterDate(afterDate);
    
                }
            }

            else if (criterionsList.size() == 2) {

                if (criterionsList.contains("startDate") && criterionsList.contains("endDate")) {

                    Date startDate = datesCriteria.get("startDate");

                    Date endDate = datesCriteria.get("endDate");
    
                    return getAllPurchasesBetweenDates(startDate, endDate);
                }

            }
        }

        throw new InvalidFilterCriteriaMapFormatException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Purchase> purchasesFilter(Map<String, Object> criteria) throws InvalidFilterCriteriaMapFormatException {

        List<Purchase> purchases = null;

        if (!criteria.isEmpty()) {

            purchases = getAllPurchases();

            for (String criterion : criteria.keySet()) {

                if (criterion.equals("supplier")) {

                    Map<String, String> supplierCriteria = (Map<String, String>) criteria.get("supplier");

                    String supplierName = supplierCriteria.get("supplierName");

                    String supplierType = supplierCriteria.get("supplierType");

                    List<Purchase> purchases_filtered_by_supplier = getAllPurchasesBySupplier(supplierName, supplierType);

                    purchases.retainAll(purchases_filtered_by_supplier);
                }

                else if (criterion.equals("status")) {

                    String status = (String) criteria.get("status");

                    List<Purchase> purchases_filtered_by_status = getAllPurchasesByStatus(Status.valueOf(status));

                    purchases.retainAll(purchases_filtered_by_status);
                }

                else if (criterion.equals("date")) {

                    Map<String, Date> datesCriteria = (Map<String, Date>) criteria.get("date");

                    List<Purchase> purchases_filtered_by_dates = purchasesDatesFilter(datesCriteria);
                    
                    purchases.retainAll(purchases_filtered_by_dates);
                }

                else throw new InvalidFilterCriteriaMapFormatException();
         
            }

        }

        return purchases;
    }


    @Override
    public List<PurchaseOrder> getPurchaseOrders(int id) throws PurchaseNotFoundException {

        Purchase purchase = getPurchaseById(id);

        return purchase.getOrders();
    }

    public void updateItemQuantity(PurchaseOrder order) {

        InventoryItem item = order.getItem();

        int quantityInStock = item.getQuantity();

        int quantityOrdered = order.getQuantity();

        int newQuantity = quantityInStock + quantityOrdered;

        item.setQuantity(newQuantity);

        tr.begin();

        em.persist(item);

        tr.commit();


    }

    @Override
    public void addPurchase(Purchase purchase) {

        List<PurchaseOrder> orders = purchase.getOrders();

        orders.forEach(order -> {

            updateItemQuantity(order);

            order.setPurchase(purchase);

        });

        purchase.setOrders(orders);

        tr.begin();

        em.persist(purchase);

        tr.commit();
    }

    @Override
    public void deletePurchase(int id) throws PurchaseNotFoundException {

        Purchase purchase = getPurchaseById(id);

        tr.begin();

        em.remove(purchase);

        tr.commit();
    }

    @Override
    public void updatePurchaseOrders(int id, List<PurchaseOrder> newOrders) throws PurchaseNotFoundException,
    
            OperationNotModifiableException {

        Purchase purchase = getPurchaseById(id);

        if (purchase.getStatus().equals(Status.PENDING)) {

            purchase.setOrders(newOrders);

            purchase.setPurchaseDate(Date.valueOf(LocalDate.now()));
    
            tr.begin();
    
            em.persist(purchase);
    
            tr.commit();
        }

        throw new OperationNotModifiableException("purchase", id);
    }

    @Override
    public void removePurchaseOrder(int purchaseId, int orderId) throws PurchaseNotFoundException, OrderNotFoundException {

        PurchaseOrder order = em.find(PurchaseOrder.class, orderId);

        tr.begin();

        em.remove(order);

        em.flush();

        tr.commit();

        if (order != null) {

            Purchase purchase = em.find(Purchase.class, purchaseId);

            if (purchase != null) {

                List<PurchaseOrder> orders = purchase.getOrders();

                if (orders.contains(order)) {

                    purchase.setOrders(orders);

                    addPurchase(purchase);
                }
            }

            else throw new PurchaseNotFoundException(purchaseId);

        }

        else throw new OrderNotFoundException(orderId);
    }

    @Override
    public void updatePurchaseStatus(int id, Status status) throws PurchaseNotFoundException {

        Purchase purchase = getPurchaseById(id);

        purchase.setStatus(status);

        tr.begin();

        em.persist(purchase);

        tr.commit();
    }









}
