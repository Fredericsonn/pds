package uiass.gisiba.eia.java.dao.exceptions;

public class InvalidOrderTypeException extends Exception {

    public InvalidOrderTypeException(String type) {

        System.out.println(type + " is not a valid order type.");
    }
}
