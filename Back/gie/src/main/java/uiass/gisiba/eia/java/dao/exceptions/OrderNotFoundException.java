package uiass.gisiba.eia.java.dao.exceptions;

public class OrderNotFoundException extends Exception {
    
    public OrderNotFoundException(int id) {

        System.out.println(id + " doesn't correspond to any existing order.");
    }
}
