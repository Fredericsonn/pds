package uiass.eia.gisiba.FX;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uiass.eia.gisiba.controller.FXManager;
import uiass.eia.gisiba.crud.ContactCrud;
import uiass.eia.gisiba.crud.InventoryItemCrud;
import uiass.eia.gisiba.crud.ProductCrud;
import uiass.eia.gisiba.crud.PurchaseCrud;
import uiass.eia.gisiba.http.dto.ContactDto;
import uiass.eia.gisiba.http.dto.InventoryDto;
import uiass.eia.gisiba.http.dto.PurchaseDto;
import uiass.eia.gisiba.http.dto.SaleDto;
import uiass.eia.gisiba.http.parsers.Parser;
import uiass.eia.gisiba.http.parsers.PurchaseParser;
import uiass.eia.gisiba.http.parsers.SaleParser;

public class OperationFX {

    public static void fillWithOperations(TableView table, String operation) {

        if (operationValidator(operation)) {

            List<List<String>> data = null;

            data = operation.equals("purchase") ? PurchaseDto.getAllPurchases() : SaleDto.getAllSales();

            data.forEach(sale -> {

                String unitPrice = sale.get(5);

                sale.set(5, unitPrice + "$");
            });

            FXManager.populateTableView(table, FXManager.sale_columns,
                    Arrays.asList("sale id", "customer id", "customerType"), data);
        }

        else
            throw new RuntimeException("Invalid Operation Type !!");
    }

    public static void fillWithFilteredOperations(TableView salesTable, List<List<String>> data) {

        data.forEach(sale -> {

            String unitPrice = sale.get(5);

            sale.set(5, unitPrice + "$");
        });

        FXManager.populateTableView(salesTable, FXManager.sale_columns,
                Arrays.asList("sale id", "customer id", "customerType"), data);
    }

    public static List<String> datesPickerValuesCollector(DatePicker startDatePicker, DatePicker endDatePicker) {

        String startDate = String.valueOf(startDatePicker.getValue());

        String endDate = String.valueOf(endDatePicker.getValue());

        if (!startDate.equals("null") || !endDate.equals("null"))

            return Arrays.asList(FXManager.convertDateFormat(startDate), FXManager.convertDateFormat(endDate));

        return null;

    }

    public static String datesFilterTypeGetter(List<String> values) {

        if (values.get(0) != null) {

            if (values.get(1) != null)
                return "between";

            else
                return "after";
        }

        else {

            if (values.get(1) != null)
                return "before";
        }

        return null;
    }

    public static boolean validFilter(List criteria) {

        for (Object criterion : criteria) {

            if (criterion != null)
                return true;
        }

        return false;
    }

    public static void criteriaSelector(List<String> filterInput) {

        filterInput.forEach(input -> {

            if (!input.equals("null")) {

            }
        });

    }

    public static String getOperationValue(String operation) {

        if (operation.equals("validate")) {

            return "VALIDATED";
        }

        else if (operation.equals("cancel")) {

            return "CANCELED";
        }

        else if (operation.equals("complete")) {

            return "COMPLETED";
        }

        throw new RuntimeException("Unvalid operation name.");

    }

    public static boolean operationValidator(String operation) {

        return operation.equalsIgnoreCase("purchase") || operation.equalsIgnoreCase("sale");
    }

    public static List<List<String>> operationSearchFilter(String name, String status, List<String> dates_values,
            String operation) {

        Map<String, Object> filter_map = new HashMap<String, Object>();

        String role = operation.equals("Purchase") ? "supplier" : "customer";

        if (dates_values != null) {

            String date_filter_type = PurchaseFX.datesFilterTypeGetter(dates_values);

            Map<String, Object> dates_filter_map = PurchaseParser.purchasesDatesFilterMapGenerator(date_filter_type,
                    dates_values);

            filter_map.put("date", dates_filter_map);

        }

        if (name != null) {

            String roleType = ContactDto.getContactType(name);

            Map<String, Object> roleMap = SaleParser.salesCustomerFilterMapGenerator(name, roleType);

            filter_map.put(role, roleMap);
        }

        if (status != null)
            filter_map.put("status", status);

        List<List<String>> operations = operation.equals("Purchase")
                ? PurchaseDto.purchasesFilter(Parser.jsonGenerator(filter_map))
                :

                SaleDto.salesFilter(Parser.jsonGenerator(filter_map));

        return operations;

    }

