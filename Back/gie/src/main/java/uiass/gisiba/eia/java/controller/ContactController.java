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

	public static void main(String[] args) {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    Spark.get("/contacts", (req,res)-> {

		List<Contact> contactsList = ContactController.service.getAllContacts();
		
		res.type("application/json");
		   		   
		return contactsList;
		   
	   } , gson::toJson );
	}
}
