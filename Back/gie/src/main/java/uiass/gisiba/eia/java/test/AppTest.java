package uiass.gisiba.eia.java.test;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;


import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.service.Service;

public class AppTest {
    private EntityManager em;
    private EntityTransaction tr;

    public AppTest() {}

    public static void main(String[] args) {

        Service service = new Service();

        /*try {
            service.addAddress("New York", "USA", "362", "Neighborhood 7", "Region 6", 71669);
        } catch (AddressNotFoundException | DuplicatedAddressException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } */

        /*Map<String, Object> columnsNewValues = new HashMap<String, Object>();

        columnsNewValues.put("first_name", "Jeremy");
        columnsNewValues.put("last_name", "Little");

        try {
            service.updateContact(24, columnsNewValues, "Person");
        } catch (ContactNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidContactTypeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }*/
        try {
            System.out.println(service.getAllContactsByType("Person"));
        } catch (InvalidContactTypeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
}
    
}