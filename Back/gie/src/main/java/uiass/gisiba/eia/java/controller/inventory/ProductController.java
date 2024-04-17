package uiass.gisiba.eia.java.controller.inventory;

import static spark.Spark.*;

import java.util.List;

import com.google.gson.Gson;

import uiass.gisiba.eia.java.controller.GetGson;
import uiass.gisiba.eia.java.dao.inventory.ProductDao;
import uiass.gisiba.eia.java.entity.inventory.Product;

public class ProductController {

    private static ProductDao dao = new ProductDao();

    public static void getProductBYId() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    get("/products/byId/:ref", (req,res)-> {

		String ref = String.valueOf(req.params(":ref"));

		Product contact = dao.getProductById(ref);
		
		res.type("application/json");

		return contact;
	
		   
		}, gson::toJson);


    }

	public static void getAllProducts() {

	    Gson gson = GetGson.getGson();
	  
	    System.out.println("Server started.");
	
	    get("/products", (req,res)-> {

		List<Product> contacts = dao.getAllProducts();
		
		res.type("application/json");

		return contacts;
	
		   
		}, gson::toJson);


    }
}
