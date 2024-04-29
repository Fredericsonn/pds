package uiass.gisiba.eia.java.dao.exceptions;

public class InventoryItemNotFoundException extends Exception {
    
    public InventoryItemNotFoundException(int id) {
        System.out.println(id + " doesn't correspond to any existing inventory item.");
    }
}
