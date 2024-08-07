package uiass.gisiba.eia.java.service;

import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import uiass.gisiba.eia.java.dao.EmailSender;
import uiass.gisiba.eia.java.dao.crm.AddressDao;
import uiass.gisiba.eia.java.dao.crm.ContactDao;
import uiass.gisiba.eia.java.dao.crm.iAddressDao;
import uiass.gisiba.eia.java.dao.crm.iContactDao;
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
import uiass.gisiba.eia.java.dao.inventory.CategoryDao;
import uiass.gisiba.eia.java.dao.inventory.InventoryItemDao;
import uiass.gisiba.eia.java.dao.inventory.OrderDao;
import uiass.gisiba.eia.java.dao.inventory.ProductDao;
import uiass.gisiba.eia.java.dao.inventory.iCategoryDao;
import uiass.gisiba.eia.java.dao.inventory.iInventoryItemDao;
import uiass.gisiba.eia.java.dao.inventory.iOrderDao;
import uiass.gisiba.eia.java.dao.inventory.iProductDao;
import uiass.gisiba.eia.java.dao.purchase.PurchaseDao;
import uiass.gisiba.eia.java.dao.purchase.iPurchaseDao;
import uiass.gisiba.eia.java.dao.sale.iSaleDao;
import uiass.gisiba.eia.java.dao.sale.SaleDao;
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


public class Service implements iService {

    private iContactDao cdao  = new ContactDao();
    private iAddressDao adao = new AddressDao();
    private iProductDao pdao = new ProductDao();
    private iCategoryDao catdao = new CategoryDao();
    private iInventoryItemDao idao = new InventoryItemDao();
    private iOrderDao odao = new OrderDao();
    private iPurchaseDao psdao = new PurchaseDao();
    private iSaleDao sdao = new SaleDao();
    private EmailSender es = new EmailSender();



/////////////////////////////////////////////////////// ADDRESS ////////////////////////////////////////////////////////////////

    @Override
    public void addAddress(String country, String city, String zipCode, String neighborhood,

            int houseNumber) throws AddressNotFoundException, DuplicatedAddressException {

        adao.addAddress(country, city, zipCode, neighborhood, houseNumber);
    }

    @Override
    public Address getAddressById(int id) throws AddressNotFoundException {

        return adao.getAddressById(id);
    }

    @Override
    public List<Address> getAllAddresses() {

        return adao.getAllAddresses();
    }

    @Override
    public void removeAddress(int id) throws AddressNotFoundException {

        adao.removeAddress(id);
    }

    @Override
    public int existingAddressChecker(Address addressToCheck) throws AddressNotFoundException {

        return adao.existingAddressChecker(addressToCheck);
    }

    @Override
    public void updateAddress(int address_id, Map<String, Object> columns_new_values) throws AddressNotFoundException {

        adao.updateAddress(address_id, columns_new_values);
    }

/////////////////////////////////////////////////////// CONTACT ////////////////////////////////////////////////////////////////

    @Override
    public void addContact(String fname, String lname, String phoneNum, String email, Address address)

            throws AddressNotFoundException, DuplicatedAddressException {
        
                cdao.addContact(fname, lname, phoneNum, email, address);
    }

    @Override
    public void addContact(String entrepriseName, EntrepriseType type, String phoneNumber, String email,

            Address address) throws AddressNotFoundException, DuplicatedAddressException {
      
        cdao.addContact(entrepriseName, type, phoneNumber, email, address);
    }

    @Override
    public void deleteContact(int id, String contactType) throws ContactNotFoundException, InvalidContactTypeException {

        cdao.deleteContact(id, contactType);
    }

    @Override
    public Contact getContactById(int id, String contactType) throws ContactNotFoundException, InvalidContactTypeException {

        return cdao.getContactById(id, contactType);
    }

    @Override
    public Contact getContactByName(String name, String contactType) throws ContactNotFoundException, InvalidContactTypeException {

        return cdao.getContactByName(name, contactType);
    }

