package uiass.gisiba.eia.java.controller.Parsers;

import java.sql.Date;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Product;

public class InventoryItemParser extends Parser {


    public static InventoryItem parseInventoryItem(String responseBody) throws CategoryNotFoundException {
    	
        JsonObject itemObject = new JsonParser().parse(responseBody).getAsJsonObject();

        Gson gson = new Gson();

        int quantity = collectInt(itemObject, "quantity");

        String dateAddedString = collectString(itemObject, "dateAdded");

        Date dateAdded = Date.valueOf(dateAddedString);

        Product product;

        if (itemObject.has("product")) {

            JsonObject productObject = itemObject.get("product").getAsJsonObject();

            String productJson = gson.toJson(productObject);

            product = ProductParser.parseProduct(productJson);
        }

        else {

            itemObject.remove("quantity");

            itemObject.remove("dateAdded");
    
            String productJson = gson.toJson(itemObject);
    
            product = ProductParser.parseProduct(productJson);
        }

        return new InventoryItem(product, quantity, dateAdded);
             
    }

/////////////////////////////// a method that parses a list of inventory item objects from a json /////////////////////////////////////////

    public static List<InventoryItem> parseInventoryItems(String responseBody) throws CategoryNotFoundException {

        List<InventoryItem> items = new ArrayList<InventoryItem>();

        JsonArray itemsArray = new JsonParser().parse(responseBody).getAsJsonArray();

        for (int i = 0 ; i < itemsArray.size() ; i++) {

            String itemJsonBody = itemsArray.get(i).toString();

            InventoryItem inventoryItem = parseInventoryItem(itemJsonBody);

            items.add(inventoryItem);
        }

        return items;

    }
}
