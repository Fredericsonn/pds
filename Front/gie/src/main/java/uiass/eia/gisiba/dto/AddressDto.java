package uiass.eia.gisiba.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class AddressDto {


//////////////////////////////////////////////////// GET METHODS /////////////////////////////////////////////////////////////

    public static List<String> getAddressById(int id) {

        String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/addresses/" + id);

        if (!responseBody.equals("Server Error.")) return Parser.parseAddress(responseBody);

        return null;
    }

        // Find a contact by its address id :
        @SuppressWarnings("unchecked")
        public static List<String> getAddressLongWay(int id, String contactType) {

            String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/contacts/" + contactType + "/byAddressId/" + id);

            if (!responseBody.equals("Internal Server Error")) {

                List contact = Parser.parseContact(responseBody, contactType);

                List address = new ArrayList<>();
    
                for (int i = 5; i < contact.size(); i++) {
                    address.add(contact.get(i));
                }
    
                return address;
            }
            return null;
        }

            
    
    public static List<List<String>> getAllAddresses() {

        List<List<String>> parsedAddresses = new ArrayList<List<String> >();

        String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/addresses");

        JsonArray contacts = new JsonParser().parse(responseBody).getAsJsonArray();

        contacts.forEach(elt -> parsedAddresses.add(Parser.parseAddress(String.valueOf(elt.getAsJsonObject()))) );


        return parsedAddresses;
    }

    public static String addressFormulater(int id) {

        List attributes = getAddressLongWay(id, "Person");


        return String.valueOf(attributes.get(1)) + ", " + String.valueOf(attributes.get(2)) + ", "  + String.valueOf(attributes.get(3)) + ", "
        + String.valueOf(attributes.get(4)) + ", " + String.valueOf(attributes.get(5)) + ", " + String.valueOf(attributes.get(6)) ;
    }

//////////////////////////////////////////////////// PUT METHOD /////////////////////////////////////////////////////////////

    public static String updateAddress(int id, String json) {

        if (json != "") return DataSender.putDataSender(json, "addresses/put/" + id );

        return "Please enter the new values to update";
    }
}
