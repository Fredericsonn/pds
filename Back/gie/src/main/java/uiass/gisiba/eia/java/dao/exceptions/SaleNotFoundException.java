package uiass.gisiba.eia.java.dao.exceptions;

public class SaleNotFoundException extends Exception {

    public SaleNotFoundException(int id) {
        
        System.out.println(id + " doesn't correspond to any existing sale");
    }
}
