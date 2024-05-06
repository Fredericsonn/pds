package uiass.gisiba.eia.java.controller.purchase;

import static spark.Spark.*;

import java.util.*;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;
import uiass.gisiba.eia.java.controller.Parsers.CategoryParser;
import uiass.gisiba.eia.java.controller.Parsers.OrderParser;
import uiass.gisiba.eia.java.controller.Parsers.Parser;
import uiass.gisiba.eia.java.controller.Parsers.PurchaseParser;
import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.OperationNotModifiableException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.PurchaseNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.inventory.Category;
import uiass.gisiba.eia.java.entity.inventory.Status;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;
import uiass.gisiba.eia.java.service.Service;



public class PurchaseController {

    private static Service service = new Service();

/////////////////////////////////////////////////// GET METHODS //////////////////////////////////////////////////////////////////

	public static void getPurchaseById() {

		Gson gson = new Gson();
  
		System.out.println("Server started.");

		get("/purchases/byId/:id", (req,res)-> {

		int id = Integer.parseInt(req.params("id"));

		Purchase purchase = service.getPurchaseById(id);
	
		res.type("application/json");

		return purchase;

	   
		}, gson::toJson);
	}	

	public static void getAllPurchases() {

		Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/purchases", (req,res)-> {

		List<Purchase> purchases = service.getAllPurchases();
		
		res.type("application/json");

		return purchases;
	
		   
		}, gson::toJson);
	}

    public static void getAllPurchasesBySupplierType() {

		Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/purchases/:contactType", (req,res)-> {

        String contactType = req.params("contactType");

		List<Purchase> purchases = service.getAllPurchasesByContactType(contactType);
		
		res.type("application/json");

		return purchases;
	
		   
		}, gson::toJson);
	}

	public static void getAllPurchasesBySupplier() {

		Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/purchases/:contactType/bySupplier/:supplierId", (req,res)-> {

		String contactType = req.params("contactType");

        int supplierId = Integer.parseInt(req.params("supplierId"));

		Contact supplier = service.getContactById(supplierId, contactType);

		Person p = null;

		//List<Purchase> purchases = service.getAllPurchasesBySupplier(supplier);
		
		res.type("application/json");

		return p;
	
		   
		}, gson::toJson);
	}

	public static void getAllPurchasesByStatus() {

		Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/purchases/:status", (req,res)-> {

        String status = req.params("status");

		List<Purchase> purchases = service.getAllPurchasesByStatus(Status.valueOf(status));
		
		res.type("application/json");

		return purchases;
	
		   
		}, gson::toJson);
	}

/////////////////////////////////////////////////// POST METHOD //////////////////////////////////////////////////////////////////

	public static void postPurchase() {

		post("/purchases/:supplierType/post", new Route() {

			@Override
			public Object handle(Request request, Response response) throws Exception {

				String body = request.body();

				String contactType = request.params("supplierType");

				Purchase purchase = PurchaseParser.parsePurchase(body, contactType);
				
				Person supplier = PurchaseParser.parsePersonSupplier(body);

				service.addPurchase(purchase.getOrders(), purchase.getPurchaseDate(), purchase.getTotal()
				
				, supplier, purchase.getStatus());

				return "Purchase created successfully";
			}
			
		});
	}

/////////////////////////////////////////////////// DELETE METHOD //////////////////////////////////////////////////////////////////

    public static void deletePurchaseController()  {

        System.out.println("Server started.");

        delete("/purchases/delete/:id", new Route()  {

            @Override
            public String handle(Request request, Response response) {

                int id = Integer.parseInt(request.params(":id"));
      
                try {

					service.deletePurchase(id);

				} catch (PurchaseNotFoundException e) {

					return e.getMessage();
				}     
                           
                return "Purchase deleted successfully.";
            }
        }
    
        );  
    }

/////////////////////////////////////////////////// PUT METHOD /////////////////////////////////////////////////////////////////////

    public static void updatePurchaseOrdersController() {

        put("/purchases/put/orders/:id" , new Route() {

            @Override
            public String handle(Request request, Response response) throws ProductNotFoundException  {

	            System.out.println("Server started.");

				String body = request.body();

				int purchaseId = Integer.parseInt(request.params("id"));

				List<PurchaseOrder> orders = OrderParser.parsePurchaseOrders(body);

				try {

					service.updatePurchaseOrders(purchaseId, orders);

				} catch (PurchaseNotFoundException e) {

					return e.getMessage();

				} catch (OperationNotModifiableException e) {

					return e.getMessage();
				}

				return "Purchase orders updated successfully";


		}});

	}

	public static void updatePurchaseStatusController() {

        put("/purchases/put/status/:id" , new Route() {

            @Override
            public String handle(Request request, Response response) throws ProductNotFoundException  {

	            System.out.println("Server started.");

				String body = request.body();

				int purchaseId = Integer.parseInt(request.params("id"));

				Status status = PurchaseParser.parseStatus(body);

				try {

					service.updatePurchaseStatus(purchaseId, status);

				} catch (PurchaseNotFoundException e) {

					return e.getMessage();
				}

				return "Purchase status updated successfully";


		}});

	}
	
}
