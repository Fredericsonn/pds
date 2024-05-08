package uiass.gisiba.eia.java.dao.exceptions;

public class OperationNotModifiableException extends Exception {

    public OperationNotModifiableException(String operation, int id) {

        System.out.println("The " + operation + " with the id " + id + " has passed the pending status, and thus can't be modified.");
    }
}
