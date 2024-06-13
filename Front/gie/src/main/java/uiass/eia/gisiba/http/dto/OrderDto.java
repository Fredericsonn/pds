package uiass.eia.gisiba.http.dto;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import uiass.eia.gisiba.http.DataSender;
import uiass.eia.gisiba.http.parsers.ProductParser;
import uiass.eia.gisiba.http.parsers.PurchaseParser;
import uiass.eia.gisiba.http.parsers.SaleParser;

public class OrderDto {


//////////////////////////////////////////////////// PURCHASE ORDERS ///////////////////////////////////////////////////////////////////

                              //////////////// GET METHODS //////////////// 

    // Find a purchase by its ref :
    public static List<String> getOrderById(int id) {

        String responseBody = DataSender.getDataSender("orders/purchase/byId/" + id);

        if (!responseBody.equals("Server Error.")) return PurchaseParser.parsePurchaseOrder(responseBody);

        return null;
    }

    // Find all the purchases by supplier type :
    public static List<List<String>> getAllPurchaseOrders() {

        String responseBody = DataSender.getDataSender("orders/purchase");;

        return PurchaseParser.parsePurchaseOrders(responseBody);

    }

    // Find all the purchases by supplier type :
    public static List<List<String>> getAllOrdersByPurchase(int purchaseId) {

        String responseBody = DataSender.getDataSender("orders/purchase/byPurchase/" + purchaseId);;

        return PurchaseParser.parsePurchaseOrders(responseBody);

    }

                              //////////////// POST METHODS //////////////// 

    // Find all the filtered products :
    public static List<List<String>> getFilteredPurchaseOrders(String json) {

        String responseBody = DataSender.postDataSender(json,"orders/purchase/filter");

        List<List<String>> orders = new ArrayList<List<String>>();

        JsonArray ordersArray = new JsonParser().parse(responseBody).getAsJsonArray();

        ordersArray.forEach(order -> orders.add(PurchaseParser.parsePurchaseOrder(String.valueOf(order.getAsJsonObject()))));

        return orders;

    }

                              //////////////// PUT METHODS //////////////// 

    // Find all the filtered products :
    public static String updateOrder(String json, int orderId) {

        if (json != null) return DataSender.putDataSender(json,"orders/purchase/put/" + orderId);

        return "Please provide a quantity to update";



    }


//////////////////////////////////////////////////// SALE ORDERS ///////////////////////////////////////////////////////////////////

                              //////////////// GET METHODS //////////////// 

    // Find a sale by its ref :
    public static List<String> getSaleOrderById(int id) {

        String responseBody = DataSender.getDataSender("orders/sale/byId/" + id);

        if (!responseBody.equals("Server Error.")) return SaleParser.parseSaleOrder(responseBody);

        return null;
    }

    // Find all the sales by supplier type :
    public static List<List<String>> getAllSaleOrders() {

        String responseBody = DataSender.getDataSender("orders/sale");;

        return SaleParser.parseSaleOrders(responseBody);

    }

    // Find all the sales by supplier type :
    public static List<List<String>> getAllOrdersBySale(int saleId) {

        String responseBody = DataSender.getDataSender("orders/sale/bySale/" + saleId);;

        return SaleParser.parseSaleOrders(responseBody);

    }

                              //////////////// POST METHODS //////////////// 

    // Find all the filtered products :
    public static List<List<String>> getFilteredSaleOrders(String json) {

        String responseBody = DataSender.postDataSender(json,"orders/sale/filter");

        List<List<String>> orders = new ArrayList<List<String>>();

        JsonArray ordersArray = new JsonParser().parse(responseBody).getAsJsonArray();

        ordersArray.forEach(order -> orders.add(SaleParser.parseSaleOrder(String.valueOf(order.getAsJsonObject()))));

        return orders;

    }

                              //////////////// PUT METHODS //////////////// 

    // Find all the filtered products :
    public static String updateSaleOrder(String json, int orderId) {

        if (json != null) return DataSender.putDataSender(json,"orders/sale/put/" + orderId);

        return "Please provide a quantity to update";



    }



}
