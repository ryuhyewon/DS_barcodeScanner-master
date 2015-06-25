package com.example.bera.ds_barcodescanner.network;

import android.util.Log;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;


/**
 * Created by hyewon on 2015-06-25.
 */

public class RestRequestHelper {

    private static RestRequestHelper instance;

    private RestCookieManager cookieManager;
    private RestAdapter restAdapter;
    private RestRequest restRequest;

    public static RestRequestHelper newInstance() {
        if (instance == null) {
            instance = new RestRequestHelper();
        }
        return instance;
    }

    public RestRequestHelper() {
        cookieManager = new RestCookieManager();
        CookieHandler.setDefault(cookieManager);

        restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://54.65.32.198:8080-임의")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        // 쿠키 설정
                        request.addHeader("Cookie", cookieManager.getCurrentCookie());
                    }
                })
                .build();

        restRequest = restAdapter.create(RestRequest.class);

    }

    public interface RestRequest {


        @POST("/post")
        @Headers({ "Content-Type: application/json;charset=UTF-8"})
        void posts(@Body PostJsonRequest postJO,
                   Callback<JsonObject> callback);


        @GET("/auth/profile")
        void profile(Callback<JsonObject> callback);

        @GET("/post")
        void posts(@Query("user") String id,
                   @Query("barcode-type")int barcode,
                   @Query("name")String name, Callback<JsonObject> callback);

    }


    public void posts(String content, Callback<JsonObject> callback){
        PostJsonRequest postJO = null;
        try{
            postJO = new PostJsonRequest(content);
        } catch(Exception e){
            e.printStackTrace();
        }
        restRequest.posts(postJO, callback);
    }

    public void posts(String content,  ArrayList<String> filelist, Callback<JsonObject> callback){
        PostJsonRequest postJO = null;
        try{
            postJO = new PostJsonRequest(content,  filelist);
        } catch(Exception e){
            e.printStackTrace();
        }
        restRequest.posts(postJO, callback);
    }

    public void profile(Callback<JsonObject> callback) {
        restRequest.profile(callback);
    }

    public void posts(String id, int barcode,String name, Callback<JsonObject> callback){
        restRequest.posts(id, barcode,name, callback);
    }


}

class RestCookieManager extends CookieManager {

    private String currentCookie;

    @Override
    public void put(URI uri, Map<String, List<String>> stringListMap) throws IOException {
        super.put(uri, stringListMap);
        if (stringListMap != null && stringListMap.get("Set-Cookie") != null)
            for (String string : stringListMap.get("Set-Cookie")) {
                if (string.contains("session")) {
                    currentCookie = string;
                }
            }
    }

    public String getCurrentCookie() {
        return currentCookie;
    }
}

class PostJsonRequest{
    final String content;
    ArrayList<String> filelist = new ArrayList<String>();

    PostJsonRequest(String content, ArrayList<String> filelist){
        this.content = content;
        this.filelist = filelist;
        Log.i("file", filelist.toString());
    }

    PostJsonRequest(String content){
        this.content = content;

    }
}

//class UserJsonRequest {
//    final String username;
//    final String password;
//
//    UserJsonRequest(String username, String password) {
//        this.username = username;
//        this.password = password;
//    }
//
//}
