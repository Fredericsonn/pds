package uiass.gisiba.eia.java.entity.delivery;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Address;

@Entity(name="Customer_Delivery")
public class CustomerDelivery extends Delivery {

    @OneToOne
    @JoinColumn(name="address_id")
    private Address delivery_address;

    // Constructors

    public CustomerDelivery(String deliveryRef, LocalDate pickupDate, LocalDate deliveryDate, Address delivery_address) {

        super(deliveryRef, pickupDate, deliveryDate);

        this.delivery_address = delivery_address;
    }

    public CustomerDelivery() {

    }

    // Getters - Setters

    public Address getDeliveryAddress() {
        return delivery_address;
    }

    public void setDeliveryAddress(Address delivery_address) {
        this.delivery_address = delivery_address;
    }

    

    

}
