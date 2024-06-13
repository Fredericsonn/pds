package uiass.gisiba.eia.java.entity.sales;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import uiass.gisiba.eia.java.entity.inventory.InventoryItem;
import uiass.gisiba.eia.java.entity.inventory.Order;

@Entity(name="Sale_Order")
public class SaleOrder extends Order {

    @ManyToOne
    @JoinColumn(name="sale_id")
    private Sale sale;

    @Column(name="profit_margin")
    private double profitMargin;

    // Constructors
    
    public SaleOrder(InventoryItem product, Time orderTime, int quantity, double profitMargin) {

        super(product, orderTime, quantity);

        this.profitMargin = profitMargin;

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

    public double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(double profitMargin) {
        this.profitMargin = profitMargin;
    }

    
    @Override
    public String toString() {

        return "sale id : " + this.getOrderId() + ", product : " + this.getItem() +

        ", quantity : " + this.getQuantity() + ", profit margin : " + this.profitMargin +  ", order time : " + this.getOrderTime();
    }

    

    
}
