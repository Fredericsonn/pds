package uiass.gisiba.eia.java.dao.inventory;

import java.util.Random;

public class ProductRefGenerator {

    public static String generateProductRef() {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder sb = new StringBuilder(6);

        Random random = new Random();

        for (int i = 0; i < 6; i++) {

            int randomIndex = random.nextInt(characters.length());

            char randomChar = characters.charAt(randomIndex);

            sb.append(randomChar);
        }

        return sb.toString();
    }

}
