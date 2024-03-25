package uiass.gisiba.eia.java.dao.exceptions;

public class NoContactsFoundInCountry extends Exception {

    public NoContactsFoundInCountry(String country) {
        
        System.out.println("There are currently no saved contacts from " + country);
    }
}
