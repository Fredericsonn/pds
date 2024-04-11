package uiass.gisiba.eia.java.entity.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Entity(name="Catalog")
public class Product {

    @Id
    private String ref;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    private ProductCatagory category;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="brand", referencedColumnName="brand"),
        @JoinColumn(name="model", referencedColumnName="model")
    })
    private Model model;
    
    @Column(name = "unit_price")
    private double unitPrice;

    // Constructors

    public Product(String ref, String productName, String description, ProductCatagory category, Model model, double unitPrice) {
        this.ref = ref;
        this.productName = productName;
        this.description = description;
        this.category = category;
        this.model = model;
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
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductCatagory getCategory() {
        return this.category;
    }

    public void setCategory(ProductCatagory category) {
        this.category = category;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
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
        + ", brand : " + this.model.getBrand() + ", model : " + this.model.getModel() + ", unit price: " + this.unitPrice;
    }







    
    
    

    
}
