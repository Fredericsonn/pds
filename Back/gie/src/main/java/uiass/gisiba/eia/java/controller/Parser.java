package uiass.gisiba.eia.java.controller;

import java.io.IOException;
import java.util.*;

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
        put("Person", Arrays.asList("firstName","lastName","phoneNumber","email","address"));
        put("Enterprise", Arrays.asList("enterpriseName","type","phoneNumber","email","address"));
    }};

    private static List<String> addressAttributes = Arrays.asList("neighborhood","city","zipCode","region","country");


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

        return jsObj.has(attribute) ? jsObj.get(attribute).getAsString() : "";

    }

    public static int collectInt(JsonObject jsObj, String attribute) {

        return jsObj.has(attribute) ? jsObj.get(attribute).getAsInt() : 0;

    }

    public static Contact parse(String responseBody, String contactType) {
    		
    	JsonObject contact = new JsonParser().parse(responseBody).getAsJsonObject();

        String[] contactStringInfo = new String[4];
        int[] contactIntInfo = new int[1];

        String[] contactStringAddress = new String[5];
        int[] contactIntAddress = new int[2];
    	
        List<String> attributes = atrributes_by_type_map.get(contactType);

        for (int i = 0; i < attributes.size(); i++) {

            String attribute = attributes.get(i);

            if (attribute.equals("id")) {
                
                contactIntInfo[0] = collectInt(contact, attribute);
            }
            contactStringInfo[i] = collectString(contact, attribute);
        }
    
        JsonObject addressObject = contact.has("address") ? contact.get("address").getAsJsonObject() : null;

        for (int i = 0; i < addressAttributes.size(); i++) {

            String attribute = addressAttributes.get(i);
            int index = 0;
            if (attribute.equals("id") || attribute.equals("houseNumber")) {
                
                contactIntAddress[index] = collectInt(addressObject, attribute);
                index++;
            }
            contactStringAddress[i] = collectString(addressObject, attribute);
        }

        String first_or_enterprise_name = (String) contactStringInfo[0]; 
        String last_name_or_enterprise_type = (String) contactStringInfo[1];
        int id = (int) contactIntInfo[0];
        String phoneNumber = (String) contactStringInfo[2];
        String email = (String) contactStringInfo[3];

        int addressId = (int) contactIntAddress[0];
        int houseNumber = (int) contactIntAddress[1];
        String neighborhood = (String) contactStringAddress[0];
        String city = (String) contactStringAddress[1];
        String zipCode = (String) contactStringAddress[2];
        String region = (String) contactStringAddress[3];
        String country = (String) contactStringAddress[4];

		Address firstParsedAddress = new Address(country, city, zipCode, region, neighborhood, houseNumber);
		firstParsedAddress.setAddressId(addressId);
		Person firstParsedPerson = new Person(first_or_enterprise_name, last_name_or_enterprise_type, phoneNumber, email, firstParsedAddress);
		firstParsedPerson.setId(id);

		return firstParsedPerson;

					 
    }
}

