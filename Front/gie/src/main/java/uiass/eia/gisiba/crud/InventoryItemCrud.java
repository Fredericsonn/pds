package uiass.eia.gisiba.crud;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import uiass.eia.gisiba.controller.FXManager;
import uiass.eia.gisiba.http.dto.InventoryDto;

public class InventoryItemCrud {


    // a method that sets the product table's columns event listeners
    public static void itemsTableEventHandler(TableView itemsTable, List<Label> labels, Parent pane, AnchorPane refresh,
    
        Button add, Button view) {

        itemsTable.setOnMouseClicked(event -> {
            if (!itemsTable.getSelectionModel().isEmpty()) {

                // We get the selected row and extract the values
                List<String> selectedItem = (List<String>) itemsTable.getSelectionModel().getSelectedItem();

                String id = selectedItem.get(0);
                String category = selectedItem.get(1);
                String brand = selectedItem.get(2);
                String model = selectedItem.get(3);
                String name = selectedItem.get(4);
                String unitPrice = selectedItem.get(5);
                String quantity = selectedItem.get(6);
                String dateAdded = selectedItem.get(7);

                // We put all the values in one list that we'll use to fill the labels
                List<String> values = Arrays.asList(category,brand + " " + model + " " + name, unitPrice + "$", "Quantity in stock : " + quantity, "Added on : " + dateAdded);

                // We use the extracted values to fill the labels
                FXManager.labelsFiller(labels, values);

                // We finally show the right pane
                pane.setVisible(true);

                // We set the refresh button to refresh the table when clicked
                refresh.setOnMouseClicked(imageClicked -> fillWithItems(itemsTable));

                // When the delete button is clicked
                view.setOnAction(view_event -> {

                    int itemId = Integer.parseInt(id);

                    List<String> product = InventoryDto.getProductByItemId(itemId);

                    String description = product.get(6);

                    showItemsDetails(Arrays.asList(category, brand + " " + model + " " + name, unitPrice + "$", description));

                });
           
        } 
            
        });
    }


    public static void showItemsDetails(List<String> values) {

        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);

        FXManager.loadFXML("/uiass/eia/gisiba/inventory/inventory/view_item_pane.fxml", pane, InventoryItemCrud.class);

        // We define the columns that will use to get the display item pane labels 
        List<String> view_item_columns = new ArrayList<String>(FXManager.catalog_labels_ids);
        view_item_columns.add(2, "unitPriceLabel");


        // We get the labels and fill them with the item's info
        List<Label> labels = FXManager.labelsCollector(pane, view_item_columns);
        labels.get(labels.size() - 1).setWrapText(true);
        FXManager.labelsFiller(labels, values); // This is where we fill the labels with the given values

        // We add the stage info and show it
        String iconPath = "/uiass/eia/gisiba/imgs/product-item.png";
        InputStream inputStream = ProductCrud.class.getResourceAsStream(iconPath);
        Image icon = new Image(inputStream);

        stage.setScene(scene);
        stage.setTitle("Item Details");
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.show();
        


    }

    public static void fillWithItems(TableView itemsTable) {

        // We send an http get request to get all the contacts of the given type
        List<List<String>> data = InventoryDto.getAllItems();  

        // We populate the table using those collected contacts
        List<String> columns = FXManager.inventory_columns;
        
        FXManager.populateTableView(itemsTable, columns, data);
    }

    public static void fillWithFilteredItems(TableView inventoryTable, List<List<String>> data) {
        
        // The columns we'll use for the table
        List<String> columns = FXManager.inventory_columns;
        
        FXManager.populateTableView(inventoryTable, columns, data);
    }
}