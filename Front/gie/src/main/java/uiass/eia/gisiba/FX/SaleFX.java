package uiass.eia.gisiba.FX;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
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
import uiass.eia.gisiba.crud.OrderCrud;
import uiass.eia.gisiba.crud.PurchaseCrud;
import uiass.eia.gisiba.crud.SaleCrud;
import uiass.eia.gisiba.http.dto.ContactDto;
import uiass.eia.gisiba.http.dto.InventoryDto;
import uiass.eia.gisiba.http.dto.OrderDto;
import uiass.eia.gisiba.http.dto.PurchaseDto;
import uiass.eia.gisiba.http.dto.SaleDto;
import uiass.eia.gisiba.http.parsers.Parser;
import uiass.eia.gisiba.http.parsers.PurchaseParser;
import uiass.eia.gisiba.http.parsers.SaleParser;

public class SaleFX extends OperationFX {

///////////////////////////////////////////////// TABLE HANDLING //////////////////////////////////////////////////////////////////

    public static void fillWithSales(TableView salesTable) {

        List<List<String>> data = SaleDto.getAllSales();

        data.forEach(sale -> {

            String unitPrice = sale.get(5);

            sale.set(5, unitPrice + "$");
        });

        FXManager.populateTableView(salesTable, FXManager.sale_columns, Arrays.asList("sale id", "customer id", "customerType"), data);
    }

    public static void fillWithFilteredSales(TableView salesTable, List<List<String>> data) {

        data.forEach(sale -> {

            String unitPrice = sale.get(5);

            sale.set(5, unitPrice + "$");
        });

        FXManager.populateTableView(salesTable, FXManager.sale_columns, Arrays.asList("sale id", "customer id", "customerType"), data);
    }

    @SuppressWarnings("unchecked")
    public static void salesTableHandler(TableView saleTable, Parent pane, Button create) {

        HBox customerHbox = FXManager.getHBox(pane, "customerHbox");

        HBox ordersHbox = FXManager.getHBox(pane, "ordersHbox");

        saleTable.setOnMouseClicked(event -> {

            if (!saleTable.getSelectionModel().isEmpty()) {

                // We get the selected row and extract the values
                List<String> selectedItem = (List<String>) saleTable.getSelectionModel().getSelectedItem();

                int saleId = Integer.parseInt(selectedItem.get(0));

                int customerId = Integer.parseInt(selectedItem.get(1));
                
                String customerType = selectedItem.get(3);

                showSaleCustomer(customerId, customerType, customerHbox);

                showSaleOrders(saleId, ordersHbox);

            }
        });

        
    }
    
    public static List<List<String>> saleSearchFilter(String supplierName, String status, List<String> dates_values) {

        Map<String,Object> filter_map = new HashMap<String,Object>();

        if (dates_values != null) {

            String date_filter_type = datesFilterTypeGetter(dates_values);

            Map<String,Object> dates_filter_map = SaleParser.salesDatesFilterMapGenerator(date_filter_type, dates_values);
    
            filter_map.put("date", dates_filter_map);

        }

        if (supplierName != null) {

            String supplierType = ContactDto.getContactType(supplierName);

            Map<String,Object> supplierMap = PurchaseParser.purchasesSupplierFilterMapGenerator(supplierName, supplierType);

            filter_map.put("supplier", supplierMap);
        }

        if (status != null) filter_map.put("status", status);

        List<List<String>> purchases = PurchaseDto.purchasesFilter(Parser.jsonGenerator(filter_map));

        return purchases;
        
    }
///////////////////////////////////////////////// COMBO BOXES HANDLING ////////////////////////////////////////////////////////////

    public static void comboBoxesHandler(ComboBox supplierComboBox, ComboBox statusComboBox) {

        List<String> statusList = Arrays.asList("PENDING", "VALIDATED","COMPLETED","CANCELED");

        customerComboBoxFiller(supplierComboBox);
        
        FXManager.populateComboBox(statusComboBox, statusList);
    }

