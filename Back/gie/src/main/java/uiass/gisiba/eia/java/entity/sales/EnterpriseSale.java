package uiass.gisiba.eia.java.entity.sales;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Enterprise;

@Entity
@DiscriminatorValue("Enterprise")
public class EnterpriseSale extends Sale {

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="customer_id")
    private Enterprise customer;

    public EnterpriseSale(int saleId,List<SaleOrder> orders, LocalDate saleDate, double total, Status state, Enterprise customer) {

        super(saleId, orders, saleDate, total, state);

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

    
}
