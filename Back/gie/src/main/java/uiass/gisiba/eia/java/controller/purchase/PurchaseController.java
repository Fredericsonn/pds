package uiass.gisiba.eia.java.controller.purchase;

import static spark.Spark.*;

import java.sql.Date;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;

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
import uiass.gisiba.eia.java.dao.exceptions.InvalidFilterCriteriaMapFormatException;
import uiass.gisiba.eia.java.dao.exceptions.OperationNotModifiableException;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.PurchaseNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.inventory.Status;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;
import uiass.gisiba.eia.java.service.Service;



public class PurchaseController {

    private static Service service = new Service();

	private static Gson gsonWithSerializer = new GsonBuilder()
                .registerTypeAdapter(Purchase.class, (JsonSerializer<Purchase>) (purchase, typeOfSrc, context) -> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("purchaseId", purchase.getPurchaseId());
                    jsonObject.add("purchaseDate", context.serialize(purchase.getPurchaseDate().toString()));
                    return jsonObject;
                })
                .create();

/////////////////////////////////////////////////// GET METHODS //////////////////////////////////////////////////////////////////

	public static void getPurchaseById() {
  
		System.out.println("Server started.");

		get("/purchases/byId/:id", (req,res)-> {

		int id = Integer.parseInt(req.params("id"));

		Purchase purchase = service.getPurchaseById(id);
	
		res.type("application/json");

		return purchase;

	   
		}, gsonWithSerializer::toJson);
	}	

	public static void getAllPurchases() {
	  
	    System.out.println("Server started.");
	
	    get("/purchases", (req,res)-> {

			System.out.println("/purchases");

		List<Purchase> purchases = service.getAllPurchases();
		
		res.type("application/json");

		return purchases;
	
		   
		}, gsonWithSerializer::toJson);
	}

    public static void getAllPurchasesBySupplierType() {
	  
	    System.out.println("Server started.");
	
	    get("/purchases/:contactType", (req,res)-> {

        String contactType = req.params("contactType");

		List<Purchase> purchases = service.getAllPurchasesByContactType(contactType);
		
		res.type("application/json");

		return purchases;
	
		   
		}, gsonWithSerializer::toJson);
	}

	public static void getAllPurchasesByPersonSupplier() {
	  
	    System.out.println("Server started.");
	
	    get("/purchases/bySupplier/:supplierType/:supplierName", (req,res)-> {

		String supplierType = req.params("supplierType");

        String supplierName = req.params("supplierName");

		List<Purchase> purchases = service.getAllPurchasesBySupplier(supplierName,supplierType);
		
		res.type("application/json");

		return purchases;
	
		   
		}, gsonWithSerializer::toJson);
	}

	public static void getAllSuppliers() {
	  
	    System.out.println("Server started.");
	
	    get("/purchases/suppliers/:supplierType", (req,res)-> {

        String supplierType = req.params("supplierType");

		List<Contact> purchases = service.getAllSuppliers(supplierType);
		
		res.type("application/json");

		return purchases;
	
		   
		}, gsonWithSerializer::toJson);
	}

	public static void getAllPurchasesByStatus() {
	  
	    System.out.println("Server started.");
	
	    get("/purchases/byStatus/:status", (req,res)-> {

        String status = req.params("status");

		List<Purchase> purchases = service.getAllPurchasesByStatus(Status.valueOf(status));
		
		res.type("application/json");

		return purchases;
	
		   
		}, gsonWithSerializer::toJson);
	}

/////////////////////////////////////////////////// POST METHOD //////////////////////////////////////////////////////////////////

	public static void postPurchase() {

		post("/purchases/:supplierType/post", new Route() {

			@Override
			public Object handle(Request request, Response response) throws Exception {

				String body = request.body();

				System.out.println(body);

				String supplierType = request.params("supplierType");

				Purchase purchase = PurchaseParser.parsePurchase(body, supplierType);
				
				service.addPurchase(purchase);

				return "Purchase created successfully";
			}
			
		});
	}

	public static void purchasesFilter() {

		post("/purchases/filter", new Route() {

			@Override
			public Object handle(Request request, Response response)  {

				String body = request.body();
				
				try {

					Map<String, Object> criteria = PurchaseParser.parsePurchaseFilterCriteriaMap(body);

					List<Purchase> purchases = service.purchasesFilter(criteria);

					return gsonWithSerializer.toJson(purchases);

				} catch (InvalidFilterCriteriaMapFormatException e) {
					
					return e.getMessage();
				}
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

/////////////////////////////////////////////////// PUT METHODS /////////////////////////////////////////////////////////////////////

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

/////////////////////////////////////////////////// DELETE METHODS /////////////////////////////////////////////////////////////////////

	public static void removePurchaseOrderController() {

        delete("/purchases/delete/:purchaseId/removeOrder/:orderId" , new Route() {

            @Override
            public String handle(Request request, Response response) throws ProductNotFoundException  {

	            System.out.println("Server started.");

				int purchaseId = Integer.parseInt(request.params("purchaseId"));

				int orderId = Integer.parseInt(request.params("orderId"));

				try {
					service.removePurchaseOrder(purchaseId, orderId);
				} catch (PurchaseNotFoundException e) {

					return e.getMessage();

				} catch (OrderNotFoundException e) {

					return e.getMessage();
				}

				return "Purchase order removed successfully";


		}});

	}
	
}
