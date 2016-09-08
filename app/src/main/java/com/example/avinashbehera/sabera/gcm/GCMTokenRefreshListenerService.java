package com.example.avinashbehera.sabera.gcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.avinashbehera.sabera.R;
import com.google.android.gms.iid.InstanceIDListenerService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by avinashbehera on 02/09/16.
 */
public class GCMTokenRefreshListenerService extends FirebaseInstanceIdService {

    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String TAG = GCMTokenRefreshListenerService.class.getSimpleName();
    public String refreshedToken = null;

    @Override
    public void onTokenRefresh() {

        Log.d(TAG,"onTokenRefresh");

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if(refreshedToken==null){
            Log.d(TAG,"refreshedToken = null");
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor;
        editor = sharedPreferences.edit();
        editor.putString("firebaseToken",refreshedToken);
        editor.apply();
        //SharedPreferenceUtils.getInstance(this).setValue(getString("firebaseToken"), refreshedToken);
        Intent registrationComplete = new Intent(REGISTRATION_SUCCESS);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        //Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        //startService(intent);
    }

}
