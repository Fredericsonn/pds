package uiass.gisiba.eia.java.dao.exceptions;

public class ContactNotFoundException extends Exception {
    public ContactNotFoundException(int id) {
        System.out.println(id + " doesn't correspond to any existing contact");
    }

    public ContactNotFoundException(String name) {
        System.out.println(name  + " doesn't correspond to any existing contact");
    }
}
