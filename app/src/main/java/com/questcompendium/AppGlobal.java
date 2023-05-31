package com.questcompendium;

import android.app.Application;
import android.content.Intent;

import com.onesignal.OSInAppMessageAction;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.questcompendium.activity.MainActivity;
import com.questcompendium.fcm.NotificationServiceExtension;
import com.questcompendium.interfaces.AppServices;
import com.questcompendium.retro.BuildRetrofit;
import com.questcompendium.utils.DiscreteScrollViewOptions;
import com.questcompendium.utils.SharedPreferenceManager;

import org.json.JSONObject;

import static com.questcompendium.utils.Constants.IntentKey.NOTIFICATION;

/**
 * Created by Chirag on 30/12/2019.
 */

public class AppGlobal extends Application {

    private static final String ONESIGNAL_APP_ID = "7607c112-f44b-4292-a959-81011d318328";

    public static AppServices moAppService, moAppServiceDirect;
    private SharedPreferenceManager moSharedPreferenceManager;
    private static AppGlobal instance;

    public static AppGlobal getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        moAppService = BuildRetrofit.createAppService();
        moAppServiceDirect = BuildRetrofit.createAppServiceDirect();
        moSharedPreferenceManager = new SharedPreferenceManager(this);
        DiscreteScrollViewOptions.init(this);

        configureOneSignal();
    }

    private void configureOneSignal() {
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        OneSignal.setNotificationOpenedHandler(new OneSignal.OSNotificationOpenedHandler() {
            @Override
            public void notificationOpened(OSNotificationOpenedResult result) {
                Intent loIntent = new Intent(getInstance(), MainActivity.class);
                loIntent.putExtra(NOTIFICATION, true);
                startActivity(loIntent);
            }
        });

        OneSignal.unsubscribeWhenNotificationsAreDisabled(true);
    }
}
