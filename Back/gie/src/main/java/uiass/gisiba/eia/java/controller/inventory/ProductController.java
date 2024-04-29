package uiass.gisiba.eia.java.controller.inventory;

import static spark.Spark.*;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import spark.Request;
import spark.Response;
import spark.Route;
import uiass.gisiba.eia.java.controller.Parser;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.dao.inventory.ProductRefGenerator;
import uiass.gisiba.eia.java.entity.inventory.Category;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCategory;
import uiass.gisiba.eia.java.service.Service;

public class ProductController {

    private static Service service = new Service();

/////////////////////////////////////////////////// GET METHODS //////////////////////////////////////////////////////////////////

    public static void getProductByRef() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/products/byRef/:ref", (req,res)-> {

		String ref = String.valueOf(req.params(":ref"));

		Product contact = service.getProductById(ref);
		
		res.type("application/json");

		return contact;
	
		   
		}, gson::toJson);


    }

	public static void getAllProducts() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/products", (req,res)-> {

		List<Product> products = service.getAllProducts();

		System.out.println(products);
		
		res.type("application/json");

		return products;
	
		   
		}, gson::toJson);

    }

	public static void getAllCategories() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/products/categories", (req,res)-> {

		List<ProductCategory> categories = service.getAllCategories();
		
		res.type("application/json");

		return categories;
	
		   
		}, gson::toJson);

    }

	public static void getAllBrandsByCategory() {

	    Gson gson = new Gson();
	  
	    System.out.println("Server started.");
	
	    get("/products/brands/byCategory/:category", (req,res)-> {
		
		ProductCategory category = ProductCategory.valueOf(req.params(":category"));

		List<ProductBrand> brands = service.getAllBrandsByCategory(category);
		
		res.type("application/json");

		return brands;
	
		   
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
	
				} catch (ProductNotFoundException  e) {
	
					return e.getMessage();
				}  
					
				return "Product deleted successfully.";
			}
		}

		);  
}

/////////////////////////////////////////////////// PUT METHOD //////////////////////////////////////////////////////////////////

    public static void updateproductController() {


	    // A list of the product table's columns

        Gson gson = new Gson();

        put("/products/put/:ref" , new Route() {

            @Override
            public String handle(Request request, Response response) throws ProductNotFoundException  {

	            System.out.println("Server started.");

		        String ref = request.params(":ref");  // We take the id of the product to update from the url

		    	String body = request.body(); 	

		    	// We collect all the values to update from the request body in one list :
		    	List productValues = Parser.productValuesCollector(gson, body);

		    	// We select only the non null values with their corresponding columns :
		    	Map<String, Object> product_columns_new_values = Parser.mapFormater(Parser.product_columns, productValues);

		    	// And finally we update the product :
		    	try {
				  
			  		service.updateProduct(ref, product_columns_new_values);

		    	} catch (ProductNotFoundException  e) {

			    	return e.getMessage();
		    	}
		   
		    	return "Product Updated successfully.";


		}});


	}

/////////////////////////////////////////////////// POST METHOD //////////////////////////////////////////////////////////////////

public static void postproductController() {

	// A list of the product table's columns

    post("/products/post" , new Route() {

        @Override
        public String handle(Request request, Response response) throws ProductNotFoundException  {

	        System.out.println("Server started.");

		    String body = request.body(); 	

			// We create the product using the parse method in the Parser class
			Product product = Parser.parseProduct(body);

			// We persist the product
			service.addProduct(product.getCategory(), product.getModel(), product.getDescription(), product.getUnitPrice());

			// The server response : 
		    return "Product created successfully.";




}});


}

}
