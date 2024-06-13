package uiass.eia.gisiba.FX;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uiass.eia.gisiba.controller.FXManager;
import uiass.eia.gisiba.crud.ContactCrud;
import uiass.eia.gisiba.crud.InventoryItemCrud;
import uiass.eia.gisiba.crud.OrderCrud;
import uiass.eia.gisiba.crud.ProductCrud;
import uiass.eia.gisiba.crud.PurchaseCrud;
import uiass.eia.gisiba.http.dto.ContactDto;
import uiass.eia.gisiba.http.dto.InventoryDto;
import uiass.eia.gisiba.http.dto.OrderDto;
import uiass.eia.gisiba.http.dto.PurchaseDto;
import uiass.eia.gisiba.http.parsers.Parser;

public class PurchaseFX extends OperationFX {


    public static void comboBoxesHandler(ComboBox supplierComboBox, ComboBox statusComboBox) {

        List<String> statusList = Arrays.asList("PENDING", "VALIDATED","COMPLETED","CANCELED");

        supplierComboBoxFiller(supplierComboBox);
        
        FXManager.populateComboBox(statusComboBox, statusList);
    }

    public static void supplierComboBoxFiller(ComboBox supplierComboBox) {

        // We get all the suppliers 
        List<List<String>> personSuppliers = PurchaseDto.getAllSuppliersByType("Person");
        List<List<String>> enterpriseSuppliers = PurchaseDto.getAllSuppliersByType("Enterprise");


        List<String> suppliersNames = new ArrayList<String>();

        enterpriseSuppliers.forEach(supplier -> {

            // we form the supplier's name
            String supplierName = supplier.get(1);

            // we add the supplier's name to the list
            suppliersNames.add(supplierName);

        });

        personSuppliers.forEach(supplier -> {

            // we form the supplier's name
            String supplierName = supplier.get(1) + " " + supplier.get(2);

            // we add the supplier's name to the list
            suppliersNames.add(supplierName);

        });

        // finally we populate the supplier combo box
        FXManager.populateComboBox(supplierComboBox, suppliersNames);
    }


    public static ContextMenu operationSetter(ContextMenu menu, TableView purchasesTable, String operation) {

        MenuItem validate = new MenuItem(operation);

        String operationValue = getOperationValue(operation);

        validate.setOnAction(event -> {

            // We warn the user about the confirmation's consequences :
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Purchase Status Update");
            alert.setContentText("You're about to " + operation + " this purchase, are you sure you want to proceed ?");
                    
            // Add "Yes" and "No" buttons
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                    
            // Show the dialog and wait for user input
            ButtonType result = alert.showAndWait().orElse(null);

            if (result == buttonTypeYes) {

                @SuppressWarnings("unchecked")
                List<String> purchase = (List<String>) purchasesTable.getSelectionModel().getSelectedItem();

                int purchaseId = Integer.parseInt(purchase.get(0));

                Map<String,Object> map = Map.of("status", operationValue); 

                String json = Parser.jsonGenerator(map);

                String updateResult = PurchaseDto.updatePurchaseStatus(purchaseId, json);

                if (updateResult.equals("Purchase status updated successfully")) {

                    FXManager.showAlert(AlertType.CONFIRMATION, "Confirmation", "Status Update", "Purchase status" +
                
                    " successfully updated to " + operationValue.toLowerCase() + ".");

                }

                else FXManager.showAlert(AlertType.ERROR, "Error", "Status Update", updateResult);

                }

 

        });

        menu.getItems().add(validate);

        return menu;
    }

    public static void editOrdersTableFiller(TableView ordersTable, String purchaseId) {

        List<List<String>> orders = OrderDto.getAllOrdersByPurchase(Integer.parseInt(purchaseId));

        FXManager.populateTableView(ordersTable, FXManager.order_columns_per_operation_type.get("purchase"), Arrays.asList("order id", "itemid"), orders);
    }

