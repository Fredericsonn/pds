package uiass.eia.gisiba.dto;

public class AppTest {
    public static void main(String[] args) {


        /*String json = "{\n" +
        "    \"enterpriseName\": \"Gonzalez and Sons\",\n" +
        "    \"type\": \"SARL\",\n" +
        "    \"id\": 8,\n" +
        "    \"phoneNumber\": \"001-808-799-5019x96644\",\n" +
        "    \"email\": \"beckyroberts@example.org\",\n" +
        "    \"addressId\": 35,\n" +
        "    \"houseNumber\": 190,\n" +
        "    \"neighborhood\": \"Neighborhood 6\",\n" +
        "    \"city\": \"Miami\",\n" +
        "    \"zipCode\": \"14519\",\n" +
        "    \"region\": \"Region 6\",\n" +
        "    \"country\": \"United States\"\n" +
        "}";*/
        String json = "{\n" +
    "    \"city\": \" madrid\",\n" +
    "    \"country\": \"Spain\"\n" +
    "}";

        AddressDto.updateAddress(1, json);





    
    
    }
}
