package com.questcompendium.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.questcompendium.R;
import com.questcompendium.activity.MainActivity;
import com.questcompendium.sql.DBHelper;
import com.questcompendium.utils.LogV2;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static com.questcompendium.utils.Constants.IntentKey.NOTIFICATION;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        LogV2.i(TAG, "From body: " + remoteMessage.getFrom().toString());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            LogV2.i(TAG, "Message data payload: " + remoteMessage.getData());
            handleNow(remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            LogV2.i(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }
    // [END receive_message]
    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */

    @Override
    public void onNewToken(String token) {
        LogV2.i(TAG, "Refreshed token: " + token);
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Handle time allotted to BroadcastReceivers.
     *
     * @param foData
     */
    private void handleNow(Map<String, String> foData) {
        DBHelper loDb = new DBHelper(this);
        String loBody = foData.get("body");
        LogV2.i("notification-body", loBody);

        try {
            JSONObject loJsonBody = new JSONObject(loBody);
            int liNotifyID = loJsonBody.getInt("fiNotifyId");
            String lsTitle = loJsonBody.getString("fsTitle");
            String lsMessage = loJsonBody.getString("fsMessage");
            String lsDate = loJsonBody.getString("fdDateTime");

            LogV2.i(TAG, "insert..");
            //loDb.insertNotification(String.valueOf(liNotifyID), lsTitle, lsMessage, lsDate);

            Intent loIntent = new Intent(this, MainActivity.class);
            loIntent.putExtra(NOTIFICATION, true);
            PendingIntent loPendingIntent = PendingIntent.getActivity(this, 123 /* Request code */, loIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            sendNotification(lsTitle, lsMessage, loPendingIntent);
        } catch (Exception e) {
            LogV2.logException(TAG, e);
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param fsTitle   FCM Notification Title.
     * @param fsMessage FCM Notification Message.
     */
    private void sendNotification(String fsTitle, String fsMessage, PendingIntent foPendingIntent) {

        String lsChannelId = getString(R.string.default_notification_channel_id);
        String lsChannel = "default-channel";
        Uri loDefaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder loNotificationBuilder =
                new NotificationCompat.Builder(this, lsChannelId)
                        .setSmallIcon(R.drawable.ic_small)
                        .setContentTitle(fsTitle)
                        .setContentText(fsMessage)
                        .setAutoCancel(true)
                        .setSound(loDefaultSoundUri)
                        .setContentIntent(foPendingIntent);

        NotificationManager loNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel loChannel = new NotificationChannel(lsChannelId, lsChannel, NotificationManager.IMPORTANCE_DEFAULT);
            loNotificationManager.createNotificationChannel(loChannel);
        }
        int id = createID();
        loNotificationManager.notify(id /* ID of notification */, loNotificationBuilder.build());
    }

    public int createID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
        return id;
    }
}
