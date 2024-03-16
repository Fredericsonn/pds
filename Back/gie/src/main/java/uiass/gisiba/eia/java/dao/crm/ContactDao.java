package uiass.gisiba.eia.java.dao.crm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import uiass.gisiba.eia.java.entity.crm.Address;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Person;

public class ContactDao implements iContactDao {
	private EntityManager em;
	private EntityTransaction tr;

    public ContactDao() {
        this.em= HibernateUtility.getEntityManger();
	    tr=em.getTransaction();
	}

	@Override
	public void addContact(String phoneNumber, String email, List<Address> addresses) {
		Contact contact = new Contact(phoneNumber,email,addresses);
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
