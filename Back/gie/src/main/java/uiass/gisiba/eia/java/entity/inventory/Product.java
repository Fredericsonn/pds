package uiass.gisiba.eia.java.entity.inventory;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.dao.inventory.ProductRefGenerator;


@Entity(name="Catalog")
public class Product {

    @Id
    @Column(name="product_ref")
    private String productRef;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="category_id")
    private Category category;

    @Column(name="name")
    private String name;

    @Column(name = "description")
    private String description;


    // Constructors

    public Product(Category category, String name, String description) {
        this.productRef = ProductRefGenerator.generateProductRef();
        this.category = category;
        this.name = name;
        this.description = description;
    }

    public Product() {

    }

    // Getters - Setters

    public String getProductRef() {
        return productRef;
    }

    public void setProductRef(String productRef) {
        this.productRef = productRef;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    

    @Override
    public String toString() {

        return "ref : " + this.productRef + ", category : " + this.category.getCategoryName() +  ", brand : "  + 
        
        this.category.getBrandName() +  ", name : " + this.name + ", description : " + this.description;
        
        
    }









    
    
    

    
}
