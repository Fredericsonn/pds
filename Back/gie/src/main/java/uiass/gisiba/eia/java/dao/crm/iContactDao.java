package uiass.gisiba.eia.java.dao.crm;


import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;
import uiass.gisiba.eia.java.entity.crm.Person;

public interface iContactDao {

    void addContact(String fname, String lname, String phoneNum, String email, Address address);

    void addContact(String entrepriseName, EntrepriseType type, String phoneNumber, String email, Address address);

    Person getContactById(int id);
}
