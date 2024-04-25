package uiass.gisiba.eia.java.service;

import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import uiass.gisiba.eia.java.dao.crm.AddressDao;
import uiass.gisiba.eia.java.dao.crm.ContactDao;
import uiass.gisiba.eia.java.dao.crm.iAddressDao;
import uiass.gisiba.eia.java.dao.crm.iContactDao;
import uiass.gisiba.eia.java.dao.crm.EmailSender;
import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.NoContactsFoundInCountry;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.dao.inventory.ProductDao;
import uiass.gisiba.eia.java.dao.inventory.iProductDao;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCategory;

public class Service implements iService {

    private iContactDao cdao = new ContactDao();
    private iAddressDao adao = new AddressDao();
    private iProductDao pdao = new ProductDao();
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
    public void notifyContact(String email, String subject, String body) throws MessagingException {

        es.sendEmail(email, subject, body);
    }

/////////////////////////////////////////////////////// PRODUCT ////////////////////////////////////////////////////////////////

    @Override
    public void addProduct(String ref, ProductCategory category, ProductBrand brand, String model, String description,

            double unitPrice) {

        pdao.addProduct(ref, category, brand, model, description, unitPrice);
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
    public List<ProductCategory> getAllCategories() {

        return pdao.getAllCategories();

    }

    @Override
    public List<ProductBrand> getAllBrandsByCategory(ProductCategory category) {

        return pdao.getAllBrandsByCategory(category);
    }

    @Override
    public void updateProduct(String ref, Map<String, Object> columnsNewValues) throws ProductNotFoundException {

        pdao.updateProduct(ref, columnsNewValues);
    }


    



}
