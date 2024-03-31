package uiass.gisiba.eia.java.controller;

import com.google.gson.Gson;


import spark.Spark;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.service.Service;

import java.util.*;

public class GetController {

	private static Service service = new Service();

	public GetController() {

	}

	public static void getContactByIdController() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    Spark.get("/contacts/:contactType/byId/:id", (req,res)-> {

		String contactType = String.valueOf(req.params(":contactType"));

		int id = Integer.parseInt(req.params(":id"));

		Contact contact = GetController.service.getContactById(id, contactType);
		
		res.type("application/json");
		
		return contact;
		   
	   } , gson::toJson );
	   
	}

	public static void getContactByNameController() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    Spark.get("/contacts/:contactType/byName/:name", (req,res)-> {

		String contactType = String.valueOf(req.params(":contactType"));

		String name = String.valueOf(req.params(":name"));

		Contact contact = GetController.service.getContactByName(name, contactType);
		
		res.type("application/json");
		   		   
		return contact;
		   
	   } , gson::toJson );
	}

	public static void getContactByAddressIdController() {
		
	}

	public static void getAllContactsByCountryController() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    Spark.get("/contacts/:contactType/byAddress/:country", (req,res)-> {

		String contactType = String.valueOf(req.params(":contactType"));

		String country = String.valueOf(req.params(":country"));

		List<Contact> contactsList = GetController.service.getAllContactsByCountry(contactType, country);
		
		res.type("application/json");
		   		   
		return contactsList;
		   
	   } , gson::toJson );
	}

	public static void getAllContactsByTypeController() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    Spark.get("/contacts/:contactType", (req,res)-> {

		String contactType = String.valueOf(req.params(":contactType"));

		List<Contact> contactsList = GetController.service.getAllContactsByType(contactType);
		
		res.type("application/json");
		   		   
		return contactsList;
		   
	   } , gson::toJson );
	}

	public static void getAllContactsController() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    Spark.get("/contacts", (req,res)-> {

		List<Contact> contactsList = GetController.service.getAllContacts();
		
		res.type("application/json");
		   		   
		return contactsList;
		   
	   } , gson::toJson );
	}

	public static  void getAddressByIdController() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    Spark.get("/addresses/:addressId", (req,res)-> {

		int address_id = Integer.valueOf(req.params(":addressId"));

		Address address = GetController.service.getAddressById(address_id);
		
		res.type("application/json");
		   		   
		return address;
		   
	   } , gson::toJson);
	   
	   
	}

	public static void getAllAddressesController() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    Spark.get("/addresses", (req,res)-> {

		List<Address> addresses = GetController.service.getAllAddresses();
		
		res.type("application/json");
		   		   
		return addresses;
		   
	   } , gson::toJson );
	}
	
}
