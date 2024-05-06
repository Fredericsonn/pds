package uiass.gisiba.eia.java.dao.inventory;

import java.util.*;

import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Category;

public interface iCategoryDao {

    Category getCategoryById(int id) throws CategoryNotFoundException;

    Category getCategoryByNames(String categoryName, String brandName, String modelName) throws CategoryNotFoundException;

    List getAllCategories();

    List<String> getAllColumnNames(String column);

    List<String> getAllColumnByFilterColumn(String column, String filterColumn, String value);

    void addCategory(String categoryName, String brandName, String modelName);

    void updateCategory(int id, Map<String,Object> columnsNewValues) throws CategoryNotFoundException;
}
