package com.tripNetra.extranet.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tripNetra.extranet.Dashboard_Main_Act;
import com.tripNetra.extranet.Login_Main_Act;
import com.tripNetra.extranet.R;
import com.tripNetra.extranet.bookings.Book_Voucher_Act;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FBase_Messaging_Service extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    String pnr ;
    String id  ;
    String status ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String Title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String Message = remoteMessage.getNotification().getBody();
        String Action = remoteMessage.getNotification().getClickAction();


        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            pnr = remoteMessage.getData().get("pnr");
            id = remoteMessage.getData().get("fid");

        }

        sendNotification(Title, Message, Action,pnr, id);

    }

    private void sendNotification(String Title, String Message,String Action, String pnr, String id) {

        Bundle b = new Bundle();
        b.putString("pnr",pnr);
        b.putString("fid",id);

        Intent intent = new Intent(Action);
        intent.putExtras(b);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri Sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.default_notification_channel_id),
                    "Extranettest", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(Title);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getResources().getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.logo_white)
                .setContentTitle(Title)
                .setContentText(Message)
                .setAutoCancel(true)
                .setSound(Sound)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setLights(Color.YELLOW, 1000, 300);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        assert notificationManager != null;
        notificationManager.notify(m, builder.build());
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i("tk",s);
    }
}