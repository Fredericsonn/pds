package uiass.gisiba.eia.java.test;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Hibernate;

import uiass.gisiba.eia.java.controller.crm.AddressController;
import uiass.gisiba.eia.java.controller.crm.ContactController;
import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.entity.inventory.Model;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCatagory;

public class Main {


    public Main() {}

    public static void main(String[] args) {
        EntityManager em = HibernateUtility.getEntityManger();
        EntityTransaction tr = em.getTransaction();
     
        /*ContactController.getContactByIdController();
        ContactController.getContactByNameController();
        ContactController.getAllContactsByTypeController();
        ContactController.getContactByAddressIdController();

        ContactController.postContactController();
        ContactController.updateContactController();
        ContactController.deleteContactController();

        AddressController.updateAddressController();

        ContactController.postEmailController();*/

        em.persist(new Product("c6lkw2h", "pc", "desc", ProductCatagory.DESKTOP_PC, new Model(ProductBrand.Hp, "Pro Book", LocalDate.of(2015, 6, 29)), 20));
        
         


        

    
}
}