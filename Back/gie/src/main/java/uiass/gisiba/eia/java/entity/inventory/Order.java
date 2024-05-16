package uiass.gisiba.eia.java.entity.inventory;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

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
    @JoinColumn(name = "item_id")
    private InventoryItem item;

    @Column(name="quantity")
    private int quantity;

    @Column(name="time")
    private Time orderTime;

    // Constructors

    public Order(InventoryItem item, Time orderTime,  int quantity) {

        this.item = item;
        this.orderTime = orderTime;
        this.quantity = quantity;
        
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

    public InventoryItem getItem() {
        return item;
    }

    public void setItem(InventoryItem item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Time getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Time orderTime) {
        this.orderTime = orderTime;
    }



    

    



    


}
