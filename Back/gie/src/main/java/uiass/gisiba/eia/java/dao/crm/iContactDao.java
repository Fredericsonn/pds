package uiass.gisiba.eia.java.dao.crm;

import java.util.List;

import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Person;

public interface iContactDao {

    void addContact(String phoneNumber, String email, List<Address> address);
    Person getContactById(int id);
}
