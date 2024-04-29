package uiass.gisiba.eia.java.dao.inventory;

import java.sql.Date;
import java.time.LocalDate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
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
    public InventoryItem getInventoryItemById(int itemId) throws InventoryItemNotFoundException {

        InventoryItem item = em.find(InventoryItem.class, itemId);

        if (item != null) return item;

        else throw new InventoryItemNotFoundException(itemId);

    }

    @Override
    public List<InventoryItem> getAllInventoryItems() {

        Query query = em.createQuery("from Inventory");

        return query.getResultList();
    }

    @Override
    public int getItemQuantity(int itemId) throws InventoryItemNotFoundException {

        return getInventoryItemById(itemId).getQuantity();
    }

    @Override
    public void addInventoryItem(Product product, int quantity, Date dateAdded) {

        InventoryItem item = new InventoryItem(product, quantity, dateAdded);

        tr.begin();

        em.persist(item);

        tr.commit();
        
    }

    @Override
    public boolean canSell(int itemId, int quantity) throws InventoryItemNotFoundException {

        InventoryItem item = getInventoryItemById(itemId);

        return item.getQuantity() >= quantity;
    }

    @Override
    public void deleteInventoryItem(int itemId) throws InventoryItemNotFoundException {

        InventoryItem item = getInventoryItemById(itemId);

        tr.begin();

        em.remove(item);

        tr.commit();
    }

    @Override
    public void updateInventoryItem(int itemId, int quantity) throws InventoryItemNotFoundException {

        InventoryItem item = getInventoryItemById(itemId);

        item.setQuantity(quantity);

        item.setDateAdded(Date.valueOf(LocalDate.now()));

        tr.begin();

        em.persist(item);

        tr.commit();
    }


}
