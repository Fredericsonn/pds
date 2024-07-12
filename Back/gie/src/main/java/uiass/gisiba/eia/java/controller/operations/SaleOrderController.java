package uiass.gisiba.eia.java.controller.operations;

import java.util.*;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;

import spark.Request;
import spark.Response;
import spark.Route;
import uiass.gisiba.eia.java.controller.Parsers.OrderParser;
import uiass.gisiba.eia.java.controller.Parsers.ProductParser;
import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidOrderTypeException;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Order;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;
import uiass.gisiba.eia.java.entity.sales.Sale;
import uiass.gisiba.eia.java.entity.sales.SaleOrder;
import uiass.gisiba.eia.java.service.Service;
import uiass.gisiba.eia.java.service.iService;

public class SaleOrderController {

    private static iService service = new Service();

	private static Gson gsonWithSerializer = new GsonBuilder()
    	.registerTypeAdapter(SaleOrder.class, (JsonSerializer<SaleOrder>) (order, typeOfSrc, context) -> {
        	JsonObject jsonObject = new JsonObject();
        	jsonObject.addProperty("orderId", order.getOrderId());
        	// Serialize the item object as a JsonElement
        	JsonElement itemElement = context.serialize(order.getItem());
        	// Add the item element to the JsonObject
        	jsonObject.add("item", itemElement);
        	jsonObject.addProperty("quantity", order.getQuantity());
            jsonObject.addProperty("profitMargin", order.getProfitMargin());
        	jsonObject.addProperty("orderTime", order.getOrderTime().toString());
			jsonObject.addProperty("saleDate", order.getSale().getSaleDate().toString());
        	return jsonObject;
    	})
    		.create();

/////////////////////////////////////////////////// GET METHODS //////////////////////////////////////////////////////////////////

    public static void getOrderById() {

	    System.out.println("Server started.");
	
	    get("/orders/sale/byId/:id", (req,res)-> {

		int id = Integer.parseInt(req.params(":id"));      

		Order order = service.getOrderById(id, "Sale");       
		
		res.type("application/json");

		return order; 
	
		   
		}, gsonWithSerializer::toJson);

    }

	public static void getAllOrders() {
		
	    System.out.println("Server started.");
	
	    get("/orders/sale", (req,res)-> {

		List<Order> orders = service.getAllOrdersByType("Sale_Order");
		
		res.type("application/json");

		return orders;
		   
		}, gsonWithSerializer::toJson);

    }

	public static void getAllOrdersBySale() {
		
	    System.out.println("Server started.");
	
	    get("/orders/sale/bySale/:saleId", (req,res)-> {

		int saleId = Integer.parseInt(req.params("saleId"));

		List<SaleOrder> orders = service.getSaleOrders(saleId);
		
		res.type("application/json");

		return orders;
		   
		}, gsonWithSerializer::toJson);

    }

/////////////////////////////////////////////////// POST METHODs //////////////////////////////////////////////////////////////////

	public static void orderSearchFilter() {
	
		post("/orders/sale/filter" , new Route() {
	
			@Override
			public String handle(Request request, Response response) throws ProductNotFoundException, CategoryNotFoundException, InvalidOrderTypeException  {

				System.out.println("Server started.");
		
				String body = request.body();
		
				Map<String,String> criteria = ProductParser.parseFilterCriteria(body, OrderParser.filter_columns);
		
				List<Order> orders = service.orderSearchFilter("Sale",criteria);
						
				return gsonWithSerializer.toJson(orders);
	
	
	}});
	
	
	}

/////////////////////////////////////////////////// PUT METHODs //////////////////////////////////////////////////////////////////

public static void updateOrder() {
	
	put("/orders/sale/put/:orderId" , new Route() {

		@Override
		public String handle(Request request, Response response) throws ProductNotFoundException, CategoryNotFoundException, InvalidOrderTypeException  {

			System.out.println("Server started.");
	
			String body = request.body();

			int orderId = Integer.parseInt(request.params("orderId"));

			Map<String,Object> map = OrderParser.updateSaleOrderParser(body);

			try {

				service.updateSaleOrder(orderId, map);

			} catch (InvalidOrderTypeException e) {

				return e.getMessage();

			} catch (OrderNotFoundException e) {

				return e.getMessage();
			}

			return "Order Updated Successfully";


}});


}

/////////////////////////////////////////////////// DELETE METHOD //////////////////////////////////////////////////////////////////

public static void deleteOrderController()  {

	System.out.println("Server started.");

	delete("/orders/sale/delete/:orderId", new Route()  {

		@Override
		public String handle(Request request, Response response)   {

			int orderId = Integer.parseInt(request.params("orderId"));

				try {

					service.deleteOrder(orderId, "Sale");

				} catch (InvalidOrderTypeException e) {

					return e.getMessage();

				} catch (OrderNotFoundException e) {

					return e.getMessage();
				}   
			
			return "Sale order deleted successfully.";
		}
	}

	);  
}
}
