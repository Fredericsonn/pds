package uiass.gisiba.eia.java.controller.inventory;

import static spark.Spark.*;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import spark.Request;
import spark.Response;
import spark.Route;
import uiass.gisiba.eia.java.controller.Parsers.InventoryItemParser;
import uiass.gisiba.eia.java.controller.Parsers.Parser;
import uiass.gisiba.eia.java.controller.Parsers.ProductParser;
import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.service.Service;
import uiass.gisiba.eia.java.service.iService;

public class InventoryItemController {

    private static iService service = new Service();

/////////////////////////////////////////////////// GET METHODS //////////////////////////////////////////////////////////////////

    public static void getItemById() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/inventoryItems/byId/:id", (req,res)-> {

		int id = Integer.parseInt(req.params(":id"));      

		InventoryItem item = service.getInventoryItemById(id);        
		
		res.type("application/json");

		return item;    
	
		   
		}, gson::toJson);


    }

	public static void getItemByProduct() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/inventoryItems/byProduct/:ref", (req,res)-> {

		String ref = req.params(":ref");      

		InventoryItem item = service.getInventoryItemByProduct(ref);        
		
		res.type("application/json");

		return item;    
	
		   
		}, gson::toJson);


    }

	public static void getAllItems() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/inventoryItems", (req,res)-> {

		List<InventoryItem> items = service.getAllInventoryItems();
		
		res.type("application/json");

		return items;
		   
		}, gson::toJson);

    }

	public static void getItemQuantity() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/inventoryItems/byId/quantity/:itemId", (req,res)-> {

        int itemId = Integer.parseInt(req.params(":itemId"));

		int quantity = service.getItemQuantity(itemId);
		
		res.type("application/json");

		return quantity;
		   
		}, gson::toJson);

    }

/////////////////////////////////////////////////// DELETE METHOD //////////////////////////////////////////////////////////////////

    public static void deleteItemController()  {

	    System.out.println("Server started.");

		delete("/inventoryItems/delete/:itemId", new Route()  {

			@Override
			public String handle(Request request, Response response)   {

				int itemId = Integer.parseInt(request.params(":itemId"));

				try {

					service.deleteInventoryItem(itemId);   
	
				} catch (InventoryItemNotFoundException  e) {
	
					return e.getMessage();
				}  
					
				return "Product out of stock.";
			}
		}

		);  
   }

/////////////////////////////////////////////////// PUT METHOD //////////////////////////////////////////////////////////////////

public static void updateItemController() {


	// A list of the product table's columns

	Gson gson = new Gson();

	put("/inventoryItem/put/:itemId" , new Route() {

		@Override
		public String handle(Request request, Response response) throws ProductNotFoundException  {

			System.out.println("Server started.");

			int itemId = Integer.parseInt(request.params(":itemId"));  // We take the id of the item to update from the url

			String body = request.body(); 	

			// We collect all the quantity value to update from the request body :
			JsonObject item = gson.fromJson(body, JsonObject.class);  // We create a json object from the body

			int quantity = Parser.collectInt(item, "quantity"); // We get the quantity value of the "quantity" attribute

			// And finally we update the product :
			try {
			  
				  service.updateInventoryItem(itemId, quantity);

			} catch (InventoryItemNotFoundException  e) {

				return e.getMessage();
			}
	   
			return "Item updated successfully.";


	}});


}

public static void updateItemUnitPriceController() {

	Gson gson = new Gson();

	put("/inventoryItem/put/unitPrice/:itemId" , new Route() {

		@Override
		public String handle(Request request, Response response) throws ProductNotFoundException  {

			System.out.println("Server started.");

			String body = request.body();

			int itemId = Integer.parseInt(request.params(":itemId"));  // We take the id of the item to update from the url

			double unitPrice = InventoryItemParser.parseUnitPrice(body);

			try {
				
				service.updateInventoryItemUnitPrice(itemId, unitPrice);

			} catch (InventoryItemNotFoundException e) {

				return e.getMessage();
			}
	   
			return "Item updated successfully.";


	}});


}

/////////////////////////////////////////////////// POST METHOD //////////////////////////////////////////////////////////////////

public static void postItemController() {

	// A list of the product table's columns

    post("/inventoryItems/post" , new Route() {

        @Override
        public String handle(Request request, Response response) throws ProductNotFoundException, CategoryNotFoundException  {

	        System.out.println("Server started.");

		    String body = request.body(); 	

			// We create the product using the parse method in the Parser class
			InventoryItem item = InventoryItemParser.parseInventoryItem(body);

			service.addInventoryItem(item.getProduct(), item.getUnitPrice(), item.getQuantity(), item.getDateAdded());;

			// The server response : 
		    return "Item created successfully.";

}});

}

	public static void itemSearchFilter() {
	
		post("/inventoryItems/filter" , new Route() {
	
			@Override
			public String handle(Request request, Response response) throws ProductNotFoundException, CategoryNotFoundException  {

				Gson gson = new Gson();

				System.out.println("Server started.");
		
				String body = request.body();
		
				Map<String,String> criteria = ProductParser.parseFilterCriteria(body, ProductParser.filter_columns);

				List<InventoryItem> items;

				try {

					items = service.getFilteredItems(criteria);

				} catch (InventoryItemNotFoundException | ProductNotFoundException e) {

					return "no items match the given criteria";

				}
						
				return gson.toJson(items);
	
	}});
	
	
	}
}
