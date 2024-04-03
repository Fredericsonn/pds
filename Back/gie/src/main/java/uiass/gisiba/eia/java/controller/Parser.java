package uiass.gisiba.eia.java.controller;

import java.io.IOException;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Person;

public class Parser {
 
    private static Map<String, List<String>> attributes_by_type_map = new HashMap<String, List<String>>() {{
        put("Person", Arrays.asList("firstName","lastName","id","phoneNumber","email"));
        put("Enterprise", Arrays.asList("enterpriseName","type","id","phoneNumber","email"));
    }};

    private static Map<String, List<String>> columns_by_type_map = new HashMap<String, List<String>>() {{
        put("Person", Arrays.asList("first_name","last_name","id","phone_number","email"));
        put("Enterprise", Arrays.asList("enterprise_name","type","id","phone_number","email"));
    }};

    private static List<String> addressAttributes = Arrays.asList("addressId","houseNumber","neighborhood","city","zipCode","region","country");


    public static String responseBodyGenerator(String url) {

        OkHttpClient client = new OkHttpClient();
    	
    	String body=null;
    	
    	Request request = new Request.Builder()
    	      .url(url)
    	      .build();

    	try (Response response = client.newCall(request).execute()) {
    	   
    		  body = response.body().string();
    		  
    	    }
    	       	  
    	  catch(IOException e ) {
    		  System.out.println(e.getMessage());
    	  }

        return body;
    }

    public static String collectString(JsonObject jsObj, String attribute) {
        JsonElement element = jsObj.get(attribute);

        return element != null ? String.valueOf(element.getAsString()) : null;
    }

    public static int collectInt(JsonObject jsObj, String attribute) {

        JsonElement element = jsObj.get(attribute);

        return element != null ? Integer.valueOf(element.getAsString()) : null;
    }


    public static Contact parseContact(String responseBody, String contactType) {
    		
    	JsonObject contact = new JsonParser().parse(responseBody).getAsJsonObject();

        List<String> contactStringInfo = new ArrayList<String>();
        
        List<Integer> contactIntInfo = new ArrayList<Integer>();

    
    	
        List<String> attributes = attributes_by_type_map.get(contactType);

        for (String attribute : attributes) {

            if (attribute.equals("id")) {
                
                contactIntInfo.add(collectInt(contact, attribute));
            }
            else contactStringInfo.add(collectString(contact, attribute));
        }
    
        JsonObject addressObject = contact.has("address") ? contact.get("address").getAsJsonObject() : null;

        for (String attribute : addressAttributes) {

            if (attribute.equals("addressId") || attribute.equals("houseNumber")) {
                
                contactIntInfo.add(collectInt(addressObject, attribute));
            }
            else contactStringInfo.add(collectString(addressObject, attribute));
        }

        String first_or_enterprise_name = contactStringInfo.get(0); 
        String last_name_or_enterprise_type = contactStringInfo.get(1);
        int id = contactIntInfo.get(0);
        String phoneNumber = contactStringInfo.get(2);
        String email = contactStringInfo.get(3);

        int addressId = contactIntInfo.get(1);
        int houseNumber = contactIntInfo.get(2);
        String neighborhood = contactStringInfo.get(4);
        String city =  contactStringInfo.get(5);
        String zipCode = contactStringInfo.get(6);
        String region = contactStringInfo.get(7);
        String country = contactStringInfo.get(8);

		Address address = new Address(country, city, zipCode, region, neighborhood, houseNumber);
		address.setAddressId(addressId);
		Person person = new Person(first_or_enterprise_name, last_name_or_enterprise_type, phoneNumber, email, address);
		person.setId(id);

		return person;

					 
    }

    public static List<Contact> parseContacts(String responseBody, String contactType) {

        List<Contact> contacts = new ArrayList<Contact>();

        JsonArray contactsArray = new JsonParser().parse(responseBody).getAsJsonArray();

        for (int i = 0 ; i < contactsArray.size() ; i++) {

            String contactJsonBody = contactsArray.get(i).toString();

            Contact contact = parseContact(contactJsonBody, contactType);

            contacts.add(contact);
        }

        return contacts;
    }

    public static Address parseAddress(String responseBody) {

    	JsonObject addressObject = new JsonParser().parse(responseBody).getAsJsonObject();

        List<String> addressStringInfo = new ArrayList<String>();

        List<Integer> addressIntInfo = new ArrayList<Integer>();
    
        for (String attribute : addressAttributes) {

            if (attribute.equals("addressId") || attribute.equals("houseNumber")) {
                
                addressIntInfo.add(collectInt(addressObject, attribute));
            }
            else addressStringInfo.add(collectString(addressObject, attribute));
        }

        int addressId = addressIntInfo.get(0);
        int houseNumber = addressIntInfo.get(1);
        String neighborhood = addressStringInfo.get(0);
        String city =  addressStringInfo.get(1);
        String zipCode = addressStringInfo.get(2);
        String region = addressStringInfo.get(3);
        String country = addressStringInfo.get(4);

        Address address = new Address(country, city, zipCode, region, neighborhood, houseNumber);
        address.setAddressId(addressId);

        return address;
    }

    public static List<Address> parseAddresses(String responseBody) {

        List<Address> addresses = new ArrayList<Address>();

        JsonArray addressesArray = new JsonParser().parse(responseBody).getAsJsonArray();

        for (int i = 0 ; i < addressesArray.size() ; i++) {

            String addressJsonBody = addressesArray.get(i).toString();

            Address address = parseAddress(addressJsonBody);

            addresses.add(address);
        }

        return addresses;

    }
}

