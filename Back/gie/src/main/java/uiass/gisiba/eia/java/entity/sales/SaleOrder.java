package uiass.gisiba.eia.java.entity.sales;

import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import uiass.gisiba.eia.java.entity.inventory.Order;
import uiass.gisiba.eia.java.entity.inventory.Product;

@Entity(name="Sale_Order")
public class SaleOrder extends Order {

    @ManyToOne
    @JoinColumn(name="sale_id")
    private Sale sale;

    // Constructors
    
    public SaleOrder(Product product, int quantity, Time orderTime) {

        super(product, quantity, orderTime);

    }

    public SaleOrder() {

    }

    // Getters - Setters

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    @Override
    public String toString() {

        return "sale id : " + this.getOrderId() + ", sale : " + this.sale + ", product : " + this.getProduct() +

        ", quantity : " + this.getQuantity() + ", order time : " + this.getOrderTime();
    }

    

    
}