    public static void purchaseOrdersTableHandler(TableView ordersTable, Parent pane, String purchaseId) {

        HBox editHbox = FXManager.getHBox(pane, "editHbox");

        // Button
        Button update = FXManager.getButton(pane, "updateQuantityBtn");

        // Text Field
        TextField quantityTextField = FXManager.getTextField(pane, "quantityTextField");
        FXManager.setTextFieldNumericFormatRule(quantityTextField);

        ordersTable.setOnMouseClicked(event -> {

            if (!ordersTable.getSelectionModel().isEmpty()) {

                List<String> order = (List<String>) ordersTable.getSelectionModel().getSelectedItem();

                int orderId = Integer.parseInt(order.get(0));

                editHbox.setDisable(false);

                update.setOnAction(update_event -> {

                    String quantity = quantityTextField.getText();

                    if (!quantity.equals("")) {

                        Map<String, Object> map = Map.of("quantity", quantity);

                        String json = Parser.jsonGenerator(map);

                        OrderDto.updateOrder(json, orderId);

                        editOrdersTableFiller(ordersTable, purchaseId);

                        editHbox.setDisable(true);
                    }

                    else FXManager.showAlert(AlertType.WARNING, "Error", "No quantity provided", "Please provide a quantity.");

                });

            }
        });
    }
    public static void editPurchaseOrders(TableView purchasesTable, String purchaseId) {

        // We create the stage that will contain the creation page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);

        // here we load the creation page fxml file

        String path = "/uiass/eia/gisiba/purchase/update_purchase_orders_pane.fxml";
        FXManager.loadFXML(path, pane, PurchaseCrud.class); 

        // We call the method that handles the creation
        PurchaseCrud.editPurchaseOrdersPane(pane, purchaseId);
        
        // We add the stage info and show it
        String iconPath = "/uiass/eia/gisiba/imgs/carts.png";
        InputStream inputStream = PurchaseCrud.class.getResourceAsStream(iconPath);
        Image icon = new Image(inputStream);

        stage.setScene(scene);
        stage.setTitle("Update Orders");
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.show();
    }

    public static void editPurchaseOperationSetter(ContextMenu menu, TableView purchaseTable, String purchaseId) {

        MenuItem edit = new MenuItem("edit");

        edit.setOnAction(event -> editPurchaseOrders(purchaseTable, purchaseId));

        menu.getItems().add(edit);
    }

    public static void purchaseTableContextMenuHandler(ContextMenu menu, TableView purchasesTable, String status) {

        menu.getItems().clear(); // Clear existing menu items

        if (!status.equals("COMPLETED") && !status.equals("CANCELED")) {

            if (status.equals("PENDING")) {

                List<String> purchase = (List<String>) purchasesTable.getSelectionModel().getSelectedItem();

                String purchaseId = purchase.get(0);

                editPurchaseOperationSetter(menu, purchasesTable, purchaseId);
    
                operationSetter(menu, purchasesTable, "validate");
    
            }
    
            else if (status.equals("VALIDATED")) {
    
                operationSetter(menu, purchasesTable, "complete");
            }
    
            operationSetter(menu, purchasesTable, "cancel");
        }
    }

