package uiass.gisiba.eia.java.entity.purchases;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.inventory.Status;

@Entity
@DiscriminatorValue("Enterprise")
public class EnterprisePurchase extends Purchase {

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="supplier_id")
    private Enterprise supplier;

    public EnterprisePurchase(List<PurchaseOrder> orders, Date purchaseDate, double total, Status status,
    
    Enterprise supplier) {

        super(orders, purchaseDate, total, status);

        this.supplier = supplier;
    }

    public EnterprisePurchase() {
    }

    public Enterprise getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Enterprise supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {

        return "id : " + this.getPurchaseId() + ",orders : " + this.getOrders() + ", supplier : " + this.supplier + ", type : enterprise" +  ", purchase date : " + this.getPurchaseDate()

        + ", status : " + this.getStatus() + ", total : " + this.getTotal();
    }

}
