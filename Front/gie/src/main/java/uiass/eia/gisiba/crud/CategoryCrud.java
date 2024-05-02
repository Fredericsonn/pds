package uiass.eia.gisiba.crud;

import java.io.InputStream;
import java.util.*;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import uiass.eia.gisiba.controller.FXManager;
import uiass.eia.gisiba.http.dto.CategoryDto;
import uiass.eia.gisiba.http.parsers.Parser;
import uiass.eia.gisiba.http.parsers.ProductParser;

public class CategoryCrud {

    public static void create_category(Parent pane, Button button) {
        
        // ComboBoxes
        ComboBox categoryComboBox = FXManager.getComboBox(pane, "categoryComboBox");
        ComboBox brandComboBox = FXManager.getComboBox(pane, "brandComboBox");

        // TextField
        TextField categoryTextField = FXManager.getTextField(pane, "categoryTextField");
        TextField brandTextField = FXManager.getTextField(pane, "brandTextField");

        // We populate the category and brand combo boxes
        categoryPopulator(Arrays.asList(categoryComboBox,brandComboBox));

        // We set the corresponding event listener to disable the non used nodes
        nodesHandler(categoryComboBox, brandComboBox, categoryTextField, brandTextField);

        // When comfirm is clicked
        button.setOnAction(event -> {
            
            if (categoryCreationValidator(categoryComboBox, brandComboBox, categoryTextField, brandTextField)) {

                String categroy = inputHandler(categoryComboBox, categoryTextField); // We get the entered category

                String brand = inputHandler(brandComboBox, brandTextField); // We get the entered brand
    
                // we create a map that will be converted into json
                Map<String, Object> map = ProductParser.categoryCreationMapGenerator(Arrays.asList(categroy,brand));
    
                // we generate the json from the map
                String json = Parser.jsonGenerator(map);
    
                // we call send the json to the back end to create the new category
                String categoryCreationResult = CategoryDto.postCategory(json);
    
                // We display the creation result :
                if (categoryCreationResult.equals("Category created successfully"))
    
                    FXManager.showAlert(AlertType.CONFIRMATION, "Confirmation", "Creation Status  :", categoryCreationResult);
        
                else FXManager.showAlert(AlertType.ERROR, "ERROR", "Creation Status  :", "Internal Server Error");
    
                whereToGoNext();
        
                ((Stage) button.getScene().getWindow()).close(); // We close the create page after confirming the creation
            }

            else FXManager.showAlert(AlertType.ERROR, "ERROR", "Missing Values", "Please provide both a category and a brand.");
        });
    }

    public static void goToCreateCategoryPage() {

        // We create the stage that will contain the creation page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);

        // here we load the creation page fxml file
        FXManager.loadFXML("/uiass/eia/gisiba/inventory/catalog/create_category_pane.fxml", pane, CategoryCrud.class); 
        
        // We collect the confirm button from the fxml file
        Button confirm = FXManager.getButton(pane, "confirmBtn");

        // We add the corresponding event listener to the button
        create_category(pane, confirm);;
        
        // We add the stage info and show it
        String iconPath = "/uiass/eia/gisiba/imgs/brand-image.png";
        InputStream inputStream = CategoryCrud.class.getResourceAsStream(iconPath);
        Image icon = new Image(inputStream);

        stage.setScene(scene);
        stage.setTitle("Create Category / Brand");
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.show();
    }

    // collects the right input and disables the non used nodes
    public static void nodesHandler(ComboBox categoryComboBox, ComboBox brandComboBox, 
    
        TextField categoryTextField, TextField brandTextField) {

        categoryComboBox.valueProperty().addListener(event -> {
            
            categoryTextField.setDisable(true);

        });

        categoryTextField.setOnKeyPressed(event -> {           

            if (event.getCode() == KeyCode.ENTER && !categoryTextField.getText().equals("")) {

                categoryComboBox.setDisable(true);
        }}); 

        brandComboBox.valueProperty().addListener(event -> {
            
            brandTextField.setDisable(true);

        });

        brandTextField.setOnKeyPressed(event -> {           

            if (event.getCode() == KeyCode.ENTER && !brandTextField.getText().equals("")) {

                brandComboBox.setDisable(true);
        }}); 

    }

    // collects the right input 
    public static String inputHandler(ComboBox comboBox, TextField textField) {

        String categroy;
    
        if (textField.isDisabled()) categroy = (String) comboBox.getValue();
        
        else categroy = textField.getText();
            
        return categroy;
    }

    public static void whereToGoNext() {

        // We ask the user for the confirmation before the delete :
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Question");
        alert.setHeaderText("Answer PLeae : ");
        alert.setContentText("Do you want to add a new product for this category ? ");
                    
        // Add "Yes" and "No" buttons
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                    
        // Show the dialog and wait for user input
        ButtonType result = alert.showAndWait().orElse(null);
        
        if (result == buttonTypeYes) {
            ProductCrud.goToCreateProductPage();
        }   
         
    }

    public static void categoryPopulator(List<ComboBox> comboBoxes) {

        ComboBox categoryComboBox = comboBoxes.get(0);

        ComboBox brandComboBox = comboBoxes.get(1);

        List<String> categoriesList = CategoryDto.getAllCategoriesNames(); // We get all the categories that we have 

        List<String> brandsList = CategoryDto.getAllBrands(); // We get all the brands that we have 

        FXManager.populateComboBox(categoryComboBox, categoriesList); // We add the categories as the category combo box items

        FXManager.populateComboBox(brandComboBox, brandsList); // We add the brands as the brand combo box items

        categoryComboBox.valueProperty().addListener(event -> {

            brandComboBox.setPromptText("brand");

            String category = (String) categoryComboBox.getValue();

            List<String> brandsByCategory = CategoryDto.getAllBrandsByCategory(category);

            brandsList.removeAll(brandsByCategory);

            FXManager.populateComboBox(brandComboBox, brandsList);
        });

    }

    public static boolean categoryCreationValidator(ComboBox categoryComboBox, ComboBox brandComboBox,
    
    TextField categoryTextField, TextField brandTextField  ) {

        return (categoryComboBox.isDisabled() || categoryTextField.isDisabled()) && 
        
        (brandComboBox.isDisabled() || brandTextField.isDisabled());
    }

}
