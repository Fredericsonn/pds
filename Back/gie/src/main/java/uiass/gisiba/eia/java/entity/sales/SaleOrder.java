package uiass.gisiba.eia.java.entity.sales;

import java.time.LocalTime;

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
    
    public SaleOrder(Product product, int quantity, LocalTime orderTime, Sale sale) {

        super(product, quantity, orderTime);

        this.sale = sale;
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

    

    
}
