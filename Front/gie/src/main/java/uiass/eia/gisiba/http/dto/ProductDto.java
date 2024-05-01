package uiass.eia.gisiba.http.dto;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import uiass.eia.gisiba.http.DataSender;
import uiass.eia.gisiba.http.parsers.Parser;
import uiass.eia.gisiba.http.parsers.ProductParser;

public class ProductDto {

//////////////////////////////////////////////////// GET METHODS ///////////////////////////////////////////////////////////////////

    // Find a product by its ref :
    public static List<String> getProductByRef(String ref) {

        String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/products/byRef/" + ref);

        if (!responseBody.equals("Server Error.")) return ProductParser.parseProduct(responseBody);

        return null;
    }

    // Find all the products :
    public static List<List<String>> getAllProducts() {

        String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/products");

        List<List<String>> products = new ArrayList<List<String>>();

        JsonArray productsArray = new JsonParser().parse(responseBody).getAsJsonArray();

        productsArray.forEach(product -> products.add(ProductParser.parseProduct(String.valueOf(product.getAsJsonObject()))));

        return products;

    }
 
//////////////////////////////////////////////////// POST METHOD ////////////////////////////////////////////////////////////////

    // Create a new product :
    public static String postProduct(String json) {

        return DataSender.postDataSender(json, "products");
    }

//////////////////////////////////////////////////// Delete METHOD /////////////////////////////////////////////////////////////

    public static String deleteProduct(String ref) {

        return DataSender.deleteDataSender("products/delete/" + ref);
    }

//////////////////////////////////////////////////// Put METHOD /////////////////////////////////////////////////////////////

public static String updateProduct(String ref, String json) {

    if (json != null) return DataSender.putDataSender(json, "products/put/" + ref );

    return "Please provide some new values to update.";
}
}
