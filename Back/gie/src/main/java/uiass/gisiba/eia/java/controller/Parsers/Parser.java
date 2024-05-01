package uiass.gisiba.eia.java.controller.Parsers;

import java.sql.Date;
import java.util.*;

import com.google.gson.*;


public class Parser {
 
    // Columns filter 
    public static Map<String, Object> mapFormater(List<String> columns, List values) {

        Map<String, Object> columns_new_values = new HashMap<String, Object>();

        if (values != null) {

            for (int i = 0; i < columns.size() ; i++) {
    
                String column = columns.get(i);
        
                Object value = values.get(i);
    
                if (column.equals("houseNumber")) {
                        
                    if ((int) value != 0) columns_new_values.put(column, value);
                }
                
                else {
                        
                    if (value != null) {
        
                        columns_new_values.put(column,value);
                    }
                }
            }
        }
    
        return columns_new_values;
    }

    public static String collectString(JsonObject jsObj, String attribute) {
        JsonElement element = jsObj.get(attribute);

        return element != null ? String.valueOf(element.getAsString()) : null;
    }

    public static int collectInt(JsonObject jsObj, String attribute) {

        JsonElement element = jsObj.get(attribute);

        return element != null ? Integer.valueOf(element.getAsString()) : null;
    }

    public static double collectDouble(JsonObject jsObj, String attribute) {

        JsonElement element = jsObj.get(attribute);

        return element != null ? Double.valueOf(element.getAsString()) : null;
    }

    public static Date collectDate(JsonObject jsObj, String attribute) {

        JsonElement element = jsObj.get(attribute);

        return element != null ? Date.valueOf(element.getAsString()) : null;
    }
    
 
}

