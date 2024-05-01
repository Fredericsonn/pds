package uiass.gisiba.eia.java.entity.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    
    @Column(name="category_name")
    private String categoryName;

    @Column(name="brand_name")
    private String brandName;

    // Constructors

    public Category(String categoryName, String brandName) {
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public boolean equals(Category category) {
        
        return this.id == category.id && this.categoryName == category.categoryName && this.brandName == category.brandName;
    }
    @Override
    public String toString() {
        return "category : " + this.categoryName + ", brand : " + this.brandName;
    }

    
}
