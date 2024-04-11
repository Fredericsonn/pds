package uiass.gisiba.eia.java.entity.delivery;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import uiass.gisiba.eia.java.entity.crm.Contact;

@MappedSuperclass
public class Delivery implements Serializable {

    @Id
    private String ref;

    @Column(name="pickup_date")
    private LocalDate pickupDate;

    @Column(name="delivery_date")
    private LocalDate deliveryDate;

    @Column(name="stakeholder")
    private Contact stakeholder;
    
    // Constructors

    public Delivery(String ref, LocalDate pickupDate, LocalDate deliveryDate, Contact stakeholder) {
        this.ref = ref;
        this.pickupDate = pickupDate;
        this.deliveryDate = deliveryDate;
        this.stakeholder = stakeholder;
    }

    public Delivery() {

    }

    // Getters - Setters

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
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

    public void setdeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Contact getStakeholder() {
        return stakeholder;
    }

    public void setStakeholder(Contact stakeholder) {
        this.stakeholder = stakeholder;
    }

    


    
}
