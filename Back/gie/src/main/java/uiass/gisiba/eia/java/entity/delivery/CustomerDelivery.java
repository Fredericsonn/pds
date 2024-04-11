package uiass.gisiba.eia.java.entity.delivery;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;

import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;

@Entity
public class CustomerDelivery extends Delivery {

    @Column(name="delivery_address")
    private Address deliveryAddress;

    // Constructors

    public CustomerDelivery(String ref, LocalDate pickupDate, LocalDate deliveryDate, Contact stakeholder,
    
            Address deliveryAddress) {

        super(ref, pickupDate, deliveryDate, stakeholder);

        this.deliveryAddress = deliveryAddress;
    }

    public CustomerDelivery() {

    }

    // Getters- Setters

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    


    
}
