package uiass.gisiba.eia.java.entity.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name="category_name")
    private ProductCategory categoryName;

    @Enumerated(EnumType.STRING)
    @Column(name="brand_name")
    private ProductBrand brandName;

    // Constructors

    public Category(ProductCategory categoryName, ProductBrand brandName) {
        this.categoryName = categoryName;
        this.brandName = brandName;
    }

    public Category() {

    }

    // Getters - Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductCategory getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(ProductCategory categoryName) {
        this.categoryName = categoryName;
    }

    public ProductBrand getBrandName() {
        return brandName;
    }

    public void setBrandName(ProductBrand brandName) {
        this.brandName = brandName;
    }

    
}
