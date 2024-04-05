package uiass.gisiba.eia.java.test;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import uiass.gisiba.eia.java.dao.crm.ContactDao;
import uiass.gisiba.eia.java.dao.crm.EmailSender;
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

        Map<String,Object> map = new HashMap<String,Object>();

        map.put("houseNumber", 2);

   
            try {
                service.updateAddress(5, map);
            } catch (AddressNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        

        //EmailSender.sendEmail("oudra.ayoub.21@gmail.com");


        
}
    
}