package uiass.gisiba.eia.java.test;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.transaction.Status;
import javax.transaction.Transaction;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uiass.gisiba.eia.java.controller.Parsers.PurchaseParser;
import uiass.gisiba.eia.java.controller.crm.AddressController;
import uiass.gisiba.eia.java.controller.crm.ContactController;
import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidFilterCriteriaMapFormatException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidOrderTypeException;
import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.PurchaseNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.SaleNotFoundException;
import uiass.gisiba.eia.java.dao.inventory.ProductDao;
import uiass.gisiba.eia.java.dao.inventory.iProductDao;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;
import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.inventory.Category;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.purchases.PersonPurchase;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;
import uiass.gisiba.eia.java.service.Service;
import uiass.gisiba.eia.java.service.iService;



public class Main {


    public Main() {}

    public static void main(String[] args) {    //drop table purchase_order, sale_order, purchase, sale, inventory, catalog, category
        
        EntityManager em = HibernateUtility.getEntityManger();

        EntityTransaction tr = em.getTransaction();

        iService service = new Service();

        Gson gson = new Gson();

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

        /*Map<String,Object> map = new HashMap<String,Object>();

        map.put("type", "SAS");

        try {
            service.updateContact(5, map, "Enterprise");
        } catch (ContactNotFoundException | InvalidContactTypeException e) {

            e.printStackTrace();
        }*/

        /*Product product = new Product("7F1C66", ProductCategory.CAMERA, ProductBrand.Canon, "GX-ALPHA", "Good Camera", 200);


        tr.begin();
        product.setUnitPrice(300);
        em.persist(product);
        em.persist(new InventoryItem(product, 5, Date.valueOf(LocalDate.now())));
        tr.commit();*/

       
        /*try {
            Purchase purchase = service.getPurchaseById(56);
            System.out.println(purchase);
        } catch (PurchaseNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        /*Map<String,Object> map = new HashMap<String,Object>();

        Map<String,Date> dateMap = new HashMap<String,Date>();

        Map<String,String> supplierMap = new HashMap<String,String>();

        supplierMap.put("supplierName", "Ayala-Franco");

        supplierMap.put("supplierType", "Enterprise");

        map.put("supplier", supplierMap);

        dateMap.put("afterDate", Date.valueOf("2021-09-21"));

        //dateMap.put("startDate", Date.valueOf("2015-05-12"));

        //map.put("date", dateMap);

        map.put("status", "CANCELED");*/


        /*Map<String,Object> map = new HashMap<String,Object>();

        Map<String,String> customerMap = new HashMap<String,String>();

        customerMap.put("customerName", "Joyce Peterson");

        customerMap.put("customerType", "Person");

        map.put("customer", customerMap);*/

        try {
            service.deleteOrder(2, "Sale");
        } catch (InvalidOrderTypeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OrderNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        

        
       

      

           
 


        
    
}
}