    ///////////////////////////////////////////////// OPERATION CREATION
    ///////////////////////////////////////////////// //////////////////////////////////////////////////////////////

    /// Products Selection FX
    public static void productSelectionSearchHandler(Button search, TableView productsTable,
            List<ComboBox> comboBoxes) {

        ComboBox categoryComboBox = comboBoxes.get(0); // We get
        ComboBox brandComboBox = comboBoxes.get(1); // the combo boxes
        ComboBox modelComboBox = comboBoxes.get(2); // from the list

        search.setOnAction(event -> {

            // We collect the entered id (we suppose it's a number)
            String categroyInput = (String) categoryComboBox.getValue();
            String brandInput = (String) brandComboBox.getValue();
            String modelInput = (String) modelComboBox.getValue();

            List<String> values = Arrays.asList(categroyInput, brandInput, modelInput);

            String json = ProductCrud.filteredProductSearchJsonGenerator(values);

            if (ProductCrud.productSearchValidator(comboBoxes)) {

                // We get the products that match the filter criteria
                List<List<String>> data = InventoryDto.getFilteredItems(json);

                if (!data.isEmpty()) { // if there are matching products

                    // We fill the products table with the matching products
                    InventoryItemCrud.fillWithFilteredItemsForPurchase(productsTable, data);

                }

                // if no product corresponds to the provided ref we show an error alert
                else
                    FXManager.showAlert(AlertType.ERROR, "ERROR", "Items Not Found",
                            " No existing items match the given criteria.");
            }

            // if the text field is empty and the search button is clicked
            else
                FXManager.showAlert(AlertType.ERROR, "ERROR", "No Selected Parameter",
                        "Please provide some parameters for the search.");
        });
    }

    public static void productSelectionSavingHandler(TableView productsTable, HBox addHbox, TextField quantityTextField,

            Button addOrder, AnchorPane cart, ImageView itemNumberImg, ImageView cartImg,
            List<Map<String, Object>> orders) {

        productsTable.setOnMouseClicked(event -> {

            Label sliderValueLabel = FXManager.getLabel(addHbox, "profitValueLabel");

            if (!productsTable.getSelectionModel().isEmpty()) {

                List<String> selectedItem = (List<String>) productsTable.getSelectionModel().getSelectedItem();

                String itemId = selectedItem.get(0);

                String time = formatTime(LocalTime.now()); // a method that formats the time in a hh:mm:ss format

                String orderTime = String.valueOf(time);

                addHbox.setDisable(false);

                cart.setVisible(true);

                addOrder.setOnAction(add_event -> {

                    String quantity = quantityTextField.getText();

                    if (!quantity.equals("")) {

                        Map<String, Object> orderMap = new HashMap<String, Object>();

                        orderMap.put("itemId", itemId);

                        orderMap.put("quantity", quantity);

                        orderMap.put("orderTime", orderTime);

                        orders.add(orderMap);

                        // if it's a sale operation
                        if (sliderValueLabel != null) {

                            String profitString = sliderValueLabel.getText();
    
                            orderMap.put("profitMargin", SaleFX.percentageCorrector(profitString));
                        }

                        quantityTextField.setText("");

                        quantityTextField.setPromptText("enter a quantity");

                        itemNumberImgChanger(itemNumberImg, orders);

                        addHbox.setDisable(true);

                    }

                    else
                        FXManager.showAlert(AlertType.ERROR, "ERROR", "Missing Quantity", "Please enter a quantity.");
                });
            }
        });

        cartImg.setOnMouseClicked(event -> {

            PurchaseCrud.viewCurentOrders(orders);
        });

    }

