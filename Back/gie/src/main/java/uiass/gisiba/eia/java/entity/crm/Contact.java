package uiass.gisiba.eia.java.entity.crm;


import javax.persistence.*;

@Entity
@Table(name = "Customer")
public class Contact {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private int id;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="phone_number")
	private String phoneNumber;

	@Column(name="email")
	private String email;

	@OneToOne(mappedBy = "contact")
	private Address address;
	
	
	// Constructor for regular contacts

	public Contact(String fname, String lname, String phoneNum) {
		
		this.firstName = fname;
		this.lastName = lname;
		this.phoneNumber = phoneNum;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	
	
	
}
