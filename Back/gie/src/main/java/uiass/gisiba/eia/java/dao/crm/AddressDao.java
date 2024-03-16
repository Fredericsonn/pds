package uiass.gisiba.eia.java.dao.crm;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import uiass.gisiba.eia.java.entity.crm.Address;

public class AddressDao implements iAddressDao {
	private EntityManager em;
	private EntityTransaction tr;

    public AddressDao() {
        this.em= HibernateUtility.getEntityManger();
	    this.tr=em.getTransaction();
	}
    @Override
    public void addAddress(String country, String city, int zipCode, String region, String neighborhood, int houseNumber) {

        Address address = new Address(country, city, zipCode, region, neighborhood, houseNumber);

		try {
			tr.begin();
			em.persist(address);
			tr.commit();

		}
		catch(Exception e) {
			tr.rollback();
			System.out.println(e);

		}
    }
	@Override
	public boolean removeAddress(String country, String city, int zipCode, String region, String neighborhood,
			int houseNumber) {

				

				try {

					tr.begin();
					Address address = em.find(Address.class, new Address(country, city, 
					zipCode, region, neighborhood, houseNumber));
					if (address != null) {
						em.remove(address);
						return true;
					}
					tr.commit();

				}catch(Exception e) {
					tr.rollback();
					System.out.println("This address doen't exist in the system !!");
		
				}
			return false;
	}

}