    public static void customerComboBoxFiller(ComboBox supplierComboBox) {

        // We get all the suppliers 
        List<List<String>> personSuppliers = SaleDto.getAllCustomersByType("Person");
        List<List<String>> enterpriseSuppliers = SaleDto.getAllCustomersByType("Enterprise");


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

///////////////////////////////////////////// CONTEXT MENU HANDLING ////////////////////////////////////////////////////////

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
    
    public static ContextMenu operationSetter(ContextMenu menu, TableView salesTable, String operation) {

        MenuItem validate = new MenuItem(operation);

        String operationValue = getOperationValue(operation);

        validate.setOnAction(event -> {

            // We warn the user about the confirmation's consequences :
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Sale Status Update");
            alert.setContentText("You're about to " + operation + " this sale, are you sure you want to proceed ?");
                    
            // Add "Yes" and "No" buttons
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                    
            // Show the dialog and wait for user input
            ButtonType result = alert.showAndWait().orElse(null);

            if (result == buttonTypeYes) {

                @SuppressWarnings("unchecked")
                List<String> sale = (List<String>) salesTable.getSelectionModel().getSelectedItem();

                int saleId = Integer.parseInt(sale.get(0));

                Map<String,Object> map = Map.of("status", operationValue); 

                String json = Parser.jsonGenerator(map);

                String updateResult = SaleDto.updateSaleStatus(saleId, json);

                if (updateResult.equals("Sale status updated successfully")) {

                    FXManager.showAlert(AlertType.CONFIRMATION, "Confirmation", "Status Update", "sale status" +
                
                    " successfully updated to " + operationValue.toLowerCase() + ".");

                }

                else FXManager.showAlert(AlertType.ERROR, "Error", "Status Update", updateResult);

                }

        });

        menu.getItems().add(validate);

        return menu;
    }

    public static void editSaleOperationSetter(ContextMenu menu, TableView salesTable, String saleId) {

        MenuItem edit = new MenuItem("edit");

        edit.setOnAction(event -> editSaleOrders(salesTable, saleId));

        menu.getItems().add(edit);
    }

    public static void salesTableContextMenuHandler(ContextMenu menu, TableView salesTable, String status) {

        menu.getItems().clear(); // Clear existing menu items

        if (!status.equals("COMPLETED") && !status.equals("CANCELED")) {

            if (status.equals("PENDING")) {

                List<String> sale = (List<String>) salesTable.getSelectionModel().getSelectedItem();

                String saleId = sale.get(0);

                editSaleOperationSetter(menu, salesTable, saleId);
    
                operationSetter(menu, salesTable, "validate");
    
            }
    
            else if (status.equals("VALIDATED")) {
    
                operationSetter(menu, salesTable, "complete");
            }
    
            operationSetter(menu, salesTable, "cancel");
        }
    }

    @SuppressWarnings("unchecked")
    public static void saleTableContextMenuAssociator(TableView salesTable) {

        ContextMenu menu = new ContextMenu();

        salesTable.setRowFactory(tv -> {

            TableRow<List<String>> row = new TableRow<>();

            row.setOnContextMenuRequested(event -> {

                if (!row.isEmpty()) {

                    List<String> sale = row.getItem();

                    String status = sale.get(6);

                    salesTableContextMenuHandler(menu, salesTable, status);

                    menu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });
    }

///////////////////////////////////////////////////// PANES DISPLAY ////////////////////////////////////////////////////////

    public static void showSaleCustomer(int customerId, String customerType, HBox pane) {

        String path = "/uiass/eia/gisiba/sale/" + customerType.toLowerCase() + "_customer_pane.fxml";

        FXManager.loadFXML(path , pane, PurchaseFX.class);
        
        List<Label> labels = FXManager.labelsCollector(pane, FXManager.labels_ids_per_contact_type_map.get(customerType));

        List<String> customer = ContactDto.getContactById(customerId, customerType);

        String firstAttribute = customer.get(1);
        String secondAttribute = customer.get(2);
        String phoneNumber = customer.get(3);
        String email = customer.get(4);
        String houseNumber = customer.get(6);
        String neighborhood = customer.get(7);
        String city = customer.get(8);
        String zipCode = customer.get(9);
        String country = customer.get(10);

        String address = houseNumber + " " + neighborhood + " " +
        
        city + " " + zipCode + " " + country; // We formulate the full address using its id

        // We put all the values in one list that we'll use to fill the labels
        List<String> values = Arrays.asList(firstAttribute,secondAttribute,phoneNumber,email,address);

        // We use the extracted values to fill the labels
        ContactCrud.contactLabelsFiller(labels, values, customerType);

        // We finally show the right pane
        pane.setVisible(true);
    }

    public static void showSaleOrders(int saleId, HBox pane) {
        
        TableView ordersTable = FXManager.getTableView(pane, "ordersTable");

        List<List<String>> orders_by_sale = OrderDto.getAllOrdersBySale(saleId);

        orders_by_sale.forEach(order -> {

            String unitPrice = order.get(6);

            order.set(6, unitPrice + "$");
        });

        OrderCrud.fillWithFilteredSaleOrders(ordersTable, orders_by_sale);

        pane.setVisible(true);
    }


    public static void editSaleOrders(TableView salesTable, String saleId) {

        // We create the stage that will contain the creation page
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        Scene scene = new Scene(pane);

        // here we load the creation page fxml file

        String path = "/uiass/eia/gisiba/sale/update_sale_orders_pane.fxml";
        FXManager.loadFXML(path, pane, SaleFX.class); 

        // We call the method that handles the creation
        SaleCrud.editSaleOrdersPane(pane, saleId);
        
        // We add the stage info and show it
        String iconPath = "/uiass/eia/gisiba/imgs/carts.png";
        InputStream inputStream = SaleFX.class.getResourceAsStream(iconPath);
        Image icon = new Image(inputStream);

        stage.setScene(scene);
        stage.setTitle("Update Orders");
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.show();
    }

///////////////////////////////////////////////////// SALE CREATION ////////////////////////////////////////////////////////

    public static String getSaleTotalAmmount(List<Map<String,Object>> orders) {

        double total = 0;

        for (Map<String,Object> orderMap : orders) {

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

    public static void saleConfirmingLabelsHandler(String customerType, String customerName, String total, VBox leftVbox) {

        HBox customerHbox = FXManager.getHBox(leftVbox, "customerHBox");

        roleHBoxHandler(customerType, customerName, customerHbox);

        Label saleDateLabel = FXManager.getLabel(leftVbox, "saleDateLabel");

        Label saleAmountLabel = FXManager.getLabel(leftVbox, "saleAmountLabel");

        // the current date
        String currentDate = String.valueOf(Date.valueOf(LocalDate.now()));

        // we set the date to the date label
        saleDateLabel.setText(currentDate);

        // we set the total to the amount label
        saleAmountLabel.setText(total + "$");
    }

    public static void profitMarginSliderHandler(HBox addHbox) {

        Slider profitSlider = FXManager.getSlider(addHbox, "profitMarginSlider");

        Label sliderValueLabel = FXManager.getLabel(addHbox, "profitValueLabel");

        // we make sure the slider only gives integer values
        profitSlider.setMajorTickUnit(1);

        profitSlider.setMinorTickCount(0);

        profitSlider.setBlockIncrement(1);

        // we add an event listener to the slider
        profitSlider.valueProperty().addListener((observable, oldValue, newValue) -> {

            String profit = newValue.toString().substring(0, 5);

            sliderValueLabel.setText(profit + "%");

        });

    }

//// Customer Selection

    public static void fillWithCustomersByType(TableView customersTable, String customerType) {

        List<List<String>> customers = SaleDto.getAllCustomersByType(customerType);

        List<String> columns = FXManager.columns_names_per_contact_type.get(customerType);

        FXManager.populateTableView(customersTable, columns, Arrays.asList("id","address id"), customers);
    }

    public static void fillTypeComboBox(ComboBox typeComboBox) {

        FXManager.populateComboBox(typeComboBox, Arrays.asList("Person", "Enterprise"));
    }

        
    public static void customerSelectionPaneButtonsHandler(TableView customersTable, Button search, AnchorPane refresh, Button next,
    
        ComboBox customerTypeComboBox, TextField enterNameTextField, TextField selectedCustomer, List<Map<String,Object>> orders) {

        search.setOnAction(event -> {

            String type = (String) customerTypeComboBox.getValue();

            if (type != null) {

                String name = enterNameTextField.getText();

                if (!name.equals("")) {
    
                    List<String> customer = ContactDto.getContactByName(name, type);

                    if (customer != null) {

                        selectedCustomer.setText(name);

                        next.setOnAction(next_event -> {
        
                            ((Stage) customerTypeComboBox.getScene().getWindow()).close(); // We close the products selection page after pressing the next button
                            
                            SaleCrud.goToSaleSummaryPane(type, name, orders);
                
                        });
                    }

                    else FXManager.showAlert(AlertType.ERROR, "ERROR", "Contact Not Found", name + " doesn't correspond to any existing contact.");
                }

                else FXManager.showAlert(AlertType.ERROR, "ERROR", "Empty Name Field", "Please provide a customer name.");
            }

            else FXManager.showAlert(AlertType.ERROR, "ERROR", "No Type Selected", "Please select a customer type.");
        });

        customersTable.setOnMouseClicked(event -> {

            if (!customersTable.getSelectionModel().isEmpty()) {

                String type = (String) customerTypeComboBox.getValue();

                List<String> selectedItem = (List<String>) customersTable.getSelectionModel().getSelectedItem();

                String name = selectedItem.get(1);

                if (type.equals("Person")) name += " " + selectedItem.get(2);

                selectedCustomer.setText(name);

                next.setOnAction(next_event -> {
        
                    ((Stage) customerTypeComboBox.getScene().getWindow()).close(); // We close the products selection page after pressing the next button
                    
                    SaleCrud.goToSaleSummaryPane(type, selectedCustomer.getText(), orders);
        
                });
            }
        });

        refresh.setOnMouseClicked(event -> {

            String type = (String) customerTypeComboBox.getValue();

            if (type != null) {

                fillWithCustomersByType(customersTable, type);
            }

            else FXManager.showAlert(AlertType.ERROR, "ERROR", "No Type Selected", "Please select a customer type.");
        });

        next.setOnAction(event -> {

            String type = (String) customerTypeComboBox.getValue();

            if (type == null) {

                FXManager.showAlert(AlertType.ERROR, "ERROR", "No Type Selected", "Please select a customer type.");
        
            }

            else {

                String selected = selectedCustomer.getText();

                if (selected.equals("")) {

                    FXManager.showAlert(AlertType.ERROR, "ERROR", "No customer Selected", "Please select a customer.");

                }
            }
            
        });
    }

    public static void fillSaleOrdersTable(TableView salesTable, String saleId) {

        List<List<String>> orders = OrderDto.getAllOrdersBySale(Integer.parseInt(saleId));

        orders.forEach(order -> {
            
            String profitMargin = order.get(9);

            order.set(9, profitMargin + "%");
        });

        FXManager.populateTableView(salesTable, FXManager.order_columns_per_operation_type.get("sale"), Arrays.asList("order id", "item id"), orders);
    }

    public static boolean validUpdateChecker(TextField quantity, Slider margin) {

        return (!quantity.getText().equals("") || margin.getValue() != 20);
    }

    public static void editOrdersTableHandler(TableView salesTable, String saleId, Parent pane) {

        // HBox
        HBox editHbox = FXManager.getHBox(pane, "editHbox");

        // Buttons
        Button update = FXManager.getButton(pane, "updateQuantityBtn");
        ImageView delete = FXManager.getImageView(pane, "deleteBtn");

        // TextField
        TextField quantityTextField = FXManager.getTextField(pane, "quantityTextField");

        // Label
        Label profitLabel = FXManager.getLabel(pane, "profitValueLabel");

        // Slider
        Slider profitMarginSlider = FXManager.getSlider(pane, "profitMarginSlider");
        profitMarginSliderHandler(editHbox); // we handle the slider's properties here

        // we fill the table with the corresponding sale's orders
        fillSaleOrdersTable(salesTable, saleId);

        salesTable.setOnMouseClicked(event -> {

            editHbox.setDisable(false);

            if (!salesTable.getSelectionModel().isEmpty()) {

                List<String> order = (List<String>) salesTable.getSelectionModel().getSelectedItem();

                int orderId = Integer.parseInt(order.get(0));

                delete.setOnMouseClicked(delete_event -> {

                    String result = OrderDto.delteOrder(orderId, "sale");

                    System.out.println(result);

                    fillSaleOrdersTable(salesTable, saleId);

                    editHbox.setDisable(true);
            
                });

                update.setOnAction(confirm_event -> {

                    if (validUpdateChecker(quantityTextField, profitMarginSlider)) {

                        Map<String,Object> map = new HashMap<String,Object>();

                        double profitMargin = profitMarginSlider.getValue();

                        String quantity = quantityTextField.getText();

                        if (quantity.equals("")) quantity = null;

                        map.put("quantity", quantity);
    
                        map.put("profitMargin", truncateToTwoDecimalPlaces(profitMargin));
    
                        String json = Parser.jsonGenerator(map);

                        System.out.println(json);
    
                        String updateResult = OrderDto.updateSaleOrder(json, orderId);
    
                        if (updateResult.equals("Order Updated Successfully")) {
    
                            fillSaleOrdersTable(salesTable, saleId);
    
                            quantityTextField.setText("");
    
                            profitMarginSlider.setValue(20);

                            profitLabel.setText("20");

                            editHbox.setDisable(true);
                        }

                        else FXManager.showAlert(AlertType.ERROR, "Error", "Internal Server Error", "an error has occured during this operation.");
                    }

                    else FXManager.showAlert(AlertType.ERROR, "Error", "No values provided", "Please provide some new values to update the order");
 
                });
            }
        });


    }

    public static String percentageCorrector(String percentage) {

        // Remove the percentage sign and parse the string to a double
        String numericPart = percentage.replace("%", "").trim();

        double value = Double.parseDouble(numericPart);

        // Divide by 100 to convert to a decimal value
        return String.valueOf(value / 100.0);
    }

    public static double truncateToTwoDecimalPlaces(double value) {

        return (double) (value * 100) / 100.0;
    }
    
}
