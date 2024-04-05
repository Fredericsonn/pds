package uiass.gisiba.eia.java.controller;

import java.io.IOException;

import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;               
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;

public class App {

    public static void main(String [] args ) {

        ContactController.getContactByIdController();
        ContactController.getContactByNameController();
        ContactController.getAllContactsByTypeController();
        ContactController.getContactByAddressIdController();

        ContactController.postContactController();
        ContactController.updateContactController();
        ContactController.deleteContactController();

        AddressController.updateAddressController();

        ContactController.postEmailController();


            

        
        


     

    
            
      
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
    		   
    	

    

