package uiass.gisiba.eia.java.entity.inventory;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.purchases.Purchase;

@Entity(name="Purchase_Order")
public class PurchaseOrder extends Order {

    @OneToOne
    @JoinColumn(name="purchase_ref")
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
