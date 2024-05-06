package uiass.gisiba.eia.java.entity.purchases;

import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import uiass.gisiba.eia.java.entity.inventory.Order;
import uiass.gisiba.eia.java.entity.inventory.Product;

@Entity(name="Purchase_Order")
public class PurchaseOrder extends Order {

    @ManyToOne
    @JoinColumn(name="purchase_id")
    private Purchase purchase;

    // Constructors

    public PurchaseOrder(Product product, int quantity, Time orderTime) {

        super(product, quantity, orderTime);
        
    }

    public PurchaseOrder() {

    }

    // Getters - Setters

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    @Override
    public String toString() {

        return "order id : " + this.getOrderId() + ", product : " + this.getProduct() +

        ", quantity : " + this.getQuantity() + ", order time : " + this.getOrderTime();
    }

    

    
}
