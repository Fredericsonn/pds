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
import uiass.gisiba.eia.java.controller.Parsers.ProductParser;
import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.service.Service;

public class ProductController {

    private static Service service = new Service();

/////////////////////////////////////////////////// GET METHODS //////////////////////////////////////////////////////////////////

    public static void getProductByRef() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/products/byRef/:ref", (req,res)-> {

		String ref = String.valueOf(req.params(":ref"));

		Product product = service.getProductById(ref);
		
		res.type("application/json");

		return product;
	
		   
		}, gson::toJson);


    }

	public static void checkForAssociatedPurchases() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/products/checker/byRef/:ref", (req,res)-> {

		String ref = String.valueOf(req.params(":ref"));

		Product product = service.getProductById(ref);

		boolean bool = service.checkForAssociatedPurchases(product);
		
		res.type("application/json");

		return bool;
	
		   
		}, gson::toJson);


    }

	public static void getAllProducts() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/products", (req,res)-> {

		List<Product> products = service.getAllProducts();
		
		res.type("application/json");

		return products;
	
		   
		}, gson::toJson);

    }

/////////////////////////////////////////////////// DELETE METHOD //////////////////////////////////////////////////////////////////

    public static void deleteProductController()  {

	    System.out.println("Server started.");

		delete("/products/delete/:ref", new Route()  {

			@Override
			public String handle(Request request, Response response)   {

				String ref = String.valueOf(request.params(":ref"));

				try {

					service.deleteProduct(ref);   
	
				} catch (ProductNotFoundException | InventoryItemNotFoundException  e) {
	
					return e.getMessage();
				}  
					
				return "Product deleted successfully.";
			}
		}

		);  
}

/////////////////////////////////////////////////// PUT METHOD //////////////////////////////////////////////////////////////////

    public static void updateProductController() {

        Gson gson = new Gson();

        put("/products/put/:ref" , new Route() {

            @Override
            public String handle(Request request, Response response) throws ProductNotFoundException  {

	            System.out.println("Server started.");

		        String ref = request.params(":ref");  // We take the ref of the product to update from the url

		    	String body = request.body(); 	

				System.out.println(body);
		    	// We collect all the values to update from the request body in one list :
		    	List productValues = ProductParser.productValuesCollector(gson, body);

				List categoryValues = CategoryParser.categoryValuesCollector(gson, body);

		    	// We select only the non null values with their corresponding columns :
		    	Map<String, Object> product_columns_new_values = Parser.mapFormater(ProductParser.product_columns, productValues);

				Map<String, Object> category_columns_new_values = Parser.mapFormater(CategoryParser.categoryAttributes, categoryValues);

				product_columns_new_values.put("category", category_columns_new_values);
				
		    	// And finally we update the product :
		    	try {
				  
			  		service.updateProduct(ref, product_columns_new_values);

		    	} catch (ProductNotFoundException | CategoryNotFoundException  e) {

			    	return e.getMessage();
		    	}
		   
		    	return "Product Updated successfully.";


		}});


	}

/////////////////////////////////////////////////// POST METHODs //////////////////////////////////////////////////////////////////

public static void postProductController() {

	// A list of the product table's columns

    post("/products/post" , new Route() {

        @Override
        public String handle(Request request, Response response) throws ProductNotFoundException, CategoryNotFoundException  {

	        System.out.println("Server started.");

		    String body = request.body(); 	

			// We create the product using the parse method in the Parser class
			Product product = ProductParser.parseProduct(body);

			// We persist the product
			service.addProduct(product.getCategory(), product.getName(), product.getDescription());

			// The server response : 
		    return "Product created successfully.";


}});


}

	public static void productSearchFilter() {
	
		post("/products/filter" , new Route() {
	
			@Override
			public String handle(Request request, Response response) throws ProductNotFoundException, CategoryNotFoundException  {

				Gson gson = new Gson();

				System.out.println("Server started.");
		
				String body = request.body();
		
				Map<String,String> criteria = ProductParser.parseFilterCriteria(body, ProductParser.filter_columns);
		
				List<Product> products = service.productSearchFilter(criteria);
						
				return gson.toJson(products);
	
	
	}});
	
	
	}

}
