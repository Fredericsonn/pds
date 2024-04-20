package uiass.eia.gisiba.controller;

import java.io.IOException;
import java.util.*;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import uiass.eia.gisiba.crud.ContactCrud;
import uiass.eia.gisiba.crud.ProductCrud;
import uiass.eia.gisiba.dto.AddressDto;
import uiass.eia.gisiba.dto.ContactDto;
import uiass.eia.gisiba.dto.ProductDto;

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

        loadFXML("/uiass/eia/gisiba/contact_center_pane.fxml", centerAnchorPane);
        loadFXML("/uiass/eia/gisiba/" + contactType.toLowerCase() + "_right_pane.fxml", rightAnchorPane);

        rightAnchorPane.setVisible(false);

        List<String> labelIds = FXManager.labels_ids_per_contact_type_map.get(contactType);
        
        // Buttons
        Button search = FXManager.getButton(centerAnchorPane, "searchBtn");
        Button createNew = FXManager.getButton(centerAnchorPane, "createNewContactBtn");
        Button update = FXManager.getButton(rightAnchorPane, "updateBtn");
        Button delete = FXManager.getButton(rightAnchorPane, "deleteBtn");
        Button notify = FXManager.getButton(rightAnchorPane, "notifyBtn");

        // Search text field
        TextField txtField = FXManager.getTextField(centerAnchorPane, "enterIdTextField");
        FXManager.setTextFieldNumericFormatRule(txtField);  // a rule that denies any non numeric input

        // Labels
        List<Label> labels = FXManager.labelsCollector(rightAnchorPane, labelIds);

        // Tables
        TableView<List<String>> contactsTable = FXManager.getTableView(centerAnchorPane, "contactsTableView");

        contactsTable.setOnMouseClicked(event -> {
            if (!contactsTable.getSelectionModel().isEmpty()) {

                // We get the selected row and extract the values
                List<String> selectedItem = (List<String>) contactsTable.getSelectionModel().getSelectedItem();
                int contactId = Integer.parseInt(selectedItem.get(0));
                String firstAttribute = selectedItem.get(1);
                String secondAttribute = selectedItem.get(2);
                String phoneNumber = selectedItem.get(3);
                String email = selectedItem.get(4);
                int addressId = Integer.parseInt(selectedItem.get(5));
                String address = AddressDto.addressFormulator(addressId, contactType); // We formulate the full address using its id

                // We put all the values in one list that we'll use to fill the labels
                List<String> values = Arrays.asList(firstAttribute,secondAttribute,phoneNumber,email,address);

                // We use the extracted values to fill the labels
                FXManager.contactLabelsFiller(labels, values, contactType);

                // We finally show the right pane
                rightAnchorPane.setVisible(true);

                // When the update button is clicked
                update.setOnAction(update_event -> {
                    this.goToUpdateContactPage(contactType, contactId, addressId);
                });

                // When the delete button is clicked
                delete.setOnAction(delete_event -> ContactCrud.deleteContact(contactType, contactId));
           
        } 
            
        });

        // We send an http get request to get all the contacts of the given type
        List<List<String>> data = ContactDto.getAllContactsByType(contactType);  

        // We populate the table using those collected contacts
        List<String> columns = FXManager.columns_names_per_contact_type.get(contactType);
        FXManager.populateTableView(contactsTable, columns, data);

        // When we press the search button
        search.setOnAction(event -> {
            

            try {

                // We collect the entered id (we suppose it's a number)
                int contactId = Integer.parseInt(txtField.getText());

                // We get the contact using that id
                List<String> info = ContactDto.getContactById(contactId, contactType);

                if (info != null) {  // if there is a contact with the given id

                    // We extract each attribute's value from the retrieved contact
                    String firstAttribute = info.get(1);
                    String secondAttribute = info.get(2);
                    String phoneNumber = info.get(3);
                    String email = info.get(4);
                    int addressId = Integer.parseInt(info.get(5));
                    String address = AddressDto.addressFormulator(addressId, contactType);

                    // We put all the values in one list that we'll use to fill the labels
                    List<String> values = Arrays.asList(firstAttribute,secondAttribute,phoneNumber,email,address);
    
                    // We use the extracted values to fill the labels
                    FXManager.contactLabelsFiller(labels, values, contactType);

                    // We finally show the right pane
                    rightAnchorPane.setVisible(true);

                    // When the update button is clicked
                    update.setOnAction(update_event -> {
                        this.goToUpdateContactPage(contactType, contactId, addressId);
                    });

                    // When the delete button is clicked
                    delete.setOnAction(delete_event -> ContactCrud.deleteContact(contactType, contactId));
                }
                
                // if no contact corresponds to the provided id we show an error alert
                else FXManager.showAlert(AlertType.ERROR, "Invalid Id", "Contact Not Found", contactId + " doesn't correspond to any existing contact.");
                 
            }

            // if the text field is empty and the search button is clicked
            catch(NumberFormatException e) {
                FXManager.showAlert(AlertType.ERROR, "Invalid Id", "Empty Id Field", "Please provide an Id.");
            }
        });

        // When the create new button is clicked
        createNew.setOnAction(event -> {
            this.goToCreateContactPage(contactType);
            
        });

        // When the notify button is clicked
        notify.setOnAction(event -> {

            // We collect the receiver's email from the email label
            String receiverEmail = FXManager.getLabel(rightAnchorPane, "emailLabel").getText();

            this.goToSendEmailPage(receiverEmail);
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

        loadFXML("/uiass/eia/gisiba/catalog_center_pane.fxml", centerAnchorPane);
        loadFXML("/uiass/eia/gisiba/catalog_right_pane.fxml", rightAnchorPane);

        rightAnchorPane.setVisible(false);

        List<String> labelIds = FXManager.catalog_labels_ids;
        
        // Buttons
        Button search = FXManager.getButton(centerAnchorPane, "searchBtn");
        Button createNew = FXManager.getButton(centerAnchorPane, "createNewProductBtn");
        Button update = FXManager.getButton(rightAnchorPane, "updateBtn");
        Button delete = FXManager.getButton(rightAnchorPane, "deleteBtn");

        // Search text field
        TextField txtField = FXManager.getTextField(centerAnchorPane, "enterRefTextField");
        FXManager.setTextFieldAlphanumericFormatRule(txtField); // We block any non alphanumeric input

        // Labels
        List<Label> labels = FXManager.labelsCollector(rightAnchorPane, labelIds);

        // Tables
        TableView<List<String>> contactsTable = FXManager.getTableView(centerAnchorPane, "productsTableView");

        contactsTable.setOnMouseClicked(event -> {
            if (!contactsTable.getSelectionModel().isEmpty()) {

                // We get the selected row and extract the values
                List<String> selectedItem = (List<String>) contactsTable.getSelectionModel().getSelectedItem();
                String ref = selectedItem.get(0);
                String category = selectedItem.get(1);
                String brand = selectedItem.get(2);
                String model = selectedItem.get(3);
                String description = selectedItem.get(4);
                String unitPrice = String.valueOf(selectedItem.get(5));

                // We put all the values in one list that we'll use to fill the labels
                List<String> values = Arrays.asList(ref,category,brand,model,unitPrice,description);

                // We use the extracted values to fill the labels
                FXManager.catalogLabelsFiller(labels, values);

                // We finally show the right pane
                rightAnchorPane.setVisible(true);

                // When the update button is clicked
                update.setOnAction(update_event -> {
                    this.goToUpdateProductPage(ref);
                });

                // When the delete button is clicked
                delete.setOnAction(delete_event -> ProductCrud.deleteProduct(ref));
           
        } 
            
        });

        // We send an http get request to get all the contacts of the given type
        List<List<String>> data = ProductDto.getAllProducts();  

        // We populate the table using those collected contacts
        List<String> columns = FXManager.catalog_columns;
        FXManager.populateTableView(contactsTable, columns, data);

        // When we press the search button
        search.setOnAction(event -> {
            
            // We collect the entered id (we suppose it's a number)
            String ref = txtField.getText();

            if (ref != "") {

                // We get the contact using that id
                List<String> info = ProductDto.getProductByRef(ref);

                if (info != null) {  // if there is a contact with the given id

                    // We extract each attribute's value from the retrieved contact
                    String category = info.get(1);
                    String brand = info.get(2);
                    String model = info.get(3);
                    String description = info.get(4);
                    String unitPrice = info.get(5);

                    // We put all the values in one list that we'll use to fill the labels
                    List<String> values = Arrays.asList(category, brand, model,description, unitPrice);
    
                    // We use the extracted values to fill the labels
                    FXManager.catalogLabelsFiller(labels, values);

                    // We finally show the right pane
                    rightAnchorPane.setVisible(true);

                    // When the update button is clicked
                    update.setOnAction(update_event -> {
                        this.goToUpdateProductPage(ref);
                    });

                    // When the delete button is clicked
                    delete.setOnAction(delete_event -> ProductCrud.deleteProduct(ref));
                }
                
                // if no product corresponds to the provided ref we show an error alert
                else FXManager.showAlert(AlertType.ERROR, "ERROR", "Product Not Found", ref + " doesn't correspond to any existing product.");
            }

            // if the text field is empty and the search button is clicked
            else FXManager.showAlert(AlertType.ERROR, "ERROR", "Empty Reference Field", "Please provide a product reference.");
        });

        // When the create new button is clicked
        createNew.setOnAction(event -> {
            this.goToCreateProductPage();
            
        });
 
    }

    // A method that display the contact creation pane
    public void goToCreateContactPage(String contactType) {

        // We create the stage that will contain the creation page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);
        loadFXML("/uiass/eia/gisiba/create_update_person_page.fxml", pane);  // here we load the creation page fxml file
        
        // We collect the confirm button from the fxml file
        Button confirm = FXManager.getButton(pane, "confirmBtn");

        // We add the corresponding event listener to the button
        ContactCrud.create_contact(pane, confirm, contactType);
        
        // We add the stage info and show it
        stage.setScene(scene);
        stage.setTitle(contactType.equals("Person") ? "Create Person" : "Create Enterprise");
        stage.setResizable(false);
        stage.show();

    }

    // A method that display the contact update pane
    public void goToUpdateContactPage(String contactType, int contactId, int addressId) {

        // We create the stage that will contain the update page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);
        loadFXML("/uiass/eia/gisiba/create_update_person_page.fxml", pane); // here we load the update page fxml file
        
        // We collect the confirm button from the fxml file
        Button confirm = FXManager.getButton(pane, "confirmBtn");

        // We add the corresponding event listener to the button
        ContactCrud.update_contact(pane, confirm, contactType, contactId, addressId);

        // We add the stage info and show it
        stage.setScene(scene);
        stage.setTitle(contactType.equals("Person") ? "Update Person" : "Update Enterprise");
        stage.setResizable(false);
        stage.show();

    }

    // A method that display the email sending pane
    public void goToSendEmailPage(String receiverEmail) {

        // We create the stage that will contain the email sending page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);
        loadFXML("/uiass/eia/gisiba/send_email_pane.fxml", pane); // here we load the email sending page fxml file

        // We collect the send button from the fxml file
        Button send = FXManager.getButton(pane, "sendEmailBtn");

        // We add the corresponding event listener to the butto
        ContactCrud.sendEmail(pane, send, receiverEmail);

        // We add the stage info and show it
        stage.setScene(scene);
        stage.setTitle("Email Sending");
        stage.setResizable(false);
        stage.show();
    }

    // A method that display the product creation pane
    public void goToCreateProductPage() {

        // We create the stage that will contain the creation page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);
        loadFXML("/uiass/eia/gisiba/create_update_catalog_pane.fxml", pane);  // here we load the creation page fxml file
        
        // We collect the confirm button from the fxml file
        Button confirm = FXManager.getButton(pane, "confirmBtn");

        // We add the corresponding event listener to the button
        ProductCrud.create_product(pane, confirm);;
        
        // We add the stage info and show it
        stage.setScene(scene);
        stage.setTitle("Create Product");
        stage.setResizable(false);
        stage.show();

    }

    // A method that display the product update pane
    public void goToUpdateProductPage(String ref) {

        // We create the stage that will contain the update page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);
        loadFXML("/uiass/eia/gisiba/create_update_catalog_pane.fxml", pane); // here we load the update page fxml file
        
        // We collect the confirm button from the fxml file
        Button confirm = FXManager.getButton(pane, "confirmBtn");

        // We add the corresponding event listener to the button
        ProductCrud.update_product(pane, confirm, ref);

        // We add the stage info and show it
        stage.setScene(scene);
        stage.setTitle("Update Product");
        stage.setResizable(false);
        stage.show();

    }

    // A method that loads an fxml file into a pane
    private void loadFXML(String fxmlFile, AnchorPane pane) {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent content = loader.load();
            pane.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
