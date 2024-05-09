package uiass.eia.gisiba.http.dto;

import java.util.List;


import uiass.eia.gisiba.http.DataSender;
import uiass.eia.gisiba.http.parsers.PurchaseParser;

public class PurchaseDto {

//////////////////////////////////////////////////// GET METHODS ///////////////////////////////////////////////////////////////////

    // Find a purchase by its ref :
    public static List getPurchaseById(int id) {

        String responseBody = DataSender.getDataSender("purchases/byId/" + id);

        if (!responseBody.equals("Server Error.")) return PurchaseParser.parsePurchase(responseBody);

        return null;
    }

    // Find all the purchases by supplier type :
    public static List<List<String>> getAllPurchasesByType(String contactType) {

        String responseBody = DataSender.getDataSender("purchases/" + contactType);;

        return PurchaseParser.parsePurchases(responseBody);

    }

    // Find all the purchases :
    public static List<List<String>> getAllPurchasesByStatus(String status) {

        String responseBody = DataSender.getDataSender("purchases/" + status);;

        return PurchaseParser.parsePurchases(responseBody);

    }

    // Find all the purchases by supplier type :
    public static List<List<String>> getAllPurchasesBySupplier(String contactType, int id) {

        String responseBody = DataSender.getDataSender("purchases/bySupplier/" + contactType + "/" + id);;

        return PurchaseParser.parsePurchases(responseBody);

    }


    // Find all the purchases :
    public static List<List<String>> getAllPurchases() {

        String responseBody = DataSender.getDataSender("purchases");;

        return PurchaseParser.parsePurchases(responseBody);

    }
 
//////////////////////////////////////////////////// POST METHOD ////////////////////////////////////////////////////////////////

    // Create a new product :
    public static String postPurchase(String json, String supplierType) {

        return DataSender.postDataSender(json, "purchases/" + supplierType + "/post");
    }

//////////////////////////////////////////////////// Delete METHOD /////////////////////////////////////////////////////////////

    public static String deletePurchase(int id) {

        return DataSender.deleteDataSender("purchases/delete/" + id);
    }

//////////////////////////////////////////////////// Put METHOD /////////////////////////////////////////////////////////////

    public static String updatePurchaseOrders(int id, String json) {

        if (json != null) return DataSender.putDataSender(json, "products/put/orders/" + id );

        return "Please provide some new values to update.";
    }

    public static String updatePurchaseStatus(int id, String json) {

        if (json != null) return DataSender.putDataSender(json, "products/put/status/" + id );

        return "Please provide some new values to update.";
    }
}
