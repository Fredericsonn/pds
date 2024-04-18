package uiass.gisiba.eia.java.controller.inventory;

import static spark.Spark.*;

import java.util.List;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;
import uiass.gisiba.eia.java.controller.GetGson;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCatagory;
import uiass.gisiba.eia.java.service.Service;

public class ProductController {

    private static Service service = new Service();

/////////////////////////////////////////////////// GET METHODS //////////////////////////////////////////////////////////////////

    public static void getProductByRef() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    get("/products/byRef/:ref", (req,res)-> {

		String ref = String.valueOf(req.params(":ref"));

		Product contact = service.getProductById(ref);
		
		res.type("application/json");

		return contact;
	
		   
		}, gson::toJson);


    }

	public static void getAllProducts() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    get("/products", (req,res)-> {

		List<Product> contacts = service.getAllProducts();
		
		res.type("application/json");

		return contacts;
	
		   
		}, gson::toJson);

    }

	public static void getAllCategories() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    get("/products/categories", (req,res)-> {

		List<ProductCatagory> categories = service.getAllCategories();
		
		res.type("application/json");

		return categories;
	
		   
		}, gson::toJson);

    }

	public static void getAllBrandsByCategory() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    get("/products/brands/byCategory/:category", (req,res)-> {
		
		ProductCatagory category = ProductCatagory.valueOf(req.params(":category"));

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
}
