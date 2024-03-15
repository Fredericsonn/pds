package uiass.gisiba.eia.java.entity.crm;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Entreprise")
public class Entreprise extends Contact {
	
    @Column(name="Entrprise Name")
    private String entrepriseName;

    @Column(name="type")
    private EntrepriseType type;


    // Constructor
    
    public Entreprise(int id, String entrepriseName, EntrepriseType type, String phoneNumber,
    
    String email, Address address) {
        
        super(id,phoneNumber,email,address);
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
