package uiass.gisiba.eia.java.dao.exceptions;

public class PurchaseNotFoundException extends Exception {

    public PurchaseNotFoundException(int id) {

        System.out.println(id + " doesn't correspond to any existing purchase");
    }
}
