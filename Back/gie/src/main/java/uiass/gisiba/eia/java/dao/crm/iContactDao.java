package uiass.gisiba.eia.java.dao.crm;


import java.util.List;

import uiass.gisiba.eia.java.dao.exceptions.*;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;

public interface iContactDao {

    void addContact(String fname, String lname, String phoneNum, String email, Address address);

    void addContact(String entrepriseName, EntrepriseType type, String phoneNumber, String email, Address address);

    Contact getContactById(int id) throws ContactIdNotFound;

    List<Contact> getAllContacts();

    List getAllContactsByType(String type) throws InvalidContactType;

    void updateContact(int id, String fname, String lname,String enterprise_name, EntrepriseType type,
    
    String phone_number, String email, int address_id) throws ContactIdNotFound;

}
