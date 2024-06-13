package uiass.gisiba.eia.java.controller.Parsers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uiass.gisiba.eia.java.dao.crm.ContactDao;
import uiass.gisiba.eia.java.dao.crm.iContactDao;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidFilterCriteriaMapFormatException;
import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.inventory.Status;
import uiass.gisiba.eia.java.entity.purchases.EnterprisePurchase;
import uiass.gisiba.eia.java.entity.purchases.PersonPurchase;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;


public class PurchaseParser extends OperationParser {

    private static iContactDao cdao = new ContactDao();

    public static Purchase parsePersonPurchase(String json) throws ContactNotFoundException  {

        JsonObject purchaseObject = new JsonParser().parse(json).getAsJsonObject();

        List<PurchaseOrder> orders = parsePurchaseOrders(json);

        Status status = Status.valueOf(purchaseObject.get("status").getAsString());  

        double total = purchaseObject.get("total").getAsDouble();

        int supplierId = collectInt(purchaseObject, "supplierId");

        Person supplier = cdao.getPersonById(supplierId);
            
        Purchase purchase = new PersonPurchase(orders, total, status, supplier);

        orders.forEach(order -> order.setPurchase(purchase));

        purchase.setOrders(orders);

        return purchase;  

    }

    public static Purchase parseEnterprisePurchase(String json) throws ContactNotFoundException  {

        JsonObject purchaseObject = new JsonParser().parse(json).getAsJsonObject();

        List<PurchaseOrder> orders = parsePurchaseOrders(json);

        Status status = Status.valueOf(purchaseObject.get("status").getAsString());  

        double total = purchaseObject.get("total").getAsDouble();

        int supplierId = collectInt(purchaseObject, "supplierId");

        Enterprise supplier = cdao.getEnterpriseById(supplierId);
            
        Purchase purchase = new EnterprisePurchase(orders, total, status, supplier);

        purchase.setOrders(orders);

        return purchase;  //parseEnterprisePurchase

    }

    public static Purchase parsePurchase(String json, String purchaseType) throws ContactNotFoundException,
    
            InvalidContactTypeException {

        if (purchaseType.equals("Person")) return parsePersonPurchase(json);

        else if (purchaseType.equals("Enterprise")) return parseEnterprisePurchase(json);

        throw new InvalidContactTypeException(purchaseType);
    }

    public static List<PurchaseOrder> parsePurchaseOrders(String json) {

        JsonObject purchaseObject = new JsonParser().parse(json).getAsJsonObject();

        JsonArray ordersObject = purchaseObject.get("orders").getAsJsonArray();

        List<PurchaseOrder> orders = new ArrayList<PurchaseOrder>();

        ordersObject.forEach(order -> {

            try {

                orders.add(OrderParser.parsePurchaseOrder(String.valueOf(order.getAsJsonObject())));

            } catch (InventoryItemNotFoundException e) {

                e.printStackTrace();
            }
        });

        return orders;
    }






    

}
