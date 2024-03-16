package uiass.gisiba.eia.java.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import uiass.gisiba.eia.java.dao.crm.ContactDao;
import uiass.gisiba.eia.java.entity.crm.Address;

public class AppTest {
    private EntityManager em;
    private EntityTransaction tr;

    public AppTest() {}

    public static void main(String[] args) {

        ContactDao contactDao = new ContactDao();
        
        contactDao.addContact("Jake", "Palmer", "+12548796231",
         "jake.palmer@gmail.com", new Address("Morocco", "Marakech", "40000", "Marakech-Safi", "Salam", 70));
        
    	
    	   

    }
}
    
