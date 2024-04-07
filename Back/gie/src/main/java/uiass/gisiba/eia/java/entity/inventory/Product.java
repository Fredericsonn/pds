package uiass.gisiba.eia.java.entity.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    private String ref;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    private ProductCatagory category;

    @Enumerated(EnumType.STRING)
    private ProductBrand brand;

    @Column
    private int quantity;
    
    @Column(name = "unit_price")
    private double unitPrice;

    // Constructors

    public Product(String ref, String productName, String description, ProductCatagory category, ProductBrand brand, int quantity, double unitPrice) {
        this.ref = ref;
        this.productName = productName;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Product() {

    }

    // Getters - Setters

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductCatagory getCategory() {
        return category;
    }

    public void setCategory(ProductCatagory category) {
        this.category = category;
    }

    public ProductBrand getBrand() {
        return brand;
    }

    public void setBrand(ProductBrand brand) {
        this.brand = brand;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    } 

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    

    @Override
    public String toString() {
        return "ref : " + this.ref + ", name : " + this.productName + ", category : " + this.category 
        + ", brand : " + this.brand + ", unit price: " + this.unitPrice;
    }





    
    
    

    
}
