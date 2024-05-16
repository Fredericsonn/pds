package uiass.gisiba.eia.java.dao.inventory;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import uiass.gisiba.eia.java.dao.HQLQueryManager;
import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Category;


public class CategoryDao implements iCategoryDao{

    private EntityManager em;
	private EntityTransaction tr;
    
    public CategoryDao() {
        this.em= HibernateUtility.getEntityManger();
	    tr=em.getTransaction();
    }

    @Override
    public Category getCategoryById(int id) throws CategoryNotFoundException {

        tr.begin();

        Category category = em.find(Category.class, id);

        tr.commit();

        if (category != null) return category;

        throw new CategoryNotFoundException(id);
    }

    @Override
    public Category getCategoryByNames(String categoryName, String brandName, String modelName) throws CategoryNotFoundException {

        Query query = em.createQuery("select c from Category c where categoryName = :categoryName and brandName = :brandName and modelName = :modelName");

        query.setParameter("categoryName", categoryName);

        query.setParameter("brandName", brandName);

        query.setParameter("modelName", modelName);

        Category category = (Category) query.getSingleResult();

        if (category != null) return category;

        else throw new CategoryNotFoundException(categoryName,brandName);
    }

    @Override
    public List<String> getAllColumnNames(String column) {

        Query query = em.createQuery("select DISTINCT " + column + "Name" + " from Category");

        return query.getResultList();
    }

    @Override
    public List<String> getAllColumnNamesByCriteria(String column, Map<String,String> criteria) {

        String hql = HQLQueryManager.selectColumnNamesByCriteria(column, criteria);

        Query query = em.createQuery(hql);

        for (String filterColumn : criteria.keySet()) {

            query.setParameter(filterColumn, criteria.get(filterColumn));
        }

        return query.getResultList();
    }

    @Override
    public List getAllCategories() {

        Query query = em.createQuery("from Category");

        return query.getResultList();
    }

    @Override
    public void addCategory(String categoryName, String brandName, String modelName) {

        Category category = new Category(categoryName, brandName, modelName);

        tr.begin();

        em.persist(category);

        tr.commit();
    }

    @Override
    public void updateCategory(int id, Map<String, Object> columnsNewValues) throws CategoryNotFoundException  {

        Category category = getCategoryById(id);

		//dynamically generate the corresponding hql string :
		String hql = HQLQueryManager.UpdateHQLQueryGenerator("Category", columnsNewValues, "id");

		// create the query using the generated hql :
		Query productQuery = em.createQuery(hql);

		// set the query parameters :
        for (String column : columnsNewValues.keySet()) {

            Object newValue = columnsNewValues.get(column);
        
            productQuery.setParameter(column, newValue);
       
        }

		productQuery.setParameter("id", id);

        tr.begin();

        productQuery.executeUpdate();

        em.refresh(category);
        
		tr.commit();
  
    }













}
