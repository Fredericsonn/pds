package uiass.gisiba.eia.java.controller;


import uiass.gisiba.eia.java.controller.crm.AddressController;
import uiass.gisiba.eia.java.controller.crm.ContactController;
import uiass.gisiba.eia.java.controller.inventory.CategoryController;
import uiass.gisiba.eia.java.controller.inventory.InventoryItemController;
import uiass.gisiba.eia.java.controller.inventory.ProductController;
import uiass.gisiba.eia.java.controller.operations.PurchaseController;
import uiass.gisiba.eia.java.controller.operations.PurchaseOrderController;
import uiass.gisiba.eia.java.controller.operations.SaleController;
import uiass.gisiba.eia.java.controller.operations.SaleOrderController;


public class AppTest {

    public static void main(String[] args) {
        
        ContactController.getContactByIdController();
        ContactController.getContactByNameController();
        ContactController.getAllContactsByTypeController();
        ContactController.getContactByAddressIdController();

        ContactController.postContactController();
        ContactController.updateContactController();
        ContactController.deleteContactController();

        ContactController.postEmailController();

        AddressController.updateAddressController();

        ProductController.getAllProducts();
        ProductController.getProductByRef();
        ProductController.deleteProductController();
        ProductController.updateProductController();
        ProductController.postProductController();
        ProductController.productSearchFilter();
        ProductController.checkForAssociatedPurchases();

        CategoryController.getAllCategories();
        CategoryController.getAllColumnNames();
        CategoryController.categorySearchFilter();
        CategoryController.updateCategoryController();
        CategoryController.postCategory();

        InventoryItemController.getAllItems();
        InventoryItemController.itemSearchFilter();
        InventoryItemController.getItemById();
        InventoryItemController.getItemByProduct();
        InventoryItemController.getItemQuantity();
        InventoryItemController.postItemController();
        InventoryItemController.deleteItemController();
        InventoryItemController.updateItemUnitPriceController();

        PurchaseOrderController.getOrderById();
        PurchaseOrderController.getAllOrders();
        PurchaseOrderController.getAllOrdersByPurchase();
        PurchaseOrderController.orderSearchFilter();
        PurchaseOrderController.updateOrder();
        PurchaseOrderController.deleteOrderController();

        PurchaseController.getAllPurchases();
        PurchaseController.getAllPurchasesBySupplierType();
        PurchaseController.getAllPurchasesByStatus();
        PurchaseController.getAllPurchasesBySupplier();
        PurchaseController.purchasesFilter();
        PurchaseController.getAllSuppliers();
        PurchaseController.getPurchaseById();
        PurchaseController.deletePurchaseController();
        PurchaseController.postPurchase();
        PurchaseController.updatePurchaseOrdersController();
        PurchaseController.updatePurchaseStatusController();
        PurchaseController.removePurchaseOrderController();

        SaleOrderController.getOrderById();
        SaleOrderController.getAllOrders();
        SaleOrderController.getAllOrdersBySale();
        SaleOrderController.orderSearchFilter();
        SaleOrderController.updateOrder();
        SaleOrderController.deleteOrderController();

        SaleController.getAllSales();
        SaleController.getAllSalesByCustomerType();
        SaleController.getAllSalesByStatus();
        SaleController.getAllSalesByCustomer();
        SaleController.getAllCustomers();
        SaleController.salesFilter();
        SaleController.getSaleById();
        SaleController.deleteSaleController();
        SaleController.postSale();
        SaleController.updateSaleOrdersController();
        SaleController.updateSaleStatusController();

    }
}
