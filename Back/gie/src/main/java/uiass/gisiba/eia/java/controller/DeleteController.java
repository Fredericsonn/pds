package uiass.gisiba.eia.java.controller;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import uiass.gisiba.eia.java.dao.crm.HQLQueryManager;
import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.service.Service;

public class DeleteController {

    private static Service service = new Service();

    public static void deleteContactController(String contactType) throws InvalidContactTypeException {

        if (HQLQueryManager.contactTypeChecker(contactType)) {

            Spark.delete("/contact/:id", new Route()  {

                
                @Override
                public String handle(Request request, Response response) throws InvalidContactTypeException  {
    
                    int id = Integer.parseInt(request.params(":id"));
    
                        try {
    
                            service.deleteContact(id, contactType);
        
                            return "Contact deleted successfully.";
        
                        } catch (ContactNotFoundException | InvalidContactTypeException e) {
        
                            return e.getMessage();
                        }                 
                }
            }
    
            );
        }

        else throw new InvalidContactTypeException(contactType);
    }

    public static void removeAddressController() {

        Spark.delete("/address/:addressId", new Route() {

            @Override
            public String handle(Request request, Response response)  {

                int addressId = Integer.parseInt(request.params(":addressId"));

                try {

                    service.removeAddress(addressId);

                    return "Address deleted successfully.";

                } catch (AddressNotFoundException  e) {

                    return e.getMessage();
                } 
            }
        }

        );
    }
}
