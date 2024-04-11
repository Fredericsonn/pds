package uiass.gisiba.eia.java.entity.delivery;

import java.time.LocalDate;

import javax.persistence.Column;

import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;

public class SupplierDelivery extends Delivery {

    @Column(name="pickup_address")
    private Address pickupAddress;

    // Constructors

    public SupplierDelivery(String ref, LocalDate pickupDate, LocalDate deliveryDate, Contact stakeholder,
    
            Address pickupAddress) {

        super(ref, pickupDate, deliveryDate, stakeholder);

        this.pickupAddress = pickupAddress;
    }

    public SupplierDelivery() {

    }

    // Getters- Setters

    public Address getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(Address pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

 
    
    
}
