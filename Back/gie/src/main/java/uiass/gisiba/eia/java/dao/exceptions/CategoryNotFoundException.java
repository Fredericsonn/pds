package uiass.gisiba.eia.java.dao.exceptions;

public class CategoryNotFoundException extends Exception{

    public CategoryNotFoundException(int id) {

        System.out.println(id + " doesn't correspond to any existing category.");
    }
}
