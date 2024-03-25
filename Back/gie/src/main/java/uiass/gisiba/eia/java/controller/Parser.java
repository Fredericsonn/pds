package uiass.gisiba.eia.java.controller;

import java.io.IOException;
import java.util.*;

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
 
    private static Map<String, List<String>> atrributes_by_type_map = new HashMap<String, List<String>>() {{
        put("Person", Arrays.asList("firstName","lastName","id","phoneNumber","email"));
        put("Enterprise", Arrays.asList("enterpriseName","type","id","phoneNumber","email"));
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

        return element != null ? String.valueOf(element) : "";
    }

    public static int collectInt(JsonObject jsObj, String attribute) {
        JsonElement element = jsObj.get(attribute);

        return element != null ? Integer.valueOf(element.getAsString()) : 0;
    }


    public static Contact parse(String responseBody, String contactType) {
    		
    	JsonObject contact = new JsonParser().parse(responseBody).getAsJsonObject();

        List<String> contactStringInfo = new ArrayList<String>();
        
        List<Integer> contactIntInfo = new ArrayList<Integer>();

    
    	
        List<String> attributes = atrributes_by_type_map.get(contactType);

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

		Address firstParsedAddress = new Address(country, city, zipCode, region, neighborhood, houseNumber);
		firstParsedAddress.setAddressId(addressId);
		Person firstParsedPerson = new Person(first_or_enterprise_name, last_name_or_enterprise_type, phoneNumber, email, firstParsedAddress);
		firstParsedPerson.setId(id);

		return firstParsedPerson;

					 
    }
}

