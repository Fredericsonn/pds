package uiass.gisiba.eia.java.controller.inventory;

import static spark.Spark.*;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;

import spark.Request;
import spark.Response;
import spark.Route;
import uiass.gisiba.eia.java.controller.Parsers.OrderParser;
import uiass.gisiba.eia.java.controller.Parsers.Parser;
import uiass.gisiba.eia.java.controller.Parsers.ProductParser;
import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidOrderTypeException;
import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Order;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;
import uiass.gisiba.eia.java.service.Service;
import uiass.gisiba.eia.java.service.iService;

public class PurchaseOrderController {


    private static iService service = new Service();

	private static Gson gsonWithSerializer = new GsonBuilder()
    	.registerTypeAdapter(PurchaseOrder.class, (JsonSerializer<PurchaseOrder>) (order, typeOfSrc, context) -> {
        	JsonObject jsonObject = new JsonObject();
        	jsonObject.addProperty("orderId", order.getOrderId());
        	// Serialize the item object as a JsonElement
        	JsonElement itemElement = context.serialize(order.getItem());
        	// Add the item element to the JsonObject
        	jsonObject.add("item", itemElement);
        	jsonObject.addProperty("quantity", order.getQuantity());
        	jsonObject.addProperty("orderTime", order.getOrderTime().toString());
			jsonObject.addProperty("purchaseDate", order.getPurchase().getPurchaseDate().toString());
        	return jsonObject;
    	})
    		.create();

/////////////////////////////////////////////////// GET METHODS //////////////////////////////////////////////////////////////////

    public static void getOrderById() {

	    System.out.println("Server started.");
	
	    get("/orders/purchaseOrders/byId/:id", (req,res)-> {

		int id = Integer.parseInt(req.params(":id"));      

		Order order = service.getOrderById(id, "Purchase");       
		
		res.type("application/json");

		return order; 
	
		   
		}, gsonWithSerializer::toJson);

    }

	public static void getAllPurchaseOrders() {
		
	    System.out.println("Server started.");
	
	    get("/orders/purchaseOrders", (req,res)-> {

		List<Order> orders = service.getAllOrdersByType("Purchase_Order");
		
		res.type("application/json");

		return orders;
		   
		}, gsonWithSerializer::toJson);

    }

	public static void getAllOrdersByPurchase() {
		
	    System.out.println("Server started.");
	
	    get("/orders/purchaseOrders/byPurchase/:purchaseId", (req,res)-> {

		int purchaseId = Integer.parseInt(req.params("purchaseId"));

		List<PurchaseOrder> orders = service.getAllOrdersByPurchase(purchaseId);
		
		res.type("application/json");

		return orders;
		   
		}, gsonWithSerializer::toJson);

    }

/////////////////////////////////////////////////// POST METHODs //////////////////////////////////////////////////////////////////

	public static void orderSearchFilter() {
	
		post("/orders/purchaseOrders/filter" , new Route() {
	
			@Override
			public String handle(Request request, Response response) throws ProductNotFoundException, CategoryNotFoundException, InvalidOrderTypeException  {

				System.out.println("Server started.");
		
				String body = request.body();
		
				Map<String,String> criteria = ProductParser.parseFilterCriteria(body, OrderParser.filter_columns);
		
				List<Order> orders = service.orderSearchFilter("Purchase",criteria);
						
				return gsonWithSerializer.toJson(orders);
	
	
	}});
	
	
	}

/////////////////////////////////////////////////// PUT METHODs //////////////////////////////////////////////////////////////////

public static void updateOrder() {
	
	put("/orders/purchaseOrders/put/:orderId" , new Route() {

		@Override
		public String handle(Request request, Response response) throws ProductNotFoundException, CategoryNotFoundException, InvalidOrderTypeException  {

			System.out.println("Server started.");
	
			String body = request.body();

			int orderId = Integer.parseInt(request.params("orderId"));

			int quantity = OrderParser.updateOrderQuantityParser(body);

			try {

				service.updateOrder(orderId, quantity, "Purchase");

			} catch (InvalidOrderTypeException e) {

				return e.getMessage();

			} catch (OrderNotFoundException e) {

				return e.getMessage();
			}

			return "Order updated successfully";


}});


}

/////////////////////////////////////////////////// DELETE METHOD //////////////////////////////////////////////////////////////////

public static void deleteOrderController()  {

	System.out.println("Server started.");

	delete("/orders/purchaseOrders/delete/:orderId", new Route()  {

		@Override
		public String handle(Request request, Response response)   {

			int orderId = Integer.parseInt(request.params("orderId"));

				try {

					service.deleteOrder(orderId, "Purchase");

				} catch (InvalidOrderTypeException e) {

					return e.getMessage();

				} catch (OrderNotFoundException e) {

					return e.getMessage();
				}   

			
				
			return "Purchase order deleted successfully.";
		}
	}

	);  
}





}
