package uiass.gisiba.eia.java.dao.inventory;


import java.util.*;

import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Category;
import uiass.gisiba.eia.java.entity.inventory.Product;


public interface iProductDao {

    void addProduct(Category categoryBrand, String model, String description, double unitPrice);

    Product getProductById(String ref) throws ProductNotFoundException;

    List<Product> productSearchFilter(Map<String,Object> columnsNewValues) throws ProductNotFoundException, CategoryNotFoundException;

    void deleteProduct(String ref) throws ProductNotFoundException, InventoryItemNotFoundException;

    List<Product> getAllProducts();

    void updateProduct(String ref, Map<String,Object> columnsNewValues) throws ProductNotFoundException, CategoryNotFoundException;

}
