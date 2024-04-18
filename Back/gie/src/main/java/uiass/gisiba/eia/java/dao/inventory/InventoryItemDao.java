package uiass.gisiba.eia.java.dao.inventory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import uiass.gisiba.eia.java.dao.crm.HibernateUtility;

public class InventoryItemDao implements iInventoryItemDao {

    private EntityManager em;
	private EntityTransaction tr;
    
    public InventoryItemDao() {
        this.em= HibernateUtility.getEntityManger();
	    tr=em.getTransaction();
    }
}
