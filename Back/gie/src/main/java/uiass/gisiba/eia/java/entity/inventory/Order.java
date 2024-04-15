package uiass.gisiba.eia.java.entity.inventory;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
public class Order {

    @Id
    @OneToOne
    @JoinColumn(name = "ref")
    private Product product;

    @Column(name="quantity")
    private int quantity;

    @Column(name="unit_price")
    private double unitPrice;

    @Column(name="time")
    private String orderTime;

    // Constructors

    public Order(Product product, int quantity, double unitPrice, String orderTime) {

        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.orderTime = orderTime;
    }

    public Order() {

    }

    // Getters - Setters 

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    



    


}
