package uiass.gisiba.eia.java.dao.exceptions;

public class InsufficientQuantityInStock extends Exception {

    public InsufficientQuantityInStock(int quantityOrdered) {

        System.out.println("There are only " + quantityOrdered + " items in stock, unable to make this sale.");
    }
}
