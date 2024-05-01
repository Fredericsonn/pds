package uiass.eia.gisiba.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import uiass.eia.gisiba.crud.ContactCrud;
import uiass.eia.gisiba.crud.ProductCrud;
import uiass.eia.gisiba.http.dto.ContactDto;
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
        
        loadFXML("/uiass/eia/gisiba/crm/contact/contact_center_pane.fxml", centerAnchorPane);
        loadFXML("/uiass/eia/gisiba/crm/contact/" + type + "/" + type + "_right_pane.fxml", rightAnchorPane);
        

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
                String houseNumber = selectedItem.get(6);
                String neighborhood = selectedItem.get(7);
                String city = selectedItem.get(8);
                String zipCode = selectedItem.get(9);
                String country = selectedItem.get(10);

                String address = houseNumber + " " + neighborhood + " " +
                
                city + " " + zipCode + " " + country; // We formulate the full address using its id

                // We put all the values in one list that we'll use to fill the labels
                List<String> values = Arrays.asList(firstAttribute,secondAttribute,phoneNumber,email,address);

                // We use the extracted values to fill the labels
                FXManager.contactLabelsFiller(labels, values, contactType);

                // We finally show the right pane
                rightAnchorPane.setVisible(true);

                // When the update button is clicked
                update.setOnAction(update_event -> {
                    // We collect ll the original values to be passed as the text fields prompt text
                    List<String> originalValues = new ArrayList<String>(values);
                    originalValues.addAll(Arrays.asList(houseNumber,neighborhood,city,zipCode,country));
                    this.goToUpdateContactPage(contactType, contactId, addressId, originalValues);
                });

                // When the delete button is clicked
                delete.setOnAction(delete_event -> {

                    // We define the contact name based on its type
                    String contactName = (contactType.equals("Person")) ? firstAttribute + " " + secondAttribute : firstAttribute;

                    // Show a confirmation dialog
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Contact Deletion");
                    alert.setContentText("The contact " + contactName + " will be deleted, do you confirm this operation ?");
                
                    // Add "Yes" and "No" buttons
                    ButtonType buttonTypeYes = new ButtonType("Yes");
                    ButtonType buttonTypeNo = new ButtonType("No");
                    alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                
                    // Show the dialog and wait for user input
                    ButtonType result = alert.showAndWait().orElse(null);
                    if (result == buttonTypeYes) {
                        // Call the deleteContact method if the user clicked "Yes"
                        ContactCrud.deleteContact(contactType, contactId);
                    }
                });
           
        } 
            
        });

        // We send an http get request to get all the contacts of the given type
        List<List<String>> data = ContactDto.getAllContactsByType(contactType);  

        // We populate the table using those collected contacts
        List<String> columns = FXManager.columns_names_per_contact_type.get(contactType);
        FXManager.populateTableView(contactsTable, columns, data);

        // When we press the search button
        search.setOnAction(event -> {
            
            // We collect the entered name 
            String contactName = txtField.getText();

            if (!contactName.equals("")) {

                // We get the contact using that id
                List<String> info = ContactDto.getContactByName(contactName, contactType);

                if (info != null) {  // if there is a contact with the given name

                    // We extract each attribute's value from the retrieved contact
                    int contactId = Integer.parseInt(info.get(0));
                    String firstAttribute = info.get(1);
                    String secondAttribute = info.get(2);
                    String phoneNumber = info.get(3);
                    String email = info.get(4);
                    int addressId = Integer.parseInt(info.get(5));
                    String houseNumber = info.get(6);
                    String neighborhood = info.get(7);
                    String city = info.get(8);
                    String zipCode = info.get(9);
                    String country = info.get(10);    
                    String address = houseNumber + " " + neighborhood + " " +
                
                    city + " " + zipCode + " " + country; 

                    // We put all the values in one list that we'll use to fill the labels
                    List<String> values = Arrays.asList(firstAttribute,secondAttribute,phoneNumber,email,address);
    
                    // We use the extracted values to fill the labels
                    FXManager.contactLabelsFiller(labels, values, contactType);

                    // We finally show the right pane
                    rightAnchorPane.setVisible(true);

                    // When the update button is clicked
                    update.setOnAction(update_event -> {
                        // We collect ll the original values to be passed as the text fields prompt text
                        List<String> originalValues = new ArrayList<String>(values);
                        originalValues.addAll(Arrays.asList(houseNumber,neighborhood,city,zipCode,country));
                        this.goToUpdateContactPage(contactType, contactId, addressId, originalValues);
                    });

                    // When the delete button is clicked
                    delete.setOnAction(delete_event -> {
                        // We ask the user for the confirmation before the delete :
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText("Contact Deletion");
                        alert.setContentText("The contact " + contactName + " will be deleted, do you confirm this operation ?");
                    
                        // Add "Yes" and "No" buttons
                        ButtonType buttonTypeYes = new ButtonType("Yes");
                        ButtonType buttonTypeNo = new ButtonType("No");
                        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                    
                        // Show the dialog and wait for user input
                        ButtonType result = alert.showAndWait().orElse(null);
                        if (result == buttonTypeYes) {
                            // Call the deleteContact method if the user clicked "Yes"
                            ContactCrud.deleteContact(contactType, contactId);
                        }
                    });
                        
                }
                
                // if no contact corresponds to the provided name we show an error alert
                else FXManager.showAlert(AlertType.ERROR, "ERROR", "Contact Not Found", contactName + " doesn't correspond to any existing contact.");
                 
            }

            // if the text field is empty and the search button is clicked
            else FXManager.showAlert(AlertType.ERROR, "ERROR", "Empty Name Field", "Please provide a contact name.");

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

        loadFXML("/uiass/eia/gisiba/inventory/catalog/catalog_center_pane.fxml", centerAnchorPane);
        loadFXML("/uiass/eia/gisiba/inventory/catalog/catalog_right_pane.fxml", rightAnchorPane);

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
        TableView<List<String>> productsTable = FXManager.getTableView(centerAnchorPane, "productsTableView");

        productsTable.setOnMouseClicked(event -> {
            if (!productsTable.getSelectionModel().isEmpty()) {

                // We get the selected row and extract the values
                List<String> selectedItem = (List<String>) productsTable.getSelectionModel().getSelectedItem();
                String ref = selectedItem.get(0);
                int categoryId = Integer.parseInt(selectedItem.get(1));
                String category = selectedItem.get(2);
                String brand = selectedItem.get(3);
                String model = selectedItem.get(4);
                String description = selectedItem.get(5);
                String unitPrice = selectedItem.get(6);

                // We put all the values in one list that we'll use to fill the labels
                List<String> values = Arrays.asList(ref,category,brand,model,unitPrice,description);

                // We use the extracted values to fill the labels
                FXManager.catalogLabelsFiller(labels, values);

                // We finally show the right pane
                rightAnchorPane.setVisible(true);

                // When the update button is clicked
                update.setOnAction(update_event -> {

                    List<String> originalValues = new ArrayList<String>(values);

                    this.goToUpdateProductPage(ref,categoryId, originalValues);
                });

                // When the delete button is clicked
                delete.setOnAction(delete_event -> ProductCrud.deleteProduct(ref));
           
        } 
            
        });

        // We send an http get request to get all the contacts of the given type
        List<List<String>> data = ProductDto.getAllProducts();  

        // We populate the table using those collected contacts
        List<String> columns = FXManager.catalog_columns;
        FXManager.populateTableView(productsTable, columns, data);

        // When we press the search button
        search.setOnAction(event -> {
            
            // We collect the entered id (we suppose it's a number)
            String ref = txtField.getText();

            if (ref != "") {

                // We get the contact using that id
                List<String> info = ProductDto.getProductByRef(ref);

                if (info != null) {  // if there is a contact with the given id

                    // We extract each attribute's value from the retrieved contact
                    int categoryId = Integer.parseInt(info.get(1));
                    String category = info.get(2);
                    String brand = info.get(3);
                    String model = info.get(4);
                    String description = info.get(5);
                    String unitPrice = info.get(6);

                    // We put all the values in one list that we'll use to fill the labels
                    List<String> values = Arrays.asList(category, brand, model,description, unitPrice);
    
                    // We use the extracted values to fill the labels
                    FXManager.catalogLabelsFiller(labels, values);

                    // We finally show the right pane
                    rightAnchorPane.setVisible(true);

                    // When the update button is clicked
                    update.setOnAction(update_event -> {

                        List<String> originalValues = new ArrayList<String>(values);

                        this.goToUpdateProductPage(ref,categoryId, originalValues);
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
        String type = contactType.toLowerCase();
        loadFXML("/uiass/eia/gisiba/crm/contact/" + type + "/" + "create_" + type + "_pane.fxml", pane);  // here we load the creation page fxml file
        
        // We collect the confirm button from the fxml file
        Button confirm = FXManager.getButton(pane, "confirmBtn");

        // We add the corresponding event listener to the button
        ContactCrud.create_contact(pane, confirm, contactType);
        
        // We add the stage info and show it
        String iconPath = "/uiass/eia/gisiba/imgs/" + (contactType.equals("Person") ? "man" : "office-building") + ".png";
        InputStream inputStream = getClass().getResourceAsStream(iconPath);
        Image icon = new Image(inputStream);

        stage.setScene(scene);
        stage.setTitle(contactType.equals("Person") ? "Create Person" : "Create Enterprise");
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.show();

    }

    // A method that display the contact update pane
    public void goToUpdateContactPage(String contactType, int contactId, int addressId, List<String> originalValues) {

        // We create the stage that will contain the update page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);
        String type = contactType.toLowerCase();
        loadFXML("/uiass/eia/gisiba/crm/contact/" + type + "/" + "update_" + type + "_pane.fxml", pane); // here we load the update page fxml file
        
        // We collect the confirm button from the fxml file
        Button confirm = FXManager.getButton(pane, "confirmBtn");

        // We add the corresponding event listener to the button
        ContactCrud.update_contact(pane, confirm, contactType, contactId, addressId, originalValues);

        // We add the stage info and show it
        String iconPath = "/uiass/eia/gisiba/imgs/" + (contactType.equals("Person") ? "man" : "office-building") + ".png";
        InputStream inputStream = getClass().getResourceAsStream(iconPath);
        Image icon = new Image(inputStream);

        stage.setScene(scene);
        stage.setTitle(contactType.equals("Person") ? "Update Person" : "Update Enterprise");
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.show();

    }

    // A method that display the email sending pane
    public void goToSendEmailPage(String receiverEmail) {

        // We create the stage that will contain the email sending page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);
        loadFXML("/uiass/eia/gisiba/crm/contact/send_email_pane.fxml", pane); // here we load the email sending page fxml file

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

        // here we load the creation page fxml file
        loadFXML("/uiass/eia/gisiba/inventory/catalog/create_update_catalog_pane.fxml", pane); 
        
        // We get the HBoxes that contain the nodes
        HBox hbox1 = FXManager.getHBox(pane, "firstHBox");
        HBox hbox2 = FXManager.getHBox(pane, "secondHBox");
        
        // We collect the confirm button from the fxml file
        Button confirm = FXManager.getButton(pane, "confirmBtn");

        // We add the corresponding event listener to the button
        ProductCrud.create_product(hbox1, hbox2, confirm);;
        
        // We add the stage info and show it
        String iconPath = "/uiass/eia/gisiba/imgs/product.png";
        InputStream inputStream = getClass().getResourceAsStream(iconPath);
        Image icon = new Image(inputStream);

        stage.setScene(scene);
        stage.setTitle("Create Product");
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.show();

    }

    // A method that display the product update pane
    public void goToUpdateProductPage(String ref, int categoryId, List<String> originalValues) {

        // We create the stage that will contain the update page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);

        // here we load the update page fxml file
        loadFXML("/uiass/eia/gisiba/inventory/catalog/create_update_catalog_pane.fxml", pane); 

        // We get the HBoxes that contain the nodes
        HBox hbox1 = FXManager.getHBox(pane, "firstHBox");
        HBox hbox2 = FXManager.getHBox(pane, "secondHBox");
        
        // We collect the confirm button from the fxml file
        Button confirm = FXManager.getButton(pane, "confirmBtn");

        // We add the corresponding event listener to the button
        ProductCrud.update_product(hbox1,hbox2, confirm, ref, categoryId, originalValues);

        // We add the stage info and show it
        String iconPath = "/uiass/eia/gisiba/imgs/product.png";
        InputStream inputStream = getClass().getResourceAsStream(iconPath);
        Image icon = new Image(inputStream);

        stage.setScene(scene);
        stage.setTitle("Update Product");
        stage.setResizable(false);
        stage.getIcons().add(icon);
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
