package uiass.eia.gisiba.http.parsers;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PurchaseParser extends Parser {

    public static List parsePurchase(String json) {

        JsonObject purchaseObject = new JsonParser().parse(json).getAsJsonObject();

        String purchaseId = collectString(purchaseObject, "id");
        String purchaseDate = collectString(purchaseObject, "purchaseDate");
        String total = collectString(purchaseObject, "total");
        String status = collectString(purchaseObject, "status");
        String supplierType = collectString(purchaseObject, "supplierType");

        JsonObject supplierObject = purchaseObject.get("supplier").getAsJsonObject();

        List<String> supplier = ContactParser.parseContact(String.valueOf(supplierObject), supplierType);

        JsonArray ordersArray = purchaseObject.get("orders").getAsJsonArray();

        List<List<String>> orders = OrderParser.parseOrders(ordersArray.getAsString());

        return Arrays.asList(purchaseId, supplier, supplierType, orders, purchaseDate, total, status);
    }

    public static List<List> parsePurchases(String json) {

        List<List> purchases = new ArrayList<List>();

        JsonArray purchasesArray = new JsonParser().parse(json).getAsJsonArray();

        purchasesArray.forEach(purchase -> purchases.add(PurchaseParser.parsePurchase(purchase.getAsString())));

        return purchases;
    }
}
