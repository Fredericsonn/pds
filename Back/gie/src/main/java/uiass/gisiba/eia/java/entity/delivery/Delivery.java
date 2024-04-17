package uiass.gisiba.eia.java.entity.delivery;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Delivery implements Serializable {

    @Id
    private String deliveryRef;

    @Column(name="pickup_date")
    private LocalDate pickupDate;

    @Column(name="delivery_date")
    private LocalDate deliveryDate;
    
    // Constructors

    public Delivery(String deliveryRef, LocalDate pickupDate, LocalDate deliveryDate) {
        this.deliveryRef = deliveryRef;
        this.pickupDate = pickupDate;
        this.deliveryDate = deliveryDate;
    }

    public Delivery() {

    }

    // Getters - Setters

    public String getDeliveryRef() {
        return deliveryRef;
    }

    public void setDeliveryRef(String deliveryRef) {
        this.deliveryRef = deliveryRef;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }



    

    


    
}
