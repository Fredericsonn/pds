package uiass.gisiba.eia.java.test;


import java.util.*;



import uiass.gisiba.eia.java.controller.crm.AddressController;
import uiass.gisiba.eia.java.controller.crm.ContactController;
import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.dao.inventory.ProductDao;
import uiass.gisiba.eia.java.dao.inventory.iProductDao;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCategory;
import uiass.gisiba.eia.java.service.Service;
import uiass.gisiba.eia.java.service.iService;



public class Main {


    public Main() {}

    public static void main(String[] args) {



        iService service = new Service();

        //service.addProduct("hx0b6f", ProductCatagory.LAPTOP, ProductBrand.Dell, "Inspiron", "tech words tech words", 6000);

        /*try {
            service.getProductById("hx0b6f");
        } catch (ProductNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        //System.out.println(service.getAllProducts());

        /*Map<String,Object> map = new HashMap<String,Object>();

        map.put("description", "Bla Bla");

        try {
            service.updateProduct("hx0b6f", map);
        } catch (ProductNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        /*try {
            service.deleteProduct("hx0b6f");
        } catch (ProductNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        Map<String,Object> map = new HashMap<String,Object>();

        map.put("type", "SAS");

        try {
            service.updateContact(5, map, "Enterprise");
        } catch (ContactNotFoundException | InvalidContactTypeException e) {

            e.printStackTrace();
        }


        

    
}
}