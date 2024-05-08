package uiass.gisiba.eia.java.dao.exceptions;

public class CategoryNotFoundException extends Exception{

    public CategoryNotFoundException(int id) {

        System.out.println(id + " doesn't correspond to any existing category.");
    }

    public CategoryNotFoundException(String categoryName, String brandName) {

        System.out.println(categoryName + " and " + brandName + " don't correspond to any existing category.");
    }
}
