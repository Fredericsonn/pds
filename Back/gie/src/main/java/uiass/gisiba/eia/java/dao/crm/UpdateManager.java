package uiass.gisiba.eia.java.dao.crm;

import java.lang.reflect.RecordComponent;
import java.util.*;

import javax.management.Query;

import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.crm.EntrepriseType;
import uiass.gisiba.eia.java.entity.crm.Person;

public class UpdateManager {



    // Dynamically identify the columns to update

    public static List<String> getPersonColumns() {
        
        List<String> columns = new ArrayList<>();
        RecordComponent[] components = Person.class.getRecordComponents();
        for (RecordComponent rc : components) {
            columns.add(rc.getName());
        }
        return columns;
    }

    public static List<String> getEnterpriseColumns() {
        
        List<String> columns = new ArrayList<>();
        RecordComponent[] components = Enterprise.class.getRecordComponents();
        for (RecordComponent rc : components) {
            columns.add(rc.getName());
        }
        return columns;
    }


    // Identify the columns to update for a Person object and returns a columns update state map

    public static Map<String,Boolean> columnsToUpdatePerson(String first_name, String last_name, String phone_number,
    
    String email, int address_id) {

        List<String> personColumns = getPersonColumns();

        Map<String,Boolean> columnsChecker = new HashMap<String,Boolean>();

        List parameters = Arrays.asList(first_name,last_name,phone_number,email,address_id);

        for (int i = 0; i < parameters.size(); i++) {

            if (parameters.get(i) != null) columnsChecker.put(personColumns.get(i),true);

            else columnsChecker.put(personColumns.get(i),false);

        }

        return columnsChecker; // a map containing the columns names as keys and a boolean repesenting wether or not 
        
                               //the column should be updated
    }

    // Identify the columns to upadate for an Enterprise object and returns a columns update state map

    public static Map<String,Boolean> columnsToUpdateEnterprise(String enterprise_name, EntrepriseType type,
    
    String phone_number, String email, int address_id) {

        List<String> enterpriseColumns = getEnterpriseColumns();

        Map<String,Boolean> columnsChecker = new HashMap<String,Boolean>();

        List parameters = Arrays.asList(enterprise_name,phone_number,email,address_id);

        for (int i = 0; i < parameters.size(); i++) {

            if (parameters.get(i) != null) columnsChecker.put(enterpriseColumns.get(i),true);

            else columnsChecker.put(enterpriseColumns.get(i),false);

        }

        return columnsChecker; // a map containing the columns names as keys and a boolean repesenting wether or not 
        
                               //the column should be updated
    }

    public static Map<String,Boolean> columnsToUpdate(Contact contact,int id, String fname, String lname,
    
    String enterprise_name, EntrepriseType type, String phone_number, String email, int address_id) {

        if (contact instanceof Person) {
            return columnsToUpdatePerson(fname, lname, phone_number, email, address_id);
        }
        
        return columnsToUpdateEnterprise(enterprise_name, type, phone_number, email, address_id);
    }

    // this method dynamically generates the hql query according to the contact type and the selected columns to update
    public static String UpdateHQLQueryGenerator(Contact contact, Map<String,Object> columnsNewValues) {

        String contactType = contact.getClass().getSimpleName();

        List<String> columns = new ArrayList<>(columnsNewValues.keySet());

        return columnsCollector(columns, contactType);
    }

    // this method is used to generate the hql from a columns update state map
    public static String columnsCollector(List<String> columns, String table) {

        String hql = "UPDATE table " + table + " set ";

        for (String column : columns) {
                
                hql += column + " = :" + column + ",";
            
        }

        hql = hql.substring(0, hql.length()-1);

        hql += " where id = :id;";
        
        return hql;
    }

    public static List attributeSelecter(Contact contact, String fname, String lname,
    
    String enterprise_name, EntrepriseType type, String phone_number, String email, int address_id) {

        List fields = new ArrayList<>();
        Map<String, Boolean> columsState = columnsToUpdate(contact, address_id, fname, lname, enterprise_name, type, phone_number, email, address_id);
        for (String column : columsState.keySet()) {
            if (columsState.get(column)) fields.add(column);
        }
        return fields;
    
    }

}
