package uiass.gisiba.eia.java.service;

import java.util.*;

import javax.mail.MessagingException;

import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.NoContactsFoundInCountry;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCategory;

public interface iService {

/////////////////////////////////////////////////////// ADDRESS ////////////////////////////////////////////////////////////////

    void addAddress(String country, String city, String zipCode, String region, String neighborhood,
    
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

    void notifyContact(String email, String subject, String body) throws MessagingException;

/////////////////////////////////////////////////////// PRODUCT ////////////////////////////////////////////////////////////////

    void addProduct(String ref, ProductCategory category, ProductBrand brand, String model, 
    
    String description, double unitPrice);

    Product getProductById(String ref) throws ProductNotFoundException;

    void deleteProduct(String ref) throws ProductNotFoundException;

    List<Product> getAllProducts();

    List<ProductCategory> getAllCategories(); 

    List<ProductBrand> getAllBrandsByCategory(ProductCategory category); 

    void updateProduct(String ref, Map<String,Object> columnsNewValues) throws ProductNotFoundException;
}
