package uiass.gisiba.eia.java.entity.purchases;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Purchase {

    @Id
    @Column(name="purchase_id")
    private int purchaseId;

    @Column(name="purchase_date")
    private LocalDate purchaseDate;

    @Column(name="total")
    private double total;

    // Constructors

    public Purchase(int purchaseId, LocalDate purchaseDate, double total) {
        this.purchaseId = purchaseId;
        this.purchaseDate = purchaseDate;
        this.total = total;
    }

    public Purchase() {

    }

    // Getters - Setters
    
    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
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
