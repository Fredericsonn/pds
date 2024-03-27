package uiass.gisiba.eia.java.controller;

import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;               
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;

public class App {

    public static void main(String [] args ) {

        UpdateController.updateAddressController();
        
      
         /*String json = "{\r\n" + //
                "            \"houseNumber\": 200,\r\n" + //
                "            \"neighborhood\": \"Neighborhood 8\",\r\n" + //
                "            \"city\": \"Jerusalem\",\r\n" + //
                "            \"zipCode\": \"7777\",\r\n" + //
                "            \"region\": \"west bank\",\r\n" + //
                "            \"country\": \"Palestine\"\r\n" + //
                "        }";*/

          String json = "{\r\n" + //
                "  \"city\": \"madrid\"\r\n" + //
                "}";
      

        DataSender.putDataSender(json, "address/1000");




    }
    				   		    
}
    		   
    	

    

