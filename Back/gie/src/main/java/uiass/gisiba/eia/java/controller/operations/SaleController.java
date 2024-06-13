package uiass.gisiba.eia.java.controller.operations;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;

import spark.Request;
import spark.Response;
import spark.Route;
import uiass.gisiba.eia.java.controller.Parsers.OrderParser;
import uiass.gisiba.eia.java.controller.Parsers.SaleParser;
import uiass.gisiba.eia.java.dao.exceptions.InvalidFilterCriteriaMapFormatException;
import uiass.gisiba.eia.java.dao.exceptions.OperationNotModifiableException;
import uiass.gisiba.eia.java.dao.exceptions.OrderNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.PurchaseNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.SaleNotFoundException;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.inventory.Status;
import uiass.gisiba.eia.java.entity.purchases.Purchase;
import uiass.gisiba.eia.java.entity.purchases.PurchaseOrder;
import uiass.gisiba.eia.java.entity.sales.Sale;
import uiass.gisiba.eia.java.entity.sales.SaleOrder;
import uiass.gisiba.eia.java.service.Service;

public class SaleController {

    private static Service service = new Service();

	private static Gson gsonWithSerializer = new GsonBuilder()
                .registerTypeAdapter(Sale.class, (JsonSerializer<Sale>) (sale, typeOfSrc, context) -> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("saleId", sale.getSaleId());
                    jsonObject.add("saleDate", context.serialize(sale.getSaleDate().toString()));
                    return jsonObject;
                })
                .create();

/////////////////////////////////////////////////// GET METHODS //////////////////////////////////////////////////////////////////

	public static void getSaleById() {
  
		System.out.println("Server started.");

		get("/sales/byId/:id", (req,res)-> {

		int id = Integer.parseInt(req.params("id"));

		Sale Sale = service.getSaleById(id);
	
		res.type("application/json");

		return Sale;

	   
		}, gsonWithSerializer::toJson);
	}	

	public static void getAllSales() {
	  
	    System.out.println("Server started.");
	
	    get("/sales", (req,res)-> {

		System.out.println("/Server started.");

		List<Sale> Sales = service.getAllSales();
		
		res.type("application/json");

		return Sales;
	
		   
		}, gsonWithSerializer::toJson);
	}

    public static void getAllSalesByCustomerType() {
	  
	    System.out.println("Server started.");
	
	    get("/sales/:contactType", (req,res)-> {

        String contactType = req.params("contactType");

		List<Sale> Sales = service.getAllSalesByContactType(contactType);
		
		res.type("application/json");

		return Sales;
	
		   
		}, gsonWithSerializer::toJson);
	}

	public static void getAllSalesByCustomer() {
	  
	    System.out.println("Server started.");
	
	    get("/sales/byCustomer/:customerType/:customerName", (req,res)-> {

		String customerType = req.params("customerType");

        String customerName = req.params("customerName");

		List<Sale> Sales = service.getAllSalesByCustomer(customerType,customerName);
		
		res.type("application/json");

		return Sales;
	
		   
		}, gsonWithSerializer::toJson);
	}

	public static void getAllCustomers() {
	  
	    System.out.println("Server started.");
	
	    get("/sales/customers/:customerType", (req,res)-> {

        String customerType = req.params("customerType");

		List<Contact> Sales = service.getAllCustomers(customerType);
		
		res.type("application/json");

		return Sales;
	
		   
		}, gsonWithSerializer::toJson);
	}

	public static void getAllSalesByStatus() {
	  
	    System.out.println("Server started.");
	
	    get("/sales/byStatus/:status", (req,res)-> {

        String status = req.params("status");

		List<Sale> Sales = service.getAllSalesByStatus(Status.valueOf(status));
		
		res.type("application/json");

		return Sales;
	
		   
		}, gsonWithSerializer::toJson);
	}

/////////////////////////////////////////////////// POST METHOD //////////////////////////////////////////////////////////////////

	public static void postSale() {

		post("/sales/:customerType/post", new Route() {

			@Override
			public Object handle(Request request, Response response) throws Exception {

				String body = request.body();

				String customerType = request.params("customerType");

				Sale sale = SaleParser.parseSale(body, customerType);
				
				service.addSale(sale);

				return "Sale created successfully";
			}
			
		});
	}

	public static void salesFilter() {

		post("/sales/filter", new Route() {

			@Override
			public Object handle(Request request, Response response)  {

				String body = request.body();
				
				try {

					Map<String, Object> criteria = SaleParser.parseOperationFilterCriteriaMap(body, "sale");

					System.out.println(criteria);

					List<Sale> sales = service.salesFilter(criteria);

					return gsonWithSerializer.toJson(sales);

				} catch (InvalidFilterCriteriaMapFormatException e) {
					
					return e.getMessage();
				}
			}
			
		});
	}


/////////////////////////////////////////////////// DELETE METHOD //////////////////////////////////////////////////////////////////

    public static void deleteSaleController()  {

        System.out.println("Server started.");

        delete("/sales/delete/:id", new Route()  {

            @Override
            public String handle(Request request, Response response) {

                int id = Integer.parseInt(request.params(":id"));
      
                try {

					service.deleteSale(id);

				} catch (SaleNotFoundException e) {

					return e.getMessage();
				}     
                           
                return "Sale deleted successfully.";
            }
        }
    
        );  
    }

/////////////////////////////////////////////////// PUT METHODS /////////////////////////////////////////////////////////////////////

    public static void updateSaleOrdersController() {

        put("/sales/put/orders/:id" , new Route() {

            @Override
            public String handle(Request request, Response response) throws ProductNotFoundException  {

	            System.out.println("Server started.");

				String body = request.body();

				int SaleId = Integer.parseInt(request.params("id"));

				List<SaleOrder> orders = SaleParser.parseSaleOrders(body);

				try {

					service.updateSaleOrders(SaleId, orders);

				} catch (SaleNotFoundException e) {

					return e.getMessage();

				} catch (OperationNotModifiableException e) {

					return e.getMessage();
				}

				return "Sale orders updated successfully";


		}});

	}

	public static void updateSaleStatusController() {

        put("/sales/put/status/:id" , new Route() {

            @Override
            public String handle(Request request, Response response) throws ProductNotFoundException  {

	            System.out.println("Server started.");

				String body = request.body();

				int SaleId = Integer.parseInt(request.params("id"));

				Status status = SaleParser.parseStatus(body);

				try {

					service.updateSaleStatus(SaleId, status);

				} catch (SaleNotFoundException e) {

					return e.getMessage();
				}

				return "Sale status updated successfully";


		}});

	}

/////////////////////////////////////////////////// DELETE METHODS /////////////////////////////////////////////////////////////////////

	/*public static void removeSaleOrderController() {

        delete("/sales/delete/:SaleId/removeOrder/:orderId" , new Route() {

            @Override
            public String handle(Request request, Response response) throws ProductNotFoundException  {

	            System.out.println("Server started.");

				int SaleId = Integer.parseInt(request.params("SaleId"));

				int orderId = Integer.parseInt(request.params("orderId"));

				try {
					service.removeSaleOrder(SaleId, orderId);
				} catch (SaleNotFoundException e) {

					return e.getMessage();

				} catch (OrderNotFoundException e) {

					return e.getMessage();
				}

				return "Sale order removed successfully";


		}});

    }*/
}
