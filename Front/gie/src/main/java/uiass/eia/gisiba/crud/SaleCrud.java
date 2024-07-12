package uiass.eia.gisiba.crud;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uiass.eia.gisiba.FX.PurchaseFX;
import uiass.eia.gisiba.FX.SaleFX;
import uiass.eia.gisiba.controller.FXManager;
import uiass.eia.gisiba.http.dto.ContactDto;
import uiass.eia.gisiba.http.parsers.Parser;

public class SaleCrud {

    public static void create_orders(Parent pane) {

        // We collect the buttons from the fxml file
        Button search = FXManager.getButton(pane, "searchBtn");
        Button next = FXManager.getButton(pane, "nextBtn");
        Button addOrder = FXManager.getButton(pane, "addOrderBtn");
        AnchorPane refresh = FXManager.getAnchorPane(pane, "refreshBtn");

        // Table
        TableView productsTable = FXManager.getTableView(pane, "productsTable");

        // Hbox
        HBox addHbox = FXManager.getHBox(pane, "addHbox");

        // cart anchor pane
        AnchorPane cart = FXManager.getAnchorPane(addHbox, "cartContainer");

        // number of cart items image
        ImageView itemNumberImg = FXManager.getImageView(cart, "itemNumberImg");

        ImageView cartImg = FXManager.getImageView(cart, "cartImg");


        // Text Field
        TextField quantityTextField = FXManager.getTextField(pane, "quantityTextField");
        FXManager.setTextFieldNumericFormatRule(quantityTextField); // numeric input only rule

        // Combo Box
        List<ComboBox> comboBoxes = ProductCrud.productSearchComboBoxesHandler(pane); // We get and fill the combo boxes

        InventoryItemCrud.fillWithItemsForPurchase(productsTable); // we fill the table with products

        // a list to store the items ids and quantities
        List<Map<String, Object>> orders = new ArrayList<>();

        // When we press the search button
        SaleFX.productSelectionSearchHandler(search, productsTable, comboBoxes);

        SaleFX.productSelectionSavingHandler(productsTable, addHbox, quantityTextField, addOrder, cart, itemNumberImg, cartImg, orders);

        // We set the refresh button to refresh the table when clicked
        refresh.setOnMouseClicked(imageClicked -> ProductCrud.fillWithProducts(productsTable));

        // we handle the slider
        SaleFX.profitMarginSliderHandler(addHbox);

        next.setOnAction(event -> {

            if (!orders.isEmpty()) {

                goToCustomerSelectionPane(orders);

                ((Stage) pane.getScene().getWindow()).close(); // We close the products selection page after pressing the next button
            }

            else FXManager.showAlert(AlertType.ERROR, "Error", "No item was selected", "Please select atleast one item.");

       });

       cartImg.setOnMouseClicked(event -> {

            viewCurentOrders(orders);
       });

    }

    @SuppressWarnings("unchecked")
    public static void select_customer(Parent pane, List<Map<String,Object>> orders) {

        // ComboBox
        ComboBox customerTypeComboBox = FXManager.getComboBox(pane, "customerTypeComboBox");

        // Text Field
        TextField enterNameTextField = FXManager.getTextField(pane, "enterNameTextField");
        FXManager.setTextFieldAlphabeticFormatRule(enterNameTextField);
        TextField selectedCustomer = FXManager.getTextField(pane, "selectedCustomerTextField");

        // Button
        Button search = FXManager.getButton(pane, "searchBtn");
        Button next = FXManager.getButton(pane, "nextBtn");
        AnchorPane refresh = FXManager.getAnchorPane(pane, "refreshBtn");

        // Table
        TableView customersTable = FXManager.getTableView(pane, "customersTable");

        // We fill the comboBox with customer types
        PurchaseFX.fillTypeComboBox(customerTypeComboBox);

        // When the combobox is clicked
        customerTypeComboBox.valueProperty().addListener((obs, oldType, newType) -> {

            if (newType != null) { // When a value is chosen

                String type = String.valueOf(newType); // we get the value

                SaleFX.fillWithCustomersByType(customersTable, type); // we fill the table with the corresponding customers

                FXManager.showWrappableAlert(AlertType.INFORMATION, "Information", "Tip", "You can either select an already existing customer from the table, or enter a contact's name in the text field and they will be selected as a supplier for the current sale, make sure to select a customer type first though.");

            }
        });

        // A method that handles all event listeners in the pane :
        SaleFX.customerSelectionPaneButtonsHandler(customersTable, search, refresh, next, 
        
        customerTypeComboBox, enterNameTextField, selectedCustomer, orders);

    }

