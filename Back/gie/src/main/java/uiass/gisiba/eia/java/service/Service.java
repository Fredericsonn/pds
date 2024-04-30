package uiass.gisiba.eia.java.service;

import java.net.UnknownHostException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import uiass.gisiba.eia.java.dao.crm.AddressDao;
import uiass.gisiba.eia.java.dao.crm.ContactDao;
import uiass.gisiba.eia.java.dao.crm.iAddressDao;
import uiass.gisiba.eia.java.dao.crm.iContactDao;
import uiass.gisiba.eia.java.dao.crm.EmailSender;
import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.NoContactsFoundInCountry;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.dao.inventory.CategoryDao;
import uiass.gisiba.eia.java.dao.inventory.InventoryItemDao;
import uiass.gisiba.eia.java.dao.inventory.ProductDao;
import uiass.gisiba.eia.java.dao.inventory.iCategoryDao;
import uiass.gisiba.eia.java.dao.inventory.iInventoryItemDao;
import uiass.gisiba.eia.java.dao.inventory.iProductDao;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;
import uiass.gisiba.eia.java.entity.inventory.Category;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCategory;

public class Service implements iService {

    private iContactDao cdao = new ContactDao();
    private iAddressDao adao = new AddressDao();
    private iProductDao pdao = new ProductDao();
    private iCategoryDao catdao = new CategoryDao();
    private iInventoryItemDao idao = new InventoryItemDao();
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
    public void addProduct(Category categoryBrand, String model, String description,

            double unitPrice) {

        pdao.addProduct(categoryBrand,model, description, unitPrice);
    }

    @Override
    public Product getProductById(String ref) throws ProductNotFoundException {

        return pdao.getProductById(ref);
    }

    @Override
    public void deleteProduct(String ref) throws ProductNotFoundException {

        pdao.deleteProduct(ref);
    }

    @Override
    public List<Product> getAllProducts() {

        return pdao.getAllProducts();
    }

    @Override
    public void updateProduct(String ref, Map<String, Object> columnsNewValues) throws ProductNotFoundException {

        pdao.updateProduct(ref, columnsNewValues);
    }
/////////////////////////////////////////////////////// Categroy ///////////////////////////////////////////////////////////////////

    @Override
    public Category getCategoryById(int id) throws CategoryNotFoundException {

        return catdao.getCategoryById(id);
    }

    @Override
    public List getAllCategories() {

        return catdao.getAllCategories();
    }
    @Override
    public List<ProductCategory> getAllCategoriesNames() {

        return catdao.getAllCategoriesNames();

    }

    @Override
    public List<ProductBrand> getAllBrandsNames() {

        return catdao.getAllBrandsNames();
    }

    @Override
    public List<ProductBrand> getAllBrandsByCategory(String category) {

        return catdao.getAllBrandsByCategory(category);
    }

    @Override
    public void addCategory(ProductCategory categoryName, ProductBrand brandName) {

        catdao.addCategory(categoryName, brandName);
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
    public List<InventoryItem> getAllInventoryItems() {

        return idao.getAllInventoryItems();
    }

    @Override
    public int getItemQuantity(int itemId) throws InventoryItemNotFoundException {
 
        return idao.getItemQuantity(itemId);
    }

    @Override
    public void addInventoryItem(Product product, int quantity, Date dateAdded) {

        idao.addInventoryItem(product, quantity, dateAdded);
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










    



}
