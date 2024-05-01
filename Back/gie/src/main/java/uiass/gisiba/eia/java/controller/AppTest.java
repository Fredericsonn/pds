package uiass.gisiba.eia.java.controller;


import uiass.gisiba.eia.java.controller.crm.AddressController;
import uiass.gisiba.eia.java.controller.crm.ContactController;
import uiass.gisiba.eia.java.controller.inventory.CategoryController;
import uiass.gisiba.eia.java.controller.inventory.InventoryItemController;
import uiass.gisiba.eia.java.controller.inventory.ProductController;


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

        CategoryController.getAllCategories();
        CategoryController.getAllCategoriesNames();
        CategoryController.getAllBrandsByCategory();
        CategoryController.updateCategoryController();
        CategoryController.getAllBrandsNames();
        CategoryController.postCategory();

        InventoryItemController.getAllItems();
        InventoryItemController.getItemById();
        InventoryItemController.getItemQuantity();
        InventoryItemController.postItemController();
        InventoryItemController.deleteItemController();


        
    }
}
