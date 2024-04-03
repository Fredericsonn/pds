package uiass.gisiba.eia.java.controller;

import java.io.IOException;

import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;               
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;

public class App {

    public static void main(String [] args ) {

       GetController.getAllContactsByTypeController();
       GetController.getContactByIdController();
       GetController.getAllAddressesController();
       GetController.getAddressByIdController();
       GetController.getContactByAddressIdController();
       GetController.getAllContactsController();

       DeleteController.deleteContactController();
       try {
        PostController.postContactController("Person");
        PostController.postContactController("Enterprise");
        
        UpdateController.updateContactController();
        UpdateController.updateAddressController();

    } catch (InvalidContactTypeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
     

    
            
      
        String json = "{" +
        "\"houseNumber\": 26," +
        "\"neighborhood\": \"Neighborhood 7\"," +
        "\"city\": \"kyoto\"," +
        "\"zipCode\": \"33254\"," +
        "\"region\": \"fukushima\"," +
        "\"country\": \"Japan\"" +
    "}";




        
      





    }
    				   		    
}
    		   
    	

    

