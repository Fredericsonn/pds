package uiass.gisiba.eia.java.controller;

import java.time.LocalDate;
import java.sql.Date;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.inventory.Category;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCategory;

public class Parser {
 
    private static Map<String, List<String>> attributes_by_type_map = new HashMap<String, List<String>>() {{
        put("Person", Arrays.asList("firstName","lastName","id","phoneNumber","email"));
        put("Enterprise", Arrays.asList("enterpriseName","type","id","phoneNumber","email"));
    }};
    
    public static Map<String, List<String>> contact_columns_by_type_map = new HashMap<String, List<String>>() {{
        put("Person", Arrays.asList("first_name","last_name","phone_number","email"));
        put("Enterprise", Arrays.asList("enterprise_name","type","phone_number","email"));
    }};

    public static List<String> address_columns = Arrays.asList("houseNumber","neighborhood","city","zipCode","country");

    public static List<String> addressAttributes = Arrays.asList("addressId","houseNumber","neighborhood","city","zipCode","country");

    public static List<String> productAttributes = Arrays.asList("productRef","model","description","unitPrice");

    public static List<String> categoryAttributes = Arrays.asList("categoryName","brandName");

    public static List<String> product_columns = Arrays.asList("category","brand","model","description","unit_price");

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

        return values;
    }

    // A method that address contact data from a json :
    public static List addressValuesCollector(Gson gson, String body) {

        JsonObject address = gson.fromJson(body, JsonObject.class);

        int houseNumber = address.has("houseNumber") ?  Parser.collectInt(address, "houseNumber") : 0;

        String neighborhood = Parser.collectString(address, "neighborhood");

        String city = Parser.collectString(address, "city");

        String zipCode = Parser.collectString(address, "zipCode");


        String country = Parser.collectString(address, "country");

        return Arrays.asList(houseNumber,neighborhood,city,zipCode,country);

    }

    // A method that address contact data from a json :
    public static List productValuesCollector(Gson gson, String body) {

        JsonObject product = gson.fromJson(body, JsonObject.class);

        String category = Parser.collectString(product, "category");

        String brand = Parser.collectString(product, "brand");

        String model = Parser.collectString(product, "model");

        String description = Parser.collectString(product, "description");

        String unitPrice = Parser.collectString(product, "unitPrice");

        return Arrays.asList(category,brand,model,description,unitPrice);

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

        List<String> attributes = attributes_by_type_map.get(contactType);

        List<String> contactStringInfo = new ArrayList<String>();
        
        List<Integer> contactIntInfo = new ArrayList<Integer>();

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
        String country = contactStringInfo.get(8);

		Address address = new Address(country, city, zipCode, neighborhood, houseNumber);
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
        String country = addressStringInfo.get(4);

        Address address = new Address(country, city, zipCode, neighborhood, houseNumber);
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
        List<String> pAttributes = new ArrayList<String>(productAttributes);

        // We iterate through our attributes and call the collectors to collect the values from the json
        for (String attribute : pAttributes) {

            if (attribute.equals("unitPrice")) {
            
                productDoubleInfo.add(collectDouble(productObject, attribute));
            }
            else productStringInfo.add(collectString(productObject, attribute));
        }

        String ref = productStringInfo.get(0); 

        String model = productStringInfo.get(1);

        String description = productStringInfo.get(2);

        double unitPrice = productDoubleInfo.get(0);

        if (productObject.has("category")) {

            JsonObject categoryObject = productObject.get("category").getAsJsonObject();

            String categoryString = collectString(categoryObject, "categoryName");

            String brandString = collectString(categoryObject, "brandName");

            Category category = new Category(ProductCategory.valueOf(categoryString), ProductBrand.valueOf(brandString));

            return new Product(category, model, description, unitPrice);
        }

        String categoryString = collectString(productObject, "categoryName");

        String brandString = collectString(productObject, "brandName");

        ProductCategory category = ProductCategory.valueOf(categoryString);

        ProductBrand brand = ProductBrand.valueOf(brandString);

        Category categoryBrand = new Category(category, brand);

        Product product = new Product(categoryBrand,model,description,unitPrice);

        return product;
                 
    }

/////////////////////////////// a method that parses a list of product objects from a json /////////////////////////////////////////

    public static List<Product> parseProducts(String responseBody) {

        List<Product> products = new ArrayList<Product>();

        JsonArray productsArray = new JsonParser().parse(responseBody).getAsJsonArray();

        for (int i = 0 ; i < productsArray.size() ; i++) {

            String productJsonBody = productsArray.get(i).toString();

            Product product = parseProduct(productJsonBody);

            products.add(product);
        }

        return products;

    }
    
/////////////////////////////// a method that parses an inventory item object from a json //////////////////////////////////////////////////
    public static InventoryItem parseInventoryItem(String responseBody) {
    	
        JsonObject itemObject = new JsonParser().parse(responseBody).getAsJsonObject();

        Gson gson = new Gson();

        int quantity = collectInt(itemObject, "quantity");

        String dateAddedString = collectString(itemObject, "dateAdded");

        Date dateAdded = Date.valueOf(dateAddedString);

        Product product;

        if (itemObject.has("product")) {

            JsonObject productObject = itemObject.get("product").getAsJsonObject();

            String productJson = gson.toJson(productObject);

            product = parseProduct(productJson);
        }

        else {

            itemObject.remove("quantity");

            itemObject.remove("dateAdded");
    
            String productJson = gson.toJson(itemObject);
    
            product = parseProduct(productJson);
        }

        return new InventoryItem(product, quantity, dateAdded);
             
    }

/////////////////////////////// a method that parses a list of inventory item objects from a json /////////////////////////////////////////

    public static List<InventoryItem> parseInventoryItems(String responseBody) {

        List<InventoryItem> items = new ArrayList<InventoryItem>();

        JsonArray itemsArray = new JsonParser().parse(responseBody).getAsJsonArray();

        for (int i = 0 ; i < itemsArray.size() ; i++) {

            String itemJsonBody = itemsArray.get(i).toString();

            InventoryItem inventoryItem = parseInventoryItem(itemJsonBody);

            items.add(inventoryItem);
        }

        return items;

    }
}

