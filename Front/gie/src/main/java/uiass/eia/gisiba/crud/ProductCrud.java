package uiass.eia.gisiba.crud;

import java.util.*;


import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import uiass.eia.gisiba.controller.FXManager;
import uiass.eia.gisiba.dto.Parser;
import uiass.eia.gisiba.dto.ProductDto;
public class ProductCrud {

    @SuppressWarnings("unchecked")

    // A method that extracts the data entered by the user and sends a post http request to the server :
    public static void create_product(Parent pane, Button button) {

        // The list of values we'll be using to generate the coluns - values map
        List values = new ArrayList<>();

        // We put all the text fields in a list to check if all the fields got input :
        List<TextField> textFields = productTextFieldsHandler(pane, "create", values);

        // We put all the combo boxes in a list to check if an item was selected :
        List<ComboBox> comboBoxes = productComboBoxesHandler(pane, "create", values);

        List<String> categoriesList = ProductDto.getAllCategories(); // We get the categories list that we have 

        ComboBox categoryComboBox = comboBoxes.get(0);
        ComboBox brandComboBox = comboBoxes.get(1);

        FXManager.populateComboBox(categoryComboBox, categoriesList); // We add the categories as the category combo box items


        categoryComboBox.valueProperty().addListener((obs, oldCategory, newCategory) -> {
    
            if (newCategory != null) {
                    
                // We get all the corresponding brands for the newly selected category
                List<String> brandsList = ProductDto.getAllBrandsByCategory(String.valueOf(newCategory)); 
            
                // Populate the brandComboBox with the brandsList
                FXManager.populateComboBox(brandComboBox, brandsList);
            }
        });
        

        // We extract the data from the text fields when the button is clicked
        button.setOnAction(event -> {

            if (FXManager.textFieldsCreationInputChecker(textFields) && 
            
            FXManager.comboBoxesCreationInputChecker(comboBoxes)) {

                String category = String.valueOf(categoryComboBox.getValue());
                
                String brand = String.valueOf(brandComboBox.getValue());

                values.add(category); values.add(brand);

                textFields.forEach(textField -> {

                    values.add(String.valueOf(textField.getText()));
                });

                // We generate the columns - values map using the values list :
                Map<String,Object> map = Parser.productCreationMapGenerator(values);
    
                // We dynamically generate the corresponding json :
                String json = Parser.jsonGenerator(map);

                System.out.println(json);
    
                // We use the json to send an http post request to the server to create the new product with the entered values :
                String productCreationResult = ProductDto.postProduct(json);

                System.out.println(productCreationResult);
    
                // We display the creation result :
                if (productCreationResult.equals("Product created successfully."))

                    FXManager.showAlert(AlertType.CONFIRMATION, "Confirmation", "Creation Status  :", productCreationResult);
    
                else FXManager.showAlert(AlertType.ERROR, "ERROR", "Creation Status  :", "Internal Server Error");
    
                ((Stage) button.getScene().getWindow()).close(); // We close the create page after confirming the creation
            
            }

            // When the user clicks on CONFIRM before provididing all the necessary data 
            else FXManager.showAlert(AlertType.ERROR, "ERROR", "Missing Data", "Please enter all the required information.");
        });
    }

        
    @SuppressWarnings("unchecked")

