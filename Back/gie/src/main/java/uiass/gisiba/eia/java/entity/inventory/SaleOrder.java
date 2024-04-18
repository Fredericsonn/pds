package uiass.gisiba.eia.java.entity.inventory;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.sales.Sale;

@Entity(name="Sale_Order")
public class SaleOrder extends Order {

    @OneToOne
    @JoinColumn(name="sale_ref")
    private Sale purchase;

    // Constructors
    
    public SaleOrder(Product product, int quantity, double unitPrice, String orderTime, Sale purchase) {

        super(product, quantity, unitPrice, orderTime);

        this.purchase = purchase;
    }

    public SaleOrder() {

    }

    // Getters - Setters

    public Sale getPurchase() {
        return purchase;
    }

    public void setPurchase(Sale purchase) {
        this.purchase = purchase;
    }

    

    
}
