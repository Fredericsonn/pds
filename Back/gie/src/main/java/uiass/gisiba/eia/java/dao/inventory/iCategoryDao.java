package uiass.gisiba.eia.java.dao.inventory;

import java.util.*;

import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Category;

public interface iCategoryDao {

    Category getCategoryById(int id) throws CategoryNotFoundException;

    Category getCategoryByNames(String categoryName, String brandName) throws CategoryNotFoundException;

    List getAllCategories();

    List<String> getAllCategoriesNames();

    List<String> getAllBrandsNames();

    List<String> getAllBrandsByCategory(String category);

    void addCategory(String categoryName, String brandName);

    void updateCategory(int id, Map<String,Object> columnsNewValues) throws CategoryNotFoundException;
}
