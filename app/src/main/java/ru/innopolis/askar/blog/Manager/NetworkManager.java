package ru.innopolis.askar.blog.Manager;

import android.content.ContentProvider;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 09.07.2017.
 */

public class NetworkManager {
    private static Response resp;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String IP_ADDRESS = "http://10.240.21.52:8085";

    public static Response connection(String json, String uri){
        Response response = null;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(IP_ADDRESS + uri)
                .post(body)
                .build();
        OkHttpClient httpClient = new OkHttpClient();
        try {
            response = httpClient.newCall(request).execute();
        } catch (IOException e) {
            Log.i("Что-то произошло! ", e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
    public static Request connection(MediaType contentType, byte[] bytes, String uri){
        RequestBody body = RequestBody.create(contentType, bytes);
        Request request = new Request.Builder()
                .url(IP_ADDRESS + uri)
                .post(body)
                .build();
        return request;
    }
    public static Response connection(String uri){
        Response response = null;
        Request request = new Request.Builder()
                .url(IP_ADDRESS + uri)
                .get()
                .build();
        OkHttpClient httpClient = new OkHttpClient();

        try {
            response = httpClient.newCall(request).execute();
        } catch (IOException e) {
            Log.i("Что-то произошло! ", e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
}
