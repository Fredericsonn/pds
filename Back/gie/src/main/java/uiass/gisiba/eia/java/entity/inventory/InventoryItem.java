package uiass.gisiba.eia.java.entity.inventory;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name="Inventory")
public class InventoryItem {

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="product_ref")
    private Product product;

    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name="quantity")
    private int quantity;

    @Column(name="date_added")
    private Date dateAdded;

    // Constructors
    public InventoryItem(Product product, double unitPrice, int quantity, Date dateAdded) {

        this.product = product;

        this.unitPrice = unitPrice;

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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    

    @Override
    public String toString() {

        return "id : " + this.id + ", ref : " + this.product.getProductRef() + ", unit price : " + this.unitPrice + ", quantity : " + this.quantity
        
        + ", added on : " + this.dateAdded;
    }


    

    

    


}