    @Override
    public Contact getContactByAddressId(String contactType, int address_id) throws AddressNotFoundException {

        return cdao.getContactByAddressId(contactType,address_id);
    }

    @Override
    public List<Contact> getAllContactsByCountry(String contactType, String country)
    
            throws InvalidContactTypeException, NoContactsFoundInCountry {

        return cdao.getAllContactsByCountry(contactType, country);
    }

    @Override
    public List<Contact> getAllContactsByType(String contactType) throws InvalidContactTypeException {

        return cdao.getAllContactsByType(contactType);
    }

    @Override
    public List<Contact> getAllContacts() throws InvalidContactTypeException {

        return cdao.getAllContacts();
    }

    @Override
    public void updateContact(int id, Map<String, Object> columnsNewValues, String contactType)

            throws ContactNotFoundException, InvalidContactTypeException {

        cdao.updateContact(id, columnsNewValues, contactType);
    }

    @Override
    public void notifyContact(String email, String subject, String body) throws MessagingException,UnknownHostException {

        es.sendEmail(email, subject, body);
    }

/////////////////////////////////////////////////////// PRODUCT ////////////////////////////////////////////////////////////////

    @Override
    public void addProduct(Category categoryBrand, String model, String description) {

        pdao.addProduct(categoryBrand,model, description);
    }

    @Override
    public Product getProductById(String ref) throws ProductNotFoundException {

        return pdao.getProductById(ref);
    }

    @Override
    public List<Product> productSearchFilter(Map<String, String> columnsNewValues)
            throws ProductNotFoundException, CategoryNotFoundException {

            return pdao.productSearchFilter(columnsNewValues);
    }

    @Override
    public boolean checkForAssociatedPurchases(Product product) {

        return pdao.checkForAssociatedPurchases(product);
    }

    @Override
    public void deleteProduct(String ref) throws ProductNotFoundException, InventoryItemNotFoundException {

        pdao.deleteProduct(ref);
    }

    @Override
    public List<Product> getAllProducts() {

        return pdao.getAllProducts();
    }

    @Override
    public void updateProduct(String ref, Map<String, Object> columnsNewValues) throws ProductNotFoundException, CategoryNotFoundException {

        pdao.updateProduct(ref, columnsNewValues);
    }
/////////////////////////////////////////////////////// Categroy ///////////////////////////////////////////////////////////////////

    @Override
    public Category getCategoryById(int id) throws CategoryNotFoundException {

        return catdao.getCategoryById(id);
    }

    @Override
    public List<String> getAllColumnNames(String column) {

        return catdao.getAllColumnNames(column);
    }

    @Override
    public List<String> getAllColumnNamesByCriteria(String column, Map<String, String> criteria) {

        return catdao.getAllColumnNamesByCriteria(column, criteria);
    }


    @Override
    public Category getCategoryByNames(String categoryName, String brandName, String modelName) throws CategoryNotFoundException {

        return catdao.getCategoryByNames(categoryName, brandName, modelName);
    }

    @Override
    public List getAllCategories() {

        return catdao.getAllCategories();
    }

    @Override
    public void addCategory(String categoryName, String brandName, String modelName) {

        catdao.addCategory(categoryName, brandName, modelName);
    }

    @Override
    public void updateCategory(int id, Map<String, Object> columnsNewValues) throws CategoryNotFoundException {

        catdao.updateCategory(id, columnsNewValues);
    }

/////////////////////////////////////////////////////// Inventory Item ////////////////////////////////////////////////////////////////

    @Override
    public InventoryItem getInventoryItemById(int itemId) throws InventoryItemNotFoundException {

        return idao.getInventoryItemById(itemId);
    }

    @Override
    public InventoryItem getInventoryItemByProduct(String ref) throws InventoryItemNotFoundException, ProductNotFoundException {

        return idao.getInventoryItemByProduct(ref);
    }

