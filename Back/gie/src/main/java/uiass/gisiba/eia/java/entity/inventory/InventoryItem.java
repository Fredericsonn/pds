package uiass.gisiba.eia.java.entity.inventory;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name="Inventory")
public class InventoryItem {

    @Id
    private int id;

    @OneToOne
    @JoinColumn(name = "ref")
    private Product product;

    @Column(name="quantity")
    private int quantity;

    @Column(name="date_added")
    private LocalDate dateAdded;

    // Constructors
    public InventoryItem(Product product, int quantity, LocalDate dateAdded) {

        this.product = product;

        this.quantity = quantity;   
        
        this.dateAdded = dateAdded;
    }

    public InventoryItem() {

    }

    // Getters - Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    

    

    


}
