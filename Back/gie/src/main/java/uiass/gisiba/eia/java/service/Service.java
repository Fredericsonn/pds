package uiass.gisiba.eia.java.service;

import java.util.List;
import java.util.Map;

import uiass.gisiba.eia.java.dao.crm.AddressDao;
import uiass.gisiba.eia.java.dao.crm.ContactDao;
import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.NoContactsFoundInCountry;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;

public class Service implements iService {

    private ContactDao cdao = new ContactDao();
    private AddressDao adao = new AddressDao();

/////////////////////////////////////////////////////// ADDRESS ////////////////////////////////////////////////////////////////

    @Override
    public void addAddress(String country, String city, String zipCode, String region, String neighborhood,

            int houseNumber) throws AddressNotFoundException, DuplicatedAddressException {

        adao.addAddress(country, city, zipCode, region, neighborhood, houseNumber);
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
    public Contact getContactByAddressId(String contactType, int address_id) {

        return getContactByAddressId(contactType,address_id);
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



}
