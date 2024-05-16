package uiass.gisiba.eia.java.controller.Parsers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uiass.gisiba.eia.java.dao.crm.ContactDao;
import uiass.gisiba.eia.java.dao.crm.iContactDao;
import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidFilterCriteriaMapFormatException;
import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.PurchaseNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.inventory.Status;
import uiass.gisiba.eia.java.entity.purchases.EnterprisePurchase;
import uiass.gisiba.eia.java.entity.purchases.PersonPurchase;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;
import uiass.gisiba.eia.java.service.Service;
import uiass.gisiba.eia.java.service.iService;

public class PurchaseParser extends Parser {

    private static iContactDao cdao = new ContactDao();

    public static Purchase parsePersonPurchase(String json) throws ContactNotFoundException  {

        JsonObject purchaseObject = new JsonParser().parse(json).getAsJsonObject();

        System.out.println(purchaseObject);

        List<PurchaseOrder> orders = parsePurchaseOrders(json);

        System.out.println(orders);

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

            } catch (InventoryItemNotFoundException e) {

                e.printStackTrace();
            }
        });

        return orders;
    }

    public static Map<String,String> parseSupplierFilterCriteriaMap(String json) {

        Map<String,String> supplierMap = new HashMap<String,String>();

        JsonObject supplierObject = new JsonParser().parse(json).getAsJsonObject();

        String supplierName = supplierObject.get("supplierName").getAsString();

        String supplierType = supplierObject.get("supplierType").getAsString();

        supplierMap.put("supplierName", supplierName);

        supplierMap.put("supplierType", supplierType);

        return supplierMap;
    }

    public static Map<String,Date> parseDatesFilterCriteriaMap(String json) throws InvalidFilterCriteriaMapFormatException {
        
        Map<String,Date> criteriaMap = new HashMap<String,Date>();

        JsonObject datesFilterObject = new JsonParser().parse(json).getAsJsonObject();

        if (datesFilterObject.has("beforeDate")) {

            LocalDate beforeDate = LocalDate.parse(datesFilterObject.get("beforeDate").getAsString());

            criteriaMap.put("beforeDate", Date.valueOf(beforeDate));

            return criteriaMap;

        }

        else if (datesFilterObject.has("afterDate")) {

            LocalDate afterDate = LocalDate.parse(datesFilterObject.get("afterDate").getAsString());

            criteriaMap.put("afterDate", Date.valueOf(afterDate));

            return criteriaMap;

        }

        else if (datesFilterObject.has("startDate") && datesFilterObject.has("endDate")) {

            LocalDate startDate = LocalDate.parse(datesFilterObject.get("startDate").getAsString());

            LocalDate endDate = LocalDate.parse(datesFilterObject.get("endDate").getAsString());

            criteriaMap.put("startDate", Date.valueOf(startDate));

            criteriaMap.put("endDate", Date.valueOf(endDate));

            return criteriaMap;

        }

        throw new InvalidFilterCriteriaMapFormatException();
    }

    public static Map<String,Object> parsePurchaseFilterCriteriaMap(String json) throws InvalidFilterCriteriaMapFormatException {

        Map<String,Object> map = new HashMap<String,Object>();

        JsonObject filterObject = new JsonParser().parse(json).getAsJsonObject();

        if (filterObject.has("supplier")) {

            JsonObject supplierObject = filterObject.getAsJsonObject("supplier").getAsJsonObject();

            Map<String,String> supplierMap = parseSupplierFilterCriteriaMap(String.valueOf(supplierObject));

            map.put("supplier", supplierMap);

        }

        if (filterObject.has("date")) {

            JsonObject dateObject = filterObject.getAsJsonObject("date").getAsJsonObject();

            Map<String,Date> dateMap = parseDatesFilterCriteriaMap(String.valueOf(dateObject));

            map.put("date", dateMap);

        }

        if (filterObject.has("status")) {

            String status = filterObject.get("status").getAsString();

            map.put("status", status);
        }

        return map;

    }




    

}
