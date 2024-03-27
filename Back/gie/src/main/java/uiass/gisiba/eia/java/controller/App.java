package uiass.gisiba.eia.java.controller;

import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;               
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;

public class App {

    public static void main(String [] args ) {


        UpdateController.updateAddressController();
    
            
      
        String json = "{" +
        "\"houseNumber\": 26," +
        "\"neighborhood\": \"Neighborhood 7\"," +
        "\"city\": \"kyoto\"," +
        "\"zipCode\": \"33254\"," +
        "\"region\": \"fukushima\"," +
        "\"country\": \"Japan\"" +
    "}";

         DataSender.putDataSender(json, "address/2");;
      





    }
    				   		    
}
    		   
    	

    

