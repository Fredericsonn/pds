package uiass.gisiba.eia.java.dao.exceptions;

public class InvalidContactType extends Exception {

    public InvalidContactType(String wrong_type) {
        System.out.println(wrong_type + " is not a valid contact type !!");
    }
}
