package uiass.gisiba.eia.java.entity.purchases;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.inventory.Order;
import uiass.gisiba.eia.java.entity.inventory.Product;

@Entity(name="Purchase_Order")
public class PurchaseOrder extends Order implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    // Constructors

    public PurchaseOrder(Product product, int quantity, double unitPrice, String orderTime, Purchase purchase) {
        super(product, quantity, unitPrice, orderTime);
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
