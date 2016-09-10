package com.example.avinashbehera.sabera.gcm;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.avinashbehera.sabera.Activity.BaseActivity;
import com.example.avinashbehera.sabera.R;
import com.example.avinashbehera.sabera.util.Constants;
import com.example.avinashbehera.sabera.util.PrefUtilsUser;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by avinashbehera on 02/09/16.
 */
public class GCMPushReceiverService extends FirebaseMessagingService {

    public static final String TAG = GCMPushReceiverService.class.getSimpleName();

    public static boolean condition_map = false;

    //This method will be called on every new message received
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG,"<Notification>" +
                "onMessageReceived"+"" +
                "<Notification>");
        String from = remoteMessage.getFrom();
        Log.d(TAG,"from = " + from);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "remoteMessage.getData().toString() " + remoteMessage.getData().toString());
            Log.d(TAG, "remoteMessage.getData(message).toString() " + remoteMessage.getData().get("message").toString());
            Map data = remoteMessage.getData();
            if(condition_map){
                handleData(data);
            }else{
                JSONParser parser = new JSONParser();
                try {
                    //JSONObject jsonObjRecv = (JSONObject)parser.parse(data.toString());
                    JSONObject jsonObjRecv = (JSONObject)parser.parse(data.get("message").toString());
                    Log.e(TAG,"jsonObjRecv = "+jsonObjRecv);
                    if(jsonObjRecv!=null && jsonObjRecv.size()>0){
                        handleData(jsonObjRecv);
                    }else{
                        Log.e(TAG,"jsonObjRecv = null or size = 0");
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }




        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


        //Displaying a notiffication with the message
        //sendNotification(data.toString());
    }

    private void handleData(Map data){

        String flag = data.get("flag").toString();
        if(flag.equals("match")){



        }else{

        }

    }

    private void handleData(JSONObject jsoObjRec){

        String flag = jsoObjRec.get("flag").toString();
        if(flag.equals("match")){

            Log.d(TAG,"flag - match");

            if(!isAppIsInBackground()){

                Log.d(TAG,"app is in foreground");
                Intent intent = new Intent(Constants.Intent_Push_Notification);
                intent.putExtra(Constants.EXTRA_Push_Not_Type,Constants.VALUE_Push_Not_Type_Match);
                intent.putExtra(Constants.EXTRA_Push_Not_Data,jsoObjRec.toJSONString());
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                playNotificationSound();

            }else{

                Log.d(TAG,"app is in background");
                Intent resultIntent = new Intent(getApplicationContext(),BaseActivity.class);
                resultIntent.putExtra(Constants.EXTRA_Push_Not_Type,Constants.VALUE_Push_Not_Type_Match);
                resultIntent.putExtra(Constants.EXTRA_Push_Not_Data,jsoObjRec.toJSONString());
                String title = "You have a match";
                JSONObject mUserJObj;
                //JSONObject mUserJObj = (JSONObject)jsonObjMatch.get(Constants.TAG_Push_Not_Match_Muser);
                JSONObject mQnJObj = (JSONObject)jsoObjRec.get(Constants.TAG_M_User_QA);
                String qnrId = mQnJObj.get(Constants.TAG_M_User_QA_qnrId).toString();
                if(qnrId.equals(PrefUtilsUser.getCurrentUser(this).getSaberaId())){
                    mUserJObj = (JSONObject)jsoObjRec.get(Constants.TAG_Push_Not_Match_Answr);
                }else{
                    mUserJObj = (JSONObject)jsoObjRec.get(Constants.TAG_Push_Not_Match_Qnr);
                }
                //JSONObject mUserJObj = (JSONObject)jsoObjRec.get(Constants.TAG_Push_Not_Match_Muser);
                String message = mUserJObj.get(Constants.TAG_Name).toString();
                String timeStamp = jsoObjRec.get(Constants.TAG_Push_Not_Match_timestamp).toString();
                sendNotification(title,message,timeStamp,resultIntent);




            }

        }
        // Msg type chat
        else{

            Log.d(TAG,"flag - chat");

            if(!isAppIsInBackground()){

                Log.d(TAG,"app is in foreground");
                Intent intent = new Intent(Constants.Intent_Push_Notification);
                intent.putExtra(Constants.EXTRA_Push_Not_Type,Constants.VALUE_Push_Not_Type_Chat);
                intent.putExtra(Constants.EXTRA_Push_Not_Data,jsoObjRec.toJSONString());
                intent.putExtra(Constants.EXTRA_Push_Not_Chat_Userid,jsoObjRec.get(Constants.TAG_M_User_Msgs_senderId).toString());
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                playNotificationSound();

            }else{

                Log.d(TAG,"app is in background");
                Intent resultIntent = new Intent(getApplicationContext(),BaseActivity.class);
                resultIntent.putExtra(Constants.EXTRA_Push_Not_Type,Constants.VALUE_Push_Not_Type_Chat);
                resultIntent.putExtra(Constants.EXTRA_Push_Not_Chat_Userid,jsoObjRec.get(Constants.TAG_M_User_Msgs_senderId).toString());
                resultIntent.putExtra(Constants.EXTRA_Push_Not_Data,jsoObjRec.toJSONString());
                String title = "New chat";
                String message = jsoObjRec.get(Constants.TAG_Push_Not_Chat_MsgTxt).toString();
                String timeStamp = jsoObjRec.get(Constants.TAG_Push_Not_Chat_timestamp).toString();
                sendNotification(title,message,timeStamp,resultIntent);

            }

        }



    }

    //This method is generating a notification and displaying the notification
    private void sendNotification(String title,String message,String timestamp,Intent intent) {

        if (TextUtils.isEmpty(message)){

            Log.e(TAG,"emptyMessage");
            return;

        }


        Context context = getApplicationContext();
        Log.d(TAG,"send Notification");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this).setTicker(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setSound(sound)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int)System.currentTimeMillis(), noBuilder.build()); //0 = ID of notification
    }

    public boolean isAppIsInBackground() {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public void playNotificationSound() {
        try {
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), sound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void clearNotifications() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
