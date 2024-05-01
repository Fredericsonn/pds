package uiass.gisiba.eia.java.controller.Parsers;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Person;

public class ContactParser extends Parser {

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

    @SuppressWarnings("unchecked")
    // A method that collects contact data from a json :
    public static List contactValuesCollector(Gson gson, String body, String contactType) {

        List values = new ArrayList<>();

        JsonObject contact = gson.fromJson(body, JsonObject.class);

        String first_or_enterprise_name = contactType.equals("Person") ? collectString(contact, "firstName") 
        
        : collectString(contact, "enterpriseName") ; 

        String last_name_or_enterprise_type = contactType.equals("Person") ? collectString(contact, "lastName") 
        
        : collectString(contact, "type");

        String phoneNumber = collectString(contact, "phoneNumber");

        String email = collectString(contact, "email");

        // we put the collected contact values in the contact values list :
        values.addAll(Arrays.asList(first_or_enterprise_name, last_name_or_enterprise_type, phoneNumber, email));

        return values;
    }

    // A method that address contact data from a json :
    public static List addressValuesCollector(Gson gson, String body) {

        JsonObject address = gson.fromJson(body, JsonObject.class);

        int houseNumber = address.has("houseNumber") ?  collectInt(address, "houseNumber") : 0;

        String neighborhood = collectString(address, "neighborhood");

        String city = collectString(address, "city");

        String zipCode = collectString(address, "zipCode");


        String country = collectString(address, "country");

        return Arrays.asList(houseNumber,neighborhood,city,zipCode,country);

    }

///////////////////////////////////////////// SINGLE PARSERS ///////////////////////////////////////////////////////////////////////
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
///////////////////////////////////////////// Collection Parsers /////////////////////////////////////////////////////////////////

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
