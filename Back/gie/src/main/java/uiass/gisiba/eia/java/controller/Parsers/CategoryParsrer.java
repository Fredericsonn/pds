package uiass.gisiba.eia.java.controller.Parsers;

import java.util.*;

import com.google.gson.*;

import uiass.gisiba.eia.java.controller.Parser;
import uiass.gisiba.eia.java.entity.inventory.Category;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCategory;

public class CategoryParsrer {

    public static List<String> categoryAttributes = Arrays.asList("categoryName","brandName");

    public static Category parseCategory(String requestBody) {

        JsonObject categoryObject = new JsonParser().parse(requestBody).getAsJsonObject();

        String categoryName = categoryObject.get("categoryName").getAsString();

        String brandName = categoryObject.get("brandName").getAsString();

        return new Category(ProductCategory.valueOf(categoryName), ProductBrand.valueOf(brandName));

    }

    public static List<Category> parseCategories(String json) {

        List<Category> categories = new ArrayList<Category>();

        JsonArray categoriesArray = new JsonParser().parse(json).getAsJsonArray();

        categoriesArray.forEach(category -> categories.add(parseCategory(category.getAsString())));

        return categories;

    }

    public static List categoryValuesCollector(Gson gson, String body) {

        JsonObject category = gson.fromJson(body, JsonObject.class);

        String categoryName = Parser.collectString(category, "categoryName");

        String brandName = Parser.collectString(category, "brandName");

        return Arrays.asList(categoryName,brandName);

    }

    public static List categoryValuesFormatter(List<String> values) {

        List valid_values = new ArrayList<>();

        String categoryName = values.get(0);

        String brandName = values.get(1);

        if (categoryName != null) valid_values.add(ProductCategory.valueOf(categoryName));

        else valid_values.add(categoryName);

        if (brandName != null) valid_values.add(ProductBrand.valueOf(brandName));

        else valid_values.add(brandName);

        return valid_values;



    }

}
