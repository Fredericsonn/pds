package uiass.eia.gisiba;

import java.util.*;


import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import uiass.eia.gisiba.dto.Parser;
import uiass.eia.gisiba.dto.ProductDto;
public class ProductCrud {

    @SuppressWarnings("unchecked")

    // A method that extracts the data entered by the user and sends a post http request to the server :
    public static void create_product(Parent pane, Button button) {

        // The list of values we'll be using to generate the coluns - values map
        List values = new ArrayList<>();

        ComboBox categoryComboBox = FXManager.getComboBox(pane, "categoryComboBox");
        ComboBox brandComboBox = FXManager.getComboBox(pane, "brandComboBox");

        TextField modelTextField= FXManager.getTextField(pane, "modelTextField");
        TextField unitPriceTextField = FXManager.getTextField(pane, "unitPriceTextField");
        FXManager.setTextFieldFloatFormatRule(unitPriceTextField);
        TextField descriptionTextField = FXManager.getTextField(pane, "descriptionTextField");


        // We put all the text fields in a list to check if all the fields got input :
        List<TextField> textFields = Arrays.asList(modelTextField,descriptionTextField,unitPriceTextField);

        // We put all the combo boxes in a list to check if an item was selected :
        List<ComboBox> comboBoxes = Arrays.asList(categoryComboBox,brandComboBox);

        List<String> categoriesList = ProductDto.getAllCategories(); // We get the categories list that we have 

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
    
                // We use the json to send an http post request to the server to create the new product with the entered values :
                String productCreationResult = ProductDto.postProduct(json);
    
                // We display the creation result :
                if (productCreationResult.equals("Product created successfully."))

                    FXManager.showAlert(AlertType.CONFIRMATION, "Confirmation", "Creation Status  :", productCreationResult);
    
                else FXManager.showAlert(AlertType.ERROR, "ERROR", "Creation Status  :", productCreationResult);
    
                ((Stage) button.getScene().getWindow()).close(); // We close the create page after confirming the creation
            
            }

            // When the user clicks on CONFIRM before provididing all the necessary data 
            else FXManager.showAlert(AlertType.ERROR, "ERROR", "Missing Data", "Please enter all the required information.");
        });
    }

        
    @SuppressWarnings("unchecked")

    // A method that extracts the data entered by the user and sends a put http request to the server :
    public static void update_product(Parent pane, Button button, String ref) {

        // The list of values we'll be using to generate the coluns - values map
        List values = new ArrayList<>();

        ComboBox categoryComboBox = FXManager.getComboBox(pane, "categoryComboBox");
        ComboBox brandComboBox = FXManager.getComboBox(pane, "brandComboBox");

        TextField modelTextField= FXManager.getTextField(pane, "modelTextField");
        TextField unitPriceTextField = FXManager.getTextField(pane, "unitPriceTextField");
        TextField descriptionTextField = FXManager.getTextField(pane, "descriptionTextField");


        // We put all the text fields in a list to check if all the fields got input :
        List<TextField> textFields = Arrays.asList(modelTextField,unitPriceTextField,descriptionTextField);

        // We put all the combo boxes in a list to check if an item was selected :
        List<ComboBox> comboBoxes = Arrays.asList(categoryComboBox,brandComboBox);

        List<String> categoriesList = ProductDto.getAllCategories(); // We get the categories list that we have 

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

            if (FXManager.textFieldsUpdateInputChecker(textFields) && 
            
            FXManager.comboBoxesUpdateInputChecker(comboBoxes)) {

                String category = String.valueOf(categoryComboBox.getValue());

                brandComboBox.setOnAction(brandEvent -> {
                   
                    if (category != "null") {

                        List<String> brandsList = ProductDto.getAllBrandsByCategory(category);

                        FXManager.populateComboBox(brandComboBox, brandsList);

                        String brand = String.valueOf(brandComboBox.getValue());

                        values.add(category); values.add(brand);

                        textFields.forEach(textField -> {

                            values.add(String.valueOf(textField.getText()));
                        });

                        // We generate the columns - values map using the values list :
                        Map<String,Object> map = Parser.productUpdateMapGenerator(values);
    
                        // We dynamically generate the corresponding json :
                        String json = Parser.jsonGenerator(map);
    
                        // We use the json to send an http post request to the server to create the new product with the entered values :
                        String productCreationResult = ProductDto.updateProduct(ref,json);
    
                        // We display the creation result :
                        if (productCreationResult.equals("Product Updated successfully."))

                        FXManager.showAlert(AlertType.CONFIRMATION, "Confirmation", "Update Status  :", productCreationResult);
    
                        else FXManager.showAlert(AlertType.ERROR, "ERROR", "Update Status  :", productCreationResult);
    
                        ((Stage) button.getScene().getWindow()).close(); // We close the create page after confirming the creation
                   }

                   // When the user click tries to select a brand before the category
                   else FXManager.showAlert(AlertType.ERROR, "ERROR", "No category was selected :", "Please select a category first.");
                });
            
            }

            // When the user clicks on CONFIRM before provididing all the necessary data 
            else FXManager.showAlert(AlertType.ERROR, "ERROR", "Missing Data", "Please enter all the required information.");
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
}
