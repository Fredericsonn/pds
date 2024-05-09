package uiass.gisiba.eia.java.controller.Parsers;

import java.sql.Time;
import java.util.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.PurchaseNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;

import uiass.gisiba.eia.java.service.iService;
import uiass.gisiba.eia.java.service.Service;

public class OrderParser extends Parser {

    public static List<String> filter_columns = Arrays.asList("categoryName","brandName","modelName", "name");

    public static iService service = new Service();

    public static PurchaseOrder parsePurchaseOrder(String json) throws CategoryNotFoundException, PurchaseNotFoundException {
        
        JsonObject orderObject = new JsonParser().parse(json).getAsJsonObject();

        int orderId = collectInt(orderObject, "orderId");

        JsonObject itemObject = orderObject.get("product").getAsJsonObject();

        InventoryItem item = InventoryItemParser.parseInventoryItem(itemObject.getAsString());

        int quantity = collectInt(itemObject, "quantity");

        Time time = Time.valueOf(collectString(itemObject, "time"));

        int purchaseId = collectInt(itemObject, "purchase");

        Purchase purchase = service.getPurchaseById(purchaseId);

        PurchaseOrder order = new PurchaseOrder(item, quantity, time);

        order.setOrderId(orderId);

        order.setPurchase(purchase);

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
}
