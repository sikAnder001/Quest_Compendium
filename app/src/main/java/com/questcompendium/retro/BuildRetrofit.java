package com.questcompendium.retro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.questcompendium.interfaces.AppServices;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuildRetrofit {

//    public static final String BASE_URL = "http://newsnytro.com/ci/hotel-compendium/preprod/api/";
    public static final String BASE_URL = "https://quest.hotelcompendium.com.au/api/";
    public static final String BASE_URL_DIRECT = "https://api.openweathermap.org/data/2.5/";

    public static AppServices createAppService() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();

        Retrofit moRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return moRetrofit.create(AppServices.class);
    }

    public static AppServices createAppServiceDirect() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(50, TimeUnit.SECONDS)
                .connectTimeout(50, TimeUnit.SECONDS)
                .build();

        Retrofit moRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_DIRECT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return moRetrofit.create(AppServices.class);
    }


}
