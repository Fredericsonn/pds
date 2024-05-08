package uiass.eia.gisiba.http.parsers;

import java.util.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OrderParser extends Parser {
    
    
        public static List parseOrder(String responseBody, String orderType) {
    
            JsonObject order = new JsonParser().parse(responseBody).getAsJsonObject();
            
       
            String id = collectString(order, "orderId");
            String  quantity = collectString(order, "quantity");
            String time = collectString(order, "time");
    
            JsonObject itemObject = order.get("item").getAsJsonObject();
    
            List<String> item = InventoryItemParser.parseItem(itemObject.getAsString());
    
            String category = item.get(2);
    
            String brand = item.get(3);
    
            String model = item.get(4);
    
            String name = item.get(5);
    
            String unitPrice = item.get(6);
    
            return Arrays.asList(id,category,brand,model,name,quantity,time);

}
              public static List<List<String>> parseOrders(String json) {
        
                         return null;
               }
}