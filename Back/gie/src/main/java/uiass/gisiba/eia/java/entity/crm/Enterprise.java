package uiass.gisiba.eia.java.entity.crm;


import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("Entreprise")
public class Enterprise extends Contact {
	
    @Column(name="Entrprise_Name")
    private String entrepriseName;

    @Enumerated(EnumType.STRING)
    private EntrepriseType type;


    // Constructor
    
    public Enterprise(String entrepriseName, EntrepriseType type, String phoneNumber,
    
    String email, Address address) {
        
        super(phoneNumber,email,address);
        this.entrepriseName = entrepriseName;
        this.type = type;
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



    

    

}
