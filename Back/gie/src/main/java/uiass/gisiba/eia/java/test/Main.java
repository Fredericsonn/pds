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
import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCatagory;
import uiass.gisiba.eia.java.service.Service;

public class Main {


    public Main() {}

    public static void main(String[] args) {


        ContactController.getContactByIdController();
        ContactController.getContactByNameController();
        ContactController.getAllContactsByTypeController();
        ContactController.getContactByAddressIdController();

        ContactController.postContactController();
        ContactController.updateContactController();
        ContactController.deleteContactController();

        AddressController.updateAddressController();

        ContactController.postEmailController();

        
         


        

    
}
}