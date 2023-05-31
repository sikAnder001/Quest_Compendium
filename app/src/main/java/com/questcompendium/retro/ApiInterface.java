package com.questcompendium.retro;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiInterface {


    @POST("login")
    @FormUrlEncoded
    Call<ResponseBody> login(@Field("mobile") String username,
                             @Field("password") String password);

   /* @GET("profile")
    Call<MyProfile> getUserDetail(@Header("Authorization") String authentication);

    @GET("webinars")
    Call<MyWebinar> getWebinar(@Header("Authorization") String authentication);

    @GET("trades")
    Call<MyTred> getTrades(@Header("Authorization") String authentication);

    @GET("subscriptions")
    Call<ResponseBody> getSubscription(@Header("Authorization") String authentication);*/

}
