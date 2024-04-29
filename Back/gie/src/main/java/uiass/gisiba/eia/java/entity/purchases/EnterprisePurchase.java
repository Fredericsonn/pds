package uiass.gisiba.eia.java.entity.purchases;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Enterprise;

@Entity
@DiscriminatorValue("Enterprise")
public class EnterprisePurchase extends Purchase {

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="supplier_id")
    private Enterprise supplier;

    public EnterprisePurchase(int purchaseId,List<PurchaseOrder> orders, LocalDate purchaseDate, double total, Enterprise supplier) {

        super(purchaseId, orders, purchaseDate, total);

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

}