    // A method that extracts the data entered by the user and sends a put http request to the server :
    public static void update_product(Parent pane, Button button, String ref, List<String> originalValues) {

        // The list of values we'll be using to generate the coluns - values map
        List values = new ArrayList<>();

        // We put all the text fields in a list :
        List<TextField> textFields = productTextFieldsHandler(pane, "update", originalValues);

        // We put all the combo boxes in a list :
        List<ComboBox> comboBoxes = productComboBoxesHandler(pane, "update", originalValues);

        List<String> categoriesList = ProductDto.getAllCategories(); // We get the categories list that we have 

        ComboBox categoryComboBox = comboBoxes.get(0);
        ComboBox brandComboBox = comboBoxes.get(1);

        FXManager.populateComboBox(categoryComboBox, categoriesList); // We add the categories as the category combo box items

        // We fill the brands combo box once a category is selected
        categoryComboBox.valueProperty().addListener((obs, oldCategory, newCategory) -> {
    
            if (newCategory != null) {
                    
                // We get all the corresponding brands for the newly selected category
                List<String> brandsList = ProductDto.getAllBrandsByCategory(String.valueOf(newCategory)); 
            
                // Populate the brandComboBox with the brandsList
                FXManager.populateComboBox(brandComboBox, brandsList);

            }
        });

        // We extract the data from the text fields when the button is clicked
        button.setOnAction(event -> {

            if (FXManager.textFieldsUpdateInputChecker(textFields) ||
            
            FXManager.comboBoxesUpdateInputChecker(comboBoxes)) {

                String category = String.valueOf(categoryComboBox.getValue());

                String brand = String.valueOf(brandComboBox.getValue());

                values.add(category); values.add(brand);

                textFields.forEach(textField -> {

                    values.add(String.valueOf(textField.getText()));
                });

                System.out.println(values);
                // We generate the columns - values map using the values list :
                Map<String,Object> map = Parser.productUpdateMapGenerator(values);
                System.out.println(map);
    
                // We dynamically generate the corresponding json :
                String json = Parser.jsonGenerator(map);
                    
                // We use the json to send an http post request to the server to create the new product with the entered values :
                String productUpdateResult = ProductDto.updateProduct(ref,json);
                    
                // We display the update result :
                if (productUpdateResult.equals("Product Updated successfully."))
                
                FXManager.showAlert(AlertType.CONFIRMATION, "Confirmation", "Update Status  :", productUpdateResult);
                    
                else FXManager.showAlert(AlertType.ERROR, "ERROR", "Update Status  :", productUpdateResult);
                    
                ((Stage) button.getScene().getWindow()).close(); // We close the create page after confirming the creation
            
            }

            // When the user clicks on CONFIRM before provididing all the necessary data 
            else FXManager.showAlert(AlertType.ERROR, "ERROR", "Missing Data", "Please provide some new values for the update");
        });
    }

    // deleting a contact :
    public static void deleteProduct(String ref) {

        String contactDeletionResult = ProductDto.deleteProduct(ref);

        // We display the deletion result :
        if (contactDeletionResult.equals("Product deleted successfully."))
        
        FXManager.showAlert(AlertType.CONFIRMATION, "Confirmation", "Deletion Status :", contactDeletionResult);

        else FXManager.showAlert(AlertType.ERROR, "ERROR", "Deletion Status :", contactDeletionResult);

    }

    public static List<TextField> productTextFieldsHandler(Parent pane, String operation, List<String> originalValues) {

        List<TextField> textFields = new ArrayList<TextField>();

        // We collect the text fields from the pane and apply correspondind input rules : 
        TextField modelTextField= FXManager.getTextField(pane, "modelTextField");
        FXManager.setTextFieldAlphanumericFormatRule(modelTextField);
        TextField descriptionTextField = FXManager.getTextField(pane, "descriptionTextField");
        FXManager.setTextFieldAlphanumericFormatRule(descriptionTextField);
        TextField unitPriceTextField = FXManager.getTextField(pane, "unitPriceTextField");
        FXManager.setTextFieldFloatFormatRule(unitPriceTextField);

        if (operation.equals("update")) {   // if we want to update

            // We set the prompt text to be the original product's values : 
            modelTextField.setPromptText(originalValues.get(3));
            unitPriceTextField.setPromptText(originalValues.get(4));
            descriptionTextField.setPromptText(originalValues.get(5));
        }

        
        // We put all the corresponding text fields in a list to later check if all the fields got input :
        textFields.addAll(Arrays.asList(modelTextField,descriptionTextField,unitPriceTextField));

        return textFields;
    }

    public static List<ComboBox> productComboBoxesHandler(Parent pane, String operation, List<String> originalValues) {

        List<ComboBox> comboBoxes = new ArrayList<ComboBox>();

        // We collect the text fields from the pane : 
        ComboBox category= FXManager.getComboBox(pane, "categoryComboBox");
        ComboBox brand = FXManager.getComboBox(pane, "brandComboBox");

        if (operation.equals("update")) {   // if we want to update

            // We set the prompt text to be the original product's values : 
            category.setPromptText(originalValues.get(1));
            brand.setPromptText(originalValues.get(2));
        }

        
        // We put all the corresponding text fields in a list to later check if all the fields got input :
        comboBoxes.add(category);
        comboBoxes.add(brand);

        return comboBoxes;
    }
    
}
