package uiass.eia.gisiba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
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

        // We change the prompt text if the contact is an enterprise : 
        TextField firstTextField= getTextField(pane, "firstTextField");
        if (contactType.equals("Enterprise")) firstTextField.setPromptText("enterprise name");
        TextField secondTextField = getTextField(pane, "secondTextField");
        if (contactType.equals("Enterprise")) secondTextField.setPromptText("type");
        TextField phoneNumberTextField = getTextField(pane, "phoneNumberTextField");
        TextField emailTextField = getTextField(pane, "emailTextField");
        setTextFieldEmailFormatRule(emailTextField);
        TextField houseNumberTextField = getTextField(pane, "houseNumberTextField");
        setTextFieldNumericFormatRule(houseNumberTextField);
        TextField neighborhoodTextField = getTextField(pane, "neighborhoodTextField");
        TextField cityTextField = getTextField(pane, "cityTextField");
        TextField zipCodeTextField = getTextField(pane, "zipCodeTextField");
        setTextFieldNumericFormatRule(zipCodeTextField);
        TextField regionTextField = getTextField(pane, "regionTextField");
        TextField countryTextField = getTextField(pane, "countryTextField");

        // We put all the text fields in a list to check if all the fields got input :
        List<TextField> textFields = Arrays.asList(firstTextField,secondTextField,phoneNumberTextField,emailTextField,

        houseNumberTextField,neighborhoodTextField,cityTextField,zipCodeTextField,regionTextField,countryTextField);

        // We extract the data from the text fields when the button is clicked
        button.setOnAction(event -> {

            if (validCreationChecker(textFields)) {

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
            
            }

            else showAlert(AlertType.ERROR, "Empty Fields", "Missing Data", "Please enter all the required information.");
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

        // We change the prompt text if the contact is an enterprise : 
        TextField firstTextField= getTextField(pane, "firstTextField");
        if (contactType.equals("Enterprise")) firstTextField.setPromptText("enterprise name");
        TextField secondTextField = getTextField(pane, "secondTextField");
        if (contactType.equals("Enterprise")) secondTextField.setPromptText("type");
        TextField phoneNumberTextField = getTextField(pane, "phoneNumberTextField");
        TextField emailTextField = getTextField(pane, "emailTextField");
        setTextFieldEmailFormatRule(emailTextField);
        TextField houseNumberTextField = getTextField(pane, "houseNumberTextField");
        setTextFieldNumericFormatRule(houseNumberTextField);
        TextField neighborhoodTextField = getTextField(pane, "neighborhoodTextField");
        TextField cityTextField = getTextField(pane, "cityTextField");
        TextField zipCodeTextField = getTextField(pane, "zipCodeTextField");
        setTextFieldNumericFormatRule(zipCodeTextField);
        TextField regionTextField = getTextField(pane, "regionTextField");
        TextField countryTextField = getTextField(pane, "countryTextField");

        // We put all the text fields in a list to check if all the fields got input :
        List<TextField> textFields = Arrays.asList(firstTextField,secondTextField,phoneNumberTextField,emailTextField,

        houseNumberTextField,neighborhoodTextField,cityTextField,zipCodeTextField,regionTextField,countryTextField);

        // We extract the data from the text fields when the button is clicked
        button.setOnAction(event -> {

            if (validUpdateChecker(textFields)) {

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
                Map<String,Object> contact_attributes_map = Parser.contactUpdateMapGenerator(contactValues, contactType);
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
            }

            else showAlert(AlertType.ERROR, "Empty Fields", "Missing Data", "At least one value is required for the update"); 
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

    // validate a creation :
    public static boolean validCreationChecker(List<TextField> textFields) {

        for (TextField textField : textFields) {

            if (textField.getText().equals("")) return false;

        }

        return true;
    }

    // validate an update :
    public static boolean validUpdateChecker(List<TextField> textFields) {

        for (TextField textField : textFields) {

            if (!textField.getText().equals("")) return true;

        }

        return false;
    }

    public static void setTextFieldNumericFormatRule(TextField numericTextField) {

        numericTextField.setTextFormatter(new TextFormatter<>(change -> {

        String newText = change.getControlNewText();

        if (newText.matches("\\d*")) { // Allow only digits

            return change;

        } else return null; // Reject the change
            
        }));
    }

    public static void setTextFieldEmailFormatRule(TextField emailTextField) {
        emailTextField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("[a-zA-Z0-9_+&*-]*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*")) {
                // Allow the change
                return change;
            } else {
                return change;
            }
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

    public static ListView getListView(Parent pane, String id) {
        return (ListView) pane.lookup("#" + id);
    }

    public static RadioButton getRadioButton(Parent pane, String id) {
        return (RadioButton) pane.lookup("#" + id);
    } 
}
