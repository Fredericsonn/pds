package uiass.gisiba.eia.java.test;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import uiass.gisiba.eia.java.dao.crm.AddressDao;
import uiass.gisiba.eia.java.dao.crm.ContactDao;
import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
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
 
        
        /*try {
            contactDao.deleteContact(1001, Person.class.getSimpleName());
        } catch (ContactNotFound | InvalidContactType e) {
            e.printStackTrace();
        } */
   
        //Address barcelona = adao.getAddressById(1002);
        //System.out.println(barcelona);

             
                //Address add1 = adao.getAddressById(26);
               /*  try {
                    contactDao.addContact("Johan", "Cruff", "+6215487923",
                    "johan.barca@gmail.com", new Address("France" , "Paris", "100911", "Catalonia", "Lionel Andres Messi", 1010));
                } catch (AddressNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/
                
            /*  catch (AddressNotFoundException e) {

                e.printStackTrace();
            }*/ 

        try {
            adao.addAddress("France" , "Paris", "100911", "Catalonia", "Lionel Andres Messi", 1010);
        } catch (AddressNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DuplicatedAddressException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
    
