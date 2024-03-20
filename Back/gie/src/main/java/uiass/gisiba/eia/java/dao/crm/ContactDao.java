package uiass.gisiba.eia.java.dao.crm;



import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


import uiass.gisiba.eia.java.dao.exceptions.*;
import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;
import uiass.gisiba.eia.java.entity.crm.Person;

public class ContactDao implements iContactDao {
	private EntityManager em;
	private EntityTransaction tr;
	private AddressDao adao = new AddressDao();

    public ContactDao() {
        this.em= HibernateUtility.getEntityManger();
	    tr=em.getTransaction();
	}

	public static boolean contactTypeChecker(String contactType) {
		return contactType == Person.class.getSimpleName() || contactType == Enterprise.class.getSimpleName();
	}

	@Override
	public void addContact(String fname, String lname, String phoneNumber, String email, Address address) throws AddressNotFoundException, DuplicatedAddressException{
		
		int original_id = adao.existingAddressChecker(address); // Check if the address already exists in the Address table
		                                                // to prevent duplication

		if (original_id != 0) {

			throw new DuplicatedAddressException();
		
		}
		Person contact = new Person(fname,lname,phoneNumber,email,address);
		tr.begin();
		em.persist(contact);
		tr.commit();
	}

	@Override
	public void addContact(String entrepriseName, EntrepriseType type, String phoneNumber, String email, Address address) throws AddressNotFoundException, DuplicatedAddressException {

		int original_id = adao.existingAddressChecker(address); // Check if the address already exists in the Address table
		                                                // to prevent duplication

		if (original_id != 0) {

			throw new DuplicatedAddressException();
		
		}

		Enterprise contact = new Enterprise(entrepriseName,type,phoneNumber,email,address);

		tr.begin();
		em.persist(contact);
		tr.commit();


	}

	@Override
	public void deleteContact(int id, String contactType) throws ContactNotFoundException, InvalidContactTypeException {

		if (contactTypeChecker(contactType)) {

			tr.begin();

			Contact contact = this.getContactById(id, contactType);

			if (contact != null) { 

				em.remove(contact);	
				System.out.println("Contact removed successfully.");	
			}

			tr.commit();

			throw new ContactNotFoundException(id);
		}

		throw new InvalidContactTypeException(contactType);

	}

	@Override
    public Contact getContactById(int id, String contactType) throws ContactNotFoundException,InvalidContactTypeException {

		if (contactType == Person.class.getSimpleName()) {

			Contact contact = em.find(Person.class, id);

			if (contact != null) return contact;
			throw new ContactNotFoundException(id);
		}

		if (contactType == Person.class.getSimpleName()) {

			Contact contact = em.find(Enterprise.class, id);
			
			if (contact != null) return contact;
			throw new ContactNotFoundException(id);
		}


		throw new InvalidContactTypeException(contactType);
	
    }

	@Override
	public Contact getContactByName(String name, String contactType) throws ContactNotFoundException, InvalidContactTypeException {

		if (contactTypeChecker(contactType)) {

			String hql = UpdateManager.getContactByNameHQLQueryGenerator(contactType);
			Query query = em.createQuery(hql);
			query.setParameter("fullName", name);
			
			if (query.getSingleResult() != null) {
				return (Contact) query.getSingleResult();
			}

			throw new ContactNotFoundException(name);

		}

		throw new InvalidContactTypeException(contactType);


		
	}

	// gets all objects of a given entity type
	public List getAll(String table) {

		Query query = em.createQuery("from " + table);
		
		return query.getResultList();		
	}

	@Override
	public List<Contact> getAllContacts(String contactType) throws InvalidContactTypeException {

		if (contactType == Person.class.getSimpleName() || contactType == Enterprise.class.getSimpleName()) {
			
			return this.getAll(contactType);
		}

		throw new InvalidContactTypeException(contactType);
		
	}

    @Override
    public void updateContact(int id,Map<String,Object> columnsNewValues, String contactType) throws ContactNotFoundException, InvalidContactTypeException {

		tr.begin();
		// get the contact to update :
		Contact contact = this.getContactById(id,contactType);

		//dynamically generate the corresponding hql string :
		String hql = UpdateManager.UpdateHQLQueryGenerator(contactType, columnsNewValues);

		// create the query using the generated hql :
		Query query = em.createQuery(hql);

		// set the query parameters :
        for (String column : columnsNewValues.keySet()) {

			Object newValue = columnsNewValues.get(column);
            
            query.setParameter(column, newValue);
            
        }

		query.setParameter("id", id);

        query.executeUpdate();

		tr.commit();
    }

	@Override
	public Contact getContactByAddresId(int address_id) {
		
		Query query = em.createQuery("from Person p, Address a where p.address.addressId = a.addressId " 
		+ "and a.addressId = :address_id");
		query.setParameter("address_id", address_id);
		Contact contact = (Contact) query.getSingleResult();
		return contact;
	}
















}
