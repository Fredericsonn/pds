package uiass.eia.gisiba.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// This class contain all the diverse parsing and data structures generating methods 
public class Parser {

    private static Map<String, List<String>> attributes_by_type_map = new HashMap<String, List<String>>() {{
        put("Person", Arrays.asList("firstName","lastName","id","phoneNumber","email"));
        put("Enterprise", Arrays.asList("enterpriseName","type","id","phoneNumber","email"));
    }};

    public static Map<String, List<String>> contact_creation_columns_by_type_map = new HashMap<String, List<String>>() {{
        put("Person", Arrays.asList("firstName","lastName","phoneNumber","email","houseNumber","neighborhood","city","zipCode","region","country"));
        put("Enterprise", Arrays.asList("enterpriseName","type","phoneNumber","email","houseNumber","neighborhood","city","zipCode","region","country"));
    }};

    public static Map<String, List<String>> contact_update_columns_by_type_map = new HashMap<String, List<String>>() {{
        put("Person", Arrays.asList("firstName","lastName","phoneNumber","email"));
        put("Enterprise", Arrays.asList("enterpriseName","type","phoneNumber","email"));
    }};

    public static List<String> addressAttributes = Arrays.asList("addressId","houseNumber","neighborhood","city","zipCode","region","country");

    public static List<String> update_address_attributes = Arrays.asList("houseNumber","neighborhood","city","zipCode","region","country");

    public static List<String> contactTextFieldsReferences = Arrays.asList("first","second","phoneNumber","email","houseNumber","neighborhood","city","zipCode","region","country");

    public static List<String> email_sending_attributes = Arrays.asList("receiver","subject","body");

    private static List<String> productAttributes = Arrays.asList("productRef","category","brand","model","description","unitPrice");

    private static List<String> product_columns = Arrays.asList("category","brand","model","description","unitPrice");

    public static Map<String,Object> contactCreationMapGenerator(List values, String contactType) {

        Map<String,Object> map = new HashMap<String,Object>();

        List<String> attributes = contact_creation_columns_by_type_map.get(contactType);

        
        for (int i=0 ; i < attributes.size() ; i++) {

            map.put(attributes.get(i), values.get(i));

        }

        return map;
    }

    public static Map<String,String> productCreationMapGenerator(List<String> values) {

        Map<String,String> map = new HashMap<String,String>();

        List<String> attributes = product_columns;
        
        for (int i=0 ; i < attributes.size() ; i++) {

            map.put(attributes.get(i), values.get(i));

        }

        return map;
    }

    public static Map<String,Object> contactUpdateMapGenerator(List values, String contactType) {

        Map<String,Object> map = new HashMap<String,Object>();

        List<String> attributes = contact_update_columns_by_type_map.get(contactType);

        
        for (int i=0 ; i < attributes.size() ; i++) {

            if (!((String) values.get(i)).equals("")) map.put(attributes.get(i), values.get(i));

        }

        return map;
    }

    public static Map<String,Object> addressMapGenerator(List values) {

        Map<String,Object> map = new HashMap<String,Object>();

        List<String> attributes = update_address_attributes;

        
        for (int i=0 ; i < attributes.size() ; i++) {
                        
            if (attributes.get(i).equals("houseNumber")) {

                String value = String.valueOf(values.get(i));

                if (Integer.parseInt(value) != 0) map.put(attributes.get(i), Integer.parseInt(value));
            }

            else if (!((String) values.get(i)).equals("")) map.put(attributes.get(i), values.get(i));
            

        }

        return map;
    }

    public static Map<String,String> productUpdateMapGenerator(List<String> values) {

        Map<String,String> map = new HashMap<String,String>();

        List<String> attributes = product_columns;

        
        for (int i=0 ; i < attributes.size() ; i++) {

            String attribute = attributes.get(i);

            String value = values.get(i);

            if (value != "null" && value != "") map.put(attribute, value);

        }

        return map;
    }

    public static Map<String,Object> emailSendingMapGenerator(List values) {

        Map<String,Object> map = new HashMap<String,Object>();

        List attributes = email_sending_attributes;

        for (int i = 0 ; i < attributes.size() ; i++) {

            String attribute = String.valueOf(attributes.get(i));

            String value = String.valueOf(values.get(i));

            map.put(attribute, value);
        }

        return map;
    }

    

    public static String jsonGenerator(Map<String,Object> attributes) {

        Gson gson = GetGson.getGson();

        if (!attributes.isEmpty()) return gson.toJson(attributes);

        return null;
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

    // Contact parser
    public static List<String> parseContact(String responseBody, String contactType) {

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
        String id = String.valueOf(contactIntInfo.get(0));
        String phoneNumber = contactStringInfo.get(2);
        String email = contactStringInfo.get(3);

        String addressId = String.valueOf(contactIntInfo.get(1));
        String houseNumber = String.valueOf(contactIntInfo.get(2));
        String neighborhood = String.valueOf(contactStringInfo.get(4));
        String city = String.valueOf(contactStringInfo.get(5));
        String zipCode = String.valueOf(contactStringInfo.get(6));
        String region = String.valueOf(contactStringInfo.get(7));
        String country = String.valueOf(contactStringInfo.get(8));

        return Arrays.asList(id,first_or_enterprise_name,last_name_or_enterprise_type,phoneNumber,email,addressId, houseNumber + " " + neighborhood
        
        + " " + city + " " + zipCode + " " + region + " " + country);
    }

    // Address parser
    public static List<String> parseAddress(String responseBody) {

    	JsonObject addressObject = new JsonParser().parse(responseBody).getAsJsonObject();

        List<String> addressStringInfo = new ArrayList<String>();

        List<Integer> addressIntInfo = new ArrayList<Integer>();
    
        for (String attribute : addressAttributes) {

            if (attribute.equals("addressId") || attribute.equals("houseNumber")) {
                
                addressIntInfo.add(collectInt(addressObject, attribute));
            }
            else addressStringInfo.add(collectString(addressObject, attribute));
        }

        String addressId = String.valueOf(addressIntInfo.get(0));
        String houseNumber = String.valueOf(addressIntInfo.get(1));
        String neighborhood = addressStringInfo.get(0);
        String city =  addressStringInfo.get(1);
        String zipCode = addressStringInfo.get(2);
        String region = addressStringInfo.get(3);
        String country = addressStringInfo.get(4);

        return Arrays.asList(addressId,houseNumber,neighborhood,city,zipCode,region,country);
    }

    public static List<String> parseProduct(String responseBody) {
    		
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
    
        String category = productStringInfo.get(1);
    
        String brand = productStringInfo.get(2);
    
        String model = productStringInfo.get(3);
    
        String description = productStringInfo.get(4);
    
        String unitPrice = String.valueOf(productDoubleInfo.get(0)) + "$";

        return Arrays.asList(ref, category, brand, model, description, unitPrice);
                     
    }

    public static List<String> parseProductCharacteristics(String responseBody) {

        List<String> characteristics = new ArrayList<String>();

        JsonArray characteristicsArray = new JsonParser().parse(responseBody).getAsJsonArray();

        characteristicsArray.forEach(jsonElt -> characteristics.add(String.valueOf(jsonElt.getAsString())));

        return characteristics;



    }

    



}
