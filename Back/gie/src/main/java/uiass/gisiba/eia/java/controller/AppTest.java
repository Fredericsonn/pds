package uiass.gisiba.eia.java.controller;


import uiass.gisiba.eia.java.controller.crm.AddressController;
import uiass.gisiba.eia.java.controller.crm.ContactController;
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

        AddressController.updateAddressController();

        ContactController.postEmailController();

        ProductController.getAllProducts();
        ProductController.getProductByRef();
        ProductController.deleteProductController();
        ProductController.getAllCategories();
        ProductController.getAllBrandsByCategory();
        ProductController.updateproductController();
        ProductController.postproductController();
        
    }
}
