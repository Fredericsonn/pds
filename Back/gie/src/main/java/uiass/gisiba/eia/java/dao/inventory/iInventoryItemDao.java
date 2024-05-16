package uiass.gisiba.eia.java.dao.inventory;

import java.util.*;
import java.sql.Date;

import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Product;

public interface iInventoryItemDao {

    InventoryItem getInventoryItemById(int itemId) throws InventoryItemNotFoundException;

    InventoryItem getInventoryItemByProduct(String ref) throws InventoryItemNotFoundException, ProductNotFoundException;

    List<InventoryItem> getAllInventoryItems();

    List<InventoryItem> getFilteredItems(Map<String,String> columnsNewValues) throws InventoryItemNotFoundException, ProductNotFoundException;
    
    void addInventoryItem(Product product, double unitPrice, int quantity, Date dateAdded);

    int getItemQuantity(int itemId) throws InventoryItemNotFoundException;

    boolean canSell(int itemId, int quantity) throws InventoryItemNotFoundException;

    void deleteInventoryItem(int itemId) throws InventoryItemNotFoundException;

    void updateInventoryItem(int itemId, int quantity) throws InventoryItemNotFoundException;
    
}
