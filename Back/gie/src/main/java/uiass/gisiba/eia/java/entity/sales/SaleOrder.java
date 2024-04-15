package uiass.gisiba.eia.java.entity.sales;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.inventory.Order;
import uiass.gisiba.eia.java.entity.inventory.Product;

@Entity(name="Sale_Order")
public class SaleOrder extends Order {

    @Id
    @OneToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    // Constructors

    public SaleOrder(Product product, int quantity, double unitPrice, String orderTime, Sale sale) {
        super(product, quantity, unitPrice, orderTime);
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
