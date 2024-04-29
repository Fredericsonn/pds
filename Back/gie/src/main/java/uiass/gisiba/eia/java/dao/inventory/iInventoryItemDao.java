package uiass.gisiba.eia.java.dao.inventory;

import java.util.*;
import java.sql.Date;

import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Product;

public interface iInventoryItemDao {

    InventoryItem getInventoryItemById(int itemId) throws InventoryItemNotFoundException;

    List<InventoryItem> getAllInventoryItems();

    void addInventoryItem(Product product, int quantity, Date dateAdded);

    int getItemQuantity(int itemId) throws InventoryItemNotFoundException;

    boolean canSell(int itemId, int quantity) throws InventoryItemNotFoundException;

    void deleteInventoryItem(int itemId) throws InventoryItemNotFoundException;

    void updateInventoryItem(int itemId, int quantity) throws InventoryItemNotFoundException;
}
