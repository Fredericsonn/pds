package uiass.gisiba.eia.java.dao.inventory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import uiass.gisiba.eia.java.dao.HQLQueryManager;
import uiass.gisiba.eia.java.dao.crm.HibernateUtility;
import uiass.gisiba.eia.java.dao.exceptions.CategoryNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.InventoryItemNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ProductNotFoundException;
import uiass.gisiba.eia.java.entity.inventory.Category;
import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Product;

public class ProductDao implements iProductDao {

    private EntityManager em;
	private EntityTransaction tr;
    private static iCategoryDao cdao = new CategoryDao();
    private iInventoryItemDao idao = new InventoryItemDao();
    
    public ProductDao() {
        this.em= HibernateUtility.getEntityManger();
	    tr=em.getTransaction();
    }

    @Override
    public void addProduct(Category categoryBrand, String model, String description) {

        Product product = new Product(categoryBrand,model,description);

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
    public List<Product> productSearchFilter(Map<String, Object> columnsToSelect)

        throws ProductNotFoundException, CategoryNotFoundException {

            String hql = HQLQueryManager.productSelectHQLQueryGenerator("Catalog", columnsToSelect);

            Query query = em.createQuery(hql);

            columnsToSelect.keySet().forEach(column -> {
                
                String value = (String) columnsToSelect.get(column);

                query.setParameter(column, value);

            });

            return query.getResultList();
    }

    @Override
    public void deleteProduct(String ref) throws ProductNotFoundException, InventoryItemNotFoundException {

        InventoryItem item = idao.getInventoryItemByProduct(ref);

        idao.deleteInventoryItem(item.getId());
    }

    @Override
    public List<Product> getAllProducts() {

        Query query = em.createQuery("from Catalog");

        return (List<Product>) query.getResultList();
    }

    @Override
    public void updateProduct(String ref, Map<String, Object> columnsNewValues) throws ProductNotFoundException, CategoryNotFoundException {

		// get the product to update :
		Product product = this.getProductById(ref);

        productCategoryHandler(columnsNewValues,product);
        
		//dynamically generate the corresponding hql string :
		String hql = HQLQueryManager.UpdateWithExclusionsHQLQueryGenerator("Catalog", columnsNewValues,

         "product_ref", Arrays.asList("category"));

        if (hql != null) {  // if there are other columns to update other than the category

            // create the query using the generated hql :
		    Query productQuery = em.createQuery(hql);

		    // set the query parameters :
            for (String column : columnsNewValues.keySet()) {
            
                if (!column.equals("category")) {

                    Object newValue = columnsNewValues.get(column);
            
                    productQuery.setParameter(column, newValue);  
                }
            
            }

		    productQuery.setParameter("product_ref", ref);

            tr.begin();

            productQuery.executeUpdate();

            em.refresh(product);

		    tr.commit();
        }

        
    }

    @SuppressWarnings("unchecked")
    public void productCategoryHandler(Map<String, Object> columnsNewValues, Product product) throws CategoryNotFoundException {

        Map<String, Object> categoryParams = (Map<String, Object>) columnsNewValues.get("category");

        if (categoryParams != null) {

            if (!categoryParams.isEmpty()) {

                String categoryName = categoryParams.keySet().contains("categoryName") ? (String) categoryParams.get("categoryName") : null;
    
                String brandName = categoryParams.keySet().contains("brandName") ? (String) categoryParams.get("brandName") : null;

                String modelName = categoryParams.keySet().contains("modelName") ? (String) categoryParams.get("modelName") : null;

                if (categoryName != null && brandName != null && modelName != null) {

                    Category category = cdao.getCategoryByNames(categoryName, brandName, modelName);
        
                    if (!product.getCategory().equals(category)) product.setCategory(category);
            
                    tr.begin();
            
                    em.persist(product);
            
                    tr.commit();
                }
            }

        }


    }









}
