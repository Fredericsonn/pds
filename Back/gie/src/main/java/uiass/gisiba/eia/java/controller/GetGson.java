package uiass.gisiba.eia.java.controller;

import com.google.gson.Gson;

public class GetGson {
    
    private static Gson gson;

    private GetGson() {}

    public static Gson getGson() {

        if (gson == null) {

            gson = new Gson();

            return gson;
        }
        
        return gson;

    }
}
