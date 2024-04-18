package uiass.gisiba.eia.java.dao.exceptions;

public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String ref) {
        System.out.println(ref + " doesn't correspond to any existing product.");
    }
}
