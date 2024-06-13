package uiass.eia.gisiba.http.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import uiass.eia.gisiba.http.DataSender;
import uiass.eia.gisiba.http.parsers.ContactParser;
import uiass.eia.gisiba.http.parsers.SaleParser;

public class SaleDto {

    //////////////////////////////////////////////////// GET METHODS ///////////////////////////////////////////////////////////////////

    // Find a Sale by its ref :
    public static List getSaleById(int id) {

        String responseBody = DataSender.getDataSender("sales/byId/" + id);

        if (!responseBody.equals("Server Error.")) return SaleParser.parseSale(responseBody);

        return null;
    }

    // Find all the Sales by supplier type :
    public static List<List<String>> getAllSalesByType(String contactType) {

        String responseBody = DataSender.getDataSender("sales/" + contactType);;

        return SaleParser.parseSales(responseBody);

    }

    // Find all the Sales :
    public static List<List<String>> getAllCustomersByType(String type) {

        String responseBody = DataSender.getDataSender("sales/customers/" + type);;

        List<List<String>> suppliers = new ArrayList<List<String>>();

        JsonArray suppliersArray = new JsonParser().parse(responseBody).getAsJsonArray();

        suppliersArray.forEach(supplier -> suppliers.add(ContactParser.parseContact(String.valueOf(supplier.getAsJsonObject()), type)));

        return suppliers;

    }

    // Find all the Sales :
    public static List<List<String>> getAllSuppliers() {

        String personResponseBody = DataSender.getDataSender("sales/customers/Person");

        String enterpriseResponseBody = DataSender.getDataSender("sales/customers/Enterprise");

        List<List<String>> customers = new ArrayList<List<String>>();

        JsonArray personCustomersArray = new JsonParser().parse(personResponseBody).getAsJsonArray();

        JsonArray enterpriseCustomersArray = new JsonParser().parse(enterpriseResponseBody).getAsJsonArray();

        personCustomersArray.forEach(customer -> customers.add(ContactParser.parseContact(String.valueOf(customer.getAsJsonObject()), "Person")));

        enterpriseCustomersArray.forEach(customer -> customers.add(ContactParser.parseContact(String.valueOf(customer.getAsJsonObject()), "Enterprise")));

        return customers;

    }

    // Find all the Sales :
    public static List<List<String>> getAllSalesByStatus(String status) {

        String responseBody = DataSender.getDataSender("sales/byStatus/" + status);;

        return SaleParser.parseSales(responseBody);

    }

    // Find all the Sales by supplier type :
    public static List<List<String>> getAllSalesByCustomer(String contactType, int id) {

        String responseBody = DataSender.getDataSender("sales/byCustomer/" + contactType + "/" + id);;

        return SaleParser.parseSales(responseBody);

    }

    // Find all the Sales :
    public static List<List<String>> getAllSales() {

        String responseBody = DataSender.getDataSender("sales");;

        return SaleParser.parseSales(responseBody);

    }
 
//////////////////////////////////////////////////// POST METHOD ////////////////////////////////////////////////////////////////

    // Create a new product :
    public static String postSale(String json, String supplierType) {

        return DataSender.postDataSender(json, "sales/" + supplierType + "/post");
    }

    // Find all the Sales by supplier type :
    public static List<List<String>> salesFilter(String json) {

        String responseBody = DataSender.postDataSender(json, "sales/filter");

        if (!responseBody.equals("Server Error.")) return SaleParser.parseSales(responseBody);

        return null;

    }

//////////////////////////////////////////////////// Delete METHOD /////////////////////////////////////////////////////////////

    public static String deleteSale(int id) {

        return DataSender.deleteDataSender("sales/delete/" + id);
    }

    public static String removeSaleOrder(int SaleId, int orderId) {

        return DataSender.deleteDataSender("Sales/delete/" + SaleId + "/removeOrder/" + orderId);
    }

//////////////////////////////////////////////////// Put METHOD /////////////////////////////////////////////////////////////

    public static String updateSaleOrders(int id, String json) {

        if (json != null) return DataSender.putDataSender(json, "sales/put/orders/" + id );

        return "Please provide some new values to update.";
    }

    public static String updateSaleStatus(int id, String json) {

        if (json != null) return DataSender.putDataSender(json, "sales/put/status/" + id );

        return "Please provide some new values to update.";
    }
}
