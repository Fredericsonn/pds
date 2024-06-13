package uiass.gisiba.eia.java.entity.sales;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.inventory.Status;

@Entity
@DiscriminatorValue("Enterprise")
public class EnterpriseSale extends Sale {

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="customer_id")
    private Enterprise customer;

    public EnterpriseSale(List<SaleOrder> orders,  double total, Status state, Enterprise customer) {

        super(orders, total, state);

        this.customer = customer;
    }

    public EnterpriseSale() {
    }

    public Enterprise getCustomer() {
        return this.customer;
    }

    public void setCustomer(Enterprise customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {

        return "id : " + this.getSaleId() + ", customer : " + this.customer + ", type : enterprise" + ", sale date : " + this.getSaleDate()

        + ", status : " + this.getStatus() + ", total : " + this.getTotal() + ", orders : " + this.getOrders();
    }

    
}
