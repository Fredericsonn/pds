package uiass.gisiba.eia.java.controller;

import uiass.gisiba.eia.java.service.Service;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.*;
import uiass.gisiba.eia.java.dao.crm.HQLQueryManager;
import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.entity.crm.Address;

import uiass.gisiba.eia.java.entity.crm.EntrepriseType;
import uiass.gisiba.eia.java.service.Service;

public class UpdateController {

    private static Service service = new Service();

    private static Map<String, List<String>> contact_columns_by_type_map = new HashMap<String, List<String>>() {{
        put("Person", Arrays.asList("first_name","last_name","phone_number","email"));
        put("Enterprise", Arrays.asList("enterprise_name","type","phone_number","email"));
    }};

    private static List<String> addressColumns = Arrays.asList("house_number","neighborhood","city","zip_code","region","country");

    // Columns filter 
    public static Map<String, Object> mapFormater(List<String> columns, List values) {

        Map<String, Object> columns_new_values = new HashMap<String, Object>();

        for (int i = 0; i < columns.size() ; i++) {

            String column = columns.get(i);

            Object value = values.get(i);

            if (value != null) {

                columns_new_values.put(column,value);
                System.out.println(columns_new_values);
            }
        }

        return columns_new_values;
    }

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
    public static void updateContactController(String contactType) throws InvalidContactTypeException {

        if (HQLQueryManager.contactTypeChecker(contactType)) {

            List<String> columns = contact_columns_by_type_map.get(contactType);  // A list of the contact table's columns

            Gson gson = GetGson.getGson();

            Spark.put("/contacts/" + contactType + "/put" + "/:id" , new Route() {

            @Override
            public String handle(Request request, Response response)  {

                System.out.println("Server started.");

                int id = Integer.parseInt(request.params(":id"));  // We take the id of the contact to update from the url

                String body = request.body();  

                // We collect all the values to update from the request body in one list :
                List contactValues = contactValuesCollector(gson, body, contactType);

                // We select only the non null values with their corresponding columns :
                Map<String, Object> contact_columns_new_values = mapFormater(columns, contactValues);

                // And finally we update the contact :
                try {
                        
                    service.updateContact(id, contact_columns_new_values, contactType);;

                } catch (ContactNotFoundException | InvalidContactTypeException  e) {

                    return e.getMessage();
                }
                 
                return "Contact updated successfully.";
        }});

        }

        else throw new InvalidContactTypeException(contactType);

    }

    public static List addressValuesCollector(Gson gson, String body) {

        JsonObject address = gson.fromJson(body, JsonObject.class);

        int houseNumber = Parser.collectInt(address, "houseNumber");

        String neighborhood = Parser.collectString(address, "neighborhood");

        String city = Parser.collectString(address, "city");

        String zipCode = Parser.collectString(address, "zipCode");

        String region = Parser.collectString(address, "region");

        String country = Parser.collectString(address, "country");

        return Arrays.asList(houseNumber,neighborhood,city,zipCode,region,country);

    }

    public static void updateAddressController() {

        Gson gson = GetGson.getGson();

        Spark.put("/addresses/put/:id", new Route() {

        @Override
        public String handle(Request request, Response response)  {

            System.out.println("Server started.");

            int addressId = Integer.parseInt(request.params(":id"));  // We take the id of the contact to update from the url

            String body = request.body(); 

            System.out.println(body);
            
            // We collect all the values to update from the request body in one list :
            List addressValues = addressValuesCollector(gson, body);

            System.out.println(addressValues);

            // We select only the non null values with their corresponding columns :
            Map<String, Object> address_new_values = mapFormater(addressColumns, addressValues);

            // And finally we update the address :
            try {
                    
                service.updateAddress(addressId, address_new_values);;

            } catch (AddressNotFoundException  e) {

                return e.getMessage();
            }
             
            return "Address updated successfully.";
    }});

    }
}

