package uiass.gisiba.eia.java.dao.crm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import uiass.gisiba.eia.java.dao.exceptions.ContactNotFound;
import uiass.gisiba.eia.java.dao.exceptions.InvalidContactType;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;

public class AddressDao implements iAddressDao {
	
	private EntityManager em;
	private EntityTransaction tr;
	private ContactDao cdao = new ContactDao();

    public AddressDao() {
        this.em= HibernateUtility.getEntityManger();
	    this.tr=em.getTransaction();
	}
    @Override
    public void addAddress(String country, String city, String zipCode, String region, String neighborhood, int houseNumber) {

        Address address = new Address(country, city, zipCode, region, neighborhood, houseNumber);
		
		tr.begin();
		em.persist(address);
		tr.commit();

	
    }

	@Override
	public Address getAddressById(int id) {

		tr.begin();
		Address address = em.find(Address.class, id);
		tr.commit();
		return address;
	}

	@Override

	public List getAllAddresses() {

		Query query = em.createQuery("from Address");
		
		return query.getResultList();		
	}
	@Override
	public void removeAddress(int id) throws ContactNotFound, InvalidContactType {

		tr.begin();

		Address address = em.find(Address.class, id);

		if (address != null) {

			em.remove(address);
			
			Contact contact = cdao.getContactByAddresId(id);

			int contactId = contact.getId();
			String contactType = contact.getClass().getSimpleName();
			cdao.deleteContact(id, contactType);

			System.out.println("Address removed successfully.");	
		}	
		tr.commit();


	}


}
