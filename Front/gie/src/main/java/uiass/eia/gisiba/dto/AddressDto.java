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
        public static String addressFormulator(int id, String contactType) {

            String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/contacts/" + contactType + "/byAddressId/" + id);

            if (!responseBody.equals("Internal Server Error")) {

                List contact = Parser.parseContact(responseBody, contactType);
    
                return String.valueOf(contact.get(6));
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

//////////////////////////////////////////////////// PUT METHOD /////////////////////////////////////////////////////////////

    public static String updateAddress(int id, String json) {

        if (json != null) return DataSender.putDataSender(json, "addresses/put/" + id );

        return "no Update";
    }
}
