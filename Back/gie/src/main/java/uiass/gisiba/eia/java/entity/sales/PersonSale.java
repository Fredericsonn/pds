package uiass.gisiba.eia.java.entity.sales;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Person;

@Entity
@DiscriminatorValue("Person")
public class PersonSale extends Sale {

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="customer_id")
    private Person customer;

    public PersonSale(int saleId,List<SaleOrder> orders, LocalDate saleDate, double total, SaleState state, Person customer) {

        super(saleId, orders, saleDate, total, state);

        this.customer = customer;
    }

    public PersonSale() {
    }

    public Person getCustomer() {
        return this.customer;
    }

    public void setCustomer(Person customer) {
        this.customer = customer;
    }

    
}
