package network;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit.Callback;
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

//    private RestCookieManager cookieManager;
    private RestAdapter restAdapter;
    private RestRequest restRequest;

    public static RestRequestHelper newInstance() {
        if (instance == null) {
            instance = new RestRequestHelper();
        }
        return instance;
    }

    public RestRequestHelper() {

        restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.eatsight.com/FoodInfo/client/check_bcd.do")
                .setLogLevel(RestAdapter.LogLevel.FULL)

                .build();

        restRequest = restAdapter.create(RestRequest.class);

    }

    public interface RestRequest {


        @POST("/post")
        @Headers({ "Content-Type: bacode/json;charset=UTF-8"})
        void posts(@Body PostJsonRequest postJO,
                   Callback<JsonObject> callback);


        @GET("/auth/profile")
        void profile(Callback<JsonObject> callback);

        @GET("/post")
        void posts(@Query("user") String id,
                   @Query("barcode-type") int barcode,
                   @Query("name") String name, Callback<JsonObject> callback);

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

