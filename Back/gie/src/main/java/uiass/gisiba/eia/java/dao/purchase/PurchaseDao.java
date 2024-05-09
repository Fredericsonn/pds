package uiass.gisiba.eia.java.dao.purchase;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import uiass.gisiba.eia.java.dao.HQLQueryManager;
import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.OperationNotModifiableException;
import uiass.gisiba.eia.java.dao.exceptions.PurchaseNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.crm.Person;
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
    public List<Purchase> getAllPurchasesBySupplier(Person supplier) {

        String hql = "from PersonPurchase p where p.supplier = :supplier";

        Query query = em.createQuery(hql);

        query.setParameter("supplier", supplier);

        return query.getResultList();
    }

    @Override
    public List<Purchase> getAllPurchasesBySupplier(Enterprise supplier) {

        String hql = "from EnterprisePurchase p where p.supplier = :supplier";

        Query query = em.createQuery(hql);

        query.setParameter("supplier", supplier);

        return query.getResultList();
    }

    @Override
    public List<Contact> getAllSuppliers(String supplierType) throws InvalidContactTypeException {

        if (HQLQueryManager.contactTypeChecker(supplierType)) {

            String hql = HQLQueryManager.selectAllSuppliersHQLQueryGenerator(supplierType);

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

    @Override
    public List<Purchase> getAllPurchasesBetweenDates(Date startDate, Date endDate) {

        String hql = HQLQueryManager.selectOperationsBetweenDatesHQLQueryGenerator("Purchase");

        Query query = em.createQuery(hql);

        query.setParameter("startDate", startDate);

        query.setParameter("endDate", endDate);

        return query.getResultList();
    }

    
    @Override
    public List<PurchaseOrder> getPurchaseOrders(int id) throws PurchaseNotFoundException {

        Purchase purchase = getPurchaseById(id);

        return purchase.getOrders();
    }


    @Override
    public void addPurchase(Purchase purchase) {

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
    public void updatePurchaseStatus(int id, Status status) throws PurchaseNotFoundException {

        Purchase purchase = getPurchaseById(id);

        purchase.setStatus(status);

        tr.begin();

        em.persist(purchase);

        tr.commit();
    }






}
