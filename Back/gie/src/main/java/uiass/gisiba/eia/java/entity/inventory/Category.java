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

    @Column(name="model_name")
    private String modelName;

    // Constructors

    public Category(String categoryName, String brandName, String modelName) {
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.modelName = modelName;
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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public boolean equals(Category category) {
        
        return this.id == category.id && this.categoryName == category.categoryName && this.brandName == category.brandName
        
        && this.modelName == category.modelName;
    }
    @Override
    public String toString() {
        return "category : " + this.categoryName + ", brand : " + this.brandName + ", model : " + this.modelName;
    }



    
}
