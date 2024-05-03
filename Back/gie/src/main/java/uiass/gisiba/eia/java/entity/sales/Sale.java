package uiass.gisiba.eia.java.entity.sales;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Sale implements Serializable {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Column(name="sale_id")
    private int saleRef;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleOrder> orders;

    @Column(name="sale_date")
    private LocalDate saleDate;

    @Column(name="total")
    private double total;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private Status status;

    // Constructors

    public Sale(int saleRef, List<SaleOrder> orders, LocalDate saleDate, double total, Status status) {
        this.saleRef = saleRef;
        this.orders = orders;
        this.saleDate = saleDate;
        this.total = total;
        this.status= status;
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

    public List<SaleOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<SaleOrder> orders) {
        this.orders = orders;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    




    

    

    
    
}