    @SuppressWarnings("unchecked")
    public static void purchaseTableContextMenuAssociator(TableView purchasesTable) {

        ContextMenu menu = new ContextMenu();

        purchasesTable.setRowFactory(tv -> {

            TableRow<List<String>> row = new TableRow<>();

            row.setOnContextMenuRequested(event -> {

                if (!row.isEmpty()) {

                    List<String> purchase = row.getItem();

                    String status = purchase.get(6);

                    purchaseTableContextMenuHandler(menu, purchasesTable, status);

                    menu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });
    }

    public static void showPurchaseSupplier(int supplierId, String supplierType, HBox pane) {

        String path = "/uiass/eia/gisiba/purchase/" + supplierType.toLowerCase() + "_supplier_pane.fxml";

        FXManager.loadFXML(path , pane, PurchaseFX.class);
        
        List<Label> labels = FXManager.labelsCollector(pane, FXManager.labels_ids_per_contact_type_map.get(supplierType));

        List<String> supplier = ContactDto.getContactById(supplierId, supplierType);

        String firstAttribute = supplier.get(1);
        String secondAttribute = supplier.get(2);
        String phoneNumber = supplier.get(3);
        String email = supplier.get(4);
        String houseNumber = supplier.get(6);
        String neighborhood = supplier.get(7);
        String city = supplier.get(8);
        String zipCode = supplier.get(9);
        String country = supplier.get(10);

        String address = houseNumber + " " + neighborhood + " " +
        
        city + " " + zipCode + " " + country; // We formulate the full address using its id

        // We put all the values in one list that we'll use to fill the labels
        List<String> values = Arrays.asList(firstAttribute,secondAttribute,phoneNumber,email,address);

        // We use the extracted values to fill the labels
        ContactCrud.contactLabelsFiller(labels, values, supplierType);

        // We finally show the right pane
        pane.setVisible(true);

    }

    public static void showPurchaseOrders(int purchaseId, Parent pane) {

        TableView ordersTable = FXManager.getTableView(pane, "ordersTable");

        List<List<String>> orders_by_purchase = OrderDto.getAllOrdersByPurchase(purchaseId);

        orders_by_purchase.forEach(order -> {

            String unitPrice = order.get(6);

            order.set(6, unitPrice + "$");
        });

        OrderCrud.fillWithFilteredPurchasedOrders(ordersTable, orders_by_purchase);

        pane.setVisible(true);

    }

////////////////////////////////////////////// Purchase Creation FX ///////////////////////////////////////////////////////



/// Supplier Selection FX

    public static void fillWithSuppliersByType(TableView suppliersTable, String supplierType) {

        List<List<String>> suppliers = PurchaseDto.getAllSuppliersByType(supplierType);

        List<String> columns = FXManager.columns_names_per_contact_type.get(supplierType);

        FXManager.populateTableView(suppliersTable, columns, Arrays.asList("id","address id"), suppliers);
    }

    public static void fillTypeComboBox(ComboBox typeComboBox) {

        FXManager.populateComboBox(typeComboBox, Arrays.asList("Person", "Enterprise"));
    }

    public static void supplierSelectionPaneButtonsHandler(TableView suppliersTable, Button search, AnchorPane refresh, Button next,
    
    ComboBox supplierTypeComboBox, TextField enterNameTextField, TextField selectedSupplier, List<Map<String,Object>> orders) {

        search.setOnAction(event -> {

            String type = (String) supplierTypeComboBox.getValue();

            if (type != null) {

                String name = enterNameTextField.getText();

                if (!name.equals("")) {
    
                    List<String> supplier = ContactDto.getContactByName(name, type);

                    if (supplier != null) {

                        selectedSupplier.setText(name);

                        next.setOnAction(next_event -> {
        
                            ((Stage) supplierTypeComboBox.getScene().getWindow()).close(); // We close the products selection page after pressing the next button
                            
                            PurchaseCrud.goToPurchaseSummaryPane(type, name, orders);
                
                        });
                    }

                    else FXManager.showAlert(AlertType.ERROR, "ERROR", "Contact Not Found", name + " doesn't correspond to any existing contact.");
                }

                else FXManager.showAlert(AlertType.ERROR, "ERROR", "Empty Name Field", "Please provide a supplier name.");
            }

            else FXManager.showAlert(AlertType.ERROR, "ERROR", "No Type Selected", "Please select a supplier type.");
        });

        suppliersTable.setOnMouseClicked(event -> {

            if (!suppliersTable.getSelectionModel().isEmpty()) {

                String type = (String) supplierTypeComboBox.getValue();

                List<String> selectedItem = (List<String>) suppliersTable.getSelectionModel().getSelectedItem();

                String name = selectedItem.get(1);

                if (type.equals("Person")) name += " " + selectedItem.get(2);

                selectedSupplier.setText(name);

                next.setOnAction(next_event -> {
        
                    ((Stage) supplierTypeComboBox.getScene().getWindow()).close(); // We close the products selection page after pressing the next button
                    
                    PurchaseCrud.goToPurchaseSummaryPane(type, selectedSupplier.getText(), orders);
        
                });
            }
        });

        refresh.setOnMouseClicked(event -> {

            String type = (String) supplierTypeComboBox.getValue();

            if (type != null) {

                fillWithSuppliersByType(suppliersTable, type);
            }

            else FXManager.showAlert(AlertType.ERROR, "ERROR", "No Type Selected", "Please select a supplier type.");
        });

        next.setOnAction(event -> {

            String type = (String) supplierTypeComboBox.getValue();

            if (type == null) {

                FXManager.showAlert(AlertType.ERROR, "ERROR", "No Type Selected", "Please select a supplier type.");
        
            }

            else {

                String selected = selectedSupplier.getText();

                if (selected.equals("")) {

                    FXManager.showAlert(AlertType.ERROR, "ERROR", "No Supplier Selected", "Please select a supplier.");

                }
            }
            
        });
    }

////////////////////////////////////////////// Purchase Creation FX ///////////////////////////////////////////////////////

/// Purchase Confirmation FX

    public static void purchaseConfirmingLabelsHandler(String supplierType, String supplierName, String total, VBox leftVbox) {

        HBox supplierHbox = FXManager.getHBox(leftVbox, "supplierHBox");

        roleHBoxHandler(supplierType, supplierName, supplierHbox);

        Label purchaseDateLabel = FXManager.getLabel(leftVbox, "purchaseDateLabel");

        Label purchaseAmountLabel = FXManager.getLabel(leftVbox, "purchaseAmountLabel");

        // the current date
        String currentDate = String.valueOf(Date.valueOf(LocalDate.now()));

        // we set the date to the date label
        purchaseDateLabel.setText(currentDate);

        // we set the total to the amount label
        purchaseAmountLabel.setText(total + "$");

    }
    

}
