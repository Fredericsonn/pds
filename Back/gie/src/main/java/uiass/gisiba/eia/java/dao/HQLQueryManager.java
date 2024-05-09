package uiass.gisiba.eia.java.dao;

import java.util.*;
import uiass.gisiba.eia.java.entity.crm.Contact;
import uiass.gisiba.eia.java.entity.crm.Enterprise;
import uiass.gisiba.eia.java.entity.crm.Person;


public class HQLQueryManager {

    // Checks if the given type is either Person or Enterpise
	public static boolean contactTypeChecker(String contactType) {

		return contactType.equals(Person.class.getSimpleName()) || contactType.equals(Enterprise.class.getSimpleName());
	}

    // this method dynamically generates the hql query according to the contact type and the selected columns to update
    public static String UpdateHQLQueryGenerator(String table, Map<String,Object> columnsNewValues, String primary_key) {

        List<String> columns = new ArrayList<>(columnsNewValues.keySet());

        return updateColumnsCollector(columns, table, primary_key);
    }

    // this method is used to generate the hql from a columns update state map
    public static String updateColumnsCollector(List<String> columns, String table, String primary_key) {
        
        String hql = "UPDATE " + table + " set ";

        for (String column : columns) {
                
                hql += column + " = :" + column + ",";
            
        }

        hql = hql.substring(0, hql.length()-1);

        hql += " where " +  primary_key +  "= :" + primary_key;
        
        return hql;
    }

    // this method dynamically generates the hql query according to the contact type and the selected columns to update
    public static String UpdateWithExclusionsHQLQueryGenerator(String table, Map<String,Object> columnsNewValues, 
    
    String primary_key, List<String> exclusions) {

        if (columnsNewValues.size() != exclusions.size()) {

            List<String> columns = new ArrayList<>(columnsNewValues.keySet());

            for (String column : columnsNewValues.keySet()) {
    
                if (exclusions.contains(column)) columns.remove(column);
            }
    
            return updateColumnsCollector(columns, table, primary_key);
        }

        return null;
    }

    public static String getContactByNameHQLQueryGenerator(String contactType) {
        if (contactType.equals(Person.class.getSimpleName())) {
            return "from  Person where concat(firstName,' ',lastName) = :fullName";
        }
        return "from  Enterprise where enterprise_name = :fullName";
    }

    public static String geContactsByCountryHQLQueryGenerator(String contactType, String country) {

        String tableAlias = String.valueOf(contactType.charAt(0)).toLowerCase();

        String hql = "from " + contactType + " " + tableAlias + " where " + tableAlias + ".address.country = :country";
        
        return hql;
    }
    
    

    public static String checkAddressExistenceHQLQueryGnenerator() {

        return "select a.addressId from Address a where country = :country AND city = :city AND zip_code = :zip_code AND " +
        "neighborhood = :neighborhood AND house_number = :house_number";
    }

    public static String getContactByAddressIdHQLQueryGenerator(String contactType) {
        
        String tableAlias = String.valueOf(contactType.charAt(0)).toLowerCase();
        
        String hql = "from " + contactType + " " + tableAlias + " where " + tableAlias 
        
        + ".address.addressId = :address_id";

        return hql;
    }

    public static String productSelectHQLQueryGenerator(String table, Map<String,Object> columnsToSelect) {

        List<String> columns = new ArrayList<>(columnsToSelect.keySet());

        return productSelectColumnsCollector(columns, table);
    }

    // this method is used to generate the hql from a columns update state map
    public static String productSelectColumnsCollector(List<String> columns, String table) {
        
        String hql = "select c from " + table + " c " + " where ";

        for (String column : columns) {

            if (table.equals("Inventory")) hql += "product.";

            hql += "category." + column + " = :" + column + " and ";
            
        }

        hql = hql.substring(0, hql.length()-5);
        
        return hql;
    }

    public static boolean validOrderType(String type) {
        return type.equals("Purchase") || type.equals("Sale");
    }

    public static String orderTableNameHandler(String orderType) {

        String table = orderType.equals("Purchase") ? "Purchase_Order" : "Sale_Order";

        return table;
    }

    public static String orderAttributeNameHandler(String orderType) {

        String table = orderType.equals("Purchase") ? "purchase" : "sale";

        return table;
    }

    public static String selectOrdersBetweenDatesHQLQueryGenerator(String orderType) {

        String table = orderTableNameHandler(orderType);

        String attribute = orderAttributeNameHandler(orderType);

        String hql = "select o from " + table + " o where o." + attribute + "." + attribute + "Date" + 
        
        " between :startDate and :endDate";

        return hql;
    }

    public static String selectOperationsBetweenDatesHQLQueryGenerator(String type) {

        String table = orderTableNameHandler(type);

        String attribute = orderAttributeNameHandler(type);

        String hql = "from " + table + " where " + attribute + "Date" + 
        
        " between :startDate and :endDate";

        return hql;
    }

    public static boolean operationContactTypeValidator(String type) {

        return type.equals("Person") || type.equals("Enterprise");
    }



}