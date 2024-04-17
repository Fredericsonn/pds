package uiass.gisiba.eia.java.entity.sales;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Contact;

@Entity
public class Sale {

    @Id
    @Column(name="sale_id")
    private int saleId;

    @OneToOne
    @Column(name="customer_id")
    private Contact customer;

    @Column(name="sale_date")
    private LocalDate saleDate;

    @Column(name="total")
    private double total;

    // Constructors

    public Sale(int saleId, LocalDate saleDate, double total, Contact customer) {
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.total = total;
        this.customer = customer;
    }

    public Sale() {

    }

    // Getters - Setters
    
    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public Contact getCustomer() {
        return customer;
    }

    public void setCustomer(Contact customer) {
        this.customer = customer;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }



    

    

    
    
}
