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
        Button search = getButton(centerAnchorPane, "searchBtn");
        Button createNew = getButton(centerAnchorPane, "createNewPersonBtn");
        Button update = getButton(rightAnchorPane, "updateBtn");
        TextField txtField = getTextField(centerAnchorPane, "enterIdTextField");
        Label fullNameLabel =getLabel(rightAnchorPane, "fullNameLabel");
        Label phoneNumberLabel = getLabel(rightAnchorPane, "phoneNumberLabel");
        Label emailLabel = getLabel(rightAnchorPane, "emailLabel");

        TableView table = (TableView) centerAnchorPane.lookup("#personTable");
        ObservableList<List<String>> data = FXCollections.observableArrayList();
        List<List<String>> contacts = ContactDto.getAllContactsByType("Person");
        contacts.forEach(contact -> data.add(contact));
        table.setItems(data);

        search.setOnAction(event -> {
            int id = Integer.parseInt(txtField.getText());
            List<String> info = ContactDto.getContactById(id, "Person");

            String firstName = String.valueOf(info.get(1));
            String lastName = String.valueOf(info.get(2));
            String phoneNumber = String.valueOf(info.get(3));
            String email = String.valueOf(info.get(4));

            fullNameLabel.setText(firstName + " " + lastName);
            phoneNumberLabel.setText(phoneNumber);
            emailLabel.setText(email);
            rightAnchorPane.setVisible(true);
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
        Button search = (Button) centerAnchorPane.lookup("#searchBtn");
        TextField txtField = (TextField) centerAnchorPane.lookup("#enterIdTextLabel");
        Label enterpriseNameLabel = (Label) rightAnchorPane.lookup("#enterpriseNameLabel");
        Label typeLabel = (Label) rightAnchorPane.lookup("#enterpriseTypeLabel");
        Label emailLabel = (Label) rightAnchorPane.lookup("#emailLabel");
        Label phoneNumberLabel = (Label) rightAnchorPane.lookup("#phoneNumberLabel");

        TableView table = (TableView) centerAnchorPane.lookup("#enterpriseTable");
        ObservableList<List<String>> data = FXCollections.observableArrayList();
        List<List<String>> addresses = ContactDto.getAllContactsByType("Enterprise");
        addresses.forEach(address -> data.add(address));
        table.setItems(data);

        search.setOnAction(event -> {
            int id = Integer.parseInt(txtField.getText());
            List<String> info = ContactDto.getContactById(id, "Person");

            String firstName = String.valueOf(info.get(1));
            String type = String.valueOf(info.get(2));
            String phoneNumber = String.valueOf(info.get(3));
            String email = String.valueOf(info.get(4));

            enterpriseNameLabel.setText(firstName);
            typeLabel.setText(type);
            phoneNumberLabel.setText(phoneNumber);
            emailLabel.setText(email);
            rightAnchorPane.setVisible(true);
        });
    }

    @FXML
    private void loadAddressPane() {
        loadFXML("address_center_pane.fxml", centerAnchorPane);
        loadFXML("address_right_pane.fxml", rightAnchorPane);
        rightAnchorPane.setVisible(false);
        Button search = (Button) centerAnchorPane.lookup("#searchBtn");
        TextField txtField = (TextField) centerAnchorPane.lookup("#enterIdTextLabel");
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

    @SuppressWarnings("unchecked")

    // A method that extracts the data entered by the user and sends a post http request to the server :
    public static void create_contact(Parent pane, Button button, String contactType) {

        List values = new ArrayList<>();

        List<String> attributes = Parser.contact_creation_columns_by_type_map.get(contactType);

        button.setOnAction(event -> {

            attributes.forEach(attribute -> {

                String value = getTextField(pane, attribute + "TextField").getText();

                if (attribute == "houseNumber") values.add(Integer.parseInt(value));
                
                else values.add(value);
            });
        
            Map<String,Object> map = Parser.createContactMapGenerator(values, "Person");
    
            String json = Parser.jsonGenerator(map);
            ContactDto.postContact(json, "Person");
        });
    }

        
    @SuppressWarnings("unchecked")

    // A method that extracts the data entered by the user and sends a put http request to the server :
    public static void update_contact(Parent pane, Button button, String contactType) {

        // lists containing each object's attributes (excluding ids) :
        List<String> contactAttributes = Parser.contact_update_columns_by_type_map.get(contactType);

        List<String> addressAttributes = Parser.update_address_attributes;

        // lists to store each attribute's entered value :
        List contactValues = new ArrayList<>();

        List addressValues = new ArrayList<>();

        button.setOnAction(event -> {

            contactAttributes.forEach(attribute -> {

                String value = getTextField(pane, attribute + "TextField").getText();

                if (value != "") contactValues.add(value);
            });

            addressAttributes.forEach(attribute -> {

                String value = getTextField(pane, attribute + "TextField").getText();

                if (value != "") {
                    
                    if (attribute == "houseNumber") addressValues.add(Integer.parseInt(value));

                    else addressValues.add(value);
                }


            });

            // We create the columns - values to update maps :
            Map<String,Object> contact_attributes_map = Parser.createContactMapGenerator(contactValues, "Person");
            Map<String,Object> address_attributes_map = Parser.createContactMapGenerator(addressValues, "Person");

            // We create their corresponding jsons :
            String contactJson = Parser.jsonGenerator(contact_attributes_map);
            String addressJson = Parser.jsonGenerator(address_attributes_map);

            // We send http put requests to update the contact or the address :
            ContactDto.updateContact(0, contactType, contactJson);
            AddressDto.updateAddress(0, addressJson);
        });
    }


    public void goToCreateContactPage(String contactType) {

        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);
        loadFXML("create_update_person_page.fxml", pane);
        

        Button confirm = getButton(pane, "confirmBtn");

        create_contact(pane, confirm, contactType);
        stage.setScene(scene);
        stage.setTitle("Create Person");
        stage.setResizable(false);
        stage.show();

    }

    public static TextField getTextField(Parent pane, String id) {
        return (TextField) pane.lookup("#"+id);
    }

    public static Button getButton(Parent pane, String id) {
        return (Button) pane.lookup("#"+id);
    }

    public static Label getLabel(Parent pane, String id) {
        return (Label) pane.lookup("#"+id);
    }

    public static TableView getTableView(Parent pane, String id) {
        return (TableView) pane.lookup("#"+id);
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
