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
    public Contact getContactById(int id) throws ContactIdNotFound {

		Contact contact = em.find(Contact.class, id);

		if (contact != null) return contact;

		throw new ContactIdNotFound(id);
	
    }

	public List getAll(String table) {

		Query query = em.createQuery("from " + table + ";");
		
		return query.getResultList();		
	}

	@Override
	public List<Contact> getAllContacts() {

		return this.getAll("contact");
	}

	@Override
	public List getAllContactsByType(String type) throws InvalidContactType {

		if (type == Person.class.getSimpleName() || type == Enterprise.class.getSimpleName()) {
			return this.getAll(type);
		}
		throw new InvalidContactType(type);
	}

    @Override
    public void updateContact(int id, String fname, String lname,String enterprise_name, EntrepriseType type,
    
    String phone_number, String email, int address_id) throws ContactIdNotFound {

		Contact contact = this.getContactById(id);
		List<Map<String,Boolean>> columns = new ArrayList<Map<String,Boolean>>();
		List parameters = UpdateManager.attributeSelecter(contact, fname, lname, enterprise_name, type, phone_number, email, address_id);
        Map<String,Boolean> columnsUpdateState = UpdateManager.columnsToUpdate(contact, id, fname, lname,
    
		enterprise_name, type, phone_number, email, address_id);

		String hql = UpdateManager.UpdateHQLQueryGenerator(contact, columnsUpdateState);
		Query query = em.createQuery(hql);
        for (Map.Entry<String, Boolean> entry : columnsUpdateState.entrySet()) {
            Map<String,Boolean> pair = new HashMap<String,Boolean>();
            pair.put(entry.getKey(), entry.getValue());
            columns.add(pair);
        }

        for (int i = 0; i < columns.size(); i ++) {
            if (columnsUpdateState.get(columns.get(i)) == true){
                query.setParameter(i, parameters.get(i));
            } 
        }

        query.executeUpdate();
    }



}
