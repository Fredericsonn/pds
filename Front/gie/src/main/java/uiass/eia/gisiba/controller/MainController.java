package uiass.eia.gisiba.controller;


import java.io.IOException;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uiass.eia.gisiba.Main;
import uiass.eia.gisiba.FX.PurchaseFX;
import uiass.eia.gisiba.FX.SaleFX;
import uiass.eia.gisiba.crud.CategoryCrud;
import uiass.eia.gisiba.crud.ContactCrud;
import uiass.eia.gisiba.crud.InventoryItemCrud;
import uiass.eia.gisiba.crud.OrderCrud;
import uiass.eia.gisiba.crud.ProductCrud;
import uiass.eia.gisiba.crud.PurchaseCrud;
import uiass.eia.gisiba.crud.SaleCrud;
import uiass.eia.gisiba.http.dto.ContactDto;
import uiass.eia.gisiba.http.dto.InventoryDto;
import uiass.eia.gisiba.http.dto.OrderDto;
import uiass.eia.gisiba.http.dto.ProductDto;
import uiass.eia.gisiba.http.dto.PurchaseDto;
import uiass.eia.gisiba.http.parsers.Parser;
import uiass.eia.gisiba.http.parsers.PurchaseParser;

public class MainController {

    @FXML 
    private AnchorPane leftAnchorPane;

    @FXML
    private AnchorPane centerAnchorPane;

    @FXML
    private AnchorPane rightAnchorPane;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    // A generic fx method that controls the crm interface depending on the contact type
    private void loadContactPane(String contactType) {

        String type = contactType.toLowerCase();
                
        FXManager.loadFXML("/uiass/eia/gisiba/crm/contact/contact_center_pane.fxml", centerAnchorPane, getClass());
        FXManager.loadFXML("/uiass/eia/gisiba/crm/contact/" + type + "/" + type + "_right_pane.fxml", rightAnchorPane, getClass());
        

        rightAnchorPane.setVisible(false);

        List<String> labelIds = FXManager.labels_ids_per_contact_type_map.get(contactType);
        
        // Buttons
        Button search = FXManager.getButton(centerAnchorPane, "searchBtn");
        Button createNew = FXManager.getButton(centerAnchorPane, "createNewContactBtn");
        Button update = FXManager.getButton(rightAnchorPane, "updateBtn");
        Button delete = FXManager.getButton(rightAnchorPane, "deleteBtn");
        Button notify = FXManager.getButton(rightAnchorPane, "notifyBtn");
        AnchorPane refresh = FXManager.getAnchorPane(centerAnchorPane, "refreshBtn");

        // Search text field
        TextField txtField = FXManager.getTextField(centerAnchorPane, "enterNameTextField");
        FXManager.setTextFieldAlphabeticFormatRule(txtField);

        // Labels
        List<Label> labels = FXManager.labelsCollector(rightAnchorPane, labelIds);

        // Tables
        TableView<List<String>> contactsTable = FXManager.getTableView(centerAnchorPane, "contactsTableView");

        // We set the contacts table's columns event listeners
        ContactCrud.contactsTableEventHandler(contactsTable, labels, rightAnchorPane, contactType, update, delete);


        // We populate the contacts table 
        ContactCrud.fillWithContacts(contactsTable, contactType);

        // We handle the search button event listener
        ContactCrud.contactSearchButtonHandler(search, txtField, labels, contactType, rightAnchorPane, update, delete);

        // When the create new button is clicked
        createNew.setOnAction(event -> {
            ContactCrud.goToCreateContactPage(contactType);
            
        });

        // When the refresh button is clicked
        refresh.setOnMouseClicked(event -> {

            ContactCrud.fillWithContacts(contactsTable, contactType);
            
        });

        // When the notify button is clicked
        notify.setOnAction(event -> {

            // We collect the receiver's email from the email label
            String receiverEmail = FXManager.getLabel(rightAnchorPane, "emailLabel").getText();

            ContactCrud.goToSendEmailPage(receiverEmail);
        });


 
    }

