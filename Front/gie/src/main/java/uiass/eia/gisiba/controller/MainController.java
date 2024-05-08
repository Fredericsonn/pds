package uiass.eia.gisiba.controller;


import java.io.InputStream;
import java.util.*;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import uiass.eia.gisiba.crud.ContactCrud;
import uiass.eia.gisiba.crud.InventoryItemCrud;
import uiass.eia.gisiba.crud.ProductCrud;
import uiass.eia.gisiba.http.dto.ContactDto;
import uiass.eia.gisiba.http.dto.InventoryDto;
import uiass.eia.gisiba.http.dto.ProductDto;

public class MainController {

    @FXML 
    private AnchorPane leftAnchorPane;

    @FXML
    private AnchorPane centerAnchorPane;

    @FXML
    private AnchorPane rightAnchorPane;

    @FXML
    private MenuItem personsMenuItem;

    /*@FXML
    private void switchToScene() throws IOException {
        // Call the method to switch to the "Person_Search_Page" scene
        Main.setRoot("Person_Search_Page");
    }*/

    

    @FXML
    // A generic fx method that controls the crm interface depending on the contact type
    private void loadContactPane(String contactType) {

        String type = contactType.toLowerCase();

        System.out.println("/uiass/eia/gisiba/contact/" + type + "/" + type + "_right_pane.fxml");
        
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

        // Search text field
        TextField txtField = FXManager.getTextField(centerAnchorPane, "enterNameTextField");
        FXManager.setTextFieldAlphabeticFormatRule(txtField);

        // Labels
        List<Label> labels = FXManager.labelsCollector(rightAnchorPane, labelIds);

        // Tables
        TableView<List<String>> contactsTable = FXManager.getTableView(centerAnchorPane, "contactsTableView");

        // We set the contacts table's columns event listeners
        ContactCrud.contactsTableEventHandler(contactsTable, labels, rightAnchorPane, contactType, update, delete);

        // We send an http get request to get all the contacts of the given type
        List<List<String>> data = ContactDto.getAllContactsByType(contactType);  

        // We populate the table using those collected contacts
        List<String> columns = FXManager.columns_names_per_contact_type.get(contactType);
        FXManager.populateTableView(contactsTable, columns, data);

        ContactCrud.contactSearchButtonHandler(search, txtField, labels, contactType, rightAnchorPane, update, delete);

        // When the create new button is clicked
        createNew.setOnAction(event -> {
            ContactCrud.goToCreateContactPage(contactType);
            
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

            //FXManager.textFieldsEmptier(textFields);
            ProductCrud.fillWithProducts(productsTable);
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
        Button add = FXManager.getButton(rightAnchorPane, "addBtn");
        Button view = FXManager.getButton(rightAnchorPane, "viewBtn");

        // Refresh Image
        AnchorPane refresh = FXManager.getAnchorPane(centerAnchorPane, "refreshPane");

        // Table Views
        TableView<List<String>> inventoryTableView = FXManager.getTableView(centerAnchorPane, "itemsTableView");

        InventoryItemCrud.itemsTableEventHandler(inventoryTableView, labels, rightAnchorPane, refresh, add, view);

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


}
