package uiass.gisiba.eia.java.entity.sales;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.delivery.CustomerDelivery;

@Entity
@DiscriminatorValue("Enterprise")
public class EnterpriseSale extends Sale {

    @OneToOne
    @JoinColumn(name="id")
    private Enterprise customer;

    public EnterpriseSale(int saleId, LocalDate saleDate, double total, CustomerDelivery delivery, Enterprise customer) {

        super(saleId, saleDate, total, delivery);

        this.customer = customer;
    }

    public EnterpriseSale() {
    }

    public Enterprise getSupplier() {
        return this.customer;
    }

    public void setSupplier(Enterprise supplier) {
        this.customer = supplier;
    }

    
}
