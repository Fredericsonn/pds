package uiass.eia.gisiba.controller;


import java.io.InputStream;
import java.util.*;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

        // When we press the search button
        search.setOnAction(event -> {
            
            // We collect the entered name 
            String contactName = txtField.getText();

            if (!contactName.equals("")) {

                // We get the contact using that id
                List<String> info = ContactDto.getContactByName(contactName, contactType);
                System.out.println(info);

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
                        ContactCrud.goToUpdateContactPage(contactType, contactId, addressId, originalValues);
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
        List<TextField> textFields = ProductCrud.productSearchTextFieldsHandler(centerAnchorPane);

        TextField categoryTextField = textFields.get(0);     // We get
        TextField brandTextField = textFields.get(1);        // the text fields
        TextField modelTextField = textFields.get(2);        // from the list

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

        // When we press the search button
        search.setOnAction(event -> {
            
            // We collect the entered id (we suppose it's a number)
            String categroyInput = categoryTextField.getText();
            String brandInput = brandTextField.getText();
            String modelInput = modelTextField.getText();

            List<String> values = Arrays.asList(categroyInput,brandInput,modelInput);

            String json = ProductCrud.filteredProductSearchJsonGenerator(values);
            

            if (ProductCrud.productSearchValidator(textFields)) {

                // We get the products that match the filter criteria
                List<List<String>> data = ProductDto.getFilteredProducts(json);

                if (!data.isEmpty()) {  // if there are matching products 

                    // We fill the products table with the matching products
                    ProductCrud.fillWithFilteredProducts(productsTable, data);

                }
                
                // if no product corresponds to the provided ref we show an error alert
                else FXManager.showAlert(AlertType.ERROR, "ERROR", "Products Not Found"," No existing products match the given criteria. Please check for spelling erros.");
            }

            // if the text field is empty and the search button is clicked
            else FXManager.showAlert(AlertType.ERROR, "ERROR", "Empty Search Fields", "Please provide some parameters for the search.");
        });

        // When the create new button is clicked
        createNew.setOnAction(event -> {
            ProductCrud.whatToCreate();
            
        });
 
    }


}
