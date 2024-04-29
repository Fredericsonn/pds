package uiass.gisiba.eia.java.controller.inventory;

import com.google.gson.Gson;
import uiass.gisiba.eia.java.service.Service;
import static spark.Spark.*;
import uiass.gisiba.eia.java.entity.inventory.Order;
import java.util.*;


public class OrderController {

	static Service service = new Service();

//////////////////////////////////////////getAllOrdersByTypeController///////////////////////////////////
    public static void getAllOrdersByTypeController() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/orders/:orderType", (req,res)-> {

		String orderType = String.valueOf(req.params(":orderType"));

		List<Order> ordersList = service.getAllOrdersByType(orderType);
		
		res.type("application/json");
		   		   
		return ordersList;
		   
	   } , gson::toJson );
	}


}
