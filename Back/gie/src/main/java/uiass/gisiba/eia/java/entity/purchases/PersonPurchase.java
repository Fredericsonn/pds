package uiass.gisiba.eia.java.entity.purchases;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import uiass.gisiba.eia.java.entity.crm.Person;
import uiass.gisiba.eia.java.entity.sales.Status;

@Entity
@DiscriminatorValue("Person")
public class PersonPurchase extends Purchase {
    
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="supplier_id")
    private Person supplier;

    public PersonPurchase(int purchaseId,List<PurchaseOrder> orders, LocalDate purchaseDate, double total,Status status,
    
    Person supplier) {

        super(purchaseId, orders, purchaseDate, total, status);

        this.supplier = supplier;
    }

    public PersonPurchase() {
    }

    public Person getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Person supplier) {
        this.supplier = supplier;
    }
}
