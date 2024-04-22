package uiass.gisiba.eia.java.entity.sales;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import uiass.gisiba.eia.java.entity.inventory.Order;
import uiass.gisiba.eia.java.entity.inventory.Product;

@Entity(name="Sale_Order")
public class SaleOrder extends Order {

    @ManyToOne
    @JoinColumn(name="sale_ref")
    private Sale purchase;

    // Constructors
    
    public SaleOrder(Product product, int quantity, String orderTime, Sale purchase) {

        super(product, quantity, orderTime);

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
