package com.questcompendium.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.questcompendium.R;

import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class Common {

    private static final String TAG = "Common";
    private static final String DATE_FORMAT = "dd-MM-yyyy";

    public static boolean isValidEmail(String fsEmail) {
        String EMAIL_ADDRESS_EXPRESSIONS = "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?";
        Pattern pattern = Pattern.compile(EMAIL_ADDRESS_EXPRESSIONS);
        Matcher matcher = pattern.matcher(fsEmail);

        boolean matchFound = matcher.matches();
        return matchFound;
    }

    public static void backpress(String msg, Activity activity, boolean isLogout) {
        final Dialog mDialougeBox = new Dialog(activity);
        mDialougeBox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialougeBox.setContentView(R.layout.simple_dialouge);
        mDialougeBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialougeBox.getWindow().setGravity(Gravity.CENTER);
        //mDialougeBox.setCancelable(false);

        try {
            if (!activity.isFinishing())
                mDialougeBox.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView tvOk = mDialougeBox.findViewById(R.id.tvOk);
        TextView tvText = mDialougeBox.findViewById(R.id.tvText);
        TextView tvCancel = mDialougeBox.findViewById(R.id.tvCancel);
        View view1 = mDialougeBox.findViewById(R.id.view1);
        // if (isSingout) {
        tvOk.setText("Yes");
        tvCancel.setText("No");
       /* } else {
            tvOk.setText("Ok");
            tvCancel.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        }*/

        if (isLogout) {
            tvOk.setText("Yes");
            tvCancel.setText("No");
        } else {
            tvOk.setText("Close");
            tvCancel.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        }

        tvText.setText(msg);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialougeBox.hide();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialougeBox.hide();
                try {
                    activity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    public static DiskCacheConfig getDefaultMainDiskCacheConfig(final Context context) {
//        return DiskCacheConfig.newBuilder(context)
//                .setBaseDirectoryPathSupplier(
//                        new Supplier<File>() {
//                            @Override
//                            public File get() {
//                                return context.getApplicationContext().getCacheDir();
//                            }
//                        })
//                .setBaseDirectoryName("image_cache")
//                .setMaxCacheSize(40 * ByteConstants.MB)
//                .setMaxCacheSizeOnLowDiskSpace(10 * ByteConstants.MB)
//                .setMaxCacheSizeOnVeryLowDiskSpace(2 * ByteConstants.MB)
//                .build();
//    }

    public static String getCurrentDate() {
        SimpleDateFormat loDateFormat = new SimpleDateFormat(DATE_FORMAT);
        String lsCurrentDate = loDateFormat.format(System.currentTimeMillis());
        return lsCurrentDate;
    }

    public static void isConnectNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        //  LogV2.i(TAG, "network conection..." + isConnected);
        // LogV2.i(TAG, "isIdleMode..." + isIdleMode(context));
        // Log.d(TAG, "isDozing..." + isDozing(context));
    }

    public static void showErrorDialog(Context foContext, String lsMessage, DialogInterface.OnClickListener foListener) {

        new AlertDialog.Builder(foContext).setMessage(lsMessage)
                .setPositiveButton("Ok", foListener).show();
    }

    public static void showErrorDialog(final Activity foContext, String lsMessage, boolean isFinish) {
        clickSingoutDialog(lsMessage, foContext, false);
    }

    public static void clickSingoutDialog(String msg, Activity activity, boolean isSingout) {
        final Dialog mDialougeBox = new Dialog(activity);
        mDialougeBox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialougeBox.setContentView(R.layout.simple_dialouge);
        mDialougeBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialougeBox.getWindow().setGravity(Gravity.CENTER);
        //mDialougeBox.setCancelable(false);

        try {
            if (!activity.isFinishing())
                mDialougeBox.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView tvOk = mDialougeBox.findViewById(R.id.tvOk);
        TextView tvText = mDialougeBox.findViewById(R.id.tvText);
        TextView tvCancel = mDialougeBox.findViewById(R.id.tvCancel);
        View view1 = mDialougeBox.findViewById(R.id.view1);
        if (isSingout) {
            tvOk.setText("Yes");
            tvCancel.setText("No");
        } else {
            tvOk.setText("Ok");
            tvCancel.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        }

        tvText.setText(msg);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialougeBox.hide();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialougeBox.hide();
            }
        });
    }

    public static String getErrorMessage(Response<ResponseBody> foResponse) {
        String fsMessage = "Something went wrong..!!, Please try again..";

        try {
            if (foResponse != null && foResponse.body() != null) {
                String json = foResponse.body().string();
                JSONObject jsonObject1 = new JSONObject(json);
                if (jsonObject1.has("message")) {
                    fsMessage = jsonObject1.getString("message");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (foResponse.code()) {
            case 401:
                break;
            case 404:
                fsMessage = "404 Page Not Found";
                break;
            default:
                break;
        }

        return fsMessage;
    }

    public static void printReqRes(Object foObject, String fsMethodName, int fiType) {
        try {
            String lsLog = null;
            if (fiType == Constants.API_REQUEST) {
                lsLog = "REQUEST: " + fsMethodName + "  >> " + getJsonFromObject(foObject);
            } else if (fiType == Constants.API_RESPONSE) {
                lsLog = "RESPONSE: " + fsMethodName + "  >> " + getJsonFromObject(foObject);
            } else {
                lsLog = "ERROR: " + fsMethodName + "  >> " + getJsonFromObject(foObject);
            }
            LogV2.i(TAG, lsLog);
        } catch (Exception e) {
            LogV2.logException(TAG, e);
        }
    }

    public static String getJsonFromObject(Object foObject) {
        try {
            Gson gson = new Gson();
            return gson.toJson(foObject);
        } catch (Exception e) {
            LogV2.logException(TAG, e);
        }
        return null;
    }

    public static String getString(EditText foEditText) {
        return foEditText.getText().toString().trim();
    }

    public static String getDateFormat(int Year, int Month, int Date) {
        try {
            return (new StringBuilder()
                    .append(Year).append("-")
                    .append((Month <= 9 ? "0" : "")).append(Month + "-")
                    .append((Date <= 9 ? "0" : "")).append(Date)).toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String setStringValue(JSONObject foJsonResponse, String fsKey) {
        String fsValue;
        if (foJsonResponse.isNull(fsKey)) {
            fsValue = "";
        } else {
            fsValue = foJsonResponse.optString(fsKey);
        }
        return fsValue;
    }

    public static double setDoubleValue(JSONObject foJsonResponse, String fsKey) {
        double fsValue;
        if (foJsonResponse.isNull(fsKey)) {
            fsValue = 0;
        } else {
            fsValue = foJsonResponse.optDouble(fsKey);
        }
        return fsValue;
    }

    public static int setIntValue(JSONObject foJsonResponse, String fsKey) {
        int fsValue;
        if (foJsonResponse.isNull(fsKey)) {
            fsValue = 0;
        } else {
            fsValue = foJsonResponse.optInt(fsKey);
        }
        return fsValue;
    }

    public static boolean setBooleanValue(JSONObject foJsonResponse, String fsKey) {
        boolean fbIsEnable;
        if (foJsonResponse.optInt(fsKey) == 1) {
            fbIsEnable = true;
        } else {
            fbIsEnable = false;
        }
        return fbIsEnable;
    }

    public static double setDoubleValue(String fsKey) {
        double fsValue;
        try {
            if (TextUtils.isEmpty(fsKey)) {
                fsValue = 0;
            } else {
                fsValue = Double.parseDouble(fsKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fsValue = 0;
        }
        return fsValue;
    }

    public static String convertDateIntoFormate(String inputDate) {
        // inputDate = inputDate.replace("T", " ");
        if (inputDate != null) {

            DateFormat outputFormat = new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN_APP, Locale.US);
            DateFormat inputFormat = new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN, Locale.US);

            //String inputText = "2012-11-17T00:00:00.000-05:00";
            Date date = null;
            try {
                date = inputFormat.parse(inputDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return outputFormat.format(date);
        } else {
            return null;
        }
    }

    public static String getTimeFromDate(String foMydate) {
        //System.out.println("foMydate : " + foMydate);
        DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN);
        Date date = null;// converting String to date
        try {
            date = df.parse(foMydate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH a");//HH:mm:ss //HH:mm a
        String time = localDateFormat.format(date);


//        System.out.println(df.format(date));
        return time;//String.valueOf(df.format(date));
    }

    public static long printDifference(String startDate, String endDate) {

        //  DateTimeUtils obj = new DateTimeUtils();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        Date date1 = null, date2 = null;
        try {
            date1 = simpleDateFormat.parse(startDate);
            date2 = simpleDateFormat.parse(endDate);

            // obj.printDifference(date1, date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //milliseconds
        long different = date2.getTime() - date1.getTime();

        /*System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);*/

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return elapsedHours;
    }

    public static void openBrowser(Context foContext, String Url) {
        if (!Url.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
            foContext.startActivity(intent);
        }
    }
}
