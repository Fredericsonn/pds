package uiass.gisiba.eia.java.entity.crm;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Contact implements Serializable {

    @Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private int id;
	
	@Column(name="phone_number")
	private String phoneNumber;

	@Column(name="email")
	private String email;

  

    // Constructor 

    public Contact(String phoneNumber, String email, List<Address> addresses) {
        this.phoneNumber = phoneNumber;
        this.email = email;
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




    
}
