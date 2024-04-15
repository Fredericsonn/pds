package uiass.gisiba.eia.java.dao.inventory;


import java.util.*;

import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Model;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.ProductCatagory;

public interface iProductDao {

    void addProduct(String ref, String description, ProductCatagory category, Model model, double unitPrice);

    Product getProductById(String ref) throws ProductNotFoundException;

    void deleteProduct(String ref) throws ProductNotFoundException;

    List<Product> getAllProducts();

    void updateProduct(String ref, Map<String,Object> columnsNewValues) throws ProductNotFoundException;
}
