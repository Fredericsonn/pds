package uiass.gisiba.eia.java.entity.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Category {
    
    @Id
    private String code_category;

    @Enumerated(EnumType.STRING)
    private ProductCatagory category_name;

    // Constructors

    public Category(String code_category, ProductCatagory category_name) {
        this.code_category = code_category;
        this.category_name = category_name;
    }

    public Category() {

    }

    // Getters - Setters
    
    public String getCode_category() {
        return code_category;
    }

    public void setCode_category(String code_category) {
        this.code_category = code_category;
    }

    public ProductCatagory getCategory_name() {
        return category_name;
    }

    public void setCategory_name(ProductCatagory category_name) {
        this.category_name = category_name;
    }

    

    
}
