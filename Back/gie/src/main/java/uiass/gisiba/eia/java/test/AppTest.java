package uiass.gisiba.eia.java.test;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import uiass.gisiba.eia.java.dao.crm.AddressDao;
import uiass.gisiba.eia.java.dao.crm.ContactDao;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFound;
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
        AddressDao adao = new AddressDao();
        try {
            System.out.println(contactDao.getContactByName("Alyssa Weber",Person.class.getSimpleName()));
        } catch (ContactNotFound | InvalidContactType e) {

            e.printStackTrace();
        } 
        
        /*try {
            contactDao.deleteContact(1001, Person.class.getSimpleName());
        } catch (ContactNotFound | InvalidContactType e) {
            e.printStackTrace();
        } 
   
        
        contactDao.addContact("Donald", "Trump", "+12548796231",
         "donald.trump@gmail.com", new Address("USA", "Floria", "40000", "Florida", "Donald Trump Street", 911));
        
        //adao.addAddress("USA", "Floria", "40000", "Florida", "Donald Trump Street", 911);

        //contactDao.addContact("Mark", "Hamelton", "+1236515478", "smthing@gmail.com", new Address("Morocco", "Marakech", "40000", "Marakech-Safi", "Salam", 70));
    	
        /*try {        
            Map<String,Object> columnsNewValues = new HashMap<String,Object>();
            columnsNewValues.put("first_name","Anthony");
            columnsNewValues.put("last_name","Velasquez");
            contactDao.updateContact(41, columnsNewValues, Person.class.getSimpleName());

        } catch (ContactNotFound | InvalidContactType e) {
            e.printStackTrace();
        }*/

        //contactDao.addContact("Joe", "Biden", "+1234789215", "biden@gmail.com", new Address("New York", "USA", "362", "Neighborhood 7", "Region 6", 71669));

    }
}
    
