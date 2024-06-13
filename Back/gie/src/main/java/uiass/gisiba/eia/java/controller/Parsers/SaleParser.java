package uiass.gisiba.eia.java.controller.Parsers;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uiass.gisiba.eia.java.dao.crm.ContactDao;
import uiass.gisiba.eia.java.dao.crm.iContactDao;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.inventory.Status;
import uiass.gisiba.eia.java.entity.purchases.EnterprisePurchase;
import uiass.gisiba.eia.java.entity.purchases.PersonPurchase;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;
import uiass.gisiba.eia.java.entity.sales.EnterpriseSale;
import uiass.gisiba.eia.java.entity.sales.PersonSale;
import uiass.gisiba.eia.java.entity.sales.Sale;
import uiass.gisiba.eia.java.entity.sales.SaleOrder;

public class SaleParser extends OperationParser {

    private static iContactDao cdao = new ContactDao();

    public static Sale parsePersonSale(String json) throws ContactNotFoundException  {

        JsonObject SaleObject = new JsonParser().parse(json).getAsJsonObject();

        List<SaleOrder> orders = parseSaleOrders(json);

        Status status = Status.valueOf(SaleObject.get("status").getAsString());  

        double total = SaleObject.get("total").getAsDouble();

        int customerId = collectInt(SaleObject, "customerId");

        Person customer = cdao.getPersonById(customerId);
            
        Sale Sale = new PersonSale(orders, total, status, customer);

        orders.forEach(order -> order.setSale(Sale));

        Sale.setOrders(orders);

        return Sale;  

    }

    public static Sale parseEnterpriseSale(String json) throws ContactNotFoundException  {

        JsonObject saleObject = new JsonParser().parse(json).getAsJsonObject();

        List<SaleOrder> orders = parseSaleOrders(json);

        Status status = Status.valueOf(saleObject.get("status").getAsString());  

        double total = saleObject.get("total").getAsDouble();

        int customerId = collectInt(saleObject, "customerId");

        Enterprise customer = cdao.getEnterpriseById(customerId);
            
        Sale Sale = new EnterpriseSale(orders, total, status, customer);

        orders.forEach(order -> order.setSale(Sale));

        Sale.setOrders(orders);

        return Sale;  //parseEnterprisePurchase

    }

    public static Sale parseSale(String json, String purchaseType) throws ContactNotFoundException,
    
            InvalidContactTypeException {

        if (purchaseType.equals("Person")) return parsePersonSale(json);

        else if (purchaseType.equals("Enterprise")) return parseEnterpriseSale(json);

        throw new InvalidContactTypeException(purchaseType);
    }

    public static List<SaleOrder> parseSaleOrders(String json) {

        JsonObject SaleObject = new JsonParser().parse(json).getAsJsonObject();

        JsonArray ordersObject = SaleObject.get("orders").getAsJsonArray();

        List<SaleOrder> orders = new ArrayList<SaleOrder>();

        ordersObject.forEach(order -> {

            try {

                orders.add(OrderParser.parseSaleOrder(String.valueOf(order.getAsJsonObject())));

            } catch (InventoryItemNotFoundException e) {

                e.printStackTrace();
            }
        });

        return orders;
    }

    public static Status parseStatus(String json) {
        return null;
    }
}