    public static void itemNumberImgChanger(ImageView imageView, List<Map<String, Object>> orders) {

        int size = orders.size();

        String path = "/uiass/eia/gisiba/imgs/cart-numbers/number-" + size + ".png";

        Image image = new Image(path);

        imageView.setImage(image);
    }

    public static String formatTime(LocalTime time) {

        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();

        // Format the time components
        String formattedTime = String.format("%02d:%02d:%02d", hour, minute, second);

        return formattedTime;
    }

////////////////////////////////////////////// Operation Creation FX ////////////////////////////////////////////////////////////

    /// Operation Confirmation FX

    public static void ordersTableFiller(VBox ordersVBox, List<Map<String, Object>> ordersList) {

        // We get the orders table view from the orders HBox
        TableView ordersTable = FXManager.getTableView(ordersVBox, "ordersTableView");

        // The list that will contain the orders we'll use to fill the table
        List<List<String>> orders = new ArrayList<List<String>>();

        // The columns we'll use for the orders table
        List<String> columns = Arrays.asList("category", "product", "quantity", "unit price", "order time");

        // We iterate over each order (map) in the list
        ordersList.forEach(order -> {

            ///// Here we get the new order's info from the map /////
            String itemId = (String) order.get("itemId");

            String quantity = (String) order.get("quantity");

            String orderTime = (String) order.get("orderTime");
            ////////////////////////////////////////////////////////////

            // We use the item's id to get the full item object from the back end
            List<String> item = InventoryDto.getItemById(Integer.parseInt(itemId));

            String category = item.get(1); /// here we get

            String brand = item.get(2); /// the info we want

            String model = item.get(3); /// to display from

            String name = item.get(4); /// from the item list (object)

            String unitPrice = item.get(5) + "$";

            String product = brand + " " + model + " " + name; // this time we want to display the whole product as one
                                                               // column

            orders.add(Arrays.asList(category, product, quantity, unitPrice, orderTime)); // we add the order to the
                                                                                          // orders list

        });

        // Finally we populate the orders table, this time no need for exclusions
        FXManager.populateTableView(ordersTable, columns, null, orders);
    }

    public static List<String> roleAttributesSelector(List<String> role) {

        // We extract each attribute's value from the retrieved contact
        String firstAttribute = role.get(1);
        String secondAttribute = role.get(2);
        String phoneNumber = role.get(3);
        String email = role.get(4);
        String houseNumber = role.get(6);
        String neighborhood = role.get(7);
        String city = role.get(8);
        String zipCode = role.get(9);
        String country = role.get(10);
        String address = houseNumber + " " + neighborhood + " " +

                city + " " + zipCode + " " + country;

        return Arrays.asList(firstAttribute, secondAttribute, phoneNumber, email, address);
    }

    public static void roleHBoxHandler(String roleType, String roleName, HBox roleHbox) {

        // we set the labels ids to get them from the Hbox
        List<String> labelsIds = FXManager.labels_ids_per_contact_type_map.get(roleType);

        // we collect the labels from the hbox using their ids
        List<Label> labels = FXManager.labelsCollector(roleHbox, labelsIds);

        // we get the role object (list) from the back end using the role's name and
        // type
        List<String> role = ContactDto.getContactByName(roleName, roleType);

        // here we select only the values we want to display
        List<String> roleAttributesToShow = roleAttributesSelector(role);

        // a method that dynamically fills a contact's labels given the contact's name
        // and type
        ContactCrud.contactLabelsFiller(labels, roleAttributesToShow, roleType);

    }

    // a method that simply gets the total amount of an operation
    public static String getOperationTotalAmmount(List<Map<String, Object>> orders) {

        double total = 0;

        for (Map<String, Object> orderMap : orders) {

            String itemId = (String) orderMap.get("itemId");

            String quantityString = (String) orderMap.get("quantity");

            List<String> item = InventoryDto.getItemById(Integer.parseInt(itemId));

            String unitPriceString = item.get(5);

            double unitPrice = Double.parseDouble(unitPriceString);

            int quantity = Integer.parseInt(quantityString);

            total += quantity * unitPrice;

        }

        total = Math.round(total * 100) / 100;

        return String.valueOf(total);
    }

