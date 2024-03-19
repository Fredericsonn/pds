package uiass.gisiba.eia.java.dao.crm;


import java.util.*;

import uiass.gisiba.eia.java.dao.exceptions.*;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;

public interface iContactDao {

    void addContact(String fname, String lname, String phoneNum, String email, Address address);

    void addContact(String entrepriseName, EntrepriseType type, String phoneNumber, String email, Address address);

    void deleteContact(int id, String contactType) throws ContactNotFound, InvalidContactType;

    Contact getContactById(int id, String contactType) throws ContactNotFound,InvalidContactType;

    Contact getContactByName(String name, String contactType) throws ContactNotFound, InvalidContactType;

    Contact getContactByAddresId(int address_id);

    List<Contact> getAllContacts(String contactType) throws InvalidContactType;

    void updateContact(int id, Map<String,Object> columnsNewValues,String contactType) throws ContactNotFound,InvalidContactType;

    


}
