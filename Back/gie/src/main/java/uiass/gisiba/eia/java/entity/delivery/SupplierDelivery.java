package uiass.gisiba.eia.java.entity.delivery;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Address;

@Entity(name="Supplier_Delivery")
public class SupplierDelivery extends Delivery {

    @OneToOne
    @JoinColumn(name="address_id")
    private Address pickup_address;

    // Constructors

    public SupplierDelivery(String deliveryRef, LocalDate pickupDate, LocalDate deliveryDate, Address pickup_address) {

        super(deliveryRef, pickupDate, deliveryDate);

        this.pickup_address = pickup_address;
    }

    public SupplierDelivery() {

    }

    // Getters - Setters
    
    public Address getPickupAddress() {
        return pickup_address;
    }

    public void setPickupAddress(Address pickup_address) {
        this.pickup_address = pickup_address;
    }

    

    

}
