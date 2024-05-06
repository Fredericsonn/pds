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
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidOrderTypeException;
import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.NoContactsFoundInCountry;
import uiass.gisiba.eia.java.dao.exceptions.OperationNotModifiableException;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.PurchaseNotFoundException;
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

    List<Product> productSearchFilter(Map<String,Object> columnsNewValues) throws ProductNotFoundException, CategoryNotFoundException;

    void deleteProduct(String ref) throws ProductNotFoundException, InventoryItemNotFoundException;

    List<Product> getAllProducts();

    void updateProduct(String ref, Map<String,Object> columnsNewValues) throws ProductNotFoundException, CategoryNotFoundException;

/////////////////////////////////////////////////////// Category ////////////////////////////////////////////////////////////////

    Category getCategoryById(int id) throws CategoryNotFoundException;

    Category getCategoryByNames(String categoryName, String brandName, String modelName) throws CategoryNotFoundException;
    
    List getAllCategories();

    List<String> getAllColumnNames(String column);

    List<String> getAllColumnByFilterColumn(String column, String filterColumn, String value);

    void addCategory(String categoryName, String brandName, String modelName);

    void updateCategory(int id, Map<String,Object> columnsNewValues) throws CategoryNotFoundException;

/////////////////////////////////////////////////////// Inventory Item ////////////////////////////////////////////////////////////////

    InventoryItem getInventoryItemById(int itemId) throws InventoryItemNotFoundException;

    InventoryItem getInventoryItemByProduct(String ref) throws InventoryItemNotFoundException, ProductNotFoundException;

    List<InventoryItem> getAllInventoryItems();

    List<InventoryItem> getFilteredItems(Map<String,Object> columnsNewValues) throws InventoryItemNotFoundException, ProductNotFoundException;

    int getItemQuantity(int itemId) throws InventoryItemNotFoundException;

    void addInventoryItem(Product product, double unitPrice, int quantity, Date dateAdded);

    boolean canSell(int itemId, int quantity) throws InventoryItemNotFoundException;

    void deleteInventoryItem(int itemId) throws InventoryItemNotFoundException;

    void updateInventoryItem(int itemId, int quantity) throws InventoryItemNotFoundException;

/////////////////////////////////////////////////////// Orders /////////////////////////////////////////////////////////////////////

    Order getOrderById(int orderId, String orderType) throws InvalidOrderTypeException, OrderNotFoundException;

    List<Order> getAllOrdersByType(String orderType);

    List<Order> getAllOrdersByStatus(String orderType, Status status) throws InvalidOrderTypeException;

    List<Order> getAllOrdersBetweenDates(String orderType, Date startDate, Date endDate) throws InvalidOrderTypeException;

    void addPurchaseOrder(Product product, int quantity, Time orderTime, Purchase purchase);

    void addSaleOrder(Product product, int quantity, Time orderTime, Sale sale);

    void deleteOrder(int orderId, String orderType) throws InvalidOrderTypeException, OrderNotFoundException;

    void updateOrder(int orderId, int quantity, String orderType) throws InvalidOrderTypeException, OrderNotFoundException;

/////////////////////////////////////////////////////// Purchase /////////////////////////////////////////////////////////////////////

    Purchase getPurchaseById(int id) throws PurchaseNotFoundException;

    List<Purchase> getAllPurchases();

    List<Purchase> getAllPurchasesByContactType(String type) throws InvalidContactTypeException;

    List<Purchase> getAllPurchasesBySupplier(Person supplier);

    List<Purchase> getAllPurchasesBySupplier(Enterprise supplier);

    List<Purchase> getAllPurchasesByStatus(Status status);

    List<Purchase> getAllPurchasesBetweenDates(Date startDate, Date endDate);

    void addPurchase(List<PurchaseOrder> orders, Date purchaseDate, double total, Person supplier, Status status);

    void addPurchase(List<PurchaseOrder> orders, Date purchaseDate, double total, Enterprise supplier, Status status);

    void deletePurchase(int id) throws PurchaseNotFoundException;

    void updatePurchaseOrders(int id, List<PurchaseOrder> newOrders) throws PurchaseNotFoundException,
    
            OperationNotModifiableException;

    void updatePurchaseStatus(int id, Status status) throws PurchaseNotFoundException;
}
