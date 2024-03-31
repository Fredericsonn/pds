package uiass.eia.gisiba.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class ContactDto {


//////////////////////////////////////////////////// GET METHODS /////////////////////////////////////////////////////////////

    // Find a contact by its id :
    public static List<String> getContactById(int id, String contactType) {

        String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/contacts/" + contactType + "/byId/" + id);

        return Parser.parseContact(responseBody, contactType);
    }

    // Find a contact by its name :
    public static List<String> getContactByName(String name, String contactType) {

        String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/contacts/" + contactType + "/byName/" + name);

        return Parser.parseContact(responseBody, contactType);
    }

    // Find all the contacts (either all persons or all enterprises) :
    public static List<List<String>> getAllContactsByType(String contactType) {

        String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/contacts/" + contactType);

        List<List<String>> parsedContacts = new ArrayList<List<String>>();

        JsonArray contacts = new JsonParser().parse(responseBody).getAsJsonArray();

        contacts.forEach(elt -> parsedContacts.add(Parser.parseContact(String.valueOf(elt.getAsJsonObject()), contactType)) );


        return parsedContacts;
    }

    // Find all contacts (persons and enterprises) :
    public static List<List<String>> getAllContacts() {

        List<List<String>> persons = getAllContactsByType("Person");

        List<List<String>> enterprises = getAllContactsByType("Enterprise");

        persons.addAll(enterprises);

        return persons;
    }

//////////////////////////////////////////////////// POST METHOD /////////////////////////////////////////////////////////////

    // Create a new contact :
    public static String postContact(String json, String contactType) {

        return DataSender.postDataSender(json, "contact/" + contactType);
    }

//////////////////////////////////////////////////// Delete METHOD /////////////////////////////////////////////////////////////

    public static String deleteContact(int id, String contactType) {

        return DataSender.deleteDataSender("contact/delete/" + contactType + "/" + id);
    }

//////////////////////////////////////////////////// Put METHOD /////////////////////////////////////////////////////////////

    public static String updateContact(int id, String contactType, String json) {

        if (json != "") return DataSender.putDataSender(json, "contacts/" + contactType + "/put" + "/" + id );

        return "Please provide some new values to update.";
    }
}
