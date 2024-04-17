package uiass.gisiba.eia.java.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;
import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCatagory;

public class Parser {
 
    private static Map<String, List<String>> attributes_by_type_map = new HashMap<String, List<String>>() {{
        put("Person", Arrays.asList("firstName","lastName","id","phoneNumber","email"));
        put("Enterprise", Arrays.asList("enterpriseName","type","id","phoneNumber","email"));
    }};
    
    public static Map<String, List<String>> contact_columns_by_type_map = new HashMap<String, List<String>>() {{
        put("Person", Arrays.asList("first_name","last_name","phone_number","email"));
        put("Enterprise", Arrays.asList("enterprise_name","type","phone_number","email"));
    }};

    public static List<String> address_columns = Arrays.asList("house_number","neighborhood","city","zip_code","region","country");

    private static List<String> addressAttributes = Arrays.asList("addressId","houseNumber","neighborhood","city","zipCode","region","country");

    private static List<String> productAttributes = Arrays.asList("ref","category","brand","model","description","unitPrice");

    private static List<String> product_columns = Arrays.asList("ref","category","brand","model","description","unitPrice");

        // Columns filter 
        public static Map<String, Object> mapFormater(List<String> columns, List values) {

            Map<String, Object> columns_new_values = new HashMap<String, Object>();
    
            for (int i = 0; i < columns.size() ; i++) {
    
                String column = columns.get(i);
    
                Object value = values.get(i);
    
                if (column.equals("houseNumber")) {
    
                    if ((int) value != 0) columns_new_values.put(column, value);
                }
    
                else {
                    
                    if (value != null) {
    
                        columns_new_values.put(column,value);
                    }
                }
            }
    
            return columns_new_values;
        }
    
        @SuppressWarnings("unchecked")
        // A method that collects contact data from a json :
        public static List contactValuesCollector(Gson gson, String body, String contactType) {

        List values = new ArrayList<>();

        JsonObject contact = gson.fromJson(body, JsonObject.class);

        String first_or_enterprise_name = contactType.equals("Person") ? Parser.collectString(contact, "firstName") 
        
        : Parser.collectString(contact, "enterpriseName") ; 

        String last_name_or_enterprise_type = contactType.equals("Person") ? Parser.collectString(contact, "lastName") 
        
        : Parser.collectString(contact, "type");

        String phoneNumber = Parser.collectString(contact, "phoneNumber");

        String email = Parser.collectString(contact, "email");

        // we put the collected contact values in the contact values list :
        values.addAll(Arrays.asList(first_or_enterprise_name, last_name_or_enterprise_type, phoneNumber, email));

        // We adapt the second value type wether it's the String last_name for Person or the Enumeration type for Enterprise :
        if (last_name_or_enterprise_type != null) {

                values.set(1, contactType.equals("Person") ? last_name_or_enterprise_type : EntrepriseType.valueOf(last_name_or_enterprise_type));

        }

        return values;
    }

    // A method that address contact data from a json :
    public static List addressValuesCollector(Gson gson, String body) {

        JsonObject address = gson.fromJson(body, JsonObject.class);

        int houseNumber = address.has("houseNumber") ?  Parser.collectInt(address, "houseNumber") : 0;

        String neighborhood = Parser.collectString(address, "neighborhood");

        String city = Parser.collectString(address, "city");

        String zipCode = Parser.collectString(address, "zipCode");

        String region = Parser.collectString(address, "region");

        String country = Parser.collectString(address, "country");

        return Arrays.asList(houseNumber,neighborhood,city,zipCode,region,country);

    }

    public static String collectString(JsonObject jsObj, String attribute) {
        JsonElement element = jsObj.get(attribute);

        return element != null ? String.valueOf(element.getAsString()) : null;
    }

    public static int collectInt(JsonObject jsObj, String attribute) {

        JsonElement element = jsObj.get(attribute);

        return element != null ? Integer.valueOf(element.getAsString()) : null;
    }

    public static double collectDouble(JsonObject jsObj, String attribute) {

        JsonElement element = jsObj.get(attribute);

        return element != null ? Double.valueOf(element.getAsString()) : null;
    }

    public static LocalDate collectDate(JsonObject jsObj, String attribute) {

        JsonElement element = jsObj.get(attribute);

        return element != null ? LocalDate.parse(element.getAsString()) : null;
    }

/////////////////////////////// a method that parses a contact object from a json ///////////////////////////////////////////////
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
/////////////////////////////// a method that parses a list of contact objects from a json /////////////////////////////////////////

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

    /////////////////////////////// a method that parses an address object from a json ///////////////////////////////////////////////

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
/////////////////////////////// a method that parses a list of address objects from a json /////////////////////////////////////////

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

/////////////////////////////// a method that parses a product object from a json //////////////////////////////////////////////////
public static Product parseProduct(String responseBody) {
    		
    JsonObject productObject = new JsonParser().parse(responseBody).getAsJsonObject();

    List<String> productStringInfo = new ArrayList<String>();
    
    List<Double> productDoubleInfo = new ArrayList<Double>();

    // The list containing attributes names used to parse the json
    List<String> attributes = productAttributes;

    // We iterate through our attributes and call the collectors to collect the values from the json
    for (String attribute : attributes) {

        if (attribute.equals("unitPrice")) {
            
            productDoubleInfo.add(collectDouble(productObject, attribute));
        }
        else productStringInfo.add(collectString(productObject, attribute));
    }

    String ref = productStringInfo.get(0); 

    String categoryString = productStringInfo.get(1);

    String brandString = productStringInfo.get(2);

    String model = productStringInfo.get(3);

    String description = productStringInfo.get(4);

    double unitPrice = productDoubleInfo.get(0);

    ProductCatagory category = ProductCatagory.valueOf(categoryString);

    ProductBrand brand = ProductBrand.valueOf(brandString);

    Product product = new Product(ref, category, brand, model, description, unitPrice);

    return product;
                 
}

}

