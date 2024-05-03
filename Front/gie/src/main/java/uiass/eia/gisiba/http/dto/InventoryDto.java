package uiass.eia.gisiba.http.dto;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import uiass.eia.gisiba.http.DataSender;
import uiass.eia.gisiba.http.parsers.InventoryItemParser;
import uiass.eia.gisiba.http.parsers.ProductParser;

public class InventoryDto {

    public static List<String> getItemById(int id) {

        String responseBody = DataSender.getDataSender("inventpryItems/byId/" + id);

        if (!responseBody.equals("Server Error.")) return InventoryItemParser.parseItem(responseBody);

        return null;
    }

    public static List<String> getProductByItemId(int id) {

        String responseBody = DataSender.getDataSender("inventoryItems/byId/" + id);

        if (!responseBody.equals("Server Error.")) return InventoryItemParser.parseItemProduct(responseBody);

        return null;
    }

    public static List<List<String>> getAllItems() {

        List<List<String>> items = new ArrayList<List<String>>();

        String json = DataSender.getDataSender("inventoryItems");

        JsonArray itemsArray = new JsonParser().parse(json).getAsJsonArray();

        itemsArray.forEach(itemJson -> {

            items.add(InventoryItemParser.parseItem(String.valueOf(itemJson.getAsJsonObject())));
        });

        return items;
    }
}
