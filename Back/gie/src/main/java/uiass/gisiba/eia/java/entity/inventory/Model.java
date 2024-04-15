package uiass.gisiba.eia.java.entity.inventory;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Model implements Serializable {

    @Id
    @Enumerated(EnumType.STRING)
    private ProductBrand brand;

    @Id
    @Column(name="model")
    private String modelName;

    @Column(name="release_date")
    private LocalDate releaseDate;

    // Constructors

    public Model(ProductBrand brand, String modelName, LocalDate releaseDate) {
        this.brand = brand;
        this.modelName = modelName;
        this.releaseDate = releaseDate;
    }

    public Model() {

    }

    // Getters - Setters

    public ProductBrand getBrand() {
        return brand;
    }

    public void setBrand(ProductBrand brand) {
        this.brand = brand;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModel(String modelName) {
        this.modelName = modelName;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }


    
}
