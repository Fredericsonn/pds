package uiass.eia.gisiba.http.dto;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import uiass.eia.gisiba.http.DataSender;
import uiass.eia.gisiba.http.parsers.OrderParser;
    
    public class OrdrerDto {
        
        public static List<List<String>> getAllOrdersByType(String orderType) {
    
            String responseBody = DataSender.getDataSender("orders/" + orderType);
    
            List<List<String>> orders = new ArrayList<List<String>>();
    
            JsonArray ordersArray = new JsonParser().parse(responseBody).getAsJsonArray();
    
            ordersArray.forEach(elt -> orders.add(OrderParser.parseOrder(String.valueOf(elt.getAsJsonObject()), orderType)) );
    
    
                return orders ;}
        /////////////////////////////////////////UPDATE ///////////////////////////////////
        public static String updateOrder(int id, String orderType, String json) {
    
            if (json != null) return DataSender.putDataSender(json, "orders/" + orderType + "/put" + "/" + id );
    
            return "orders Updated Successfully.";
        }
    }