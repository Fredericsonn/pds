package uiass.eia.gisiba.http.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import uiass.eia.gisiba.http.DataSender;
import uiass.eia.gisiba.http.parsers.ProductParser;

public class CategoryDto {

//////////////////////////////////////////////////// GET METHODS ///////////////////////////////////////////////////////////////////

    // Find all the categories :
    public static List<List<String>> getAllCategories() {

        List<List<String>> categories = new ArrayList<List<String>>();

        String responseBody = DataSender.getDataSender("categories");

        JsonArray categoriesArray = new JsonParser().parse(responseBody).getAsJsonArray();

        categoriesArray.forEach(category -> categories.add(ProductParser.parseCategory(String.valueOf(category.getAsJsonObject()))));

        return categories;


    }
    // Find all the categories names :
    public static List<String> getAllCategoriesNames() {

        List<String> categories = new ArrayList<String>();

        String responseBody = DataSender.getDataSender("categories/categoriesNames");

        JsonArray categoriesArray = new JsonParser().parse(responseBody).getAsJsonArray();

        categoriesArray.forEach(category -> categories.add(category.getAsString()));

        return categories;
    }

    // Find all the brands :
    public static List<String> getAllBrands() {

        List<String> brands = new ArrayList<String>();

        String responseBody = DataSender.getDataSender("categories/brandsNames");

        JsonArray brandsArray = new JsonParser().parse(responseBody).getAsJsonArray();

        brandsArray.forEach(brand -> brands.add(brand.getAsString()));

        return brands;
    }

    // Find all the brands by a given category:
    public static List<String> getAllBrandsByCategory(String category) {

        List<String> brands = new ArrayList<String>();

        String responseBody = DataSender.getDataSender("categories/brandsNames/byCategory/" + category);

        JsonArray brandsArray = new JsonParser().parse(responseBody).getAsJsonArray();

        brandsArray.forEach(brand -> brands.add(brand.getAsString()));

        return brands;
    }

//////////////////////////////////////////////////// POST METHOD ///////////////////////////////////////////////////////////////////

public static String postCategory(String json) {

    if (json != null) return DataSender.postDataSender(json, "categories/post");

    return "Please provide some new values to post.";
}
//////////////////////////////////////////////////// PUT METHOD ///////////////////////////////////////////////////////////////////

    public static String updateCategory(int id, String json) {

        if (json != null) return DataSender.putDataSender(json, "categories/put/" + id);

        return "Please provide some new values to update.";
}
}
