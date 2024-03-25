package uiass.gisiba.eia.java.controller;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import spark.Spark;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.service.Service;
import java.util.*;

public class ContactController {

	private static Service service = new Service();

	public ContactController() {

	}

	public static void getContactByIdController(int id, String contactType) {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    Spark.get("/contacts/:id", (req,res)-> {

		String param = req.params("id");
		
		Contact contact = ContactController.service.getContactById(id, contactType);
		
		res.type("application/json");
		   		   
		return contact;
		   
	   } , gson::toJson );
	}

	public static void getContactByName(String name, String contactType) {
	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    Spark.get("/contacts/:name", (req,res)-> {
		
		Contact contact = ContactController.service.getContactByName(name, contactType);
		
		res.type("application/json");
		   		   
		return contact;
		   
	   } , gson::toJson );	
	}

    public void getContactByAddresId(String contactType, int address_id) {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    Spark.get("/contacts/:addressId", (req,res)-> {
		
		Contact contact = ContactController.service.getContactByAddressId(contactType,address_id);
		
		res.type("application/json");
		   		   
		return contact;
		   
	   } , gson::toJson );	
	}
	 
}
