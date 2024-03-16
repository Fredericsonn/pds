package uiass.gisiba.eia.java.dao.crm;

public interface iAddressDao {

    void addAddress(String country, String city, int zipCode, String region, String neighborhood, int houseNumber);

    boolean removeAddress(String country, String city, int zipCode, String region, String neighborhood, int houseNumber);
}
