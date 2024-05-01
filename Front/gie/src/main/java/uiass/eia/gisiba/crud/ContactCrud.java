package uiass.eia.gisiba.crud;


import java.util.*;

import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import uiass.eia.gisiba.controller.FXManager;
import uiass.eia.gisiba.http.dto.AddressDto;
import uiass.eia.gisiba.http.dto.ContactDto;
import uiass.eia.gisiba.http.parsers.ContactParser;
import uiass.eia.gisiba.http.parsers.Parser;

public class ContactCrud {


    @SuppressWarnings("unchecked")

    // A method that extracts the data entered by the user and sends a post http request to the server :
    public static void create_contact(Parent pane, Button button, String contactType) {

        // The list of values we'll be using to generate the coluns - values map
        List values = new ArrayList<>();

        // The list of attributes that reference the text fields' ids :
        List<String> attributes = new ArrayList<String>(ContactParser.contactCreationTextFieldsReferences);


        // We collect all the text fields in a list and set the corresponding prompt text :
        List<TextField> textFields = contactCreationTextFieldsHandler(pane, contactType);

        ComboBox typeComboBox = null;

        if (contactType.equals("Enterprise")) {

            attributes.remove(1);

            typeComboBox = FXManager.getComboBox(pane, "enterpriseTypeComboBox");
            
            List<String> types = new ArrayList<>(Arrays.asList("SA","SAS","SARL","SNC"));

            FXManager.populateComboBox(typeComboBox, types);
            
        }

        List<ComboBox> comboBoxes = new ArrayList<ComboBox>();

        if (typeComboBox != null) comboBoxes.add(typeComboBox);

        // We extract the data from the text fields when the button is clicked
        button.setOnAction(event -> {

            if (FXManager.textFieldsCreationInputChecker(textFields) && (FXManager.comboBoxesCreationInputChecker(comboBoxes)
            
            || comboBoxes.isEmpty())) {

                attributes.forEach(attribute -> {

                    String value = FXManager.getTextField(pane, attribute + "TextField").getText(); // We collect the value of the text field entered by the user
    
                    if (attribute.equals("houseNumber")) values.add(value != "" ? Integer.parseInt(value) : 0); // We must save the house number as an integer.
                    
                    else values.add(value); // for every other attribute we just save the value as string
                });

                if (contactType.equals("Enterprise")) values.add(comboBoxes.get(0).getValue());
    
                // We generate the columns - values map using the values list :
                Map<String,Object> map = ContactParser.contactCreationMapGenerator(values, contactType);
    
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
    public static void update_contact(Parent pane, Button button, String contactType, int contactId, 
    
    int addressId, List<String> originalValues) {

        // lists containing each object's attributes (excluding ids) :
        List<String> contactAttributes = new ArrayList<String>(ContactParser.contactUpdateTextFieldsReferences);

        List<String> addressAttributes = ContactParser.update_address_attributes;

        // lists to store each attribute's entered value :
        List contactValues = new ArrayList<>();

        List addressValues = new ArrayList<>();


        // We collect all the text fields in a list and set the corresponding prompt text :
        List<TextField> textFields = contactUpdateTextFieldsHandler(pane, contactType, originalValues);

        ComboBox typeComboBox = null;

        if (contactType.equals("Enterprise")) {

            contactAttributes.remove(1);

            typeComboBox = FXManager.getComboBox(pane, "enterpriseTypeComboBox");
            
            List<String> types = new ArrayList<>(Arrays.asList("SA","SAS","SARL","SNC"));

            String originalEnterpriseType = originalValues.get(1);

            FXManager.populateComboBox(typeComboBox, types);

            typeComboBox.setPromptText(originalEnterpriseType);
            
        }

        List<ComboBox> comboBoxes = new ArrayList<ComboBox>();

        if (typeComboBox != null) comboBoxes.add(typeComboBox);

        // We extract the data from the text fields when the button is clicked
        button.setOnAction(event -> {

            if (FXManager.textFieldsUpdateInputChecker(textFields) || FXManager.comboBoxesUpdateInputChecker(comboBoxes)) {

                contactAttributes.forEach(attribute -> {

                    String value = FXManager.getTextField(pane, attribute + "TextField").getText(); // We collect the value of the text field entered by the user
    
                    contactValues.add(value); // We add it to the contact values list
                });
    
                addressAttributes.forEach(attribute -> {
    
                    String value = FXManager.getTextField(pane, attribute + "TextField").getText(); // We collect the value of the text field entered by the user
    
                    // We must save the house number as an integer :
                    if (attribute.equals("houseNumber")) addressValues.add(!value.equals("") ? Integer.parseInt(value) : 0); 
    
                    else addressValues.add(value); // for every other attribute we just save the value as string
                    
    
                });

                if (contactType.equals("Enterprise")) contactValues.add(comboBoxes.get(0).getValue());
    
                // We create the columns - values to update maps :
                Map<String,Object> contact_attributes_map = ContactParser.contactUpdateMapGenerator(contactValues, contactType);
                Map<String,Object> address_attributes_map = ContactParser.addressMapGenerator(addressValues);
    
                // We create their corresponding jsons :
                String contactJson = Parser.jsonGenerator(contact_attributes_map);
                String addressJson = Parser.jsonGenerator(address_attributes_map);
    
                // We send http put requests to update the contact or the address :
                String contactUpdateResult = ContactDto.updateContact(contactId, contactType, contactJson);
                String addressUpdateResult = AddressDto.updateAddress(addressId, addressJson);
    
                // We display the update result :
                if (contactUpdateResult.equals("Contact updated successfully.") || 
                
                addressUpdateResult.equals("Address updated successfully."))

                FXManager.showAlert(AlertType.CONFIRMATION, "Update Status", "Result :", contactUpdateResult);
    
                else if (!contactUpdateResult.equals("Address Updated Successfully.")) {

                FXManager.showAlert(AlertType.ERROR, "Update Status", "Result :", contactUpdateResult);

                }

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
                Map<String,Object> emailSendingMap = ContactParser.emailSendingMapGenerator(values);

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

    public static List<TextField> contactCreationTextFieldsHandler(Parent pane, String contactType) {

        List<TextField> textFields = new ArrayList<TextField>();

        TextField firstTextField= FXManager.getTextField(pane, "firstTextField");
        FXManager.setTextFieldPureAlphabeticFormatRule(firstTextField);
        TextField phoneNumberTextField = FXManager.getTextField(pane, "phoneNumberTextField");
        FXManager.setTextFieldAlphanumericFormatRule(phoneNumberTextField);
        TextField emailTextField = FXManager.getTextField(pane, "emailTextField");
        FXManager.setTextFieldEmailFormatRule(emailTextField);
        TextField houseNumberTextField = FXManager.getTextField(pane, "houseNumberTextField");
        FXManager.setTextFieldNumericFormatRule(houseNumberTextField);
        TextField neighborhoodTextField = FXManager.getTextField(pane, "neighborhoodTextField");
        FXManager.setTextFieldAlphanumericFormatRule(neighborhoodTextField);
        TextField cityTextField = FXManager.getTextField(pane, "cityTextField");
        FXManager.setTextFieldPureAlphabeticFormatRule(cityTextField);
        TextField zipCodeTextField = FXManager.getTextField(pane, "zipCodeTextField");
        FXManager.setTextFieldNumericFormatRule(zipCodeTextField);
        TextField countryTextField = FXManager.getTextField(pane, "countryTextField");
        FXManager.setTextFieldAlphabeticFormatRule(countryTextField);

        if (contactType.equals("Person")) {
            TextField secondTextField = FXManager.getTextField(pane, "secondTextField");
            FXManager.setTextFieldPureAlphabeticFormatRule(secondTextField);

            textFields.addAll(Arrays.asList(firstTextField,secondTextField,phoneNumberTextField,emailTextField,houseNumberTextField,
            
            neighborhoodTextField,cityTextField,zipCodeTextField,countryTextField));
            
        }

        else textFields.addAll(Arrays.asList(firstTextField,phoneNumberTextField,emailTextField,houseNumberTextField,
            
        neighborhoodTextField,cityTextField,zipCodeTextField,countryTextField));

        return textFields;
    }

    public static List<TextField> contactUpdateTextFieldsHandler(Parent pane, String contactType, List<String> originalValues) {

        List<TextField> contactTextFields = new ArrayList<TextField>();

        List<TextField> textFields = new ArrayList<TextField>();

                // We collect the text fields from the pane : 
                TextField firstTextField= FXManager.getTextField(pane, "firstTextField");
                FXManager.setTextFieldAlphabeticFormatRule(firstTextField);
                /*TextField secondTextField = FXManager.getTextField(pane, "secondTextField");
                FXManager.setTextFieldAlphabeticFormatRule(secondTextField);*/
                TextField phoneNumberTextField = FXManager.getTextField(pane, "phoneNumberTextField");
                FXManager.setTextFieldAlphanumericFormatRule(phoneNumberTextField);
                TextField emailTextField = FXManager.getTextField(pane, "emailTextField");
                FXManager.setTextFieldEmailFormatRule(emailTextField);
                TextField houseNumberTextField = FXManager.getTextField(pane, "houseNumberTextField");
                FXManager.setTextFieldNumericFormatRule(houseNumberTextField);
                TextField neighborhoodTextField = FXManager.getTextField(pane, "neighborhoodTextField");
                FXManager.setTextFieldAlphanumericFormatRule(neighborhoodTextField);
                TextField cityTextField = FXManager.getTextField(pane, "cityTextField");
                FXManager.setTextFieldAlphabeticFormatRule(cityTextField);
                TextField zipCodeTextField = FXManager.getTextField(pane, "zipCodeTextField");
                FXManager.setTextFieldNumericFormatRule(zipCodeTextField);
                TextField countryTextField = FXManager.getTextField(pane, "countryTextField");
                FXManager.setTextFieldAlphabeticFormatRule(countryTextField);

                // Here we select the text fields to consider depending on the contact type :
                if (contactType.equals("Person")) {

                    TextField secondTextField = FXManager.getTextField(pane, "secondTextField");
                    secondTextField.setPromptText(originalValues.get(1)); // We set its original value
                    FXManager.setTextFieldAlphabeticFormatRule(secondTextField); 

                    textFields.addAll(Arrays.asList(firstTextField,secondTextField,phoneNumberTextField,emailTextField,
        
                    houseNumberTextField,neighborhoodTextField,cityTextField,zipCodeTextField,countryTextField));
                }

                else textFields.addAll(Arrays.asList(firstTextField,phoneNumberTextField,emailTextField,
        
                houseNumberTextField,neighborhoodTextField,cityTextField,zipCodeTextField,countryTextField));
        
                // We set the prompt text to be the original contact's values : 
                firstTextField.setPromptText(originalValues.get(0));
                phoneNumberTextField.setPromptText(originalValues.get(2));
                emailTextField.setPromptText(originalValues.get(3));
                //////// We skip the address value as it is just used to fill the right panel ////////
                houseNumberTextField.setPromptText(originalValues.get(5));
                neighborhoodTextField.setPromptText(originalValues.get(6));
                cityTextField.setPromptText(originalValues.get(7));
                zipCodeTextField.setPromptText(originalValues.get(8));
                countryTextField.setPromptText(originalValues.get(9));
        
                // We put all the corresponding text fields in a list to later check if all the fields got input :
                contactTextFields.addAll(textFields);

                return contactTextFields;
    }



}
