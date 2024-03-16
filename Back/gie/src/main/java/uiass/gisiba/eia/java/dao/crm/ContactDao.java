package uiass.gisiba.eia.java.dao.crm;


import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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
    public Person getContactById(int id) {

        throw new UnsupportedOperationException("Unimplemented method 'getContactById'");
    }



}
