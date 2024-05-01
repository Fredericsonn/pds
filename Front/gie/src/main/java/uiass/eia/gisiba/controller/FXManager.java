package uiass.eia.gisiba.controller;

import java.io.IOException;
import java.util.*;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class FXManager {

    public static Map<String, List<String>> labels_ids_per_contact_type_map =  new HashMap<String, List<String>>() {{
        put("Person", Arrays.asList("fullNameLabel","phoneNumberLabel","emailLabel","addressLabel"));
        put("Enterprise", Arrays.asList("enterpriseNameLabel","enterpriseTypeLabel","phoneNumberLabel","emailLabel","addressLabel"));
    }};

    public static List<String> catalog_labels_ids = Arrays.asList("categoryLabel","brandModelLabel","unitPriceLabel", "descriptionLabel");

    public static Map<String, List<String>> columns_names_per_contact_type = new HashMap<String, List<String>>() {{
        put("Person", Arrays.asList("id","first name","last name","phone number", "email", "address id","house number","neighborhood","city","zip code","country"));
        put("Enterprise", Arrays.asList("id","enterprise name","type","phone number", "email", "address id","house number","neighborhood","city","zip code","country"));
    }};

    public static List<String> catalog_columns = Arrays.asList("ref","category id","category","brand","model","description","unit price");



    // A method that loads an fxml file into a pane
    public static void loadFXML(String fxmlFile, AnchorPane pane, Class c) {
        
        try {
            FXMLLoader loader = new FXMLLoader(c.getResource(fxmlFile));
            Parent content = loader.load();
            pane.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Used to collect labels in a pane given a list of ids
    public static List<Label> labelsCollector(Parent pane, List<String> labelsIds) {

        List<Label> labels = new ArrayList<Label>();

        for (String id : labelsIds) {

            labels.add(getLabel(pane, id));
        }

        return labels;
    }

    // Used to dynamically set each label's corresponding text given a contact values list and a contact type
    public static void contactLabelsFiller(List<Label> labels,  List<String> values, String contactType) {

        labels.get(labels.size() - 1).setWrapText(true); // Allow the label to have multi-line text

        // We use the extracted values to fill the labels
        for (int i = 0 ; i < values.size() ; i++) {

            if (contactType.equals("Person")) {

                // this if statement is responsible for filling the Full Name label in case the contact is a person
                if (i == 0) { 

                    labels.get(i).setText(values.get(i) + " " + values.get(i + 1)); // We concatenate the first and last names

                }

                else { if (i != 1)
                labels.get(i-1).setText(values.get(i)); // if it's another attribute we just set the value directly
                }
            }

            // if it's an enterprise we just set the value directly
            else labels.get(i).setText(values.get(i)); 
        }
    }

    // Used to dynamically set each label's corresponding text given a product values list 
    public static void catalogLabelsFiller(List<Label> labels,  List<String> values) {

        labels.get(labels.size() - 1).setWrapText(true); // Allow the label to have multi-line text
        
        // We use the extracted values to fill the labels
        for (int i = 1 ; i < values.size() ; i++) {

            // this if statement is responsible for filling the brand and model labels 
            if (i < 2)

                labels.get(i-1).setText(values.get(i)); // if it's another attribute we just set the value directly

            else if (i == 2) { 

                labels.get(i-1).setText(values.get(i) + " " + values.get(i + 1)); // We concatenate the brand and the model
            }

            else if (i != 3 ) labels.get(i-2).setText(values.get(i));
          
            }

        
    }

    // Used to dynamically populate tables given the data and the contact type  
    public static void populateTableView(TableView<List<String>> tableView, List<String> columns, List<List<String>> data) {

        // Clear existing columns and items
        tableView.getColumns().clear();
        tableView.getItems().clear();

        // Create columns
        for (int i = 0; i < columns.size(); i++) {

            final int columnIndex = i;

            TableColumn<List<String>, String> column = new TableColumn<>(columns.get(i));

            column.setCellValueFactory(cellData -> {

                List<String> row = cellData.getValue();

                if (row != null && columnIndex < row.size()) {

                    return new SimpleStringProperty(row.get(columnIndex));

                } else {

                    return new SimpleStringProperty(""); // Return an empty property for empty cells
                }
            });

            if (columns.get(i).equals("id") || columns.get(i).equals("ref") ||
            
            columns.get(i).equals("address id") || columns.get(i).equals("category id") ) column.setVisible(false);

            tableView.getColumns().add(column);
        }

        // Add data
        tableView.getItems().addAll(data);
    }

    @SuppressWarnings("unchecked")
    // Populate a combo box using a list of items
    public static void populateComboBox(ComboBox comboBox, List<?> itemsList) {

        comboBox.getItems().clear();

        ObservableList items = FXCollections.observableArrayList(itemsList);

        comboBox.setItems(items);
    }

    // validate a creation :
    public static boolean textFieldsCreationInputChecker(List<TextField> textFields) {

        for (TextField textField : textFields) {

            if (textField.getText().equals("")) return false;

        }

        return true;
    }

    public static boolean comboBoxesCreationInputChecker(List<ComboBox> comboBoxes) {

        for (ComboBox comboBox : comboBoxes) {

            if (comboBox.getValue() == null) return false;

        }

        return true;
    }

    // validate an update :
    public static boolean textFieldsUpdateInputChecker(List<TextField> textFields) {

        for (TextField textField : textFields) {

            if (!textField.getText().equals("")) return true;

        }

        return false;
    }

    public static boolean comboBoxesUpdateInputChecker(List<ComboBox> comboBoxes) {

        for (ComboBox comboBox : comboBoxes) {

            if (comboBox.getValue() != null) return true;

        }

        return false;
    }

    // numeric only text field rule
    public static void setTextFieldNumericFormatRule(TextField numericTextField) {

        numericTextField.setTextFormatter(new TextFormatter<>(change -> {

        String newText = change.getControlNewText();

        if (newText.matches("\\d*")) { // Allow only digits

            return change;

        } else return null; // Reject the change
            
        }));
    }

    // alphanumeric only text field rule
    public static void setTextFieldAlphanumericFormatRule(TextField alphanumericTextField) {

        alphanumericTextField.setTextFormatter(new TextFormatter<>(change -> {

            String newText = change.getControlNewText();

            // Allow only alphanumeric characters and a max length of 6
            if (newText.matches("[a-zA-Z0-9]*")) { 

                return change;

            } else {

                return null; // Reject the change

            }
        }));
    }

    // alphabetic only text field rule
    public static void setTextFieldPureAlphabeticFormatRule(TextField alphabeticTextField) {
        alphabeticTextField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // Allow only alphabetic characters, spaces, and "-" characters
            if (newText.matches("[a-zA-Z]*")) {
                return change;
            } else {
                return null; // Reject the change
            }
        }));
    }
    public static void setTextFieldAlphabeticFormatRule(TextField alphabeticTextField) {
        alphabeticTextField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // Allow only alphabetic characters, spaces, and "-" characters
            if (newText.matches("[a-zA-Z -]*")) {
                return change;
            } else {
                return null; // Reject the change
            }
        }));
    }

    // Rule for only alphanumeric characters, dots, and at symbols
    public static void setTextFieldEmailFormatRule(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // Allow only alphanumeric characters, dots, and at symbols
            if (newText.matches("[a-zA-Z0-9.@]*")) {
                return change;
            } else {
                return null; // Reject the change
            }
        }));
    }

    public static void setTextFieldFloatFormatRule(TextField floatTextField) {
        floatTextField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) { // Allow digits, optional decimal point
                return change;
            } else {
                return null; // Reject the change
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

    // A list of getters that return different nodes using their id and the parent containing them
    public static TextField getTextField(Parent pane, String id) {
        return (TextField) pane.lookup("#"+id);
    }

    public static Button getButton(Parent pane, String id) {
        return (Button) pane.lookup("#"+id);
    }

    public static Label getLabel(Parent pane, String id) {
        return (Label) pane.lookup("#"+id);
    }

    public static TableView<List<String>> getTableView(Parent pane, String id) {
        return (TableView<List<String>>) pane.lookup("#"+id);
    }
    public static ListView getListView(Parent pane, String id) {
        return (ListView) pane.lookup("#" + id);
    }

    public static RadioButton getRadioButton(Parent pane, String id) {
        return (RadioButton) pane.lookup("#" + id);
    } 

    public static TextArea getTextArea(Parent pane, String id) {

        return (TextArea) pane.lookup("#" + id);
    } 

    public static ComboBox getComboBox(Parent pane, String id) {

        return (ComboBox) pane.lookup("#" + id);
    } 

    public static HBox getHBox(Parent pane, String id) {

        return (HBox) pane.lookup("#" + id);
    } 
}
