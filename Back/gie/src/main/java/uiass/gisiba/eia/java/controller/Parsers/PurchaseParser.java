package uiass.gisiba.eia.java.controller.Parsers;

import java.sql.Date;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.inventory.Status;
import uiass.gisiba.eia.java.entity.purchases.EnterprisePurchase;
import uiass.gisiba.eia.java.entity.purchases.PersonPurchase;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;

public class PurchaseParser extends Parser {


    public static Purchase parsePurchase(String json, String contactType) throws InvalidContactTypeException {

        JsonObject purchaseObject = new JsonParser().parse(json).getAsJsonObject();

        Purchase purchase;

        int purchaseId = collectInt(purchaseObject, "purchaseId");

        List<PurchaseOrder> orders = parsePurchaseOrders(json);

        Date purchaseDate = Date.valueOf(purchaseObject.get("purchaseDate").getAsString());

        double total = collectDouble(purchaseObject, "total");

        Status status = Status.valueOf(purchaseObject.get("status").getAsString());  

        if (contactType.equals("Person")) {

            Person supplier = parsePersonSupplier(json);
            
            purchase = new PersonPurchase(orders, purchaseDate, total, status, supplier);

            purchase.setPurchaseId(purchaseId);

            return purchase;

        }

        else if (contactType.equals("Enterprise")) {

            Enterprise supplier = parseEnterpriseSupplier(json);

            purchase = new EnterprisePurchase(orders, purchaseDate, total, status, supplier);

            purchase.setPurchaseId(purchaseId);

            return purchase;


        }

        throw new InvalidContactTypeException(contactType);
    }

    public static List<Purchase> parsePurchases(String json) {

        return null;
    }

    public static Person parsePersonSupplier(String json)  {

        JsonObject purchaseObject = new JsonParser().parse(json).getAsJsonObject();

        JsonObject supplierObject = purchaseObject.get("supplier").getAsJsonObject();

        Person supplier = ContactParser.parsePerson(String.valueOf(supplierObject));

        return supplier;
    }

    public static Enterprise parseEnterpriseSupplier(String json)  {

        JsonObject purchaseObject = new JsonParser().parse(json).getAsJsonObject();

        JsonObject supplierObject = purchaseObject.get("supplier").getAsJsonObject();

        Enterprise supplier = ContactParser.parseEnterprise(String.valueOf(supplierObject));

        return supplier;
    }

    public static Status parseStatus(String json) {

        JsonObject statusObject = new JsonParser().parse(json).getAsJsonObject();

        String status = statusObject.get("status").getAsString();

        return Status.valueOf(status);
    }

    public static List<PurchaseOrder> parsePurchaseOrders(String json) {

        JsonObject purchaseObject = new JsonParser().parse(json).getAsJsonObject();

        JsonArray ordersObject = purchaseObject.get("orders").getAsJsonArray();

        List<PurchaseOrder> orders = new ArrayList<PurchaseOrder>();

        ordersObject.forEach(order -> orders.add(OrderParser.parsePurchaseOrder(String.valueOf(order.getAsJsonObject()))));

        return orders;
    }

    public static String purchaseAdapter(Purchase purchase) {

        Gson gson = new Gson();

        int purchaseId = purchase.getPurchaseId();

        List<PurchaseOrder> orders = purchase.getOrders();

        purchase.setOrders(null);

        orders.forEach(order -> order.setPurchase(null));

        String ordersJson = gson.toJson(orders);

        JsonArray ordersArray = new JsonParser().parse(ordersJson).getAsJsonArray();

        ordersArray.forEach(order -> order.getAsJsonObject().addProperty("purchase", purchaseId));

        System.out.println(ordersArray.get(0));

        String purchaseJson = gson.toJson(purchase);

        /*JsonObject purchaseObject = new JsonParser().parse(purchaseJson).getAsJsonObject();

        purchaseObject.addProperty("orders", ordersArray.getAsString());

        String purchaseAdaptedJson = purchaseObject.getAsString();*/

        return null;


    }

    public static String purchasesAdapter(List<Purchase> purchases) {

        JsonArray purchaseArray = new JsonArray();

        purchases.forEach(purchase -> purchaseArray.add(purchaseAdapter(purchase)));

        return purchaseArray.getAsString();

    }

    

}
