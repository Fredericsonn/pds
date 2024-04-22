package uiass.gisiba.eia.java.dao.inventory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Product;

public class InventoryItemDao implements iInventoryItemDao {

    private EntityManager em;
	private EntityTransaction tr;
    
    public InventoryItemDao() {
        this.em= HibernateUtility.getEntityManger();
	    tr=em.getTransaction();
    }

    @Override
    public InventoryItem getInventoryItemById(int itemId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInventoryItemById'");
    }

    @Override
    public List<InventoryItem> getAllInventoryItems() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllInventoryItems'");
    }

    @Override
    public int getItemQuantity(int itemId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getItemQuantity'");
    }

    @Override
    public void addInventoryItem(Product product, int quantity, LocalDate dateAdded) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addInventoryItem'");
    }

    @Override
    public void deleteInventoryItem(int itemId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteInventoryItem'");
    }

    @Override
    public void updateInventoryItem(int id, Map<String, Object> columnsNewValues) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInventoryItem'");
    }
}
