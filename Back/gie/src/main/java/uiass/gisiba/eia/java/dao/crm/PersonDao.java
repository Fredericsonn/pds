package uiass.gisiba.eia.java.dao.crm;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class PersonDao implements iPersonDao {

	private EntityManager em;
	private EntityTransaction tr;
    private List<String> columns = Arrays.asList("id","first_name","last_name","phone_number", "email","address_id");

    public PersonDao() {
        this.em= HibernateUtility.getEntityManger();
	    tr=em.getTransaction();
	}

    // Dynamically identify the columns to update

    public Map<String,Boolean> columnsToUpdate(String first_name, String last_name, String phone_number,
    
    String email, int address_id) {

        Map<String,Boolean> columnsChecker = new HashMap<String,Boolean>();

        List parameters = Arrays.asList(first_name,last_name,phone_number,email,address_id);

        for (int i = 0; i < parameters.size(); i++) {

            if (parameters.get(i) != null) columnsChecker.put(this.columns.get(i),true);

            else columnsChecker.put(this.columns.get(i),false);

        }

        return columnsChecker; // a map containing the columns names as keys and a boolean repesenting wether or not 
        
                               //the column should be updated
    }

    // dynamically generates the hql query according to the selected columns to update

    public String UpdateQueryGenerator(Map<String,Boolean> columnsChecker) {

        String query = "UPDATE table Person set ";

        for (String column : columnsChecker.keySet()) {

            if (columnsChecker.get(column)) {
                
                query += column + " = :" + column + ",";
            }
        }

        query = query.substring(0, query.length()-1);

        query += " where id = :id;";

        return query;
    }

    @Override
    public void updatePerson(int id,String fname, String lname, String phoneNum, String email, int address_id) {
        
        Map<String,Boolean> columnsUpdateStatus = this.columnsToUpdate(fname, lname, phoneNum, email, address_id);

        List<Map<String,Boolean>> columns = new ArrayList<Map<String,Boolean>>();

        List parameters = Arrays.asList(fname,lname,phoneNum,email,address_id);

        String hql = this.UpdateQueryGenerator(columnsUpdateStatus);

        Query query = em.createQuery(hql);

        for (Map.Entry<String, Boolean> entry : columnsUpdateStatus.entrySet()) {
            Map<String,Boolean> pair = new HashMap<String,Boolean>();
            pair.put(entry.getKey(), entry.getValue());
            columns.add(pair);
        }

        for (int i = 0; i < columns.size(); i ++) {
            if (columnsUpdateStatus.get(this.columns.get(i)) == true){
                query.setParameter(i, parameters.get(i));
            } 
        }

        query.executeUpdate();
        
    }

}
