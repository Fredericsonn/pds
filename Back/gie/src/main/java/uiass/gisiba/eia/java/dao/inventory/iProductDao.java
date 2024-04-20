package uiass.gisiba.eia.java.dao.inventory;


import java.util.*;

import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCategory;

public interface iProductDao {

    void addProduct(String ref, ProductCategory category, ProductBrand brand, String model, 
    
    String description, double unitPrice);

    Product getProductById(String ref) throws ProductNotFoundException;

    void deleteProduct(String ref) throws ProductNotFoundException;

    List<Product> getAllProducts();

    List<ProductCategory> getAllCategories(); 

    List<ProductBrand> getAllBrandsByCategory(ProductCategory category); 

    void updateProduct(String ref, Map<String,Object> columnsNewValues) throws ProductNotFoundException;
}
