package uiass.gisiba.eia.java.dao.crm;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;


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
