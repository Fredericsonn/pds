package uiass.gisiba.eia.java.entity.inventory;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY) 
    @Column(name="order_id")
    private int orderId;

    @OneToOne
    @JoinColumn(name = "product_ref")
    private Product product;

    @Column(name="quantity")
    private int quantity;

    @Column(name="time")
    private String orderTime;

    // Constructors

    public Order(Product product, int quantity, String orderTime) {

        this.product = product;
        this.quantity = quantity;
        this.orderTime = orderTime;
    }

    public Order() {

    }

    // Getters - Setters 

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

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

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }



    

    



    


}