    @FXML
    // The person controller
    private void loadPersonPane() {
        loadContactPane("Person");
    }

    @FXML
    // The enterprise controller
    private void loadEnterprisePane() {
        loadContactPane("Enterprise");
    }

    @FXML
    // An fx method that controls the catalog 
    private void loadProductPane() {

        FXManager.loadFXML("/uiass/eia/gisiba/inventory/catalog/catalog_center_pane.fxml", centerAnchorPane, getClass());
        FXManager.loadFXML("/uiass/eia/gisiba/inventory/catalog/catalog_right_pane.fxml", rightAnchorPane, getClass());

        rightAnchorPane.setVisible(false);

        List<String> labelIds = FXManager.catalog_labels_ids;
        
        // Buttons
        Button search = FXManager.getButton(centerAnchorPane, "searchBtn");
        Button createNew = FXManager.getButton(centerAnchorPane, "createNewProductBtn");
        Button update = FXManager.getButton(rightAnchorPane, "updateBtn");
        Button delete = FXManager.getButton(rightAnchorPane, "deleteBtn");

        // Search text fields
        // a method that collects the text fields and sets input rules :
        List<ComboBox> comboBoxes = ProductCrud.productSearchComboBoxesHandler(centerAnchorPane);

        ComboBox categoryComboBox = comboBoxes.get(0);     // We get
        ComboBox brandComboBox = comboBoxes.get(1);        // the text fields
        ComboBox modelComboBox = comboBoxes.get(2);        // from the list

        // Labels
        List<Label> labels = FXManager.labelsCollector(rightAnchorPane, labelIds);

        // Refresh Image
        AnchorPane refresh = FXManager.getAnchorPane(centerAnchorPane, "refreshPane");  

        // Tables
        TableView<List<String>> productsTable = FXManager.getTableView(centerAnchorPane, "productsTableView");

        // A method that handles the table rows event listners
        ProductCrud.productTableEventHandler(productsTable, labels, rightAnchorPane,refresh, update, delete);

        // We the table with all the products
        ProductCrud.fillWithProducts(productsTable);

        // We set the refresh button to refresh the table when clicked
        refresh.setOnMouseClicked(imageClicked -> {

            loadProductPane();
        });

        // When we press the search button
        search.setOnAction(event -> {
            
            // We collect the entered id (we suppose it's a number)
            String categroyInput = (String) categoryComboBox.getValue();
            String brandInput = (String) brandComboBox.getValue();
            String modelInput = (String) modelComboBox.getValue();

            List<String> values = Arrays.asList(categroyInput,brandInput,modelInput);

            String json = ProductCrud.filteredProductSearchJsonGenerator(values);            

            if (ProductCrud.productSearchValidator(comboBoxes)) {

                // We get the products that match the filter criteria
                List<List<String>> data = ProductDto.getFilteredProducts(json);

                if (!data.isEmpty()) {  // if there are matching products 

                    // We fill the products table with the matching products
                    ProductCrud.fillWithFilteredProducts(productsTable, data);

                }
                
                // if no product corresponds to the provided ref we show an error alert
                else FXManager.showAlert(AlertType.ERROR, "ERROR", "Products Not Found"," No existing products match the given criteria.");
            }

            // if the text field is empty and the search button is clicked
            else FXManager.showAlert(AlertType.ERROR, "ERROR", "No Selected Parameter", "Please provide some parameters for the search.");
        });

        // When the create new button is clicked
        createNew.setOnAction(event -> {
            ProductCrud.whatToCreate();
            
        });
 
    }

