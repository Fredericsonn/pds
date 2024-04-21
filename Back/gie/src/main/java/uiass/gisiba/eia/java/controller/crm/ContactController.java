package uiass.gisiba.eia.java.controller.crm;

import java.util.*;

import javax.mail.MessagingException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.*;
import uiass.gisiba.eia.java.controller.GetGson;
import uiass.gisiba.eia.java.controller.Parser;
import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;
import uiass.gisiba.eia.java.service.Service;

public class ContactController {

    static Service service = new Service();

	public ContactController() {

	}

/////////////////////////////////////////////////// GET METHODS //////////////////////////////////////////////////////////////////
	public static void getContactByIdController() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    get("/contacts/:contactType/byId/:id", (req,res)-> {

		String contactType = String.valueOf(req.params(":contactType"));

		int id = Integer.parseInt(req.params(":id"));

		Contact contact = service.getContactById(id, contactType);
		
		res.type("application/json");

		return contact;
	
		   
		}, gson::toJson );
	   
	}

	public static void getContactByNameController() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    get("/contacts/:contactType/byName/:name", (req,res)-> {

		String contactType = String.valueOf(req.params(":contactType"));

		String name = String.valueOf(req.params(":name"));

		Contact contact = service.getContactByName(name, contactType);
		
		res.type("application/json");
		   		   
		return contact;
		   
	   } , gson::toJson );
	}

	public static void getContactByAddressIdController() {
		
	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    get("/contacts/:contactType/byAddressId/:id", (req,res)-> {

		String contactType = String.valueOf(req.params(":contactType"));

		int id = Integer.parseInt(req.params(":id"));

		Contact contact = service.getContactByAddressId(contactType, id);
		
		res.type("application/json");
						  
		return contact;

		   
	} , gson::toJson );		
	}

	public static void getAllContactsByCountryController() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    get("/contacts/:contactType/byAddressCountry/:country", (req,res)-> {

		String contactType = String.valueOf(req.params(":contactType"));

		String country = String.valueOf(req.params(":country"));

		List<Contact> contactsList = service.getAllContactsByCountry(contactType, country);
		
		res.type("application/json");
		   		   
		return contactsList;
		   
	   } , gson::toJson );
	}

	public static void getAllContactsByTypeController() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    get("/contacts/:contactType", (req,res)-> {

		String contactType = String.valueOf(req.params(":contactType"));

		List<Contact> contactsList = service.getAllContactsByType(contactType);
		
		res.type("application/json");
		   		   
		return contactsList;
		   
	   } , gson::toJson );
	}

	public static void getAllContactsController() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    get("/contacts", (req,res)-> {

		List<Contact> contactsList = service.getAllContacts();
		
		res.type("application/json");
		   		   
		return contactsList;
		   
	   } , gson::toJson );
	}


/////////////////////////////////////////////////// POST METHOD //////////////////////////////////////////////////////////////////
public static void postContactController() {

    Gson gson = GetGson.getGson();

    System.out.println("Server started.");

    post("/contacts/:contactType/post", new Route() {

    @Override
    public String handle(Request request, Response response)  {

        String contactType = String.valueOf(request.params(":contactType"));

        String body = request.body();

        System.out.println(body);

        JsonObject contact = gson.fromJson(body, JsonObject.class);

        String first_or_enterprise_name = contactType.equals("Person") ? Parser.collectString(contact, "firstName") 
        
        : Parser.collectString(contact, "enterpriseName") ; 

        String last_name_or_enterprise_type = contactType.equals("Person") ? Parser.collectString(contact, "lastName") 
        
        : Parser.collectString(contact, "type");

        String phoneNumber = Parser.collectString(contact, "phoneNumber");

        String email = Parser.collectString(contact, "email");

        int houseNumber = Parser.collectInt(contact, "houseNumber");

        String neighborhood = Parser.collectString(contact, "neighborhood");

        String city = Parser.collectString(contact, "city");

        String zipCode = Parser.collectString(contact, "zipCode");

        String region = Parser.collectString(contact, "region");

        String country = Parser.collectString(contact, "country");

        Address address = new Address(country, city, zipCode, region, neighborhood, houseNumber);

        try {

            if (contactType.equals("Person")) {

                service.addContact(first_or_enterprise_name, last_name_or_enterprise_type, phoneNumber, email, address);
            }

            else {
                
            // We adapt the second value type wether it's the String last_name for Person or the Enumeration type for Enterprise
                service.addContact(first_or_enterprise_name, EntrepriseType.valueOf(last_name_or_enterprise_type),
                
                phoneNumber, email, address);
            }

        } catch (AddressNotFoundException | DuplicatedAddressException e) {

            return "The provided address is already linked to a contact.";
        }
         
        return "Contact created successfully.";
}});


}

//////////////////////////////////////////  Email Sending Controller ////////////////////////////////////////////////////////////  
    public static void postEmailController() {

        Gson gson = GetGson.getGson();

        System.out.println("Server Started.");

        post("/email/post", new Route() {

            @Override
            public String handle(Request request, Response response) throws Exception {

                String body = request.body();

                JsonObject emailContent = gson.fromJson(body, JsonObject.class);

                String email = Parser.collectString(emailContent, "receiver");

                String subject = Parser.collectString(emailContent, "subject");

                String emailBody = Parser.collectString(emailContent, "body");

                try {

                    service.notifyContact(email, subject, emailBody);  
                    

                } catch(MessagingException e) {

                    return "Email not sent.";
                }

                return "Email Sent Successfully.";

                
            }
            
        });
    }

/////////////////////////////////////////////////// DELETE METHOD //////////////////////////////////////////////////////////////////
    public static void deleteContactController()  {

        System.out.println("Server started.");

            delete("/contacts/delete/:contactType/:id", new Route()  {

                @Override
                public String handle(Request request, Response response) throws InvalidContactTypeException  {

                    String contactType = String.valueOf(request.params(":contactType"));

                    int id = Integer.parseInt(request.params(":id"));
    
                        try {
    
                            service.deleteContact(id, contactType);       
        
                        } catch (ContactNotFoundException | InvalidContactTypeException e) {
        
                            return e.getMessage();
                        }  
                        
                        return "Contact deleted successfully.";
                }
            }
    
            );  
    }
/////////////////////////////////////////////////// Update METHOD //////////////////////////////////////////////////////////////////

    public static void updateContactController()  {

        // A list of the contact table's columns

        Gson gson = GetGson.getGson();

		System.out.println("Server started.");

        put("/contacts/:contactType/put/:id" , new Route() {

            @Override
            public String handle(Request request, Response response)   {
                
				// We retireve the contact type from the url 
                String contactType = String.valueOf(request.params(":contactType"));

                // We retireve the contact id from the url 
                int id = Integer.parseInt(request.params(":id")); 

                String body = request.body(); // We get the json containing update information as string
                    
                // The corresponding list of attribute (used to parse the json)
                List<String> columns = Parser.contact_columns_by_type_map.get(contactType);
    
                // We collect all the values to update from the request body in one list :
                List contactValues = Parser.contactValuesCollector(gson, body, contactType);
    
                // We select only the non null values with their corresponding columns and form the update map :
                Map<String, Object> contact_columns_new_values = Parser.mapFormater(columns, contactValues);
    
                // And finally we update the contact :
                try {
                            
                    service.updateContact(id, contact_columns_new_values, contactType);;
    
                } catch (ContactNotFoundException | InvalidContactTypeException  e) {
    
                    return e.getMessage();
                }
                     
                return "Contact updated successfully.";

    
        }});

    }
}