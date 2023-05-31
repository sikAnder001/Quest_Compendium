package com.questcompendium.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal.OSRemoteNotificationReceivedHandler;
import com.questcompendium.R;
import com.questcompendium.activity.MainActivity;
import com.questcompendium.sql.DBHelper;
import com.questcompendium.utils.LogV2;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.questcompendium.utils.Constants.IntentKey.NOTIFICATION;

public class NotificationServiceExtension implements OSRemoteNotificationReceivedHandler {

    private static final String TAG = "NotificationServiceExtension";

    @Override
    public void remoteNotificationReceived(Context context, OSNotificationReceivedEvent notificationReceivedEvent) {
        OSNotification notification = notificationReceivedEvent.getNotification();

        if (notification.getAdditionalData().length() > 0) {
            handleNow(context, notification.getAdditionalData());
            notificationReceivedEvent.complete(null);
        } else notificationReceivedEvent.complete(notification);
    }

    public static void insertNotificationInDB(Context foContext, JSONObject foJsonBody) {
        try {
            int liNotifyID = foJsonBody.getInt("fiNotifyId");
            String lsTitle = foJsonBody.getString("title");
            String lsMessage = foJsonBody.getString("message");
            String lsDate = foJsonBody.getString("fdDateTime");
            String lsImage = foJsonBody.getString("fsImg");
            String lsBtnTxt = foJsonBody.getString("btnTxt");
            String lsBtnLnk = foJsonBody.getString("btnLnk");

            DBHelper loDb = new DBHelper(foContext);
            LogV2.i(TAG, "INSERT FROM ONE SIGNAL");
            loDb.insertNotification(String.valueOf(liNotifyID), lsTitle, lsMessage, lsDate, lsImage, lsBtnTxt, lsBtnLnk);
            loDb.close();
        } catch (Exception e) {
            LogV2.logException(TAG, e);
        }
    }

    private void handleNow(Context foContext, JSONObject foJsonBody) {
        insertNotificationInDB(foContext, foJsonBody);
        try {
            String lsTitle = foJsonBody.getString("title");
            String lsMessage = foJsonBody.getString("message");
            String lsImage = foJsonBody.getString("fsImg");

            if (lsImage != null && !lsImage.isEmpty()) {
                final Bitmap[] bitmap = {null};
                Glide.with(foContext.getApplicationContext())
                        .asBitmap()
                        .load(lsImage)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                bitmap[0] = resource;
                                // TODO Do some work: pass this bitmap
                                Intent loIntent = new Intent(foContext, MainActivity.class);
                                loIntent.putExtra(NOTIFICATION, true);
                                PendingIntent loPendingIntent = PendingIntent.getActivity(foContext, 123 /* Request code */, loIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT);

                                sendNotification(foContext, lsTitle, lsMessage, loPendingIntent, bitmap[0]);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            } else {
                Intent loIntent = new Intent(foContext, MainActivity.class);
                loIntent.putExtra(NOTIFICATION, true);
                PendingIntent loPendingIntent = PendingIntent.getActivity(foContext, 123 /* Request code */, loIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                sendNotification(foContext, lsTitle, lsMessage, loPendingIntent, null);
            }
        } catch (Exception e) {
            LogV2.logException(TAG, e);
        }
    }

    private void sendNotification(Context foContext, String fsTitle, String fsMessage, PendingIntent foPendingIntent, Bitmap foBitmap) {

        String lsChannelId = foContext.getString(R.string.default_notification_channel_id);
        String lsChannel = "default-channel";
        Uri loDefaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder loNotificationBuilder =
                new NotificationCompat.Builder(foContext, lsChannelId)
                        .setSmallIcon(R.drawable.ic_small)
                        .setContentTitle(fsTitle)
                        .setContentText(fsMessage)
                        .setAutoCancel(true)
                        .setSound(loDefaultSoundUri)
                        .setContentIntent(foPendingIntent);

        if (foBitmap != null)
            loNotificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(foBitmap));

        NotificationManager loNotificationManager =
                (NotificationManager) foContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel loChannel = new NotificationChannel(lsChannelId, lsChannel, NotificationManager.IMPORTANCE_DEFAULT);
            loNotificationManager.createNotificationChannel(loChannel);
        }
        int id = createID();
        loNotificationManager.notify(id /* ID of notification */, loNotificationBuilder.build());
    }

    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.US).format(now));
        return id;
    }

}
