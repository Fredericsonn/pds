package uiass.gisiba.eia.java.dao.inventory;


import java.util.*;

import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCatagory;

public interface iProductDao {

    void addProduct(String ref, ProductCatagory category, ProductBrand brand, String model, 
    
    String description, double unitPrice);

    Product getProductById(String ref) throws ProductNotFoundException;

    void deleteProduct(String ref) throws ProductNotFoundException;

    List<Product> getAllProducts();

    List<ProductCatagory> getAllCategories(); 

    List<ProductBrand> getAllBrandsByCategory(ProductCatagory category); 

    void updateProduct(String ref, Map<String,Object> columnsNewValues) throws ProductNotFoundException;
}
