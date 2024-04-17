package uiass.gisiba.eia.java.entity.delivery;

import java.time.LocalDate;

import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.purchases.Purchase;

public class SupplierDelivery extends Delivery {

    @OneToOne
    private Address pickupAddress;

    @OneToOne
    private Purchase purchase;

    // Constructors

    public SupplierDelivery(String ref, LocalDate pickupDate, LocalDate deliveryDate, Contact stakeholder,
    
            Address pickupAddress, Purchase purchase) {

        super(ref, pickupDate, deliveryDate);

        this.pickupAddress = pickupAddress;

        this.purchase = purchase;
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

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    

 
    
    
}
