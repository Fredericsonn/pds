package uiass.eia.gisiba;

import java.io.IOException;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import uiass.eia.gisiba.dto.AddressDto;
import uiass.eia.gisiba.dto.ContactDto;

public class MainController {

    @FXML 
    private AnchorPane leftAnchorPane;

    @FXML
    private AnchorPane centerAnchorPane;

    @FXML
    private AnchorPane rightAnchorPane;

    @FXML
    private MenuItem personsMenuItem;

    @FXML
    private void switchToScene() throws IOException {
        // Call the method to switch to the "Person_Search_Page" scene
        Main.setRoot("Person_Search_Page");
    }
    @SuppressWarnings("unchecked")
    @FXML
    private void loadContactPane(String contactType) {

        loadFXML("contact_center_pane.fxml", centerAnchorPane);
        loadFXML(contactType.toLowerCase() + "_right_pane.fxml", rightAnchorPane);

        rightAnchorPane.setVisible(false);

        List<String> labelIds = FXManager.ids_per_contact_type_map.get(contactType);
        
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


        // List
        ListView list = (ListView) centerAnchorPane.lookup("#contactListView");

        list.setOnMouseClicked(event ->  {
            if (!list.getSelectionModel().isEmpty()) {

                // We get the selected row and extract the values
                List<String> selectedItem = (List<String>) list.getSelectionModel().getSelectedItem();
                int contactId = Integer.parseInt(selectedItem.get(0));
                String firstAttribute = selectedItem.get(1);
                String secondAttribute = selectedItem.get(2);
                String phoneNumber = selectedItem.get(3);
                String email = selectedItem.get(4);
                int addressId = Integer.parseInt(selectedItem.get(5));
                String address = AddressDto.addressFormulater(addressId);

                List<String> values = Arrays.asList(firstAttribute,secondAttribute,phoneNumber,email,address);

                // We use the extracted values to fill the labels
                FXManager.contactLabelsFiller(labels, values, contactType);

                // We show the right pane
                rightAnchorPane.setVisible(true);
                update.setOnAction(update_event -> {
                    this.goToUpdateContactPage(contactType, contactId, addressId);
                });
    
                delete.setOnAction(delete_event -> Crud.deleteContact(contactType, contactId));
           
        } 
            
        });
    
        ObservableList<List<String>> data = FXCollections.observableArrayList(); // We define the observable list that will be used to fill the table rows
        List<List<String>> contacts = ContactDto.getAllContactsByType(contactType);  // We get all the contacts from the database
        contacts.forEach(contact -> data.add(contact)); // add all the contacts to observale 
        
        list.setItems(data);
        
        
        search.setOnAction(event -> {
            

            try {

                int contactId = Integer.parseInt(txtField.getText());
                List<String> info = ContactDto.getContactById(contactId, contactType);

                if (info != null) {

                    String firstAttribute = info.get(1);
                    String secondAttribute = info.get(2);
                    String phoneNumber = info.get(3);
                    String email = info.get(4);
                    int addressId = Integer.parseInt(info.get(5));
                    String address = AddressDto.addressFormulater(addressId);
    
                    List<String> values = Arrays.asList(firstAttribute,secondAttribute,phoneNumber,email,address);
    
                    // We use the extracted values to fill the labels
                    FXManager.contactLabelsFiller(labels, values, contactType);

                    rightAnchorPane.setVisible(true);
        
                    update.setOnAction(update_event -> {
                        this.goToUpdateContactPage(contactType, contactId, addressId);
                    });
        
                    delete.setOnAction(delete_event -> Crud.deleteContact(contactType, contactId));
                }
                
                else FXManager.showAlert(AlertType.ERROR, "Invalid Id", "Contact Not Found", contactId + " doesn't correspond to any existing contact.");
                 
            }

            catch(NumberFormatException e) {
                FXManager.showAlert(AlertType.ERROR, "Invalid Id", "Empty Id Field", "Please provide an Id.");
            }
        });

        createNew.setOnAction(event -> {
            this.goToCreateContactPage(contactType);
            
        });

        notify.setOnAction(event -> {

            String receiverEmail = FXManager.getLabel(rightAnchorPane, "emailLabel").getText();

            this.goToSendEmailPage(receiverEmail);
        });


 
    }

    @FXML
    private void loadPersonPane() {
        loadContactPane("Person");
    }

    @FXML
    private void loadEnterprisePane() {
        loadContactPane("Enterprise");
    }
 
    public void goToCreateContactPage(String contactType) {

        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);
        loadFXML("create_update_person_page.fxml", pane);
        

        Button confirm = FXManager.getButton(pane, "confirmBtn");

        Crud.create_contact(pane, confirm, contactType);
        stage.setScene(scene);
        stage.setTitle(contactType.equals("Person") ? "Create Person" : "Create Enterprise");
        stage.setResizable(false);
        stage.show();

    }

    public void goToUpdateContactPage(String contactType, int contactId, int addressId) {

        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);
        loadFXML("create_update_person_page.fxml", pane);
        

        Button confirm = FXManager.getButton(pane, "confirmBtn");

        Crud.update_contact(pane, confirm, contactType, contactId, addressId);
        stage.setScene(scene);
        stage.setTitle(contactType.equals("Person") ? "Update Person" : "Update Enterprise");
        stage.setResizable(false);
        stage.show();

    }

    public void goToSendEmailPage(String receiverEmail) {
        
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);
        loadFXML("send_email_pane.fxml", pane);

        Button send = FXManager.getButton(pane, "sendEmailBtn");

        Crud.sendEmail(pane, send, receiverEmail);

        stage.setScene(scene);
        stage.setTitle("Email Sending");
        stage.setResizable(false);
        stage.show();
    }



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
