package uiass.eia.gisiba;


import java.util.*;

import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
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
        TextField firstTextField= FXManager.getTextField(pane, "firstTextField");
        if (contactType.equals("Enterprise")) firstTextField.setPromptText("enterprise name");
        TextField secondTextField = FXManager.getTextField(pane, "secondTextField");
        if (contactType.equals("Enterprise")) secondTextField.setPromptText("type");
        TextField phoneNumberTextField = FXManager.getTextField(pane, "phoneNumberTextField");
        TextField emailTextField = FXManager.getTextField(pane, "emailTextField");
        FXManager.setTextFieldEmailFormatRule(emailTextField);
        TextField houseNumberTextField = FXManager.getTextField(pane, "houseNumberTextField");
        FXManager.setTextFieldNumericFormatRule(houseNumberTextField);
        TextField neighborhoodTextField = FXManager.getTextField(pane, "neighborhoodTextField");
        TextField cityTextField = FXManager.getTextField(pane, "cityTextField");
        TextField zipCodeTextField = FXManager.getTextField(pane, "zipCodeTextField");
        FXManager.setTextFieldNumericFormatRule(zipCodeTextField);
        TextField regionTextField = FXManager.getTextField(pane, "regionTextField");
        TextField countryTextField = FXManager.getTextField(pane, "countryTextField");

        // We put all the text fields in a list to check if all the fields got input :
        List<TextField> textFields = Arrays.asList(firstTextField,secondTextField,phoneNumberTextField,emailTextField,

        houseNumberTextField,neighborhoodTextField,cityTextField,zipCodeTextField,regionTextField,countryTextField);

        // We extract the data from the text fields when the button is clicked
        button.setOnAction(event -> {

            if (validCreationChecker(textFields)) {

                attributes.forEach(attribute -> {

                    String value = FXManager.getTextField(pane, attribute + "TextField").getText(); // We collect the value of the text field entered by the user
    
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
                FXManager.showAlert(AlertType.CONFIRMATION, "Creation Status", "Result :", contactCreationResult);
    
                else FXManager.showAlert(AlertType.ERROR, "Creation Status", "Result :", contactCreationResult);
    
                ((Stage) button.getScene().getWindow()).close(); // We close the create page after confirming the creation
            
            }

            else FXManager.showAlert(AlertType.ERROR, "Empty Fields", "Missing Data", "Please enter all the required information.");
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
        TextField firstTextField= FXManager.getTextField(pane, "firstTextField");
        if (contactType.equals("Enterprise")) firstTextField.setPromptText("enterprise name");
        TextField secondTextField = FXManager.getTextField(pane, "secondTextField");
        if (contactType.equals("Enterprise")) secondTextField.setPromptText("type");
        TextField phoneNumberTextField = FXManager.getTextField(pane, "phoneNumberTextField");
        TextField emailTextField = FXManager.getTextField(pane, "emailTextField");
        FXManager.setTextFieldEmailFormatRule(emailTextField);
        TextField houseNumberTextField = FXManager.getTextField(pane, "houseNumberTextField");
        FXManager.setTextFieldNumericFormatRule(houseNumberTextField);
        TextField neighborhoodTextField = FXManager.getTextField(pane, "neighborhoodTextField");
        TextField cityTextField = FXManager.getTextField(pane, "cityTextField");
        TextField zipCodeTextField = FXManager.getTextField(pane, "zipCodeTextField");
        FXManager.setTextFieldNumericFormatRule(zipCodeTextField);
        TextField regionTextField = FXManager.getTextField(pane, "regionTextField");
        TextField countryTextField = FXManager.getTextField(pane, "countryTextField");

        // We put all the text fields in a list to check if all the fields got input :
        List<TextField> textFields = Arrays.asList(firstTextField,secondTextField,phoneNumberTextField,emailTextField,

        houseNumberTextField,neighborhoodTextField,cityTextField,zipCodeTextField,regionTextField,countryTextField);

        // We extract the data from the text fields when the button is clicked
        button.setOnAction(event -> {

            if (validUpdateChecker(textFields)) {

                contactAttributes.forEach(attribute -> {

                    String value = FXManager.getTextField(pane, attribute + "TextField").getText(); // We collect the value of the text field entered by the user
    
                    contactValues.add(value); // for every other attribute we just save the value as string
                });
    
                addressAttributes.forEach(attribute -> {
    
                    String value = FXManager.getTextField(pane, attribute + "TextField").getText(); // We collect the value of the text field entered by the user
    
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
                FXManager.showAlert(AlertType.CONFIRMATION, "Update Status", "Result :", contactUpdateResult);
    
                else FXManager.showAlert(AlertType.ERROR, "Update Status", "Result :", contactUpdateResult);
                ((Stage) button.getScene().getWindow()).close(); // We close the update page after confirming the update
            }

            else FXManager.showAlert(AlertType.ERROR, "Empty Fields", "Missing Data", "At least one value is required for the update"); 
        });
    }

    // deleting a contact :
    public static void deleteContact(String contactType, int id) {

        String contactDeletionResult = ContactDto.deleteContact(id, contactType);

        // We display the deletion result :
        if (contactDeletionResult.equals("Contact deleted successfully."))
        
        FXManager.showAlert(AlertType.CONFIRMATION, "Deletion Status", "Result :", contactDeletionResult);

        else FXManager.showAlert(AlertType.ERROR, "Deletion Status", "Result :", contactDeletionResult);

    }

    // sending an email to a contact :
    public static void sendEmail(Parent pane, Button button, String receiverEmail) {

        button.setOnAction(event -> {

            // We get the email subject and body entered by the user :
            String subject = FXManager.getTextField(pane, "subjectTextField").getText();
            String emailBody = FXManager.getTextArea(pane, "bodyTextArea").getText();

            // if both subject and body were provided :
            if (subject != "" && emailBody != "") {

                // We put the receiver, subject and body in a list 
                List<String> values = Arrays.asList(receiverEmail, subject, emailBody); 

                // We call the Parser class to generate the corresponding map
                Map<String,Object> emailSendingMap = Parser.emailSendingMapGenerator(values);

                // We convert the map to json
                String json = Parser.jsonGenerator(emailSendingMap);

                // Finally we send the json in a post request to the corresponding endpoint 
                String emailSendingResult = ContactDto.postEmail(json);
    
                // We display the sending result :
                if (emailSendingResult.equals("Email Sent Successfully."))
            
                FXManager.showAlert(AlertType.CONFIRMATION, "Sending Status", "Result :", emailSendingResult);
        
                else FXManager.showAlert(AlertType.ERROR, "Sending Status", "Result :", emailSendingResult);

                ((Stage) button.getScene().getWindow()).close(); // We close the sending page after sending the email
            }

            // if one field or more are missing
            else FXManager.showAlert(AlertType.ERROR, "Missing Data", "Empty Fields :", "Please fill all the required fields.");
        });
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


}
