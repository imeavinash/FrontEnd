package com.example.avinashbehera.sabera.util;

import android.widget.Toast;

/**
 * Created by avinashbehera on 24/08/16.
 */
public class Constants {

    public static final String TAG_UserFBId = "fb_id";
    public static final String TAG_UserSaberaId = "user_sabera_id";
    public static final String TAG_Name = "fullName";
    public static final String TAG_Email = "fb_email";
    public static final String TAG_Gender = "sex";
    public static final String TAG_Birthday = "birthday";
    public static final String TAG_User_Exists_Status = "user_exists_status";


    public static final String EXTRA_UserFBId = "fb_id";
    public static final String EXTRA_Name = "fullName";
    public static final String EXTRA_Email = "fb_email";
    public static final String EXTRA_Gender = "sex";
    public static final String EXTRA_Birthday = "birthday";

    public static final int toastLengthShort = Toast.LENGTH_SHORT;
    public static final int toastLengthLong = Toast.LENGTH_LONG;

    public static final boolean backendTest = false;

    public static final String getSaberaIdURL = "http://192.168.0.189:9000/fblogin";
    public static final String checkIfUserExistsURL = "http://192.168.0.189:9000/fblogin";
    public static final String getUserDataFromServerURL = "http://192.168.0.189:9000/fblogin";
    public static final String sendDataToServerForRegistrationURL = "http://192.168.0.189:9000/fblogin";


}