    public static void confirm_sale(Parent pane, String customerType, String customerName, List<Map<String,Object>> orders) {

        // we get the customer hbox to load the appropriate fxml
        HBox customerHbox = FXManager.getHBox(pane, "customerHBox");

        // the fxml file's path
        String path = "/uiass/eia/gisiba/sale/" + customerType.toLowerCase() + "_customer_HBox.fxml";

        // we load the fxml file
        FXManager.loadFXML(path, customerHbox, SaleCrud.class);

        // we get the customer vbox 
        VBox ordersVbox = FXManager.getVBox(pane, "ordersVBox");

        // we get the left vbox
        VBox leftVbox = FXManager.getVBox(pane, "leftVBox");

        // Confirm sale Button
        Button confirm = FXManager.getButton(pane, "confirmSaleBtn");

        // The map we'll use to generate the json
        Map<String,Object> saleMap = new HashMap<String,Object>();

        // here we get the sale's total amount
        String total = SaleFX.getSaleTotalAmmount(orders);

        // a method that takes care of populating the orders table using the orders list
        SaleFX.ordersTableFiller(ordersVbox, orders);

        // a method that takes care of filling the labels in the pane 
        SaleFX.saleConfirmingLabelsHandler(customerType, customerName, total, leftVbox);

        confirm.setOnAction(event -> {

            String saleDate = String.valueOf(Date.valueOf(LocalDate.now()));

            String ordersJsonArray = Parser.jsonGenerator(orders);

            // we get the customer object (list) from the backend and take just the id
            int customerId = Integer.parseInt(ContactDto.getContactByName(customerName, customerType).get(0));

            saleMap.put("customerId", customerId);

            saleMap.put("orders", ordersJsonArray);

            saleMap.put("saleDate", saleDate);

            saleMap.put("total", total);   
            
            // a method that handles asks the user for the sale status and performs the creation accordingly  
            SaleFX.operationConfirmingDialogBoxHandler(saleMap, customerType, "Sale");

            ((Stage) pane.getScene().getWindow()).close(); // We close the products selection page after pressing the confirm button
         
        });   

    }

    @SuppressWarnings("unchecked")
    public static void saleTableHandler(TableView saleTable, Parent pane, Button create) {

        HBox customerHbox = FXManager.getHBox(pane, "customerHbox");

        HBox ordersHbox = FXManager.getHBox(pane, "ordersHbox");

        saleTable.setOnMouseClicked(event -> {

            if (!saleTable.getSelectionModel().isEmpty()) {

                // We get the selected row and extract the values
                List<String> selectedItem = (List<String>) saleTable.getSelectionModel().getSelectedItem();

                int saleId = Integer.parseInt(selectedItem.get(0));

                int customerId = Integer.parseInt(selectedItem.get(1));
                
                String customerType = selectedItem.get(3);

                SaleFX.showSaleCustomer(customerId, customerType, customerHbox);

                SaleFX.showSaleOrders(saleId, ordersHbox);

            }
        });

        
    }

    public static void editSaleOrdersPane(Parent pane, String saleId) {

        // Table View
        TableView ordersTable = FXManager.getTableView(pane, "ordersTableView");

        // Button
        ImageView confirm = FXManager.getImageView(pane, "confirmBtn");

        SaleFX.editOrdersTableHandler(ordersTable, saleId, pane);

        confirm.setOnMouseClicked(event -> {

            ((Stage) pane.getScene().getWindow()).close(); // We close the page after pressing the confirm button

        });
    
    }

    public static void goToCreateSalePane() {

        goToItemsSelectionPane();

    }

    public static void goToItemsSelectionPane() {

        // We create the stage that will contain the creation page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);

        // here we load the creation page fxml file

        String path = "/uiass/eia/gisiba/sale/create_sale_products_selection_pane.fxml";
        FXManager.loadFXML(path, pane, SaleCrud.class); 

        // We call the method that handles the creation
        create_orders(pane);
        
        // We add the stage info and show it
        String iconPath = "/uiass/eia/gisiba/imgs/carts.png";
        InputStream inputStream = SaleCrud.class.getResourceAsStream(iconPath);
        Image icon = new Image(inputStream);

        stage.setScene(scene);
        stage.setTitle("Select Items To Order");
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.show();

    }

    public static void viewCurentOrders(List<Map<String,Object>> orders) {

        // We create the stage that will contain the view page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);

        // here we load the creation page fxml file
        String path = "/uiass/eia/gisiba/sale/create_sale_products_selection_view_orders_pane.fxml";
        FXManager.loadFXML(path, pane, SaleCrud.class); 

        VBox vbox = FXManager.getVBox(pane, "mainVbox");

        // We call the method that handles the creation
        SaleFX.ordersTableFiller(vbox, orders);
        
        // We add the stage info and show it
        String iconPath = "/uiass/eia/gisiba/imgs/completed-task.png";
        InputStream inputStream = SaleCrud.class.getResourceAsStream(iconPath);
        Image icon = new Image(inputStream);

        stage.setScene(scene);
        stage.setTitle("Current orders");
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.show();
    }

    public static void goToCustomerSelectionPane(List<Map<String,Object>>  orders) {

        // We create the stage that will contain the creation page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);

        // here we load the creation page fxml file

        String path = "/uiass/eia/gisiba/sale/create_sale_customer_selection_pane.fxml";
        FXManager.loadFXML(path, pane, SaleCrud.class); 

        // We call the method that handles the creation
        select_customer(pane, orders);
        
        // We add the stage info and show it
        String iconPath = "/uiass/eia/gisiba/imgs/customer.png";
        InputStream inputStream = SaleCrud.class.getResourceAsStream(iconPath);
        Image icon = new Image(inputStream);

        stage.setScene(scene);
        stage.setTitle("Select customer");
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.show();
    }

    public static void goToSaleSummaryPane(String customerType, String customerName, List<Map<String,Object>>  orders) {

        // We create the stage that will contain the creation page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);

        // here we load the creation page fxml file
        String path = "/uiass/eia/gisiba/sale/sale_summary_pane.fxml";
        FXManager.loadFXML(path, pane, SaleCrud.class); 

        // We call the method that handles the creation
        confirm_sale(pane, customerType, customerName, orders);
        
        // We add the stage info and show it
        String iconPath = "/uiass/eia/gisiba/imgs/check.png";
        InputStream inputStream = SaleCrud.class.getResourceAsStream(iconPath);
        Image icon = new Image(inputStream);

        stage.setScene(scene);
        stage.setTitle("Confirm Sale");
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.show();
    }

}
