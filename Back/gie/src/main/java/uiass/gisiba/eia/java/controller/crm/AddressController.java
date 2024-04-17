package uiass.gisiba.eia.java.controller.crm;

import java.util.*;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.*;
import uiass.gisiba.eia.java.controller.GetGson;
import uiass.gisiba.eia.java.controller.Parser;
import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.service.Service;

public class AddressController {

    static Service service = new Service();

/////////////////////////////////////////////////// GET METHODS //////////////////////////////////////////////////////////////////
    
    public static  void getAddressByIdController() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    get("/addresses/:addressId", (req,res)-> {

		int address_id = Integer.parseInt(req.params(":addressId"));

		Address address = service.getAddressById(address_id);
		
		res.type("application/json");
		   		   
		return address;
		   
	   } , gson::toJson);
	   
	   
	}

	public static void getAllAddressesController() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    get("/addresses", (req,res)-> {

		List<Address> addresses = service.getAllAddresses();
		
		res.type("application/json");
		   		   
		return addresses;
		   
	   } , gson::toJson );
	}

/////////////////////////////////////////////////// Update METHOD //////////////////////////////////////////////////////////////////

    public static void updateAddressController() {

        Gson gson = GetGson.getGson();

        System.out.println("Server started.");

        put("/addresses/put/:id", new Route() {

        @Override
        public String handle(Request request, Response response)  {

            // We retireve the address id from the url
            int addressId = Integer.parseInt(request.params(":id")); // We retrieve the id of the address to update from the url

            String body = request.body(); // We get the json containing update information as string
            
            // We collect all the values to update from the request body in one list :
            List addressValues = Parser.addressValuesCollector(gson, body);

            // We select only the non null values with their corresponding columns and form the update map :
            Map<String, Object> address_new_values = Parser.mapFormater(Parser.address_columns, addressValues);
            
            // the address_update_columns list in the Parser class contains columns name used to parse Address jsons

            // And finally we update the address :
            try {
                    
                service.updateAddress(addressId, address_new_values);

            } catch (AddressNotFoundException  e) {

                return e.getMessage();
            }
             
            return "Address updated successfully.";
    }});

    }
}
