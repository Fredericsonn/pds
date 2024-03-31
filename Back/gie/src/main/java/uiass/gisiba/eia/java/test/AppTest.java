package uiass.gisiba.eia.java.test;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import uiass.gisiba.eia.java.dao.crm.ContactDao;
import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.NoContactsFoundInCountry;
import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.service.Service;

public class AppTest {
    private EntityManager em;
    private EntityTransaction tr;

    public AppTest() {}

    public static void main(String[] args) {

        Service service = new Service();

        try {
            System.out.println(service.getAddressById(1));
        } catch (AddressNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*  Map<String, Object> map = new HashMap<String, Object>();
        map.put("country", "Germany");
        map.put("city", "Munich");

        try {
            service.updateAddress(2, map);
        } catch (AddressNotFoundException e) {

            e.printStackTrace();
        }*/
   
        
}
    
}