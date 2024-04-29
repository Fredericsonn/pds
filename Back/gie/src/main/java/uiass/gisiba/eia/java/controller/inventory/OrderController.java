package uiass.gisiba.eia.java.controller.inventory;

import com.google.gson.Gson;
import uiass.gisiba.eia.java.service.Service;
import static spark.Spark.*;
import uiass.gisiba.eia.java.entity.inventory.Order;
import java.util.*;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;


public class OrderController {

	static Service service = new Service();
///////////////////////////////////////////////getAllOrderController/////////////////////////////////////////////
         public static void getAllOrderController() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/orders/:orderType", (req,res)-> {

		List<Order> ordersList = service.getAllOrders();
		
		res.type("application/json");
		   		   
		return ordersList;
		   
	   } , gson::toJson );
	}
}
