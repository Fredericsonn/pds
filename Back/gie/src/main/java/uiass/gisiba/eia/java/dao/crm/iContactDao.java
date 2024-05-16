package uiass.gisiba.eia.java.dao.crm;


import java.util.*;

import uiass.gisiba.eia.java.dao.exceptions.*;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;
import uiass.gisiba.eia.java.entity.crm.Person;

public interface iContactDao {

    void addContact(String fname, String lname, String phoneNum, String email, Address address) throws AddressNotFoundException, DuplicatedAddressException;

    void addContact(String entrepriseName, EntrepriseType type, String phoneNumber, String email, Address address) throws AddressNotFoundException, DuplicatedAddressException;

    void deleteContact(int id, String contactType) throws ContactNotFoundException, InvalidContactTypeException;

    Contact getContactById(int id, String contactType) throws ContactNotFoundException,InvalidContactTypeException;

    Person getPersonById(int id) throws ContactNotFoundException;

    Enterprise getEnterpriseById(int id) throws ContactNotFoundException;

    Contact getContactByName(String name, String contactType) throws ContactNotFoundException, InvalidContactTypeException;

    Contact getContactByAddressId(String contactType, int address_id) throws AddressNotFoundException;

    List<Contact> getAllContactsByCountry(String contactType, String country) throws InvalidContactTypeException, NoContactsFoundInCountry;

    List<Contact> getAllContactsByType(String contactType) throws InvalidContactTypeException;

    List<Contact> getAllContacts() throws InvalidContactTypeException;

    void updateContact(int id, Map<String,Object> columnsNewValues,String contactType) throws ContactNotFoundException,InvalidContactTypeException;

    


}
