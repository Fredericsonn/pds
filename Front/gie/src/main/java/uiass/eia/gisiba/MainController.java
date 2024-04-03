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
    
    }

    @FXML
    private void loadAddressPane() {

        loadFXML("address_center_pane.fxml", centerAnchorPane);
        loadFXML("address_right_pane.fxml", rightAnchorPane);

        // We hide the right anchor pane until we select or search for an address :
        rightAnchorPane.setVisible(false);

        // Buttons
        Button search = (Button) centerAnchorPane.lookup("#searchBtn");

        // TextFields
        TextField txtField = (TextField) centerAnchorPane.lookup("#enterIdTextField");
        Crud.setTextFieldNumericFormatRule(txtField);

        // Labels
        Label houseNumberLabel = Crud.getLabel(rightAnchorPane, "houseNumberLabel");
        Label neighborhoodLabel = Crud.getLabel(rightAnchorPane, "neighborhoodLabel");
        Label cityZipCodeLabel = Crud.getLabel(rightAnchorPane, "city_zipCodeLabel");  
        Label regionLabel = Crud.getLabel(rightAnchorPane, "regionLabel"); 
        Label countryLabel = Crud.getLabel(rightAnchorPane, "countryLabel"); 

        // Radio Buttons 
        RadioButton personRadioButton = Crud.getRadioButton(centerAnchorPane, "personRdBtn");
        RadioButton enterpriseRadioButton = Crud.getRadioButton(centerAnchorPane, "enterpriseRdBtn");

        // List
        ListView list = (ListView) centerAnchorPane.lookup("#listView");

        // We set an event listener for when we select an item :
        list.setOnMouseClicked(event ->  {

            if (!list.getSelectionModel().isEmpty()) {
               
                // We get the selected row and extract the values
                List<String> selectedItem = (List<String>) list.getSelectionModel().getSelectedItem();
                
                String houseNumber = selectedItem.get(1);
                String neighborhood = selectedItem.get(2);
                String city = selectedItem.get(3);
                String zipCode = selectedItem.get(4);
                String region = selectedItem.get(5);
                String country = selectedItem.get(6);
                
 
                // We use the extracted values to fill the labels
                houseNumberLabel.setText(houseNumber);
                neighborhoodLabel.setText(neighborhood);
                cityZipCodeLabel.setText(city + ", " + zipCode);
                regionLabel.setText(region);
                countryLabel.setText(country);
 
                // We show the right anchor pane 
                rightAnchorPane.setVisible(true);
                          
            } 
                           
        });   
        ObservableList<List<String>> data = FXCollections.observableArrayList(); // a container to display data within a table
        List<List<String>> contacts = AddressDto.getAllAddresses(); // We send a request to collect all the addresses
        contacts.forEach(contact -> data.add(contact)); // We add every address separately to the observable list
        list.setItems(data); // We add the observable list to the table

        // When we click on search :
        search.setOnAction(event -> {

            try {
                int id = Integer.parseInt(txtField.getText()); // We get the value entered by the user
            
                if (personRadioButton.isSelected() || enterpriseRadioButton.isSelected()) {
                    
                    
    
                    // We collect the contact type from the radio buttons :
                    String contactType = personRadioButton.isSelected() ? "Person" : "Enterprise"; 
        
                    List<String> info = AddressDto.getAddressLongWay(id, contactType); // We search for the address using the id

                    if (info != null) {

                        // We extract all the values from the list (Address object received from the server)
                        String houseNumber = String.valueOf(info.get(1));
                        String neighborhood = String.valueOf(info.get(2));
                        String city = String.valueOf(info.get(3));
                        String zipCode = String.valueOf(info.get(4));
                        String region = String.valueOf(info.get(5));
                        String country = String.valueOf(info.get(6));
        
                        // We use the extracted values to fill the labels
                        houseNumberLabel.setText(houseNumber);
                        neighborhoodLabel.setText(neighborhood);
                        cityZipCodeLabel.setText(city + ", " + zipCode);
                        regionLabel.setText(region);
                        countryLabel.setText(country);
        
                        // We show the right anchor pane 
                        rightAnchorPane.setVisible(true);
                    }

                    else Crud.showAlert(AlertType.ERROR, "Invalid Id", "Address Not Found", id + " doesn't correspond to any existing address.");
                }
                else  Crud.showAlert(AlertType.ERROR, "Type Error", "No type was selected.", "Please Select a type.");
                    

            } catch(NumberFormatException e) {
                Crud.showAlert(AlertType.ERROR, "Empty Id Field", "No Id was provided.", "Please provide an Id.");
            }
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
