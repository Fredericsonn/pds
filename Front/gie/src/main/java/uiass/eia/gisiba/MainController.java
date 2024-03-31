package uiass.eia.gisiba;

import java.io.IOException;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private Button searchButton;

    @FXML
    private void switchToScene() throws IOException {
        // Call the method to switch to the "Person_Search_Page" scene
        App.setRoot("Person_Search_Page");
    }

    @FXML
    private void loadPersonPane() {

        loadFXML("person_center_pane.fxml", centerAnchorPane);
        loadFXML("person_right_pane.fxml", rightAnchorPane);
        rightAnchorPane.setVisible(false);

        // Buttons
        Button search = Crud.getButton(centerAnchorPane, "searchBtn");
        Button createNew = Crud.getButton(centerAnchorPane, "createNewPersonBtn");
        Button update = Crud.getButton(rightAnchorPane, "updateBtn");
        Button delete = Crud.getButton(rightAnchorPane, "deleteBtn");

        // Search text field
        TextField txtField = Crud.getTextField(centerAnchorPane, "enterIdTextField");

        // Labels
        Label fullNameLabel = Crud.getLabel(rightAnchorPane, "fullNameLabel");
        Label phoneNumberLabel = Crud.getLabel(rightAnchorPane, "phoneNumberLabel");
        Label emailLabel = Crud.getLabel(rightAnchorPane, "emailLabel");
        Label addressLabel = Crud.getLabel(rightAnchorPane, "addressLabel");

        // Table
        TableView table = (TableView) centerAnchorPane.lookup("#personTable");
        ObservableList<List<String>> data = FXCollections.observableArrayList(); // We define the observable list that will be used to fill the table rows
        List<List<String>> contacts = ContactDto.getAllContactsByType("Person");  // We get all the contacts from the database
        contacts.forEach(contact -> data.add(contact)); // add all the contacts to observale list
        table.setItems(data); // fill the table with the contacts via the observable list

        search.setOnAction(event -> {
            int contactId = Integer.parseInt(txtField.getText());
            List<String> info = ContactDto.getContactById(contactId, "Person");

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
        });

        createNew.setOnAction(event -> {
            this.goToCreateContactPage("Person");
            
        });
 
    }

    @FXML
    private void loadEnterprisePane() {

        loadFXML("enterprise_center_pane.fxml", centerAnchorPane);
        loadFXML("enterprise_right_pane.fxml", rightAnchorPane);
        rightAnchorPane.setVisible(false);

        // Buttons
        Button search = Crud.getButton(centerAnchorPane, "searchBtn");
        Button createNew = Crud.getButton(centerAnchorPane, "createNewEnterpriseBtn");
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

        // Table
        TableView table = (TableView) centerAnchorPane.lookup("#enterpriseTable");
        ObservableList<List<String>> data = FXCollections.observableArrayList(); // We define the observable list that will be used to fill the table rows
        List<List<String>> contacts = ContactDto.getAllContactsByType("Enterprise");  // We get all the contacts from the database
        contacts.forEach(contact -> data.add(contact)); // We add all the contacts to observale list
        table.setItems(data); // We fill the table with the contacts via the observable list

        search.setOnAction(event -> {

            int contactId = Integer.parseInt(txtField.getText());
            List<String> info = ContactDto.getContactById(contactId, "Enterprise");

            String enterpriseName = String.valueOf(info.get(1));
            String enterpriseType = String.valueOf(info.get(2));
            String phoneNumber = String.valueOf(info.get(3));
            String email = String.valueOf(info.get(4));
            int addressId = Integer.parseInt(info.get(5));

            //String address = AddressDto.addressFormulater(addressId);

            enterpriseTypeLabel.setText(enterpriseType);
            enterpriseNameLabel.setText(enterpriseName);
            phoneNumberLabel.setText(phoneNumber);
            emailLabel.setText(email);
            addressLabel.setText(String.valueOf(addressId));
            rightAnchorPane.setVisible(true);

            update.setOnAction(update_event -> {
                this.goToUpdateContactPage("Enterprise", contactId, addressId);
            });

            delete.setOnAction(delete_event -> Crud.deleteContact("Enterprise", contactId));
        });

        createNew.setOnAction(event -> {
            this.goToCreateContactPage("Enterprise");
            
        });
    
    }

    @FXML
    private void loadAddressPane() {
        loadFXML("address_center_pane.fxml", centerAnchorPane);
        loadFXML("address_right_pane.fxml", rightAnchorPane);
        rightAnchorPane.setVisible(false);
        Button search = (Button) centerAnchorPane.lookup("#searchBtn");
        TextField txtField = (TextField) centerAnchorPane.lookup("#enterIdTextField");
        Label houseNumberLabel = (Label) rightAnchorPane.lookup("#houseNumberLabel");
        Label neighborhoodLabel = (Label) rightAnchorPane.lookup("#neighborhoodLabel");
        Label cityZipCodeLabel = (Label) rightAnchorPane.lookup("#city_zipCodeLabel");
        Label regionLabel = (Label) rightAnchorPane.lookup("#regionLabel");
        Label countryLabel = (Label) rightAnchorPane.lookup("#countryLabel");

        TableView table = (TableView) centerAnchorPane.lookup("#addressTable");
        ObservableList<List<String>> data = FXCollections.observableArrayList();
        List<List<String>> contacts = AddressDto.getAllAddresses();
        contacts.forEach(contact -> data.add(contact));
        table.setItems(data);

        search.setOnAction(event -> {
            int id = Integer.parseInt(txtField.getText());
            List<String> info = AddressDto.getAddressById(id);

            String houseNumber = String.valueOf(info.get(1));
            String neighborhood = String.valueOf(info.get(2));
            String city = String.valueOf(info.get(3));
            String zipCode = String.valueOf(info.get(4));
            String region = String.valueOf(info.get(5));
            String country = String.valueOf(info.get(6));

            houseNumberLabel.setText(houseNumber);
            neighborhoodLabel.setText(neighborhood);
            cityZipCodeLabel.setText(city + ", " + zipCode);
            regionLabel.setText(region);
            countryLabel.setText(country);
            rightAnchorPane.setVisible(true);
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
        stage.setTitle("Update Person");
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
