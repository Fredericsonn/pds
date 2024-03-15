package uiass.gisiba.eia.java.entity.crm;


import javax.persistence.*;

@Entity
@DiscriminatorValue("Person")
@Table(name = "Person")
public class Person extends Contact {

	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	


	
	
	// Constructor for regular contacts

	public Person(int id,String fname, String lname, String phoneNum, String email, Address address) {
		super(id,phoneNum,email,address);
		this.firstName = fname;
		this.lastName = lname;
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



	
	
	
}
