package uiass.gisiba.eia.java.entity.sales;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.delivery.CustomerDelivery;

@Entity
@DiscriminatorValue("Person")
public class PersonSale extends Sale {

    @OneToOne
    @JoinColumn(name="id")
    private Person customer;

    public PersonSale(int saleId, LocalDate saleDate, double total, CustomerDelivery delivery, Person customer) {

        super(saleId, saleDate, total, delivery);

        this.customer = customer;
    }

    public PersonSale() {
    }

    public Person getSupplier() {
        return this.customer;
    }

    public void setSupplier(Person supplier) {
        this.customer = supplier;
    }

    
}
