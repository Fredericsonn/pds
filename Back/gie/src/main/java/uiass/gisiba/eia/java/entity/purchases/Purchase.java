package uiass.gisiba.eia.java.entity.purchases;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Supplier;
import uiass.gisiba.eia.java.entity.delivery.SupplierDelivery;


@Entity(name="Purchase")
public class Purchase {

    @Id
    @Column(name="purchase_ref")
    private String purchaseRef;

    @OneToOne
    @JoinColumn(name="supplier_id")
    private Supplier supplier;

    @OneToMany(mappedBy = "purchase")
    private List<PurchaseOrder> orders;

    @Column(name="purchase_date")
    private LocalDate purchaseDate;

    @Column(name="total")
    private double total;

    // Constructors

    public Purchase(String purchaseRef, Supplier supplier,List<PurchaseOrder> orders, LocalDate purchaseDate, double total) {
        this.purchaseRef = purchaseRef;
        this.supplier = supplier;
        this.orders = orders;
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
