package uiass.gisiba.eia.java.dao.crm;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.SessionFactory;

import uiass.gisiba.eia.java.entity.crm.Contact;

public class ContactDao implements iContactDao {
	private EntityManager em;
	private EntityTransaction tr;


	    public ContactDao() {

	        this.em= HibernateUtility.getEntityManger();
	        tr=em.getTransaction();
	    }
    @Override
    public Contact getContactById(int id) {

        throw new UnsupportedOperationException("Unimplemented method 'getContactById'");
    }

}
