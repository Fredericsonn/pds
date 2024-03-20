package uiass.gisiba.eia.java.dao.crm;

import java.util.*;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Person;


public class UpdateManager {


    // this method dynamically generates the hql query according to the contact type and the selected columns to update
    public static String UpdateHQLQueryGenerator(String contactType, Map<String,Object> columnsNewValues) {

        List<String> columns = new ArrayList<>(columnsNewValues.keySet());

        return columnsCollector(columns, contactType);
    }

    // this method is used to generate the hql from a columns update state map
    public static String columnsCollector(List<String> columns, String table) {
        String hql = "UPDATE " + table + " set ";

        for (String column : columns) {
                
                hql += column + " = :" + column + ",";
            
        }

        hql = hql.substring(0, hql.length()-1);

        hql += " where id = :id";
        
        return hql;
    }

    public static String getContactByNameHQLQueryGenerator(String contactType) {
        if (contactType == Person.class.getSimpleName()) {
            return "from  Person where concat(firstName,' ',lastName) = :fullName";
        }
        return "from  Enterprise where enterpriseName = :fullName";
    }

    public static String checkAddressExistenceHQLQueryGnenerator() {

        return "select a.addressId from Address a where country = :country AND city = :city AND zip_code = :zip_code AND " +
        "region = :region AND neighborhood = :neighborhood AND house_number = :house_number";
    }

    public static String idReset(String table, int value) {
        return "";
    }


}
