package uiass.gisiba.eia.java.dao.inventory;

import java.time.LocalDate;

import uiass.gisiba.eia.java.entity.inventory.ProductBrand;

public interface iModelDao {

    void addModel(ProductBrand brand, String modelName, LocalDate releaseDate);

}
