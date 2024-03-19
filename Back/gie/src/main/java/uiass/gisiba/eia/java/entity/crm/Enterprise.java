package uiass.gisiba.eia.java.entity.crm;


import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("Entreprise")
public class Enterprise extends Contact {
	
    @Column(name="enterprise_name")
    private String entrepriseName;

    @Enumerated(EnumType.STRING)
    private EntrepriseType type;


    // Constructors
    
    public Enterprise(String entrepriseName, EntrepriseType type, String phoneNumber,
    
    String email, Address address) {
        
        super(phoneNumber,email,address);
        this.entrepriseName = entrepriseName;
        this.type = type;
    }

    public Enterprise() {

    }



    // Getters - Setters 

    public String getEntrepriseName() {
        return entrepriseName;
    }

    public EntrepriseType getType() {
        return type;
    }

    public void setEntrepriseName(String entrepriseName) {
        this.entrepriseName = entrepriseName;
    }

    public void setType(EntrepriseType type) {
        this.type = type;
    }

	@Override
	public String toString() {
		return "Enterprise Name : " + this.entrepriseName + ", type : " + type + ", id : " + this.getId() + ", phone number : " + 
		this.getPhoneNumber() + ", email : " + this.getEmail() + ", address : " + this.getAddress();
	}

    

    

}