    @FXML
    public void loadInventoryPane() {

        FXManager.loadFXML("/uiass/eia/gisiba/inventory/inventory/inventory_center_pane.fxml", centerAnchorPane, getClass());
        FXManager.loadFXML("/uiass/eia/gisiba/inventory/inventory/inventory_right_pane.fxml", rightAnchorPane, getClass());

        rightAnchorPane.setVisible(false);

        // Labels
        List<String> labelsIds = FXManager.inventory_labels_ids;

        // Search text fields
        // a method that collects the text fields and sets input rules :
        List<ComboBox> comboBoxes = ProductCrud.productSearchComboBoxesHandler(centerAnchorPane);
        ComboBox categoryComboBox = comboBoxes.get(0);     // We get
        ComboBox brandComboBox = comboBoxes.get(1);        // the text fields
        ComboBox modelComboBox = comboBoxes.get(2);        // from the list

        // Labels
        List<Label> labels = FXManager.labelsCollector(rightAnchorPane, labelsIds);

        // Buttons
        Button search = FXManager.getButton(centerAnchorPane, "searchBtn");
        Button setPrice = FXManager.getButton(rightAnchorPane, "setPriceBtn");
        Button view = FXManager.getButton(rightAnchorPane, "viewBtn");

        // Refresh Image
        AnchorPane refresh = FXManager.getAnchorPane(centerAnchorPane, "refreshPane");

        // Table Views
        TableView<List<String>> inventoryTableView = FXManager.getTableView(centerAnchorPane, "itemsTableView");

        InventoryItemCrud.itemsTableEventHandler(inventoryTableView, labels, rightAnchorPane, refresh, setPrice, view);

        InventoryItemCrud.fillWithItems(inventoryTableView);

        // When we press the search button
        search.setOnAction(event -> {
            
            // We collect the entered id (we suppose it's a number)
            String categroyInput = (String) categoryComboBox.getValue();
            String brandInput = (String) brandComboBox.getValue();
            String modelInput = (String) modelComboBox.getValue();

            List<String> values = Arrays.asList(categroyInput,brandInput,modelInput);

            String json = ProductCrud.filteredProductSearchJsonGenerator(values);            

            if (ProductCrud.productSearchValidator(comboBoxes)) {

                // We get the products that match the filter criteria
                List<List<String>> data = InventoryDto.getFilteredItems(json);

                if (!data.isEmpty()) {  // if there are matching products 

                    // We fill the products table with the matching products
                    InventoryItemCrud.fillWithFilteredItems(inventoryTableView, data);

                }
                
                // if no product corresponds to the provided ref we show an error alert
                else FXManager.showAlert(AlertType.ERROR, "ERROR", "Items Not Found"," No existing items match the given criteria.");
            }

            // if the text field is empty and the search button is clicked
            else FXManager.showAlert(AlertType.ERROR, "ERROR", "No Selected Parameter", "Please provide some parameters for the search.");
        });

    }

    public void loadOrdersPane(String operation) {

        FXManager.loadFXML("/uiass/eia/gisiba/orders/orders_center_pane.fxml", centerAnchorPane, getClass());

        FXManager.loadFXML("/uiass/eia/gisiba/orders/" + operation.toLowerCase() + "_order_right_pane.fxml", rightAnchorPane, getClass());

        rightAnchorPane.setVisible(false);

        List<String> labelIds = FXManager.order_labels_names_per_operation_type.get(operation);
        
        // Buttons
        Button search = FXManager.getButton(centerAnchorPane, "searchBtn");
        Button view = FXManager.getButton(rightAnchorPane, "viewBtn");
        Button stats = FXManager.getButton(rightAnchorPane, "statsBtn");

        // Search text fields
        // a method that collects the text fields and sets input rules :
        List<ComboBox> comboBoxes = ProductCrud.productSearchComboBoxesHandler(centerAnchorPane);

        // Labels
        List<Label> labels = FXManager.labelsCollector(rightAnchorPane, labelIds);

        // Refresh Image
        AnchorPane refresh = FXManager.getAnchorPane(centerAnchorPane, "refreshPane");  

        // Tables
        TableView<List<String>> ordersTable = FXManager.getTableView(centerAnchorPane, "ordersTableView");

        // A method that handles the table rows event listners
        OrderCrud.orderTableEventHandler(ordersTable, labels, rightAnchorPane, refresh, view, stats, operation);

        // We the table with all the orders
        OrderCrud.fillWithOrders(ordersTable, operation);

        // We set the refresh button to refresh the table when clicked
        refresh.setOnMouseClicked(imageClicked -> {

            //FXManager.textFieldsEmptier(textFields);
            OrderCrud.fillWithOrders(ordersTable, operation);
        });

        // When we press the search button
        OrderCrud.ordersSearchButtonHandler(search, ordersTable, comboBoxes, operation);
 
    
}

