package uiass.gisiba.eia.java.controller.Parsers;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Category;
import uiass.gisiba.eia.java.entity.inventory.Product;

import uiass.gisiba.eia.java.service.Service;

public class ProductParser extends Parser {

    private static Service service = new Service();

    public static List<String> productAttributes = Arrays.asList("productRef","name","description");

    public static List<String> product_columns = Arrays.asList("name","description");

    public static List<String> filter_columns = Arrays.asList("categoryName","brandName","modelName");

    // A method that collects product data from a json :
    public static List productValuesCollector(Gson gson, String body) {

        JsonObject product = gson.fromJson(body, JsonObject.class);

        String name = collectString(product, "name");

        String description = collectString(product, "description");

        return Arrays.asList(name,description);

    }

    public static Product parseProduct(String responseBody) throws CategoryNotFoundException {
    		
        JsonObject productObject = new JsonParser().parse(responseBody).getAsJsonObject();

        List<String> productInfo = new ArrayList<String>();
    
        // The list containing attributes names used to parse the json
        List<String> pAttributes = new ArrayList<String>(productAttributes);

        // We iterate through our attributes and call the collectors to collect the values from the json
        for (String attribute : pAttributes) {

            productInfo.add(collectString(productObject, attribute));
        }

        String ref = productInfo.get(0); 

        String name = productInfo.get(1);

        String description = productInfo.get(2);

        JsonObject categoryObject = productObject.get("category").getAsJsonObject();

        String categoryName = collectString(categoryObject, "categoryName");

        String brandName = collectString(categoryObject, "brandName");

        String modelName = collectString(categoryObject, "modelName");

        Category category = service.getCategoryByNames(categoryName, brandName, modelName);    

        Product product = new Product(category,name,description);

        return product;
                 
    }

/////////////////////////////// a method that parses a list of product objects from a json /////////////////////////////////////////

    public static List<Product> parseProducts(String responseBody) throws CategoryNotFoundException {

        List<Product> products = new ArrayList<Product>();

        JsonArray productsArray = new JsonParser().parse(responseBody).getAsJsonArray();

        for (int i = 0 ; i < productsArray.size() ; i++) {

            String productJsonBody = productsArray.get(i).toString();

            Product product = parseProduct(productJsonBody);

            products.add(product);
        }

        return products;

    }

/////////////////////////////// this method is used to parse the filter criteria map ///////////////////////////////////////////////

    public static Map<String, Object> parseFilterCriteria(String json) {

        Map<String, Object> criteria = new HashMap<String, Object>();

        JsonObject criteriaObject = new JsonParser().parse(json).getAsJsonObject();

        for (String column : filter_columns) {

            String filter_column_value = collectString(criteriaObject, column);

            if (filter_column_value != null) criteria.put(column, filter_column_value);

        }

        return criteria;

    }
}
