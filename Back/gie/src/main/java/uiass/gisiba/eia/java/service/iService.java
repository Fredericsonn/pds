package uiass.gisiba.eia.java.service;

import java.time.LocalDate;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

import javax.mail.MessagingException;

import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InsufficientQuantityInStock;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidFilterCriteriaMapFormatException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidOrderTypeException;
import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.NoContactsFoundInCountry;
import uiass.gisiba.eia.java.dao.exceptions.OperationNotModifiableException;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.PurchaseNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.SaleNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;
import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.inventory.Category;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Order;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.Status;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;
import uiass.gisiba.eia.java.entity.sales.Sale;
import uiass.gisiba.eia.java.entity.sales.SaleOrder;


public interface iService {

/////////////////////////////////////////////////////// ADDRESS ////////////////////////////////////////////////////////////////

    void addAddress(String country, String city, String zipCode, String neighborhood,
    
    int houseNumber) throws AddressNotFoundException,DuplicatedAddressException;

    Address getAddressById(int id) throws AddressNotFoundException;

    List<Address> getAllAddresses();

    void removeAddress(int id) throws AddressNotFoundException;

    int existingAddressChecker(Address addressToCheck) throws AddressNotFoundException;

    void updateAddress(int address_id, Map<String, Object> columns_new_values) throws AddressNotFoundException;


/////////////////////////////////////////////////////// CONTACT ////////////////////////////////////////////////////////////////

    void addContact(String fname, String lname, String phoneNum, String email, Address address) throws AddressNotFoundException, DuplicatedAddressException;

    void addContact(String entrepriseName, EntrepriseType type, String phoneNumber, String email, Address address) throws AddressNotFoundException, DuplicatedAddressException;

    void deleteContact(int id, String contactType) throws ContactNotFoundException, InvalidContactTypeException;

    Contact getContactById(int id, String contactType) throws ContactNotFoundException,InvalidContactTypeException;

    Contact getContactByName(String name, String contactType) throws ContactNotFoundException, InvalidContactTypeException;

    Contact getContactByAddressId(String contactType,int address_id) throws AddressNotFoundException;

    List<Contact> getAllContactsByCountry(String contactType, String country) throws InvalidContactTypeException, NoContactsFoundInCountry;

    List<Contact> getAllContactsByType(String contactType) throws InvalidContactTypeException;

    List<Contact> getAllContacts() throws InvalidContactTypeException;

    void updateContact(int id, Map<String,Object> columnsNewValues,String contactType) throws ContactNotFoundException,InvalidContactTypeException;

    void notifyContact(String email, String subject, String body) throws MessagingException,UnknownHostException;

/////////////////////////////////////////////////////// PRODUCT ////////////////////////////////////////////////////////////////

    void addProduct(Category categoryBrand, String model, String description);

    Product getProductById(String ref) throws ProductNotFoundException;

    List<Product> productSearchFilter(Map<String,String> columnsNewValues) throws ProductNotFoundException, CategoryNotFoundException;

    boolean checkForAssociatedPurchases(Product product);

    void deleteProduct(String ref) throws ProductNotFoundException, InventoryItemNotFoundException;

    List<Product> getAllProducts();

    void updateProduct(String ref, Map<String,Object> columnsNewValues) throws ProductNotFoundException, CategoryNotFoundException;

/////////////////////////////////////////////////////// Category ////////////////////////////////////////////////////////////////

    Category getCategoryById(int id) throws CategoryNotFoundException;

    Category getCategoryByNames(String categoryName, String brandName, String modelName) throws CategoryNotFoundException;
    
    List getAllCategories();

    List<String> getAllColumnNames(String column);

    List<String> getAllColumnNamesByCriteria(String column, Map<String,String> criteria);

    void addCategory(String categoryName, String brandName, String modelName);

    void updateCategory(int id, Map<String,Object> columnsNewValues) throws CategoryNotFoundException;

/////////////////////////////////////////////////////// Inventory Item ////////////////////////////////////////////////////////////////

    InventoryItem getInventoryItemById(int itemId) throws InventoryItemNotFoundException;

    InventoryItem getInventoryItemByProduct(String ref) throws InventoryItemNotFoundException, ProductNotFoundException;