    @FXML
    public void loadPurchaseOrdersPane() {

        loadOrdersPane("purchase");
    }

    @SuppressWarnings("unchecked")
    public void loadPurchasePane() throws IOException {

        String fxml = "/uiass/eia/gisiba/purchase/purchases_control_pane.fxml";

        AnchorPane pane = FXManager.switchScene(centerAnchorPane, getClass(), fxml);

        // Buttons 
        Button search = FXManager.getButton(pane, "searchBtn");
        Button create = FXManager.getButton(pane, "createNewBtn");

        // ComboBoxes
        ComboBox supplierComboBox = FXManager.getComboBox(pane, "supplierComboBox");
        ComboBox statusComboBox = FXManager.getComboBox(pane, "statusComboBox");

        // DatePickers
        DatePicker startDatePicker = FXManager.getDatePicker(pane, "startDatePicker");
        DatePicker endDatePicker = FXManager.getDatePicker(pane, "endDatePicker");

        // Images
        AnchorPane refreshBtn = FXManager.getAnchorPane(pane, "refreshImgContainer");
        ImageView goBack = FXManager.getImageView(pane, "goBackImg");

        // Table
        TableView purchasesTable = FXManager.getTableView(pane, "purchasesTableView");

        //HBox
        HBox supplierHBox = FXManager.getHBox(pane, "supplierHbox");
        HBox ordersHBox = FXManager.getHBox(pane, "ordersHbox");

        // we fill the purchases table with the purchases
        PurchaseFX.fillWithOperations(purchasesTable, "purchase");
        
        PurchaseFX.purchaseTableContextMenuAssociator(purchasesTable);

        // we fill the suppliers combo box with the suppliers
        PurchaseFX.comboBoxesHandler(supplierComboBox,statusComboBox);

        PurchaseCrud.purchasesTableHandler(purchasesTable, pane, create);

        search.setOnAction(event -> {

            String supplierName = (String) supplierComboBox.getValue(); 

            String status = (String) statusComboBox.getValue();

            List<String> dates_values = PurchaseFX.datesPickerValuesCollector(startDatePicker, endDatePicker); 

            List filterInput = Arrays.asList(supplierName, status, dates_values);

            if (PurchaseFX.validFilter(filterInput)) {

                List<List<String>> purchases = PurchaseFX.operationSearchFilter(supplierName, status, dates_values, "Purchase");

                if (!purchases.isEmpty()) {

                    PurchaseFX.fillWithFilteredOperations(purchasesTable, purchases);
                }

                else FXManager.showAlert(AlertType.WARNING, "Error", "No match", "No data matches the given criteria");

            }

            else FXManager.showAlert(AlertType.WARNING, "Error", "No criteria provided", "Please provide some parameters for the search.");


        });

        refreshBtn.setOnMouseClicked(event -> {

            supplierComboBox.setPromptText("supplier"); supplierComboBox.setValue(null);  

            statusComboBox.setPromptText("status"); statusComboBox.setValue(null);  

            startDatePicker.setValue(null); startDatePicker.setPromptText("start date");

            endDatePicker.setValue(null); endDatePicker.setPromptText("end date");

            supplierHBox.setVisible(false);

            ordersHBox.setVisible(false);

            PurchaseFX.fillWithOperations(purchasesTable, "purchase");

        });

        goBack.setOnMouseClicked(event -> {

            FXManager.switchScene(ordersHBox, getClass(), "/uiass/eia/gisiba/main.fxml");

        });

        create.setOnAction(event -> {

            PurchaseCrud.goToCreatePurchasePane();
        });

        
    }

