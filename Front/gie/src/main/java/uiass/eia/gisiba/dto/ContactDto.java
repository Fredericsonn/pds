package uiass.eia.gisiba.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class ContactDto {


//////////////////////////////////////////////////// GET METHODS /////////////////////////////////////////////////////////////

    // Find a contact by its id :
    public static List getContactById(int id, String contactType) {

        String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/contacts/" + contactType + "/byId/" + id);

        return Parser.parseContact(responseBody, contactType);
    }

    // Find a contact by its name :
    public static List getContactByName(String name, String contactType) {

        String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/contacts/" + contactType + "/byName/" + name);

        return Parser.parseContact(responseBody, contactType);
    }

    // Find all the contacts (either all persons or all enterprises) :
    public static List<List> getAllContactsByType(String contactType) {

        String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/contacts/" + contactType);

        List<List> parsedContacts = new ArrayList<List>();

        JsonArray contacts = new JsonParser().parse(responseBody).getAsJsonArray();

        contacts.forEach(elt -> parsedContacts.add(Parser.parseContact(String.valueOf(elt.getAsJsonObject()), contactType)) );


        return parsedContacts;
    }

    // Find all contacts (persons and enterprises) :
    public static List<List> getAllContacts() {

        List<List> persons = getAllContactsByType("Person");

        List<List> enterprises = getAllContactsByType("Enterprise");

        persons.addAll(enterprises);

        return persons;
    }

//////////////////////////////////////////////////// POST METHOD /////////////////////////////////////////////////////////////

    // Create a new contact :
    public static void postContact(String json, String contactType) {

        DataSender.postDataSender(json, "contact/" + contactType);
    }

//////////////////////////////////////////////////// Delete METHOD /////////////////////////////////////////////////////////////

    public static void deleteContact(int id, String contactType) {

        DataSender.deleteDataSender("contact/delete/" + contactType + "/" + id);
    }

//////////////////////////////////////////////////// Put METHOD /////////////////////////////////////////////////////////////

    public static void updateContact(int id, String contactType, String json) {

        DataSender.putDataSender(json, "contacts/" + contactType + "/put" + "/" + id );
    }
}