    @Override
    public List<InventoryItem> getAllInventoryItems() {

        return idao.getAllInventoryItems();
    }

    @Override
    public List<InventoryItem> getFilteredItems(Map<String, String> columnsNewValues)
    
            throws InventoryItemNotFoundException, ProductNotFoundException {

                return idao.getFilteredItems(columnsNewValues);
    }


    @Override
    public int getItemQuantity(int itemId) throws InventoryItemNotFoundException {
 
        return idao.getItemQuantity(itemId);
    }

    @Override
    public void addInventoryItem(Product product, double unitPrice, int quantity, Date dateAdded) {

        idao.addInventoryItem(product, unitPrice, quantity, dateAdded);
    }

    @Override
    public boolean canSell(int itemId, int quantity) throws InventoryItemNotFoundException {

        return idao.canSell(itemId, quantity);
    }

    @Override
    public void deleteInventoryItem(int itemId) throws InventoryItemNotFoundException {

        idao.deleteInventoryItem(itemId);
    }

    @Override
    public void updateInventoryItem(int itemId, int quantity) throws InventoryItemNotFoundException {

        idao.updateInventoryItem(itemId, quantity);
    }

    @Override
    public void updateInventoryItemUnitPrice(int itemId, double unitPrice) throws InventoryItemNotFoundException {

        idao.updateInventoryItemUnitPrice(itemId, unitPrice);
    }

/////////////////////////////////////////////////////// Orders ////////////////////////////////////////////////////////////////

    @Override
    public Order getOrderById(int orderId, String orderType) throws InvalidOrderTypeException, OrderNotFoundException {

        return odao.getOrderById(orderId, orderType);
    }

    @Override
    public List<Order> getAllOrdersByType(String orderType) {

        return odao.getAllOrdersByType(orderType);
    }

    @Override
    public List<PurchaseOrder> getAllOrdersByPurchase(int purchaseId) {

        return odao.getAllOrdersByPurchase(purchaseId);
    }

    @Override
    public List<Order> orderSearchFilter(String orderType, Map<String, String> criteria) throws InvalidOrderTypeException {

        return odao.orderSearchFilter(orderType, criteria);
    }

    @Override
    public void addPurchaseOrder(InventoryItem product, Time orderTime, int quantity, Purchase purchase) {

        odao.addPurchaseOrder(product, orderTime, quantity, purchase);
    }

    @Override
    public void addSaleOrder(InventoryItem product, Time orderTime, int quantity, Sale sale, double margin) {

        odao.addSaleOrder(product, orderTime, quantity, sale, margin);
    }

    @Override
    public void deleteOrder(int orderId, String orderType) throws InvalidOrderTypeException, OrderNotFoundException {

        odao.deleteOrder(orderId, orderType);
    }

    @Override
    public void updateOrder(int orderId, int quantity, String orderType)

            throws InvalidOrderTypeException, OrderNotFoundException {

        odao.updateOrder(orderId, quantity, orderType);
                
    }

    @Override
    public void updateSaleOrder(int orderId, Map<String, Object> newValues)

            throws InvalidOrderTypeException, OrderNotFoundException {

        odao.updateSaleOrder(orderId, newValues);
    }

/////////////////////////////////////////////////////// Purchase /////////////////////////////////////////////////////////////////////

    @Override
    public Purchase getPurchaseById(int id) throws PurchaseNotFoundException {

        return psdao.getPurchaseById(id);
    }

    @Override
    public List<Purchase> getAllPurchases() {

        return psdao.getAllPurchases();
    }

    @Override
    public List<Purchase> getAllPurchasesByContactType(String type) throws InvalidContactTypeException {

        return psdao.getAllPurchasesByContactType(type);
    }

    @Override
    public List<Purchase> getAllPurchasesBySupplier(String name, String supplierType) {

        return psdao.getAllPurchasesBySupplier(name, supplierType);
    }

