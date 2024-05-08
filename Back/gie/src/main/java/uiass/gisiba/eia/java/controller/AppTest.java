package uiass.gisiba.eia.java.controller;


import uiass.gisiba.eia.java.controller.crm.AddressController;
import uiass.gisiba.eia.java.controller.crm.ContactController;
import uiass.gisiba.eia.java.controller.inventory.CategoryController;
import uiass.gisiba.eia.java.controller.inventory.InventoryItemController;
import uiass.gisiba.eia.java.controller.inventory.ProductController;
import uiass.gisiba.eia.java.controller.purchase.PurchaseController;
import uiass.gisiba.eia.java.dao.inventory.iInventoryItemDao;


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

        CategoryController.getAllCategories();
        CategoryController.getAllColumnNames();
        CategoryController.getAllColumnByFilterColumn();
        CategoryController.updateCategoryController();
        CategoryController.postCategory();

        InventoryItemController.getAllItems();
        InventoryItemController.itemSearchFilter();
        InventoryItemController.getItemById();
        InventoryItemController.getItemByProduct();
        InventoryItemController.getItemQuantity();
        InventoryItemController.postItemController();
        InventoryItemController.deleteItemController();

        PurchaseController.getAllPurchases();
        PurchaseController.getAllPurchasesBySupplierType();
        PurchaseController.getAllPurchasesByStatus();
        PurchaseController.getAllPurchasesBySupplier();
        PurchaseController.deletePurchaseController();
        PurchaseController.postPurchase();
        PurchaseController.updatePurchaseOrdersController();
        PurchaseController.updatePurchaseStatusController();



        
    }
}
