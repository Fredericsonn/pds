package uiass.gisiba.eia.java.dao.exceptions;

public class InvalidContactTypeException extends Exception {

    public InvalidContactTypeException(String wrong_type) {
        System.out.println(wrong_type + " is not a valid contact type !!");
    }
}
