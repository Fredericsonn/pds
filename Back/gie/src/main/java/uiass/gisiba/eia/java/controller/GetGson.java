package uiass.gisiba.eia.java.controller;

import com.google.gson.Gson;

// We're not using the singleton because it apparently using a single gson object causes problems with multiple operations
public class GetGson {

    private static volatile Gson gson;

    private GetGson() {}

    public static Gson getGson() {
        if (gson == null) {
            synchronized (GetGson.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }
}
