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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import uiass.eia.gisiba.dto.AddressDto;
import uiass.eia.gisiba.dto.ContactDto;
import uiass.eia.gisiba.dto.Parser;

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
        App.setRoot("Person_Search_Page");
    }
    
    @SuppressWarnings("unchecked")
    @FXML
    private void loadPersonPane() {

        loadFXML("contact_center_pane.fxml", centerAnchorPane);
        loadFXML("person_right_pane.fxml", rightAnchorPane);
        rightAnchorPane.setVisible(false);
        
        // Buttons
        Button search = Crud.getButton(centerAnchorPane, "searchBtn");
        Button createNew = Crud.getButton(centerAnchorPane, "createNewContactBtn");
        Button update = Crud.getButton(rightAnchorPane, "updateBtn");
        Button delete = Crud.getButton(rightAnchorPane, "deleteBtn");
        Button notify = Crud.getButton(rightAnchorPane, "notifyBtn");

        // Search text field
        TextField txtField = Crud.getTextField(centerAnchorPane, "enterIdTextField");
        Crud.setTextFieldNumericFormatRule(txtField);  // a rule that denies any non numeric input

        // Labels
        Label fullNameLabel = Crud.getLabel(rightAnchorPane, "fullNameLabel");
        Label phoneNumberLabel = Crud.getLabel(rightAnchorPane, "phoneNumberLabel");
        Label emailLabel = Crud.getLabel(rightAnchorPane, "emailLabel");
        Label addressLabel = Crud.getLabel(rightAnchorPane, "addressLabel");
        addressLabel.setWrapText(true);

        // List
        ListView list = (ListView) centerAnchorPane.lookup("#contactListView");

        list.setOnMouseClicked(event ->  {
            if (!list.getSelectionModel().isEmpty()) {

                // We get the selected row and extract the values
                List<String> selectedItem = (List<String>) list.getSelectionModel().getSelectedItem();
                int contactId = Integer.parseInt(selectedItem.get(0));
                String fullName = selectedItem.get(1) + " " + selectedItem.get(2);
                String phoneNumber = selectedItem.get(3);
                String email = selectedItem.get(4);
                int addressId = Integer.parseInt(selectedItem.get(5));
                String address = AddressDto.addressFormulater(addressId);

                // We use the extracted values to fill the labels
                fullNameLabel.setText(fullName);
                phoneNumberLabel.setText(phoneNumber);
                emailLabel.setText(email);
                addressLabel.setText(String.valueOf(address));

                // We show the right pane
                rightAnchorPane.setVisible(true);
                update.setOnAction(update_event -> {
                    this.goToUpdateContactPage("Person", contactId, addressId);
                });
    
                delete.setOnAction(delete_event -> Crud.deleteContact("Person", contactId));
           
        } 
            
        });
    
        ObservableList<List<String>> data = FXCollections.observableArrayList(); // We define the observable list that will be used to fill the table rows
        List<List<String>> contacts = ContactDto.getAllContactsByType("Person");  // We get all the contacts from the database
        contacts.forEach(contact -> data.add(contact)); // add all the contacts to observale 
        
        list.setItems(data);
        
        
        search.setOnAction(event -> {
            

            try {

                int contactId = Integer.parseInt(txtField.getText());
                List<String> info = ContactDto.getContactById(contactId, "Person");

                if (info != null) {

                    String firstName = String.valueOf(info.get(1));
                    String lastName = String.valueOf(info.get(2));
                    String phoneNumber = String.valueOf(info.get(3));
                    String email = String.valueOf(info.get(4));
                    int addressId = Integer.parseInt(info.get(5));
        
                    //String address = AddressDto.addressFormulater(addressId);
        
                    fullNameLabel.setText(firstName + " " + lastName);
                    phoneNumberLabel.setText(phoneNumber);
                    emailLabel.setText(email);
                    addressLabel.setText(String.valueOf(addressId));
                    rightAnchorPane.setVisible(true);
        
                    update.setOnAction(update_event -> {
                        this.goToUpdateContactPage("Person", contactId, addressId);
                    });
        
                    delete.setOnAction(delete_event -> Crud.deleteContact("Person", contactId));
                }
                
                else Crud.showAlert(AlertType.ERROR, "Invalid Id", "Contact Not Found", contactId + " doesn't correspond to any existing contact.");
                 
            }

            catch(NumberFormatException e) {
                Crud.showAlert(AlertType.ERROR, "Invalid Id", "Empty Id Field", "Please provide an Id.");
            }
        });

        createNew.setOnAction(event -> {
            this.goToCreateContactPage("Person");
            
        });

        notify.setOnAction(event -> {

            String receiverEmail = Crud.getLabel(rightAnchorPane, "emailLabel").getText();

            this.goToSendEmailPage(receiverEmail);
        });


 
    }

    @FXML
    private void loadEnterprisePane() {

        loadFXML("contact_center_pane.fxml", centerAnchorPane);
        loadFXML("enterprise_right_pane.fxml", rightAnchorPane);
        rightAnchorPane.setVisible(false);

        // Buttons
        Button search = Crud.getButton(centerAnchorPane, "searchBtn");
        Button createNew = Crud.getButton(centerAnchorPane, "createNewContactBtn");
        Button update = Crud.getButton(rightAnchorPane, "updateBtn");
        Button delete = Crud.getButton(rightAnchorPane, "deleteBtn");
        Button notify = Crud.getButton(rightAnchorPane, "notifyBtn");

        // Search text field
        TextField txtField = Crud.getTextField(centerAnchorPane, "enterIdTextField");

        // Labels
        Label enterpriseTypeLabel = Crud.getLabel(rightAnchorPane, "enterpriseTypeLabel");
        Label enterpriseNameLabel = Crud.getLabel(rightAnchorPane, "enterpriseNameLabel");
        Label phoneNumberLabel = Crud.getLabel(rightAnchorPane, "phoneNumberLabel");
        Label emailLabel = Crud.getLabel(rightAnchorPane, "emailLabel");
        Label addressLabel = Crud.getLabel(rightAnchorPane, "addressLabel");
        addressLabel.setWrapText(true);

        // List
        ListView list = (ListView) centerAnchorPane.lookup("#contactListView");

        list.setOnMouseClicked(event ->  {

            if (!list.getSelectionModel().isEmpty()) {
       
                // We get the selected row and extract the values
                List<String> selectedItem = (List<String>) list.getSelectionModel().getSelectedItem();
                int contactId = Integer.parseInt(selectedItem.get(0));
                String enterpriseName = selectedItem.get(1);
                String enterpriseType = selectedItem.get(2);
                String phoneNumber = selectedItem.get(3);
                String email = selectedItem.get(4);
                int addressId = Integer.parseInt(selectedItem.get(5));

                // We get the full address using the address id :
                String address = AddressDto.addressFormulater(addressId);

                // We use the extracted values to fill the labels
                enterpriseNameLabel.setText(enterpriseName);
                enterpriseTypeLabel.setText(enterpriseType);
                phoneNumberLabel.setText(phoneNumber);
                emailLabel.setText(email);
                addressLabel.setText(String.valueOf(address));

                // We show the right pane
                rightAnchorPane.setVisible(true);
                update.setOnAction(update_event -> {
                    this.goToUpdateContactPage("Enterprise", contactId, addressId);
                });
    
                delete.setOnAction(delete_event -> Crud.deleteContact("Enterprise", contactId));
                  
               } 
                   
        });
            
        ObservableList<List<String>> data = FXCollections.observableArrayList(); // We define the observable list that will be used to fill the table rows
        List<List<String>> contacts = ContactDto.getAllContactsByType("Enterprise");  // We get all the contacts from the database
        contacts.forEach(contact -> data.add(contact)); // We add all the contacts to observale list
        list.setItems(data); // We fill the table with the contacts via the observable list

        search.setOnAction(event -> {

            try {
                int contactId = Integer.parseInt(txtField.getText());
                List<String> info = ContactDto.getContactById(contactId, "Enterprise");

                if (info != null) {

                    String enterpriseName = info.get(1);
                    String enterpriseType = info.get(2);
                    String phoneNumber = info.get(3);
                    String email = info.get(4);
                    int addressId = Integer.parseInt(info.get(5));
    
                    // We get the full address using the address id :
                    String address = AddressDto.addressFormulater(addressId);
    
                    // We use the extracted values to fill the labels
                    enterpriseNameLabel.setText(enterpriseName);
                    enterpriseTypeLabel.setText(enterpriseType);
                    phoneNumberLabel.setText(phoneNumber);
                    emailLabel.setText(email);
                    addressLabel.setText(String.valueOf(address));
                    rightAnchorPane.setVisible(true);
        
                    update.setOnAction(update_event -> {
                        this.goToUpdateContactPage("Enterprise", contactId, addressId);
                    });
        
                    delete.setOnAction(delete_event -> Crud.deleteContact("Enterprise", contactId));
                }

                else Crud.showAlert(AlertType.ERROR, "Invalid Id", "Contact Not Found", contactId + " doesn't correspond to any existing contact.");
            }

            catch(NumberFormatException e) {
                Crud.showAlert(AlertType.ERROR, "Empty Id Field", "No Id was provided.", "Please provide an Id.");
            }
        });

        createNew.setOnAction(event -> {
            this.goToCreateContactPage("Enterprise");
            
        });

        notify.setOnAction(event -> {

            String receiverEmail = Crud.getLabel(rightAnchorPane, "emailLabel").getText();
            
            this.goToSendEmailPage(receiverEmail);
        });
    
    }

    public void goToCreateContactPage(String contactType) {

        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);
        loadFXML("create_update_person_page.fxml", pane);
        

        Button confirm = Crud.getButton(pane, "confirmBtn");

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
        

        Button confirm = Crud.getButton(pane, "confirmBtn");

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

        Button send = Crud.getButton(pane, "sendEmailBtn");

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
