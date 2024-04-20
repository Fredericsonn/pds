package uiass.gisiba.eia.java.dao.crm;



import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;


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

		if (HQLQueryManager.contactTypeChecker(contactType)) {

			tr.begin();

			Contact contact = this.getContactById(id, contactType);

			if (contact != null) { 

				em.remove(contact);	
				tr.commit();
			}

			

			else {

				tr.commit();

				throw new ContactNotFoundException(id);
			}
		}

		else throw new InvalidContactTypeException(contactType);

	}

	@Override
    public Contact getContactById(int id, String contactType) throws ContactNotFoundException,InvalidContactTypeException {

		if (contactType.equals(Person.class.getSimpleName())) {

			Contact contact = em.find(Person.class, id);

			if (contact != null) return contact;
			throw new ContactNotFoundException(id);
		}

		if (contactType.equals(Enterprise.class.getSimpleName())) {

			Contact contact = em.find(Enterprise.class, id);
			
			if (contact != null) return contact;
			throw new ContactNotFoundException(id);
		}


		throw new InvalidContactTypeException(contactType);
	
    }

	@Override
	public Contact getContactByName(String name, String contactType) throws ContactNotFoundException, InvalidContactTypeException {

		if (HQLQueryManager.contactTypeChecker(contactType)) {

			String hql = HQLQueryManager.getContactByNameHQLQueryGenerator(contactType);

			Query query = em.createQuery(hql);
			
			query.setParameter("fullName", name);
			
			if (query.getSingleResult() != null) {

				return (Contact) query.getSingleResult();
			}

			throw new ContactNotFoundException(name);

		}

		throw new InvalidContactTypeException(contactType);

	}

	@Override
	public Contact getContactByAddressId(String contactType, int address_id) throws AddressNotFoundException {

		String hql = HQLQueryManager.getContactByAddressIdHQLQueryGenerator(contactType);

		Query query = em.createQuery(hql);

		query.setParameter("address_id", address_id);

		tr.begin();
		Contact contact = (Contact) query.getSingleResult();
		tr.commit();
		
		if (contact != null )return contact;

		throw new AddressNotFoundException(address_id);
	}

	@Override
	public List<Contact> getAllContactsByCountry(String contactType, String country) throws InvalidContactTypeException, NoContactsFoundInCountry {

		if (HQLQueryManager.contactTypeChecker(contactType)) {

			String hql = HQLQueryManager.geContactsByCountryHQLQueryGenerator(contactType, country);

			Query query = em.createQuery(hql);

			query.setParameter("country", country);

			List<Contact> contacts = query.getResultList();

			if (!contacts.isEmpty()) {

				return contacts;
			}

			throw new NoContactsFoundInCountry(country);
		}
		
		throw new InvalidContactTypeException(contactType);
	}

	@Override
	public List<Contact> getAllContactsByType(String contactType) throws InvalidContactTypeException {

		if (HQLQueryManager.contactTypeChecker(contactType)) {
			
			Query query = em.createQuery("from " + contactType);

			return query.getResultList();
		}

		throw new InvalidContactTypeException(contactType);
		
	}

	@Override
	public List<Contact> getAllContacts() throws InvalidContactTypeException {

		List<Contact> persons = this.getAllContactsByType(Person.class.getSimpleName());       // All person contacts

		List<Contact> enterprises = this.getAllContactsByType(Enterprise.class.getSimpleName()); // All enterprise contacts

		persons.addAll(enterprises);  // We combine all

		return persons;		
	}

    @Override
    public void updateContact(int id,Map<String,Object> columnsNewValues, String contactType) throws ContactNotFoundException, InvalidContactTypeException {

		tr.begin();
		// get the contact to update :
		Contact contact = this.getContactById(id,contactType);

		//dynamically generate the corresponding hql string :
		String hql = HQLQueryManager.UpdateHQLQueryGenerator(contactType, columnsNewValues, "id");

		// create the query using the generated hql :
		Query query = em.createQuery(hql);

		// set the query parameters :
        for (String column : columnsNewValues.keySet()) {

			Object newValue = columnsNewValues.get(column);

			if (column.equals("type")) {

				System.out.println(column);

				query.setParameter(column, EntrepriseType.valueOf(String.valueOf(newValue)));
			}
		           
            else query.setParameter(column, newValue);
            
        }

		query.setParameter("id", id);

        query.executeUpdate();
		
		em.refresh(contact);

		tr.commit();
    }




















}
