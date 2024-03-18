package uiass.gisiba.eia.java.test;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import uiass.gisiba.eia.java.dao.crm.AddressDao;
import uiass.gisiba.eia.java.dao.crm.ContactDao;
import uiass.gisiba.eia.java.dao.exceptions.ContactIdNotFound;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactType;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Person;

public class AppTest {
    private EntityManager em;
    private EntityTransaction tr;

    public AppTest() {}

    public static void main(String[] args) {

        ContactDao contactDao = new ContactDao();
        //AddressDao adao = new AddressDao();
        
        //contactDao.addContact("Jake", "Palmer", "+12548796231",
        // "jake.palmer@gmail.com", new Address("Morocco", "Marakech", "40000", "Marakech-Safi", "Salam", 70));
        
        //adao.addAddress("Morocco", "Marakech", "40000", "Marakech-Safi", "Salam", 70);

        //contactDao.addContact("Mark", "Hamelton", "+1236515478", "smthing@gmail.com", new Address("Morocco", "Marakech", "40000", "Marakech-Safi", "Salam", 70));
    	
        /*try {        
            Map<String,Object> columnsNewValues = new HashMap<String,Object>();
            columnsNewValues.put("first_name","Anthony");
            columnsNewValues.put("last_name","Velasquez");
            contactDao.updateContact(41, columnsNewValues, Person.class.getSimpleName());

        } catch (ContactIdNotFound | InvalidContactType e) {
            e.printStackTrace();
        }*/

        //contactDao.addContact("Joe", "Biden", "+1234789215", "biden@gmail.com", new Address("New York", "USA", "362", "Neighborhood 7", "Region 6", 71669));

    }
}
    
