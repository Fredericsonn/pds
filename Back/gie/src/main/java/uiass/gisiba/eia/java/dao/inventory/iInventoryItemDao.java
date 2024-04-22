package uiass.gisiba.eia.java.dao.inventory;

import java.time.LocalDate;
import java.util.*;

import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Product;

public interface iInventoryItemDao {

    InventoryItem getInventoryItemById(int itemId);

    List<InventoryItem> getAllInventoryItems();

    int getItemQuantity(int itemId);

    void addInventoryItem(Product product, int quantity, LocalDate dateAdded);

    void deleteInventoryItem(int itemId);

    void updateInventoryItem(int id, Map<String,Object> columnsNewValues);
}
