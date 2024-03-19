package uiass.gisiba.eia.java.dao.exceptions;

public class ContactNotFound extends Exception {
    public ContactNotFound(int id) {
        System.out.println(id + "doesn't correspond to any existing contact");
    }

    public ContactNotFound(String name) {
        System.out.println(name  + "doesn't correspond to any existing contact");
    }
}