    @Override
    public List<Contact> getAllSuppliers(String supplierType) throws InvalidContactTypeException {

        return psdao.getAllSuppliers(supplierType);
    }

    @Override
    public List<Purchase> getAllPurchasesByStatus(Status status) {

        return psdao.getAllPurchasesByStatus(status);
    }

    @Override
    public List<Purchase> purchasesDatesFilter(Map<String, Date> datesCriteria) throws InvalidFilterCriteriaMapFormatException {

        return psdao.purchasesDatesFilter(datesCriteria);
    }

    @Override
    public List<Purchase> purchasesFilter(Map<String, Object> criteria) throws InvalidFilterCriteriaMapFormatException {

        return psdao.purchasesFilter(criteria);
    }


    @Override
    public List<PurchaseOrder> getPurchaseOrders(int id) throws PurchaseNotFoundException {

        return psdao.getPurchaseOrders(id);
    }

    @Override
    public void addPurchase(Purchase purchase) {

        psdao.addPurchase(purchase);
    }

    @Override
    public void deletePurchase(int id) throws PurchaseNotFoundException {

        psdao.deletePurchase(id);
    }

    @Override
    public void updatePurchaseOrders(int id, List<PurchaseOrder> newOrders) throws PurchaseNotFoundException,
    
            OperationNotModifiableException {

        psdao.updatePurchaseOrders(id, newOrders);
    }

    @Override
    public void removePurchaseOrder(int purchaseId, int orderId) throws PurchaseNotFoundException, OrderNotFoundException {
  
                psdao.removePurchaseOrder(purchaseId, orderId);
    }
    
    @Override
    public void updatePurchaseStatus(int id, Status status) throws PurchaseNotFoundException {

        psdao.updatePurchaseStatus(id, status);
    }

/////////////////////////////////////////////////////// Sale /////////////////////////////////////////////////////////////////////

    @Override
    public Sale getSaleById(int id) throws SaleNotFoundException {

        return sdao.getSaleById(id);
    }

    @Override
    public void addSale(Sale sale) throws InsufficientQuantityInStock {

        sdao.addSale(sale);
    }

    @Override
    public void deleteSale(int id) throws SaleNotFoundException {

        sdao.deleteSale(id);
    }

    @Override
    public List<Sale> getAllSales() {

        return sdao.getAllSales();
    }

    @Override
    public List<Sale> getAllSalesByContactType(String type) throws InvalidContactTypeException {

        return sdao.getAllSalesByContactType(type);
    }

    @Override
    public List<Sale> getAllSalesByCustomer(String name, String customerType) {

        return sdao.getAllSalesByCustomer(name, customerType);
    }

    @Override
    public List<Contact> getAllCustomers(String customerType) throws InvalidContactTypeException {

        return sdao.getAllCustomers(customerType);
    }

    @Override
    public List<Sale> getAllSalesByStatus(Status status) {

        return sdao.getAllSalesByStatus(status);
    }

    @Override
    public List<Sale> salesDatesFilter(Map<String, Date> datesCriteria) throws InvalidFilterCriteriaMapFormatException {

        return sdao.salesDatesFilter(datesCriteria);
    }

    @Override
    public List<Sale> salesFilter(Map<String, Object> criteria) throws InvalidFilterCriteriaMapFormatException {

        return sdao.salesFilter(criteria);
    }

    @Override
    public List<SaleOrder> getSaleOrders(int id) throws SaleNotFoundException {

        return sdao.getSaleOrders(id);
    }

    @Override
    public void updateSaleOrders(int id, List<SaleOrder> newOrders) throws SaleNotFoundException,
    
            OperationNotModifiableException {

        sdao.updateSaleOrders(id, newOrders);
    }

    @Override
    public void updateSaleStatus(int id, Status status) throws SaleNotFoundException {

        sdao.updateSaleStatus(id, status);
    }












































    



}
