package uiass.gisiba.eia.java.entity.purchases;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Supplier;
import uiass.gisiba.eia.java.entity.delivery.SupplierDelivery;


@Entity(name="Purchase")
public class Purchase {

    @Id
    @Column(name="purchase_ref")
    private String purchaseRef;

    @OneToOne
    @JoinColumn(name="id")
    private Supplier supplier;

    @OneToOne
    @JoinColumn(name="delivery_ref")
    private SupplierDelivery delivery;

    @Column(name="purchase_date")
    private LocalDate purchaseDate;

    @Column(name="total")
    private double total;

    // Constructors

    public Purchase(String purchaseRef, Supplier supplier, LocalDate purchaseDate, double total) {
        this.purchaseRef = purchaseRef;
        this.supplier = supplier;
        this.purchaseDate = purchaseDate;
        this.total = total;

    }

    public Purchase() {

    }

    // Getters - Setters
    
    public String getPurchaseRef() {
        return purchaseRef;
    }

    public void setPurchaseRef(String purchaseRef) {
        this.purchaseRef = purchaseRef;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }



    


    


    

    

    

}
