package uiass.gisiba.eia.java.entity.crm;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Supplier extends Contact {

    @Column(name="supplier_code")
    private String supplierCode;

    // Constructors

    public Supplier(String phoneNumber, String email, Address address, String supplierCode) {

        super(phoneNumber, email, address);

        this.supplierCode = supplierCode;
    }

    public Supplier() {

    }

    // Getters - Setters
    
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    

    
}
