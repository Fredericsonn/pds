package uiass.gisiba.eia.java.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import uiass.gisiba.eia.java.dao.crm.HQLQueryManager;
import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.entity.crm.Address;

import uiass.gisiba.eia.java.entity.crm.EntrepriseType;
import uiass.gisiba.eia.java.service.Service;

public class PostController {

    private static Service service = new Service();

    public static void postAddressController() {

        Spark.post("/address/post", new Route() {

            @Override
            public String handle(Request request, Response response)  {

                JsonObject json = new JsonParser().parse(request.body()).getAsJsonObject();
                

                String country = json.get("country").getAsString();
                String city = json.get("city").getAsString();
                String zipCode = json.get("zipCode").getAsString();
                String region = json.get("region").getAsString();
                String neighborhood = json.get("neighborhood").getAsString();
                int houseNumber = json.get("houseNumber").getAsInt();                

                try {

                    service.addAddress(country, city, zipCode, region, neighborhood, houseNumber);

                } catch (AddressNotFoundException | DuplicatedAddressException e) {

                    return "The provided address is already linked to a contact.";
                } 

                return "Address created successfully.";

            }

            
        });
    }

    public static void postContactController(String contactType) throws InvalidContactTypeException {

        if (HQLQueryManager.contactTypeChecker(contactType)) {

            Gson gson = GetGson.getGson();

            Spark.post("/contact/" + contactType + "/post", new Route() {

            @Override
            public String handle(Request request, Response response)  {

                String body = request.body();

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

                        service.addContact(first_or_enterprise_name, EntrepriseType.valueOf(last_name_or_enterprise_type),
                        
                        phoneNumber, email, address);
                    }

                } catch (AddressNotFoundException | DuplicatedAddressException e) {

                    return "The provided address is already linked to a contact.";
                }
                 
                return "Contact created successfully.";
        }});

        }

        else throw new InvalidContactTypeException(contactType);

    }
}
