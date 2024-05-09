package uiass.gisiba.eia.java.dao.purchase;

import java.sql.Date;
import java.util.*;

import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.OperationNotModifiableException;
import uiass.gisiba.eia.java.dao.exceptions.PurchaseNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.inventory.Status;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;

public interface iPurchaseDao {

    Purchase getPurchaseById(int id) throws PurchaseNotFoundException;

    List<Purchase> getAllPurchases();

    List<Purchase> getAllPurchasesByContactType(String type) throws InvalidContactTypeException;

    List<Purchase> getAllPurchasesBySupplier(Person supplier);

    List<Purchase> getAllPurchasesBySupplier(Enterprise supplier);

    List<Contact> getAllSuppliers(String supplierType) throws InvalidContactTypeException;

    List<Purchase> getAllPurchasesByStatus(Status status);

    List<Purchase> getAllPurchasesBetweenDates(Date startDate, Date endDate);

    List<PurchaseOrder> getPurchaseOrders(int id) throws PurchaseNotFoundException;

    void addPurchase(Purchase purchase);

    void deletePurchase(int id) throws PurchaseNotFoundException;

    void updatePurchaseOrders(int id, List<PurchaseOrder> newOrders) throws PurchaseNotFoundException,
    
            OperationNotModifiableException;

    void updatePurchaseStatus(int id, Status status) throws PurchaseNotFoundException;
}
