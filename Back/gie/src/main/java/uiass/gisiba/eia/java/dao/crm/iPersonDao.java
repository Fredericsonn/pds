package uiass.gisiba.eia.java.dao.crm;

public interface iPersonDao {
    void updatePerson(int id, String fname, String lname, String phoneNum, String email, int address_id);
}
