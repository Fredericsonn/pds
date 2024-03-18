package uiass.gisiba.eia.java.entity.crm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Address {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name = "address_id")
	private int addressId;

    @Column(name = "house_number")
    private int houseNumber;

	@Column(name = "neighborhood")
    private String neighborhood;

	@Column(name = "city")
    private String city;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "region")
    private String region;

	@Column(name="country")
    private String country;

    // Constructors

    public Address(String country, String city, String zipCode, String region, String neighborhood, int houseNumber) {
		this.country = country;
		this.city = city;
        this.zipCode = zipCode;
        this.region = region;
        this.neighborhood = neighborhood;
        this.houseNumber = houseNumber;
    }

	public Address() {
		
	}

    public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
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

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String formulateAddress() {
		return this.houseNumber + " " +  this.neighborhood + " " + this.city + ", " + this.zipCode + ", "  
		+ this.region +  ", " + this.country;
	}


   
}