    @FXML
    public void loadSaleOrdersPane() {

        loadOrdersPane("sale");
    }

    @SuppressWarnings("unchecked")
    @FXML
    public void loadSalePane() {

        String fxml = "/uiass/eia/gisiba/sale/sales_control_pane.fxml";

        AnchorPane pane = FXManager.switchScene(centerAnchorPane, getClass(), fxml);

        // Buttons 
        Button search = FXManager.getButton(pane, "searchBtn");
        Button create = FXManager.getButton(pane, "createNewBtn");

        // ComboBoxes
        ComboBox customerComboBox = FXManager.getComboBox(pane, "customerComboBox");
        ComboBox statusComboBox = FXManager.getComboBox(pane, "statusComboBox");

        // DatePickers
        DatePicker startDatePicker = FXManager.getDatePicker(pane, "startDatePicker");
        DatePicker endDatePicker = FXManager.getDatePicker(pane, "endDatePicker");

        // Images
        AnchorPane refreshBtn = FXManager.getAnchorPane(pane, "refreshImgContainer");
        ImageView goBack = FXManager.getImageView(pane, "goBackImg");

        // Table
        TableView salesTable = FXManager.getTableView(pane, "salesTableView");

        //HBox
        HBox customerHBox = FXManager.getHBox(pane, "customerHbox");
        HBox ordersHBox = FXManager.getHBox(pane, "ordersHbox");

        // we fill the sales table with the sales
        SaleFX.fillWithSales(salesTable);

        // we handle and set the context menu to the sales table
        SaleFX.saleTableContextMenuAssociator(salesTable);

        // we fill the customers combo box with the customers
        SaleFX.comboBoxesHandler(customerComboBox,statusComboBox);

        // we set event listeners for the when the table is clicked
        SaleFX.salesTableHandler(salesTable, pane, create);

        // when the search button is clicked
        search.setOnAction(event -> {

            String customerName = (String) customerComboBox.getValue(); 

            String status = (String) statusComboBox.getValue();

            List<String> dates_values = SaleFX.datesPickerValuesCollector(startDatePicker, endDatePicker); 

            List filterInput = Arrays.asList(customerName, status, dates_values);

            if (SaleFX.validFilter(filterInput)) {

                List<List<String>> sales = SaleFX.operationSearchFilter(customerName, status, dates_values, "Sale");

                if (!sales.isEmpty()) {

                    SaleFX.fillWithFilteredOperations(salesTable, sales);
                }

                else FXManager.showAlert(AlertType.WARNING, "Error", "No match", "No data matches the given criteria");

            }

            else FXManager.showAlert(AlertType.WARNING, "Error", "No criteria provided", "Please provide some parameters for the search.");


        });

        refreshBtn.setOnMouseClicked(event -> {

            customerComboBox.setPromptText("customer"); customerComboBox.setValue(null);  

            statusComboBox.setPromptText("status"); statusComboBox.setValue(null);  

            startDatePicker.setValue(null); startDatePicker.setPromptText("start date");

            endDatePicker.setValue(null); endDatePicker.setPromptText("end date");

            customerHBox.setVisible(false);

            ordersHBox.setVisible(false);

            SaleFX.fillWithOperations(salesTable, "sale");

        });

        goBack.setOnMouseClicked(event -> {

            FXManager.switchScene(ordersHBox, getClass(), "/uiass/eia/gisiba/main.fxml");

        });

        create.setOnAction(event -> {

            SaleCrud.goToCreateSalePane();
        });
    }


}
