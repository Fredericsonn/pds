package uiass.gisiba.eia.java.controller.Parsers;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.PurchaseNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;
import uiass.gisiba.eia.java.entity.sales.SaleOrder;
import uiass.gisiba.eia.java.service.iService;
import uiass.gisiba.eia.java.service.Service;

public class OrderParser extends Parser {

    public static List<String> filter_columns = Arrays.asList("categoryName","brandName","modelName", "name");

    public static iService service = new Service();

    public static PurchaseOrder parsePurchaseOrder(String json) throws InventoryItemNotFoundException {
        
        JsonObject orderObject = new JsonParser().parse(json).getAsJsonObject();

        int itemId = collectInt(orderObject, "itemId");

        InventoryItem item = service.getInventoryItemById(itemId);

        int quantity = collectInt(orderObject, "quantity");

        Time orderTime = Time.valueOf(LocalTime.parse(collectString(orderObject, "orderTime")));

        PurchaseOrder order = new PurchaseOrder(item, orderTime, quantity);

        System.out.println(order);

        return order;

    }

    public static SaleOrder parseSaleOrder(String json) throws InventoryItemNotFoundException {
        
        JsonObject orderObject = new JsonParser().parse(json).getAsJsonObject();

        int itemId = collectInt(orderObject, "itemId");

        InventoryItem item = service.getInventoryItemById(itemId);

        int quantity = collectInt(orderObject, "quantity");

        double margin = collectDouble(orderObject, "profitMargin");

        Time orderTime = Time.valueOf(LocalTime.parse(collectString(orderObject, "orderTime")));

        SaleOrder order = new SaleOrder(item, orderTime, quantity, margin);

        return order;

    }

    public static List<PurchaseOrder> parsePurchaseOrders(String json) {
        
        return null;
    }

    public static int collectOrderQuantity(String json) {

        JsonObject orderObject = new JsonParser().parse(json).getAsJsonObject();

        String quantityValue = collectString(orderObject, "quantity");

        int quantity = Integer.parseInt(quantityValue);

        return quantity;
    }

    @SuppressWarnings("rawtypes")
    public static int updateOrderQuantityParser(String json) {

        JsonObject updateObject = new JsonParser().parse(json).getAsJsonObject();

        int quantity = collectInt(updateObject, "quantity");

        return quantity;
    }

    public static Map<String,Object> updateSaleOrderParser(String json) {

        Map<String,Object> map = new HashMap<String,Object>();

        JsonObject orderObject = new JsonParser().parse(json).getAsJsonObject();

        String quantityString = collectString(orderObject, "quantity");

        double profitMargin = collectDouble(orderObject, "profitMargin");

        System.out.println(profitMargin);

        int quantity = quantityString != null ? Integer.parseInt(quantityString) : 0;

        map.put("quantity", quantity);

        map.put("profitMargin", profitMarginAdapter(profitMargin));

        System.out.println(map);
        
        return map;

    }

    public static double profitMarginAdapter(double value) {

        return value / 100;
    }
}
