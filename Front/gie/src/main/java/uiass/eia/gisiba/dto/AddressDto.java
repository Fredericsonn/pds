package uiass.eia.gisiba.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class AddressDto {


//////////////////////////////////////////////////// GET METHODS /////////////////////////////////////////////////////////////

    public static List<String> getAddressById(int id) {

        String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/addresses/" + id);

        return Parser.parseAddress(responseBody);
    }
    
    public static List<List<String>> getAllAddresses() {

        List<List<String>> parsedAddresses = new ArrayList<List<String> >();

        String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/addresses");

        JsonArray contacts = new JsonParser().parse(responseBody).getAsJsonArray();

        contacts.forEach(elt -> parsedAddresses.add(Parser.parseAddress(String.valueOf(elt.getAsJsonObject()))) );


        return parsedAddresses;
    }

//////////////////////////////////////////////////// PUT METHOD /////////////////////////////////////////////////////////////

    public static void updateAddress(int id, String json) {

        if (json != "") DataSender.putDataSender(json, "addresses/put/" + id );
    }
}
