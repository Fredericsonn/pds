package uiass.eia.gisiba;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import uiass.eia.gisiba.dto.AddressDto;
import uiass.eia.gisiba.dto.ContactDto;
import uiass.eia.gisiba.dto.Parser;

public class Crud {

    @SuppressWarnings("unchecked")

    // A method that extracts the data entered by the user and sends a post http request to the server :
    public static void create_contact(Parent pane, Button button, String contactType) {

        // The list of values we'll be using to generate the coluns - values map
        List values = new ArrayList<>();

        // The list of attributes that reference the text fields' ids :
        List<String> attributes = Parser.textFieldsReferences;

        // We extract the data from the text fields when the button is clicked
        button.setOnAction(event -> {

            attributes.forEach(attribute -> {

                String value = getTextField(pane, attribute + "TextField").getText(); // We collect the value of the text field entered by the user

                if (attribute.equals("houseNumber")) values.add(value != "" ? Integer.parseInt(value) : 0); // We must save the house number as an integer.
                
                else values.add(value); // for every other attribute we just save the value as string
            });

            // We generate the columns - values map using the values list :
            Map<String,Object> map = Parser.contactCreationMapGenerator(values, contactType);

            // We dynamically generate the corresponding json :
            String json = Parser.jsonGenerator(map);

            // We use the json to send an http post request to the server to create the new contact with the entered values :
            String contactCreationResult = ContactDto.postContact(json, contactType);

            // We display the creation result :
            if (contactCreationResult.equals("Contact created successfully."))
            showAlert(AlertType.CONFIRMATION, "Creation Status", "Result :", contactCreationResult);

            else showAlert(AlertType.ERROR, "Creation Status", "Result :", contactCreationResult);

            ((Stage) button.getScene().getWindow()).close(); // We close the create page after confirming the creation
        });
    }

        
    @SuppressWarnings("unchecked")

    // A method that extracts the data entered by the user and sends a put http request to the server :
    public static void update_contact(Parent pane, Button button, String contactType, int contactId, int addressId) {

        // lists containing each object's attributes (excluding ids) :
        List<String> contactAttributes = Parser.textFieldsReferences;

        List<String> addressAttributes = Parser.update_address_attributes;

        // lists to store each attribute's entered value :
        List contactValues = new ArrayList<>();

        List addressValues = new ArrayList<>();

        // We set an event listener to start collecting data when the button is clicked :
        button.setOnAction(event -> {

            contactAttributes.forEach(attribute -> {

                String value = getTextField(pane, attribute + "TextField").getText(); // We collect the value of the text field entered by the user

                contactValues.add(value); // for every other attribute we just save the value as string
            });

            addressAttributes.forEach(attribute -> {

                String value = getTextField(pane, attribute + "TextField").getText(); // We collect the value of the text field entered by the user

                // We must save the house number as an integer :
                if (attribute.equals("houseNumber")) addressValues.add(!value.equals("") ? Integer.parseInt(value) : 0); 

                else addressValues.add(value); // for every other attribute we just save the value as string
                

            });

            // We create the columns - values to update maps :
            Map<String,Object> contact_attributes_map = Parser.contactUpdateMapGenerator(contactValues, "Person");
            Map<String,Object> address_attributes_map = Parser.addressMapGenerator(addressValues);

            // We create their corresponding jsons :
            String contactJson = Parser.jsonGenerator(contact_attributes_map);
            String addressJson = Parser.jsonGenerator(address_attributes_map);

            // We send http put requests to update the contact or the address :
            String contactUpdateResult = ContactDto.updateContact(contactId, contactType, contactJson);
            AddressDto.updateAddress(addressId, addressJson);

            // We display the update result :
            if (contactUpdateResult.equals("Contact updated successfully."))
            showAlert(AlertType.CONFIRMATION, "Update Status", "Result :", contactUpdateResult);

            else showAlert(AlertType.ERROR, "Update Status", "Result :", contactUpdateResult);
            ((Stage) button.getScene().getWindow()).close(); // We close the update page after confirming the update
        });
    }

    // deleting a contact :
    public static void deleteContact(String contactType, int id) {

        String contactDeletionResult = ContactDto.deleteContact(id, contactType);

        // We display the deletion result :
        if (contactDeletionResult.equals("Contact deleted successfully."))
        showAlert(AlertType.CONFIRMATION, "Deletion Status", "Result :", contactDeletionResult);

        else showAlert(AlertType.ERROR, "Deletion Status", "Result :", contactDeletionResult);

    }

    public static void setTextFieldNumericFormatRule(TextField numericTextField) {

        numericTextField.setTextFormatter(new TextFormatter<>(change -> {

        String newText = change.getControlNewText();

        if (newText.matches("\\d*")) { // Allow only digits

            return change;

        } else return null; // Reject the change
            
        }));
    }

    public static void setTextFieldEmailFormatRule(TextField numericTextField) {

        numericTextField.setTextFormatter(new TextFormatter<>(change -> {

        String newText = change.getControlNewText();

        if (newText.matches("^[a-zA-Z0-9_+&*-]+(?:\\\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,7}$")) { // Email Format

            return change;

        } else return null; // Reject the change
            
        }));
    }
    // A method that generates alerts : 
    public static void showAlert(AlertType type, String title, String header, String message) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header); 
        alert.setContentText(message);
        alert.showAndWait();
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
}
