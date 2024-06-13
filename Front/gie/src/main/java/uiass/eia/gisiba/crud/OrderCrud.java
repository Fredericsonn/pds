package uiass.eia.gisiba.crud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import uiass.eia.gisiba.controller.FXManager;
import uiass.eia.gisiba.http.dto.OrderDto;
import uiass.eia.gisiba.http.dto.ProductDto;

public class OrderCrud {



    // a method that sets the order table's columns event listeners
    public static void orderTableEventHandler(TableView ordersTableView, List<Label> labels, Parent pane, AnchorPane refresh,
    
        Button view, Button stats, String operation) {

        ordersTableView.setOnMouseClicked(event -> {
            if (!ordersTableView.getSelectionModel().isEmpty()) {

                // We get the selected row and extract the values
                List<String> selectedItem = (List<String>) ordersTableView.getSelectionModel().getSelectedItem();
                int orderId = Integer.parseInt(selectedItem.get(0));
                int itemId = Integer.parseInt(selectedItem.get(1));
                String category = selectedItem.get(2);
                String brand = selectedItem.get(3);
                String model = selectedItem.get(4);
                String name = selectedItem.get(5);
                String unitPrice = selectedItem.get(6);
                String quantity = selectedItem.get(7);
                String orderDateTime = selectedItem.get(8);
                String profitMargin = "";

                if (operation.equals("sale")) profitMargin = selectedItem.get(9);

                // We put all the values in one list that we'll use to fill the labels
                //List<String> values = Arrays.asList(category,brand,model,name,unitPrice,quantity,orderTime,purchaseDate);

                List<String> valuesToShow = operation.equals("purchase") ? Arrays.asList(category,brand + " " + model + " " + name,"Price : " + unitPrice 
                
                + "$", "Quantity Ordered : " + quantity, "Date : " + orderDateTime) :

                Arrays.asList(category,brand + " " + model + " " + name,"Price : " + unitPrice 
                
                + "$", "Quantity Ordered : " + quantity, "Profit Margin : " + profitMargin + "%", "Date : " + orderDateTime);

                // We use the extracted values to fill the labels
                FXManager.labelsFiller(labels, valuesToShow);

                // We finally show the right pane
                pane.setVisible(true);

                view.setOnAction(view_event -> {

                    goToViewOrderPane();
                });

                // When the stats button is clicked
                stats.setOnAction(stats_event -> {


                });
           
        } 
            
        });
    }

    public static void fillWithPurchaseOrders(TableView table) {

        // We send an http get request to get all the orders 
        List<List<String>> data = OrderDto.getAllPurchaseOrders();  

        // We populate the table using those collected orders
        List<String> columns = FXManager.order_columns_per_operation_type.get("purchase");
        
        FXManager.populateTableView(table, columns, Arrays.asList("order id", "item id","category"), data);
    }

    public static void fillWithFilteredPurchasedOrders(TableView table, List<List<String>> data) {
        
        // The columns we'll use for the table
        List<String> columns = FXManager.order_columns_per_operation_type.get("purchase");
        
        FXManager.populateTableView(table, columns, Arrays.asList("order id", "item id","category"), data);
    }

    public static void fillWithSaleOrders(TableView table) {

        // We send an http get request to get all the orders 
        List<List<String>> data = OrderDto.getAllSaleOrders();  

        // We populate the table using those collected orders
        List<String> columns = FXManager.order_columns_per_operation_type.get("sale");
        
        FXManager.populateTableView(table, columns, Arrays.asList("order id", "item id","category"), data);
    }

    public static void fillWithFilteredSaleOrders(TableView table, List<List<String>> data) {
        
        // The columns we'll use for the table
        List<String> columns = FXManager.order_columns_per_operation_type.get("purchase");
        
        FXManager.populateTableView(table, columns, Arrays.asList("order id", "item id","category"), data);
    }

    public static void fillWithOrders(TableView table, String operation) {

        if (operationValidator(operation)) {

            if (operation.equals("purchase")) fillWithPurchaseOrders(table);

            else fillWithSaleOrders(table);

        }

        else throw new RuntimeException("Invalid Operation Type !!");
    }

    public static void fillWithFilteredOrders(TableView table, List<List<String>> data, String operation) {

        if (operationValidator(operation)) {

            if (operation.equals("purchase")) fillWithFilteredPurchasedOrders(table, data);

            else fillWithFilteredPurchasedOrders(table, data);

        }

        else throw new RuntimeException("Invalid Operation Type !!");
    }

    public static void ordersSearchButtonHandler(Button search, TableView ordersTable, List<ComboBox> comboBoxes, String operation) {

                search.setOnAction(event -> {

            ComboBox categoryComboBox = comboBoxes.get(0);     // We get
            ComboBox brandComboBox = comboBoxes.get(1);        // the text fields
            ComboBox modelComboBox = comboBoxes.get(2);        // from the list
            
            // We collect the entered id (we suppose it's a number)
            String categroyInput = (String) categoryComboBox.getValue();
            String brandInput = (String) brandComboBox.getValue();
            String modelInput = (String) modelComboBox.getValue();

            List<String> values = Arrays.asList(categroyInput,brandInput,modelInput);

            String json = ProductCrud.filteredProductSearchJsonGenerator(values);            

            if (ProductCrud.productSearchValidator(comboBoxes)) {

                // We get the orders that match the filter criteria
                List<List<String>> data = null;

                if (operation.equals("purchase")) data = OrderDto.getFilteredPurchaseOrders(json);

                else data = OrderDto.getFilteredSaleOrders(json);

                if (!data.isEmpty()) {  // if there are matching products 

                    // We fill the products table with the matching products
                    OrderCrud.fillWithFilteredOrders(ordersTable, data, operation);

                }
                
                // if no product corresponds to the provided ref we show an error alert
                else FXManager.showAlert(AlertType.ERROR, "ERROR", "Orders Not Found"," No saved orders match the given criteria.");
            }

            // if the text field is empty and the search button is clicked
            else FXManager.showAlert(AlertType.ERROR, "ERROR", "No Selected Parameter", "Please provide some parameters for the search.");
        });
    }

    public static void goToViewOrderPane() {

    }

    public static void goToOrderStatsPane() {

    }

    public static boolean operationValidator(String operation) {

        return operation.equalsIgnoreCase("purchase") || operation.equalsIgnoreCase("sale");
    }
}
