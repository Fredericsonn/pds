package uiass.gisiba.eia.java.entity.inventory;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Inventory extends Product {
    
    @Column
    private int quantity;

    // Constructors

    public Inventory(String ref, String productName, Category category, ProductBrand brand, double unitPrice, int quantity) {

        super(ref, productName, category, brand, unitPrice);
        this.quantity = quantity;
    }

    public Inventory() {

    }

    // Getters - Setters
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    

    
}
