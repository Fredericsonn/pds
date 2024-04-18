package uiass.gisiba.eia.java.entity.sales;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.delivery.CustomerDelivery;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Sale implements Serializable {

    @Id
    @Column(name="sale_ref")
    private int saleRef;

    @OneToOne
    @JoinColumn(name="delivery_ref")
    private CustomerDelivery delivery;

    @Column(name="sale_date")
    private LocalDate saleDate;

    @Column(name="total")
    private double total;

    // Constructors

    public Sale(int saleRef, LocalDate saleDate, double total, CustomerDelivery delivery) {
        this.saleRef = saleRef;
        this.saleDate = saleDate;
        this.total = total;
        this.delivery = delivery;
    }

    public Sale() {

    }

    // Getters - Setters
    
    public int getSaleRef() {
        return saleRef;
    }

    public void setSaleRef(int saleRef) {
        this.saleRef = saleRef;
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
