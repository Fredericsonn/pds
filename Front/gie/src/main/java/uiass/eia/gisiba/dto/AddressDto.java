package uiass.eia.gisiba.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class AddressDto {


//////////////////////////////////////////////////// GET METHODS /////////////////////////////////////////////////////////////

    public static List<String> getAddressById(int id) {

        String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/addresses/" + id);

        System.out.println(responseBody);

        return Parser.parseAddress(responseBody);
    }
    
    public static List<List<String>> getAllAddresses() {

        List<List<String>> parsedAddresses = new ArrayList<List<String> >();

        String responseBody = DataSender.responseBodyGenerator("http://localhost:4567/addresses");

        JsonArray contacts = new JsonParser().parse(responseBody).getAsJsonArray();

        contacts.forEach(elt -> parsedAddresses.add(Parser.parseAddress(String.valueOf(elt.getAsJsonObject()))) );


        return parsedAddresses;
    }

    public static String addressFormulater(int id) {

        List attributes = getAddressById(id);


        return String.valueOf(attributes.get(1)) + ", " + String.valueOf(attributes.get(2)) + ", "  + String.valueOf(attributes.get(3)) + ", "
        + String.valueOf(attributes.get(4)) + ", " + String.valueOf(attributes.get(5)) + ", " + String.valueOf(attributes.get(6)) ;
    }

//////////////////////////////////////////////////// PUT METHOD /////////////////////////////////////////////////////////////

    public static String updateAddress(int id, String json) {

        if (json != "") return DataSender.putDataSender(json, "addresses/put/" + id );

        return "Please enter the new values to update";
    }
}
