package uiass.gisiba.eia.java.dao.inventory;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import uiass.gisiba.eia.java.dao.crm.HQLQueryManager;
import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Category;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCategory;

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
    public List getAllCategories() {

        Query query = em.createQuery("from Category");

        return query.getResultList();
    }

    @Override
    public List<ProductCategory> getAllCategoriesNames() {

        String hql = "select DISTINCT categoryName from Category";

        Query query = em.createQuery(hql);

        return query.getResultList();
    }

    @Override
    public List<ProductBrand> getAllBrandsNames() {

        String hql = "select DISTINCT brandName from Category";

        Query query = em.createQuery(hql);

        return query.getResultList();
    }


    @Override
    public List<ProductBrand> getAllBrandsByCategory(String category) {

        String hql = "select DISTINCT brandName from Category where categoryName = :categoryName";

        Query query = em.createQuery(hql);

        query.setParameter("categoryName", ProductCategory.valueOf(category));

        return query.getResultList();
    }

    @Override
    public void addCategory(ProductCategory categoryName, ProductBrand brandName) {

        Category category = new Category(categoryName, brandName);

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
