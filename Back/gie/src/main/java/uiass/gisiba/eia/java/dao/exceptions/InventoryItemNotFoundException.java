package uiass.gisiba.eia.java.dao.exceptions;

public class InventoryItemNotFoundException extends Exception {
    
    public InventoryItemNotFoundException(int id) {
        System.out.println(id + " doesn't correspond to any existing inventory item.");
    }

    public InventoryItemNotFoundException(String ref) {

        System.out.println("The product " + ref + " is currently out of stock");
    }
}
