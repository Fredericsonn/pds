package uiass.gisiba.eia.java.dao.exceptions;

public class ContactIdNotFound extends Exception {
    public ContactIdNotFound(int id) {
        System.out.println(id + "doesn't correspond to any contact");
    }
}
