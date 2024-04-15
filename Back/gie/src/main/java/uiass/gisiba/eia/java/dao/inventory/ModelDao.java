package uiass.gisiba.eia.java.dao.inventory;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.entity.inventory.Model;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;

public class ModelDao implements iModelDao  {

    private EntityManager em;
	private EntityTransaction tr;
    
    public ModelDao() {
        this.em= HibernateUtility.getEntityManger();
	    tr=em.getTransaction();
    }
    
    @Override
    public void addModel(ProductBrand brand, String modelName, LocalDate releaseDate) {

        Model model = new Model(brand, modelName, releaseDate);

        tr.begin();
        em.persist(model);
        tr.commit();

    }

}
