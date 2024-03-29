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
    		  System.out.println(e.getMessage());
    	  }

        return body;
    }
    
    public static void postDataSender(String json, String entity) {

        // Create an instance of OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Define the request body

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        // Define the POST request
        Request request = new Request.Builder()
                .url("http://localhost:4567/" + entity + "/post")  
                .post(requestBody)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // Print the response body
                System.out.println(response.body().string());
            }
        });
    }

    public static void putDataSender(String json, String path) {

        // Create an instance of OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Define the request body

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        // Define the POST request
        Request request = new Request.Builder()
                .url("http://localhost:4567/" + path)  
                .put(requestBody)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // Print the response body
                System.out.println(response.body().string());
            }
        });
    }

    public static void getDataSender(String path) {

        // Create an instance of OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Define the POST request
        Request request = new Request.Builder()
                .url("http://localhost:4567/" + path)  // Change the URL to your endpoint
                .get()
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // Print the response body
                System.out.println(response.body().string());
            }
        });
    }

    public static void deleteDataSender(String path) {

        // Create an instance of OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Define the POST request
        Request request = new Request.Builder()
                .url("http://localhost:4567/" + path)  // Change the URL to your endpoint
                .delete()
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // Print the response body
                System.out.println(response.body().string());
            }
        });
    }
}
