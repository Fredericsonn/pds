package uiass.gisiba.eia.java.entity.delivery;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.sales.Sale;

@Entity
public class CustomerDelivery extends Delivery {

    @OneToOne
    private Address deliveryAddress;

    @OneToOne
    private Sale sale;

    // Constructors

    public CustomerDelivery(String ref, LocalDate pickupDate, LocalDate deliveryDate, Contact stakeholder,
    
            Address deliveryAddress, Sale sale) {

        super(ref, pickupDate, deliveryDate, stakeholder);

        this.deliveryAddress = deliveryAddress;

        this.sale = sale;
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

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    
    


    
}
