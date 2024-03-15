package uiass.gisiba.eia.java.entity.crm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AddressId implements Serializable {

    @Column(name = "country")
    private String country;

    @Column(name = "zip_code")
    private int zipCode;

    public AddressId(String country, int zipCode) {
        this.country = country;
        this.zipCode = zipCode;
    }
}
