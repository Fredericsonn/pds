package uiass.gisiba.eia.java.entity.purchases;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import uiass.gisiba.eia.java.entity.inventory.Order;
import uiass.gisiba.eia.java.entity.inventory.Product;

@Entity(name="Purchase_Order")
public class PurchaseOrder extends Order {

    @ManyToOne
    @JoinColumn(name="purchase_ref")
    private Purchase purchase;

    // Constructors

    public PurchaseOrder(Product product, int quantity, String orderTime, Purchase purchase) {

        super(product, quantity, orderTime);

        this.purchase = purchase;
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

    

    
}