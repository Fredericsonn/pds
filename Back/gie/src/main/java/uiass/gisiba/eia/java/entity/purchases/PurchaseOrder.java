package uiass.gisiba.eia.java.entity.purchases;

import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Order;

@Entity(name="Purchase_Order")
public class PurchaseOrder extends Order {

    @ManyToOne
    @JoinColumn(name="purchase_id")
    private Purchase purchase;

    // Constructors

    public PurchaseOrder(InventoryItem item, Time orderTime, int quantity) {

        super(item, orderTime, quantity);
        
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

        return "order id : " + this.getOrderId() + ", product : " + this.getItem() +

        ", quantity : " + this.getQuantity() + ", order time : " + this.getOrderTime();
    }

    

    
}
