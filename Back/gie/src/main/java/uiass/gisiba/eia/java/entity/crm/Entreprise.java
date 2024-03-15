package uiass.gisiba.eia.java.entity.crm;

import javax.persistence.Column;
import javax.persistence.Id;

public class Entreprise {

    @Id
    private int patentNumber;
	
    @Column(name="Name")
    private String entrepriseName;

    @Column(name="type")
    private EntrepriseType type;

	@Column(name="phone_number")
	private String phoneNumber;

	@Column(name="email")
	private String email;

	@Column(name="adress")
	private Address address;

    // Constructor
    
    public Entreprise(int patentNumber, String entrepriseName, EntrepriseType type, String phoneNumber,
    
    String email, Address address) {

        this.patentNumber = patentNumber;
        this.entrepriseName = entrepriseName;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }



    // Getters - Setters 

    public int getPatentNumber() {
        return patentNumber;
    }

    public String getEntrepriseName() {
        return entrepriseName;
    }

    public EntrepriseType getType() {
        return type;
    }

    public void setPatentNumber(int patentNumber) {
        this.patentNumber = patentNumber;
    }

    public void setEntrepriseName(String entrepriseName) {
        this.entrepriseName = entrepriseName;
    }

    public void setType(EntrepriseType type) {
        this.type = type;
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
