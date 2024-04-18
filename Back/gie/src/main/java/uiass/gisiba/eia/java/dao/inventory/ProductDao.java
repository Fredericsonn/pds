package uiass.gisiba.eia.java.dao.inventory;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import uiass.gisiba.eia.java.dao.crm.HQLQueryManager;
import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Product;
import uiass.gisiba.eia.java.entity.inventory.ProductBrand;
import uiass.gisiba.eia.java.entity.inventory.ProductCatagory;

public class ProductDao implements iProductDao {

    private EntityManager em;
	private EntityTransaction tr;
    
    public ProductDao() {
        this.em= HibernateUtility.getEntityManger();
	    tr=em.getTransaction();
    }

    @Override
    public void addProduct(String ref, ProductCatagory category, ProductBrand brand, String model, String description, double unitPrice) {

        Product product = new Product(ref, category, brand, model,description, unitPrice);

        tr.begin();
        em.persist(product);
        tr.commit();

    }

    @Override
    public Product getProductById(String ref) throws ProductNotFoundException {

        tr.begin();

        Product product = em.find(Product.class, ref);

        tr.commit();

        if (product != null) return product;

        throw new ProductNotFoundException(ref);
    }

    @Override
    public void deleteProduct(String ref) throws ProductNotFoundException {

        tr.begin();

        Product product = em.find(Product.class, ref);

        if (product != null) { 
            
            em.remove(product);

            tr.commit();
        }

        else {
            
            tr.commit();

            throw new ProductNotFoundException(ref);
        }
    }

    @Override
    public List<Product> getAllProducts() {

        Query query = em.createQuery("from Catalog");

        return (List<Product>) query.getResultList();
    }

    @Override
    public List<ProductCatagory> getAllCategories() {

        String hql = "select DISTINCT category from Catalog";

        Query query = em.createQuery(hql);

        return query.getResultList();
    }

    @Override
    public List<ProductBrand> getAllBrandsByCategory(ProductCatagory category) {

        String hql = "select DISTINCT brand from Catalog where category = :category";

        Query query = em.createQuery(hql);

        query.setParameter("category", category);

        return query.getResultList();
    }

    @Override
    public void updateProduct(String ref, Map<String, Object> columnsNewValues) throws ProductNotFoundException {

		// get the product to update :
		Product product = this.getProductById(ref);

		//dynamically generate the corresponding hql string :
		String hql = HQLQueryManager.UpdateHQLQueryGenerator("Catalog", columnsNewValues, "product_ref");

		// create the query using the generated hql :
		Query query = em.createQuery(hql);

		// set the query parameters :
        for (String column : columnsNewValues.keySet()) {

			Object newValue = columnsNewValues.get(column);
            
            query.setParameter(column, newValue);
            
        }

		query.setParameter("product_ref", ref);

        tr.begin();

        query.executeUpdate();

		tr.commit();
    }



}
