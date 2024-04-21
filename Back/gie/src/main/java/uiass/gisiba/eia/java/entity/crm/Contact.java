package uiass.gisiba.eia.java.entity.crm;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;


@MappedSuperclass
public class Contact implements Serializable {

    @Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)     //uiass.gisiba.eia.java.controller.AppTest
	private int id;
	
	@Column(name="phone_number")
	private String phoneNumber;

	@Column(name="email")
	private String email;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_id")
    private Address address;

    // Constructors

    public Contact(String phoneNumber, String email, Address address) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public Contact() {
        
    }

    // Getters - Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    



    
}
