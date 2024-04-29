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

    @Column(name="model")
    private String model;

    @Column(name = "description")
    private String description;
    
    @Column(name = "unit_price")
    private double unitPrice;

    // Constructors

    public Product(Category category, String model, String description, double unitPrice) {
        this.productRef = ProductRefGenerator.generateProductRef();
        this.category = category;
        this.model = model;
        this.description = description;
        this.unitPrice = unitPrice;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    

    @Override
    public String toString() {

        return "ref : " + this.productRef + ", category : " + this.category.getCategoryName() +  ", brand : "  + 
        
        this.category.getBrandName() +  ", model : " + this.model + ", description : " + this.description + ", unit price: " +
        
        this.unitPrice;
        
        
    }









    
    
    

    
}
