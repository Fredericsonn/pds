package uiass.gisiba.eia.java.entity.crm;


import javax.persistence.*;

@Entity
@DiscriminatorValue("Person")
public class Person extends Contact {

	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	// Constructors
	
	public Person(String fname, String lname, String phoneNum, String email, Address address) {
		
		super(phoneNum,email,address);
		this.firstName = fname;
		this.lastName = lname;
	}

	public Person() {

	}



	// Getters - Setters
	

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return this.firstName + " " + this.lastName + ", id : " + this.getId() + ", phone number : " + 
		this.getPhoneNumber() + ", email : " + this.getEmail() + ", address : " + this.getAddress().formulateAddress();
	}



	
	
	
}
