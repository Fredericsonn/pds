package uiass.gisiba.eia.java.test;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.transaction.Transaction;

import com.google.gson.Gson;

import uiass.gisiba.eia.java.controller.crm.AddressController;
import uiass.gisiba.eia.java.controller.crm.ContactController;
import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.dao.inventory.ProductDao;
import uiass.gisiba.eia.java.dao.inventory.iProductDao;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;
import uiass.gisiba.eia.java.entity.inventory.Category;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.service.Service;
import uiass.gisiba.eia.java.service.iService;



public class Main {


    public Main() {}

    public static void main(String[] args) {

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

        /*Category cat = new Category(ProductCategory.CAMERA, ProductBrand.Canon);

        service.addProduct(cat, "GX-ALPHA", "Good Camera", 200);*/

        Map<String,Object> map = new HashMap<String,Object>();

        Map<String,Object> categoryMap = new HashMap<String,Object>();

        map.put("unitPrice", 150.0);

        map.put("description", "a very good product");

        categoryMap.put("categoryName", "LAPTOP");

        categoryMap.put("brandName", "Dell");

        map.put("category", categoryMap);

        try {
            service.updateProduct("00ENDE", map);
        } catch (ProductNotFoundException | CategoryNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        
        

    
}
}