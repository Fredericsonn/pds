package uiass.gisiba.eia.java.controller.inventory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import spark.Request;
import spark.Response;
import spark.Route;
import uiass.gisiba.eia.java.service.Service;
import static spark.Spark.*;
import uiass.gisiba.eia.java.controller.Parsers.Parser;
import uiass.gisiba.eia.java.dao.exceptions.InvalidOrderTypeException;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
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
        
        ///////////////////////////////////UpdateOrderController///////////////////////////////////////////
        public static void UpdateOrderController(){
             Gson gson = new Gson();
    
            put("/orders/:orderType/put/:id" , new Route() {
    
                @Override
                public String handle(Request request, Response response) throws OrderNotFoundException, InvalidOrderTypeException  {
    
                    System.out.println("Server started.");
    
                    // We take the id of the order to update from the url
    
                    int orderId = Integer.parseInt(request.params(":id"));  
    
                    // We retireve the order  type from the url 
    
                    String orderType = String.valueOf(request.params(":orderType"));
    
                    // We collect the quantity value to update from the request body :
    
                    String body = request.body(); 
    
                    JsonObject jsonObject = gson.fromJson(body, JsonObject.class);  // We create a json object from the body
    
                    int quantity = Parser.collectInt(jsonObject, "quantity"); // We get the quantity value of the "quantity" attribute
                    
                    // And finally we update the product :
                    try {
                      
                          service.updateOrder( orderId,  quantity,  orderType) ;
    
                    } catch (OrderNotFoundException|InvalidOrderTypeException e) {
    
                        return e.getMessage();
                    }
               
                    return "order Updated successfully.";
    
    
            }});
        }
        }