package uiass.gisiba.eia.java.dao.crm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import uiass.gisiba.eia.java.dao.exceptions.AddressNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.ContactNotFoundException;
import uiass.gisiba.eia.java.dao.exceptions.DuplicatedAddressException;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactTypeException;
import uiass.gisiba.eia.java.entity.crm.Address;

public class AddressDao implements iAddressDao {
	
	private EntityManager em;
	private EntityTransaction tr;

    public AddressDao() {
        this.em= HibernateUtility.getEntityManger();
	    this.tr=em.getTransaction();
	}
    @Override
    public void addAddress(String country, String city, String zipCode, String region, String neighborhood, int houseNumber) throws AddressNotFoundException,DuplicatedAddressException {

        Address address = new Address(country, city, zipCode, region, neighborhood, houseNumber);

		if (this.existingAddressChecker(address) != 0) {

			throw new DuplicatedAddressException();
			
		}

		tr.begin();
		em.persist(address);
		tr.commit();

	
    }

	@Override
	public Address getAddressById(int id) throws AddressNotFoundException {

		tr.begin();

		Address address = em.find(Address.class, id);

		if (address != null) {

			return address;

		}

		tr.commit();

		throw new AddressNotFoundException(id);
		
	}

	@Override

	public List getAllAddresses() {

		Query query = em.createQuery("from Address");
		
		return query.getResultList();		
	}
	@Override
	public void removeAddress(int id) throws AddressNotFoundException {

		tr.begin();

		Address address = em.find(Address.class, id);

		if (address != null) {

			em.remove(address);

			System.out.println("Address removed successfully.");	
		}	

		tr.commit();

		throw new AddressNotFoundException(id);


	}

	@Override
	public int existingAddressChecker(Address addressToCheck) throws AddressNotFoundException {

		tr.begin();

		// Generate the hql to find any matching addresses 
		String hql = HQLQueryManager.checkAddressExistenceHQLQueryGnenerator();
		TypedQuery<Integer> query = em.createQuery(hql, Integer.class);

		// Get the address attributes to fill the query's parameters
		String country = addressToCheck.getCountry();
		String city = addressToCheck.getCity();
		String zipCode = addressToCheck.getZipCode();
		String region = addressToCheck.getRegion();
		String neighborhood = addressToCheck.getNeighborhood();
		int houseNumber = addressToCheck.getHouseNumber();

		// Set the query's parameters
		query.setParameter("country", country);
        query.setParameter("city", city);
        query.setParameter("zip_code", zipCode);
        query.setParameter("region", region);
        query.setParameter("neighborhood", neighborhood);
        query.setParameter("house_number", houseNumber);

		int id;

		try {  // if a match is found we return the original address's id

			id = query.getSingleResult();
			tr.commit();
			

			} catch(NoResultException e) {  // if no match is found we return 0
				
				id = 0;
			    tr.commit();
			}

		return id;
		
	}

}
