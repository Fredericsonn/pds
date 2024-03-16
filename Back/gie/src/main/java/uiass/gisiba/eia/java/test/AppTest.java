package uiass.gisiba.eia.java.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import uiass.gisiba.eia.java.dao.crm.AddressDao;

public class AppTest {
    private EntityManager em;
    private EntityTransaction tr;

    public AppTest() {}

    public static void main(String[] args) {
        
    	AddressDao addressdao = new AddressDao();

        addressdao.addAddress("Morocco", "Agadir", 86000, "Souss Massa", "Salam", 70);
    	
    	   

    }
}
    
