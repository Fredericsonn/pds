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

    public ContactDao() {
        this.em= HibernateUtility.getEntityManger();
	    tr=em.getTransaction();
	}

	@Override
	public void addContact(String fname, String lname, String phoneNumber, String email, Address address) {
		Person contact = new Person(fname,lname,phoneNumber,email,address);
		try {
			tr.begin();
			em.persist(contact);
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	@Override
	public void addContact(String entrepriseName, EntrepriseType type, String phoneNumber, String email, Address address) {
		Enterprise contact = new Enterprise(entrepriseName,type,phoneNumber,email,address);
		try {
			tr.begin();
			em.persist(contact);
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
	}

	@Override
    public Contact getContactById(int id, String contactType) throws ContactIdNotFound,InvalidContactType {

		if (contactType == Person.class.getSimpleName()) {

			Contact contact = em.find(Person.class, id);

			if (contact != null) return contact;
			throw new ContactIdNotFound(id);
		}

		if (contactType == Person.class.getSimpleName()) {

			Contact contact = em.find(Enterprise.class, id);
			
			if (contact != null) return contact;
			throw new ContactIdNotFound(id);
		}


		throw new InvalidContactType(contactType);
	
    }

	// gets all objects of a given entity type
	public List getAll(String table) {

		Query query = em.createQuery("from " + table);
		
		return query.getResultList();		
	}

	@Override
	public List<Contact> getAllContacts(String contactType) {

		return this.getAll(contactType);
	}

    @Override
    public void updateContact(int id,Map<String,Object> columnsNewValues, String contactType) throws ContactIdNotFound, InvalidContactType {

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
	public void notifyContact(int id, String msg) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'notifyContact'");
	}



}
