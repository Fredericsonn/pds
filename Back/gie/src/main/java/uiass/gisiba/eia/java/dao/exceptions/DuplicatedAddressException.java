package uiass.gisiba.eia.java.dao.exceptions;

public class DuplicatedAddressException extends Exception {

    public DuplicatedAddressException() {
        System.out.println("The provided address is already linked to a contact");
    }
}
