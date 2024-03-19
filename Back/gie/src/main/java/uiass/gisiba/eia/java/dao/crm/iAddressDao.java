package uiass.gisiba.eia.java.dao.crm;

import java.util.List;

import uiass.gisiba.eia.java.dao.exceptions.ContactNotFound;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactType;
import uiass.gisiba.eia.java.entity.crm.Address;

public interface iAddressDao {

    void addAddress(String country, String city, String zipCode, String region, String neighborhood, int houseNumber);

    Address getAddressById(int id);

    List<Address> getAllAddresses();

    void removeAddress(int id) throws ContactNotFound, InvalidContactType;
}
