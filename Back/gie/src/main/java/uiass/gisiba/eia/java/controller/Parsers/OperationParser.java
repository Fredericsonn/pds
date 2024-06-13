package uiass.gisiba.eia.java.controller.Parsers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uiass.gisiba.eia.java.dao.exceptions.InvalidFilterCriteriaMapFormatException;
import uiass.gisiba.eia.java.entity.inventory.Status;

public class OperationParser extends Parser {

    public static Status parseStatus(String json) {

        JsonObject statusObject = new JsonParser().parse(json).getAsJsonObject();

        String status = statusObject.get("status").getAsString();

        return Status.valueOf(status);
    }

///////////////////////////////////////////////////// FILTERS /////////////////////////////////////////////////////////////////////

    public static boolean roleValidator(String role) {

        return role.equalsIgnoreCase("customer") || role.equalsIgnoreCase("supplier");
    }

    public static boolean operationValidator(String role) {

        return role.equalsIgnoreCase("purchase") || role.equalsIgnoreCase("sale");
    }

    public static Map<String,String> parseContactFilterCriteriaMap(String json, String contactRole) {

        if (roleValidator(contactRole)) {

            String role = contactRole.toLowerCase();

            Map<String,String> contactMap = new HashMap<String,String>();
    
            JsonObject contactObject = new JsonParser().parse(json).getAsJsonObject();
    
            String contactName = contactObject.get(role + "Name").getAsString();
    
            String contactType = contactObject.get(role + "Type").getAsString();
    
            contactMap.put(role + "Name", contactName);
    
            contactMap.put(role + "Type", contactType);
    
            return contactMap;
        }

        throw new RuntimeException("Invalid Role !!");
    }

    public static Map<String,Date> parseDatesFilterCriteriaMap(String json) throws InvalidFilterCriteriaMapFormatException {
        
        Map<String,Date> criteriaMap = new HashMap<String,Date>();

        JsonObject datesFilterObject = new JsonParser().parse(json).getAsJsonObject();

        if (datesFilterObject.has("beforeDate")) {

            LocalDate beforeDate = LocalDate.parse(datesFilterObject.get("beforeDate").getAsString());

            criteriaMap.put("beforeDate", Date.valueOf(beforeDate));

            return criteriaMap;

        }

        else if (datesFilterObject.has("afterDate")) {

            LocalDate afterDate = LocalDate.parse(datesFilterObject.get("afterDate").getAsString());

            criteriaMap.put("afterDate", Date.valueOf(afterDate));

            return criteriaMap;

        }

        else if (datesFilterObject.has("startDate") && datesFilterObject.has("endDate")) {

            LocalDate startDate = LocalDate.parse(datesFilterObject.get("startDate").getAsString());

            LocalDate endDate = LocalDate.parse(datesFilterObject.get("endDate").getAsString());

            criteriaMap.put("startDate", Date.valueOf(startDate));

            criteriaMap.put("endDate", Date.valueOf(endDate));

            return criteriaMap;

        }

        throw new InvalidFilterCriteriaMapFormatException();
    }

    public static Map<String,Object> parseOperationFilterCriteriaMap(String json, String operation) throws InvalidFilterCriteriaMapFormatException {

        Map<String,Object> map = new HashMap<String,Object>();

        JsonObject filterObject = new JsonParser().parse(json).getAsJsonObject();

        if (operationValidator(operation)) {

            String role = operation.equalsIgnoreCase("purchase") ? "supplier" : "customer";

            if (filterObject.has(role)) {

                JsonObject supplierObject = filterObject.getAsJsonObject(role).getAsJsonObject();
    
                Map<String,String> supplierMap = parseContactFilterCriteriaMap(String.valueOf(supplierObject), role);
    
                map.put(role, supplierMap);
    
            }
    
            if (filterObject.has("date")) {
    
                JsonObject dateObject = filterObject.getAsJsonObject("date").getAsJsonObject();
    
                Map<String,Date> dateMap = parseDatesFilterCriteriaMap(String.valueOf(dateObject));
    
                map.put("date", dateMap);
    
            }
    
            if (filterObject.has("status")) {
    
                String status = filterObject.get("status").getAsString();
    
                map.put("status", status);
            }
    
            return map;
        }

        throw new RuntimeException("Invalid Operation !!");

    }


}
