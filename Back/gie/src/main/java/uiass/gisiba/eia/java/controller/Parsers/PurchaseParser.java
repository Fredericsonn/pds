package uiass.gisiba.eia.java.controller.Parsers;

import java.sql.Date;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.PurchaseNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.inventory.Status;
import uiass.gisiba.eia.java.entity.purchases.EnterprisePurchase;
import uiass.gisiba.eia.java.entity.purchases.PersonPurchase;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;

public class PurchaseParser extends Parser {


    public static Purchase parsePersonPurchase(String json)  {

        JsonObject purchaseObject = new JsonParser().parse(json).getAsJsonObject();

        int purchaseId = collectInt(purchaseObject, "purchaseId");

        List<PurchaseOrder> orders = parsePurchaseOrders(json);

        Date purchaseDate = Date.valueOf(purchaseObject.get("purchaseDate").getAsString());

        double total = collectDouble(purchaseObject, "total");

        Status status = Status.valueOf(purchaseObject.get("status").getAsString());  

        Person supplier = parsePersonSupplier(json);
            
        Purchase purchase = new PersonPurchase(orders,purchaseDate, total, status, supplier);

        purchase.setPurchaseId(purchaseId);

        purchase.setOrders(orders);

        return purchase;  

    }

    public static Purchase parseEnterprisePurchase(String json)  {

        JsonObject purchaseObject = new JsonParser().parse(json).getAsJsonObject();

        int purchaseId = collectInt(purchaseObject, "purchaseId");

        List<PurchaseOrder> orders = parsePurchaseOrders(json);

        Date purchaseDate = Date.valueOf(purchaseObject.get("purchaseDate").getAsString());

        double total = collectDouble(purchaseObject, "total");

        Status status = Status.valueOf(purchaseObject.get("status").getAsString());  

        Enterprise supplier = parseEnterpriseSupplier(json);
            
        Purchase purchase = new EnterprisePurchase(orders,purchaseDate, total, status, supplier);

        purchase.setPurchaseId(purchaseId);

        purchase.setOrders(orders);

        return purchase;  //parseEnterprisePurchase

    }

    public static Purchase parsePurchase(String json, String purchaseType) throws InvalidContactTypeException {

        if (purchaseType.equals("Person")) return parsePersonPurchase(json);

        else if (purchaseType.equals("Enterprise")) return parseEnterprisePurchase(json);

        throw new InvalidContactTypeException(purchaseType);
    }

    public static List<Purchase> parsePurchases(String json) {

        JsonArray purchaseArray = new JsonParser().parse(json).getAsJsonArray();

        List<Purchase> purchases = new ArrayList<Purchase>();

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

        ordersObject.forEach(order -> {

            try {

                orders.add(OrderParser.parsePurchaseOrder(String.valueOf(order.getAsJsonObject())));

            } catch (CategoryNotFoundException | PurchaseNotFoundException e) {

                e.printStackTrace();
            }
        });

        return orders;
    }




    

}
