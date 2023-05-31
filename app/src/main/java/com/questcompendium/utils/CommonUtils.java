package com.questcompendium.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.WindowManager;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommonUtils {

  public static JSONObject getDeviceDetails(Context foContext) {
    SharedPreferenceManager loSharedPreferenceManager = new SharedPreferenceManager(foContext);
    Calendar loCalender = Calendar.getInstance();

    JSONObject loJsonObject = new JSONObject();
    try {
      loJsonObject.put("api-level", android.os.Build.VERSION.SDK_INT);
      loJsonObject.put("versionName", getVersionName(foContext));
      loJsonObject.put("versionCode", getVersionCode(foContext));
      loJsonObject.put("deviceDateTime", getFormattedDateTime(loCalender.getTimeInMillis()));
      loJsonObject.put("manufacturer", getDeviceName());
      loJsonObject.put("deviceId", getDeviceUniqueId(foContext));
      loJsonObject.put("platformId", 1); // Android
      return loJsonObject;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getFormattedDateTime(long foTimeInMillis) {
    Date loDate = new Date(foTimeInMillis);

    Calendar loCalendar = Calendar.getInstance();
    loCalendar.setTime(loDate);

    return new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa", Locale.ENGLISH).format(loCalendar.getTime());
  }

  public static Map<String, String> getUrlValues(String url) throws UnsupportedEncodingException {
    int i = url.indexOf("?");
    Map<String, String> paramsMap = new HashMap<>();
    if (i > -1) {
      String searchURL = url.substring(url.indexOf("?") + 1);
      String params[] = searchURL.split("&");

      for (String param : params) {
        String temp[] = param.split("=");
        paramsMap.put(temp[0], java.net.URLDecoder.decode(temp[1], "UTF-8"));
      }
    }

    return paramsMap;
  }

  public static String getDeviceName() {
    String manufacturer = Build.MANUFACTURER;
    String model = Build.MODEL;
    if (model.startsWith(manufacturer)) {
      return capitalize(model);
    } else {
      return capitalize(manufacturer) + " " + model;
    }
  }

  private static String capitalize(String s) {
    if (s == null || s.length() == 0) {
      return "";
    }
    char first = s.charAt(0);
    if (Character.isUpperCase(first)) {
      return s;
    } else {
      return Character.toUpperCase(first) + s.substring(1);
    }
  }

  public static String getDeviceUniqueId(Context foContext) {
    return Settings.Secure.getString(foContext.getContentResolver(),
            Settings.Secure.ANDROID_ID);
  }

  public static String getVersionName(Context foContext) throws Exception {
    PackageInfo loPackageInfo = foContext.getPackageManager().getPackageInfo(foContext.getPackageName(), 0);
    return loPackageInfo.versionName;
  }

  public static int getVersionCode(Context foContext) throws Exception {
    PackageInfo loPackageInfo = foContext.getPackageManager().getPackageInfo(foContext.getPackageName(), 0);
    return loPackageInfo.versionCode;
  }

  public static void hideKeyboard(Activity foContext) {
    foContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
  }

  public static String validatePhoneNumber(String fsPhoneNumber) {

    if (TextUtils.isEmpty(fsPhoneNumber)) {
      return "Invalid phone number.";
    } else if (fsPhoneNumber.length() != 10) {
      return "Enter valid 10 digit number";
    }
    return "";
  }
}
