package uiass.gisiba.eia.java.controller.inventory;

import static spark.Spark.*;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import spark.Request;
import spark.Response;
import spark.Route;
import uiass.gisiba.eia.java.controller.Parsers.CategoryParser;
import uiass.gisiba.eia.java.controller.Parsers.Parser;
import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Category;
import uiass.gisiba.eia.java.service.Service;

public class CategoryController {

    private static Service service = new Service();

/////////////////////////////////////////////////// GET METHODS //////////////////////////////////////////////////////////////////

	public static void getAllCategories() {

		Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/categories", (req,res)-> {

		List<Category> categories = service.getAllCategories();
		
		res.type("application/json");

		return categories;
	
		   
		}, gson::toJson);
	}
	public static void getAllCategoriesNames() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/categories/categoriesNames", (req,res)-> {

		List<String> categories = service.getAllCategoriesNames();
		
		res.type("application/json");

		return categories;
	
		   
		}, gson::toJson);

    }

	public static void getAllBrandsNames() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/categories/brandsNames", (req,res)-> {

		List<String> brands = service.getAllBrandsNames();
		
		res.type("application/json");

		return brands;
	
		   
		}, gson::toJson);

    }

	public static void getAllBrandsByCategory() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/categories/brandsNames/byCategory/:categoryName", (req,res)-> {
		
		String category = req.params(":categoryName");

		List<String> brands = service.getAllBrandsByCategory(category);
		
		res.type("application/json");

		return brands;
	
		   
		}, gson::toJson);

    }

/////////////////////////////////////////////////// POST METHOD //////////////////////////////////////////////////////////////////

	public static void postCategory() {

		post("/categories/post", new Route() {

			@Override
			public Object handle(Request request, Response response) throws Exception {

				String body = request.body();

				Category category = CategoryParser.parseCategory(body);

				service.addCategory(category.getCategoryName(), category.getBrandName());

				return "Category created successfully";
			}
			
		});
	}

/////////////////////////////////////////////////// PUT METHOD /////////////////////////////////////////////////////////////////////

    public static void updateCategoryController() {

        Gson gson = new Gson();

        put("/categories/put/:id" , new Route() {

            @Override
            public String handle(Request request, Response response) throws ProductNotFoundException  {

	            System.out.println("Server started.");

		        int id = Integer.parseInt(request.params(":id"));  // We take the id of the product to update from the url

		    	String body = request.body(); 	

		    	// We collect all the values to update from the request body in one list :
		    	List categoryValues = CategoryParser.categoryValuesCollector(gson, body);
				System.out.println(categoryValues);

				// We convert the non null values to their corresponding enum values :
				List validCategoryValues = CategoryParser.categoryValuesFormatter(categoryValues);
				System.out.println(validCategoryValues);

		    	// We select only the non null values with their corresponding columns :
		    	Map<String, Object> category_columns_new_values = Parser.mapFormater(CategoryParser.categoryAttributes, validCategoryValues);
				System.out.println(category_columns_new_values);

		    	// And finally we update the category :
		    	try {
				  
			  		service.updateCategory(id, category_columns_new_values);

		    	} catch (CategoryNotFoundException  e) {

			    	return e.getMessage();
		    	}
		   
		    	return "Category updated successfully.";


		}});


	}
}