    List<InventoryItem> getAllInventoryItems();

    List<InventoryItem> getFilteredItems(Map<String,String> columnsNewValues) throws InventoryItemNotFoundException, ProductNotFoundException;

    int getItemQuantity(int itemId) throws InventoryItemNotFoundException;

    void addInventoryItem(Product product, double unitPrice, int quantity, Date dateAdded);

    boolean canSell(int itemId, int quantity) throws InventoryItemNotFoundException;

    void deleteInventoryItem(int itemId) throws InventoryItemNotFoundException;

    void updateInventoryItem(int itemId, int quantity) throws InventoryItemNotFoundException;

    void updateInventoryItemUnitPrice(int itemId, double unitPrice) throws InventoryItemNotFoundException;

/////////////////////////////////////////////////////// Orders /////////////////////////////////////////////////////////////////////

    Order getOrderById(int orderId, String orderType) throws InvalidOrderTypeException, OrderNotFoundException;

    List<Order> getAllOrdersByType(String orderType);

    List<PurchaseOrder> getAllOrdersByPurchase(int purchaseId);

    List<Order> orderSearchFilter(String orderType, Map<String,String> criteria) throws InvalidOrderTypeException;

    void addPurchaseOrder(InventoryItem product, Time orderTime, int quantity, Purchase purchase);

    void addSaleOrder(InventoryItem product, Time orderTime, int quantity, Sale sale, double margin);

    void deleteOrder(int orderId, String orderType) throws InvalidOrderTypeException, OrderNotFoundException;

    void updateOrder(int orderId, int quantity, String orderType) throws InvalidOrderTypeException, OrderNotFoundException;

/////////////////////////////////////////////////////// Purchase /////////////////////////////////////////////////////////////////////

    Purchase getPurchaseById(int id) throws PurchaseNotFoundException;

    List<Purchase> getAllPurchases();

    List<Purchase> getAllPurchasesByContactType(String type) throws InvalidContactTypeException;

    List<Purchase> getAllPurchasesBySupplier(String name, String supplierType);

    List<Contact> getAllSuppliers(String supplierType) throws InvalidContactTypeException;

    List<Purchase> getAllPurchasesByStatus(Status status);

    List<Purchase> purchasesDatesFilter(Map<String,Date> datesCriteria) throws InvalidFilterCriteriaMapFormatException;

    List<Purchase> purchasesFilter(Map<String, Object> criteria) throws InvalidFilterCriteriaMapFormatException;

    List<PurchaseOrder> getPurchaseOrders(int id) throws PurchaseNotFoundException;

    void addPurchase(Purchase purchase);

    void deletePurchase(int id) throws PurchaseNotFoundException;

    void updatePurchaseOrders(int id, List<PurchaseOrder> newOrders) throws PurchaseNotFoundException,
    
            OperationNotModifiableException;

    void removePurchaseOrder(int purchaseId, int orderId) throws PurchaseNotFoundException, OrderNotFoundException;

    void updatePurchaseStatus(int id, Status status) throws PurchaseNotFoundException;

/////////////////////////////////////////////////////// Sale /////////////////////////////////////////////////////////////////////

    Sale getSaleById(int id) throws SaleNotFoundException;

    void addSale(Sale sale) throws InsufficientQuantityInStock;

    void deleteSale(int id) throws SaleNotFoundException;

    List<Sale> getAllSales();

    List<Sale> getAllSalesByContactType(String type) throws InvalidContactTypeException;

    public List<Sale> getAllSalesByCustomer(String name, String customerType);

    List<Contact> getAllCustomers(String customerType) throws InvalidContactTypeException;

    List<Sale> getAllSalesByStatus(Status status);

    List<Sale> salesDatesFilter(Map<String,Date> datesCriteria) throws InvalidFilterCriteriaMapFormatException;

    List<Sale> salesFilter(Map<String, Object> criteria) throws InvalidFilterCriteriaMapFormatException;

    List<SaleOrder> getSaleOrders(int id) throws SaleNotFoundException;

    void updateSaleOrders(int id, List<SaleOrder> newOrders) throws SaleNotFoundException,
    
            OperationNotModifiableException;

    void updateSaleStatus(int id, Status status) throws SaleNotFoundException;
}
