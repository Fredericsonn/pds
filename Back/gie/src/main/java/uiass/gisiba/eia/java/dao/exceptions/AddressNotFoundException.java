package uiass.gisiba.eia.java.dao.exceptions;

public class AddressNotFoundException extends Exception{

    public AddressNotFoundException(int id) {
        System.out.println(id + " doesn't correspond to any existing address");
    }

}
