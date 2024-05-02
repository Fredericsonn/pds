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

    public static List<String> productAttributes = Arrays.asList("productRef","model","description","unitPrice");

    public static List<String> product_columns = Arrays.asList("model","description","unitPrice");

    public static List<String> filter_columns = Arrays.asList("categoryName","brandName","model");

    // A method that collects product data from a json :
    public static List productValuesCollector(Gson gson, String body) {

        JsonObject product = gson.fromJson(body, JsonObject.class);

        String model = collectString(product, "model");

        String description = collectString(product, "description");

        String unitPrice = collectString(product, "unitPrice");

        if (unitPrice != null) return Arrays.asList(model,description,Double.parseDouble(unitPrice)); 

        return Arrays.asList(model,description,unitPrice);

    }

    public static Product parseProduct(String responseBody) throws CategoryNotFoundException {
    		
        JsonObject productObject = new JsonParser().parse(responseBody).getAsJsonObject();

        List<String> productStringInfo = new ArrayList<String>();
    
        List<Double> productDoubleInfo = new ArrayList<Double>();

        // The list containing attributes names used to parse the json
        List<String> pAttributes = new ArrayList<String>(productAttributes);

        // We iterate through our attributes and call the collectors to collect the values from the json
        for (String attribute : pAttributes) {

            if (attribute.equals("unitPrice")) {
            
                productDoubleInfo.add(collectDouble(productObject, attribute));
            }
            else productStringInfo.add(collectString(productObject, attribute));
        }

        String ref = productStringInfo.get(0); 

        String model = productStringInfo.get(1);

        String description = productStringInfo.get(2);

        double unitPrice = productDoubleInfo.get(0);

        JsonObject categoryObject = productObject.get("category").getAsJsonObject();

        String categoryName = collectString(categoryObject, "categoryName");

        String brandName = collectString(categoryObject, "brandName");

        Category category = service.getCategoryByNames(categoryName, brandName);    

        Product product = new Product(category,model,description,unitPrice);

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
