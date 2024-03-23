package uiass.gisiba.eia.java.controller;

import uiass.gisiba.eia.java.entity.crm.Person;

public class App {

    public static void main(String [] args ) {

		ContactController.getContactByIdController(200,"Person");
    
		String body = Parser.responseBodyGenerator("http://localhost:4567/contacts/id");

		System.out.println(Parser.parse(body,"Person"));


    }
    				   		    
}
    		   
    	

    

