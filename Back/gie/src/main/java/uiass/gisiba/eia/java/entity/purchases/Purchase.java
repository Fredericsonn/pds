package uiass.gisiba.eia.java.entity.purchases;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;



@Entity(name="Purchase")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Purchase {

    @Id
    @Column(name="purchase_id")
    private int purchaseId;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<PurchaseOrder> orders;

    @Column(name="purchase_date")
    private LocalDate purchaseDate;

    @Column(name="total")
    private double total;

    // Constructors

    public Purchase(int purchaseId, List<PurchaseOrder> orders, LocalDate purchaseDate, double total) {
        this.purchaseId = purchaseId;
        this.orders = orders;
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

    public List<PurchaseOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<PurchaseOrder> orders) {
        this.orders = orders;
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
