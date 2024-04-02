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

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phoneNumber", "123456789");

        
        try {
            service.updateContact(5, map, "Enterprise");
        } catch (ContactNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidContactTypeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*try {
            System.out.println(service.getAddressById(1));
        } catch (AddressNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/


        /*try {
            service.updateAddress(2, map);
        } catch (AddressNotFoundException e) {

            e.printStackTrace();
        }*/
   
        
}
    
}