    public static void operationConfirmingDialogBoxHandler(Map<String, Object> operationMap, String roleType, String operation) {

        // We ask the user for what to set as the purchase's status :
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Awaiting");
        alert.setHeaderText(operation + " Status");
        alert.setContentText("By confirming, a new pending " +  operation.toLowerCase() + " will be created." +
        
        " Do you want to directly validate the " + operation.toLowerCase() + " ?");
                    
        // Add "Yes" and "No" buttons
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                    
        // Show the dialog and wait for user input
        ButtonType result = alert.showAndWait().orElse(null);

        if (result == buttonTypeYes) {

            // Set the purchase status as "VALIDATED" if the user clicked "Yes"
            yesButtonTypeClickedHandler(operationMap, roleType, operation);
        }

        else if (result == buttonTypeNo) {

            // Set the purchase status as "PENDING" if the user clicked "No"
            operationMap.put("status", "PENDING");

            String json = Parser.jsonGenerator(operationMap);

            json = removeBackslashesAndQuotes(json);

            String creationResult = operation.equals("Purchase") ? PurchaseDto.postPurchase(json, roleType) :
            
            SaleDto.postSale(json, roleType);

            if (creationResult.equals(operation + " created successfully")) 
  
            FXManager.showAlert(AlertType.INFORMATION, operation + " Creation", "Creation Report", "A new " + 
            
            " validated " + operation.toLowerCase() + " was successfully created. You can manually validate it in the monitor page.");

            else FXManager.showAlert(AlertType.ERROR, operation + " Creation", "Creation Report", creationResult);
        }
    }

    public static void yesButtonTypeClickedHandler(Map<String, Object> operationMap, String roleType, String operation) {

        // We warn the user about the confirmation's consequences :
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(operation + " State");
        alert.setContentText("By confirming, the " + operation.toLowerCase() + " will be validated and you will no longer be able to" + 
        
        " modify it, are you sure you want to proceed ?");
                    
        // Add "Yes" and "No" buttons
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                    
        // Show the dialog and wait for user input
        ButtonType result = alert.showAndWait().orElse(null);

        if (result == buttonTypeYes) {

            // Set the purchase status as "VALIDATED" if the user clicked "Yes"
            operationMap.put("status", "VALIDATED");

            String json = Parser.jsonGenerator(operationMap);

            json = removeBackslashesAndQuotes(json);

            String creationResult = operation.equals("Purchase") ? PurchaseDto.postPurchase(json, roleType) :
            
            SaleDto.postSale(json, roleType);

            if (creationResult.equals(operation + " created successfully")) 
  
            FXManager.showAlert(AlertType.INFORMATION, operation + " Creation", "Creation Report", "A new " + 
            
            " validated " + operation.toLowerCase() + " was successfully created.");

            else FXManager.showAlert(AlertType.ERROR, operation + " Creation", "Creation Report", creationResult);
        }

        else if (result == buttonTypeNo) {

            // Set the purchase status as "PENDING" if the user clicked "No"
            operationMap.put("status", "PENDING");

            String json = Parser.jsonGenerator(operationMap);

            json = removeBackslashesAndQuotes(json);

            String creationResult = PurchaseDto.postPurchase(json, roleType);

            if (creationResult.equals(operation + " created successfully")) 
  
            FXManager.showAlert(AlertType.INFORMATION, operation + " Creation", "Creation Report", "A new " + 
            
            " pending " + operation.toLowerCase() + " was successfully created. You can manually validate it in the monitor page.");

            else FXManager.showAlert(AlertType.ERROR, operation + " Creation", "Creation Report", creationResult);
        }
    }

    public static String removeBackslashesAndQuotes(String json) {
        // Remove backslashes
        String cleanedJson = json.replaceAll("\\\\", "");
        
        // Remove double quotes before and after "orders" list brackets
        cleanedJson = cleanedJson.replaceAll("\"(\\[.*?\\])\"", "$1");
        
        return cleanedJson;
    }
}
