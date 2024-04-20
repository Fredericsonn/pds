package uiass.gisiba.eia.java.entity.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;


@Entity(name="Catalog")
public class Product {

    @Id
    @Column(name="product_ref")
    private String productRef;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    private ProductBrand brand;

    @Column(name="model")
    private String model;

    @Column(name = "description")
    private String description;
    
    @Column(name = "unit_price")
    private double unitPrice;

    // Constructors

    public Product(String productRef, ProductCategory category, ProductBrand brand, String model, String description, double unitPrice) {
        this.productRef = productRef;
        this.category = category;
        this.brand = brand;
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

    public ProductCategory getCategory() {
        return this.category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public ProductBrand getBrand() {
        return brand;
    }

    public void setBrand(ProductBrand brand) {
        this.brand = brand;
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

        return "ref : " + this.productRef + ", category : " + this.category  + ", model : "  + this.model +  ", brand : " + this.model + 
        
         ", description : " + this.description + ", unit price: " + this.unitPrice;
    }







    
    
    

    
}
