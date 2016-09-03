package com.example.avinashbehera.sabera.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.avinashbehera.sabera.Activity.BaseActivity;
import com.example.avinashbehera.sabera.R;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by avinashbehera on 02/09/16.
 */
public class GCMPushReceiverService extends FirebaseMessagingService {

    public static final String TAG = GCMPushReceiverService.class.getSimpleName();

    //This method will be called on every new message received
    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.d(TAG,"<Notification>" +
                "onMessageReceived"+"" +
                "<Notification>");
        String from = message.getFrom();
        Map data = message.getData();
        //Displaying a notiffication with the message
        sendNotification(data.toString());
    }

    //This method is generating a notification and displaying the notification
    private void sendNotification(String message) {
        Log.d(TAG,"send Notification");
        Intent intent = new Intent(this, BaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int)System.currentTimeMillis(), noBuilder.build()); //0 = ID of notification
    }
}
