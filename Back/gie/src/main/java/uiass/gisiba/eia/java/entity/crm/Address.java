package uiass.gisiba.eia.java.entity.crm;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;


@Entity
public class Address {

    @Embedded
    private String country;

    @Column(name = "zip_code", insertable = false, updatable = false)
    private int zipCode;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "house_number")
    private int houseNumber;

    public Address(String country, String city, int zipCode, String region, String neighborhood, int houseNumber) {
        this.country = country;
        this.zipCode = zipCode;
        this.region = region;
        this.neighborhood = neighborhood;
        this.houseNumber = houseNumber;
    }

    public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public int getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(int houseNumber) {
		this.houseNumber = houseNumber;
	}
   
}