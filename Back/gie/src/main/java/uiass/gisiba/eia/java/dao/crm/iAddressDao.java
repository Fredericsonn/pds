package uiass.gisiba.eia.java.dao.crm;

import java.util.List;

import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.entity.crm.Address;

public interface iAddressDao {

    void addAddress(String country, String city, String zipCode, String region, String neighborhood,
    
    int houseNumber) throws AddressNotFoundException,DuplicatedAddressException;

    Address getAddressById(int id) throws AddressNotFoundException;

    List<Address> getAllAddresses();

    void removeAddress(int id) throws AddressNotFoundException;

    int existingAddressChecker(Address addressToCheck) throws AddressNotFoundException;

}
