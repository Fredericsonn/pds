package uiass.eia.gisiba;

import java.util.*;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class FXManager {

    public static Map<String, List<String>> ids_per_contact_type_map =  new HashMap<String, List<String>>() {{
        put("Person", Arrays.asList("fullNameLabel","phoneNumberLabel","emailLabel","addressLabel"));
        put("Enterprise", Arrays.asList("enterpriseTypeLabel","enterpriseNameLabel","phoneNumberLabel","emailLabel","addressLabel"));
    }};

    public static List<Label> labelsCollector(Parent pane, List<String> LabelsIds) {

        List<Label> labels = new ArrayList<Label>();

        for (String id : LabelsIds) {

            labels.add(getLabel(pane, id));
        }

        return labels;
    }

    public static void contactLabelsFiller(List<Label> labels,  List<String> values,String contactType) {

        // We use the extracted values to fill the labels
        for (int i = 1 ; i < values.size() ; i++) {

            if (contactType.equals("Person")) {

                // this if statement is responsible for filling the Full Name label in case the contact is a person
                if (i == 1) { 

                    labels.get(i-1).setText(values.get(i) + " " + values.get(i + 1)); // We concatenate the first and last names

                    i++; // we increment i by 1 so it omits the following value in the next iteration
                }

                else labels.get(i-1).setText(values.get(i)); // if it's another attribute we just set the value directly
            }

            // if it's an enterprise we just set the value directly
            else labels.get(i).setText(values.get(i)); 
        }
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

    public static TextArea getTextArea(Parent pane, String id) {

        return (TextArea) pane.lookup("#" + id);
    } 
}
