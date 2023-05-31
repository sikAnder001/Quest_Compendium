package com.questcompendium.interfaces;

import com.google.gson.JsonObject;
import com.questcompendium.model.Category;
import com.questcompendium.model.Facility;
import com.questcompendium.model.Hotel;
import com.questcompendium.model.Menu;
import com.questcompendium.model.MenuN;
import com.questcompendium.model.SearchList;
import com.questcompendium.model.Temp;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface AppServices {

    //    @POST("doAgentLogin")
//    Call<AgentLoginResponse> doAgentLogin(@Body AgentLoginRequest loAgentLoginRequest);
    @POST("getHotelList")
    @FormUrlEncoded
    Call<Hotel> getHotel(@Field("fdLat") String latitude,
                         @Field("fdLong") String longitute,
                         @Field("fsSearch") String search);

    @POST("getHotelMenu")
        //@FormUrlEncoded
    Call<Menu> getHotelMoreDetail(@Body JsonObject root/*@Field("fiHotelsId") String hotelId*/);

    @POST("getMenubarList")
    @FormUrlEncoded
    Call<MenuN> getMenuBarList(@Field("fiHotelId") String hotelId);

    // @POST("getHotelFacility")
    @POST("getCategory")
    @FormUrlEncoded
    Call<Category> getHotelCategory(@Field("fiHotelId") String hotelId,
                                    @Field("fiMenuId") String menuId);

    @POST("getCategory")
    @FormUrlEncoded
    Call<Category> getAboutUs();

    @POST("getCategorySerachValue")
    @FormUrlEncoded
    Call<SearchList> getSearchValue(@Field("fiHotelsId") String fiHotelsId,
                                    @Field("fsSearch") String fsSearch);

    @POST("getHotelFacility")
    @FormUrlEncoded
    Call<Facility> getHotelFacility(@Field("fiHotelId") String hotelId,
                                    @Field("fiMenuId") String menuId,
                                    @Field("fiCategoryId") String fsCatId);

    /*@GET("weather?lat=" + "{lat}" + "&lon=" + "{long}" + "&appid=e7b2054dc37b1f464d912c00dd309595&units=Metric")
    @FormUrlEncoded
    Call<ResponseBody> getCurrTemp(@Query("lat") String lat,
                                   @Query("lon") String lon);*/

    @GET
    Call<Temp> getCurrTemp(@Url String url);

    @POST("hotelAnalytics")
    @FormUrlEncoded
    Call<Facility> postHotelAnalytics(
            @Field("fiHotelId") String fiHotelId,
            @Field("is_scan") String is_scan,
            @Field("menu_title") String menu_title,
            @Field("menu_id") String menu_id);

}
