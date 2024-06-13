package uiass.eia.gisiba.http.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SaleParser extends Parser {

    public static List<String> parseSale(String json) {

        JsonObject saleObject = new JsonParser().parse(json).getAsJsonObject();

        String saleId = collectString(saleObject, "saleId");
        String saleDate = collectString(saleObject, "saleDate");
        String total = collectString(saleObject, "total");
        String status = collectString(saleObject, "status");

        JsonObject customerObject = saleObject.get("customer").getAsJsonObject();

        String customerType = customerObject.has("type") ? "Enterprise" : "Person";

        List<String> customer = ContactParser.parseContact(String.valueOf(customerObject), customerType);

        String customerName = customer.get(1);

        if (customerType.equals("Person"))  customerName += " " + customer.get(2);

        String customerId = customer.get(0);

        return Arrays.asList(saleId, customerId, customerName, customerType, saleDate, total, status);
    }

    public static List<String> parseSaleOrder(String json) {

        JsonObject orderObject = new JsonParser().parse(json).getAsJsonObject();

        String orderId = collectString(orderObject, "orderId");

        String quantity = collectString(orderObject, "quantity");

        String profitMargin = collectString(orderObject, "profitMargin");

        String orderTime = collectString(orderObject, "orderTime");

        String saleDate = collectString(orderObject, "saleDate");

        JsonObject itemOject = orderObject.get("item").getAsJsonObject();

        List<String> item = InventoryItemParser.parseItem(String.valueOf(itemOject));

        String itemId = item.get(0);

        String category = item.get(1);

        String brand = item.get(2);

        String model = item.get(3);

        String name = item.get(4);

        String unitPrice = item.get(5);

        return Arrays.asList(orderId, itemId, category, brand, model, name, unitPrice, quantity, saleDate + ", " + orderTime, profitMargin);
    }
    public static List<List<String>> parseSaleOrders(String json) {

        JsonArray ordersArray = new JsonParser().parse(json).getAsJsonArray();

        List<List<String>> orders = new ArrayList<>();
    
        ordersArray.forEach(order -> {

            JsonObject orderObject = order.getAsJsonObject();

            orders.add(parseSaleOrder(orderObject.toString()));

        });
    
        return orders;
    }
    
    public static List<List<String>> parseSales(String json) {
        
        List<List<String>> sales = new ArrayList<List<String>>();

        JsonArray salesArray = new JsonParser().parse(json).getAsJsonArray();

        salesArray.forEach(sale -> sales.add(SaleParser.parseSale(String.valueOf(sale.getAsJsonObject()))));

        return sales;
    }

    public static Map<String,Object> salesDatesFilterMapGenerator(String type, List<String> values) {

        Map<String,Object> map = new HashMap<String,Object>();

        if (type.equals("after")) {

            String beforeDate = values.get(0);

            map.put("afterDate", beforeDate);
        }

        else if (type.equals("before")) {

            String afterDate = values.get(1);

            map.put("beforeDate", afterDate);
        }

        else if (type.equals("between")) {

            String startDate = values.get(0);

            String endDate = values.get(1);

            map.put("startDate", startDate);

            map.put("endDate", endDate);
        }

        return map;
    }

    public static Map<String,Object> salesCustomerFilterMapGenerator(String customerName, String customerType) {

        Map<String,Object> customerMap = new HashMap<String,Object>();

        customerMap.put("customerName", customerName);

        customerMap.put("customerType", customerType);

        return customerMap;
    }
}
