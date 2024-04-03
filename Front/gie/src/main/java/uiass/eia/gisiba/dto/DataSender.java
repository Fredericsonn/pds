package uiass.eia.gisiba.dto;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DataSender {

    public static String responseBodyGenerator(String url) {

        OkHttpClient client = new OkHttpClient();
    	
    	String body=null;
    	
    	Request request = new Request.Builder()
    	      .url(url)
    	      .build();

    	try (Response response = client.newCall(request).execute()) {
    	   
    		  body = response.body().string();
    		  
    	    }
    	       	  
    	  catch(IOException e ) {
    		  return "Server Error.";
    	  }

        return body;
    }
    
    public static String postDataSender(String json, String entity) {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url("http://localhost:4567/" + entity + "/post")  
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {

                throw new IOException("Unexpected code " + response);
            }
    
            return response.body().string();

        } catch (IOException e) {

            return "Internal Server Error."; 
        }
    }

    public static String putDataSender(String json, String path) {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url("http://localhost:4567/" + path)
                .put(requestBody)
                .build();
    
        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {

                throw new IOException("Unexpected code " + response);
            }
    
            return response.body().string();

        } catch (IOException e) {

            return e.getMessage(); 
        }
    }
    

    public static String getDataSender(String path) {

        // Create an instance of OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Define the POST request
        Request request = new Request.Builder()
                .url("http://localhost:4567/" + path)  // Change the URL to your endpoint
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {

                throw new IOException("Unexpected code " + response);
            }
    
            return "Data collected successfully";

        } catch (IOException e) {

            return "Internal Server Error."; 
        }
    }

    public static String deleteDataSender(String path) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://localhost:4567/" + path)  
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {

                throw new IOException("Unexpected code " + response);
            }
    
            return response.body().string();

        } catch (IOException e) {

            return "IGNORE IT"; 
        }
    }
    
}
