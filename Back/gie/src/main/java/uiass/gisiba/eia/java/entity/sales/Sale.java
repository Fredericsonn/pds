package uiass.gisiba.eia.java.entity.sales;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Sale {

    @Id
    @Column(name="sale_id")
    private int saleId;

    @Column(name="sale_date")
    private LocalDate saleDate;

    @Column(name="total")
    private double total;

    // Constructors

    public Sale(int saleId, LocalDate saleDate, double total) {
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.total = total;
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
