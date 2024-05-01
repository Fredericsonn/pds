package uiass.gisiba.eia.java.dao.inventory;

import java.util.*;

import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Category;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCategory;

public interface iCategoryDao {

    Category getCategoryById(int id) throws CategoryNotFoundException;

    Category getCategoryByNames(String categoryName, String brandName) throws CategoryNotFoundException;

    List getAllCategories();

    List<ProductCategory> getAllCategoriesNames();

    List<ProductBrand> getAllBrandsNames();

    List<ProductBrand> getAllBrandsByCategory(String category);

    void addCategory(ProductCategory categoryName, ProductBrand brandName);

    void updateCategory(int id, Map<String,Object> columnsNewValues) throws CategoryNotFoundException;
}
