package com.questcompendium.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {

    SharedPreferences moSharedPreferences;
    private static final String TOKEN = "token";
    private static final String TITLE_IMG = "title_img";
    private static final String BACK_IMG = "back_img";
    private static final String IS_SET_TUTORIAL = "is_tutorial";
    private static final String HOTEL_ID = "hotel_id";

    public SharedPreferenceManager(Context foContext) {
        moSharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(foContext.getApplicationContext());
    }

    public void clear() {
        moSharedPreferences.edit().clear().commit();
    }

    //START isLogIN
    public void setTutorial(boolean fbIsAcceptedTC) {
        if (moSharedPreferences != null) {
            SharedPreferences.Editor loEditor = moSharedPreferences.edit();
            loEditor.putBoolean(IS_SET_TUTORIAL, fbIsAcceptedTC);
            loEditor.commit();
        }
    }

    public boolean isSetTutorial() {
        if (moSharedPreferences != null) {
            return moSharedPreferences.getBoolean(IS_SET_TUTORIAL, false);
        }
        return false;
    }
    //END isLogIn

    public void setFCMToken(String username) {
        if (moSharedPreferences != null) {
            SharedPreferences.Editor loEditor = moSharedPreferences.edit();
            loEditor.putString(TOKEN, username);
            loEditor.commit();
        }
    }

    public String getFCMToken() {
        if (moSharedPreferences != null) {
            return moSharedPreferences.getString(TOKEN, "");
        }
        return "";
    }

    public void setHotelId(String hotelId) {
        if (moSharedPreferences != null) {
            SharedPreferences.Editor loEditor = moSharedPreferences.edit();
            loEditor.putString(HOTEL_ID, hotelId);
            loEditor.commit();
        }
    }

    public String getHotelId() {
        if (moSharedPreferences != null) {
            return moSharedPreferences.getString(HOTEL_ID, "");
        }
        return "";
    }

    public void setTitleImg(String username) {
        if (moSharedPreferences != null) {
            SharedPreferences.Editor loEditor = moSharedPreferences.edit();
            loEditor.putString(TITLE_IMG, username);
            loEditor.commit();
        }
    }

    public String getTitleImg() {
        if (moSharedPreferences != null) {
            return moSharedPreferences.getString(TITLE_IMG, "");
        }
        return "";
    }

    public void setBackImg(String username) {
        if (moSharedPreferences != null) {
            SharedPreferences.Editor loEditor = moSharedPreferences.edit();
            loEditor.putString(BACK_IMG, username);
            loEditor.commit();
        }
    }

    public String getBackImg() {
        if (moSharedPreferences != null) {
            return moSharedPreferences.getString(BACK_IMG, "");
        }
        return "";
    }

}
