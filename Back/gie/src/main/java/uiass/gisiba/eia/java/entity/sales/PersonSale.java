package uiass.gisiba.eia.java.entity.sales;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.inventory.Status;

@Entity
@DiscriminatorValue("Person")
public class PersonSale extends Sale {

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="customer_id")
    private Person customer;

    public PersonSale(List<SaleOrder> orders, double total, Status state, Person customer) {

        super(orders, total, state);

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

    @Override
    public String toString() {

        return "id : " + this.getSaleId() + ", customer : " + this.customer + ", type : person" + ", sale date : " + this.getSaleDate()

        + ", status : " + this.getStatus() + ", total : " + this.getTotal() + ", orders : " + this.getOrders();
    }

    
}
