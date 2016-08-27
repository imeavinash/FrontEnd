package com.example.avinashbehera.sabera.util;

import android.widget.Toast;

/**
 * Created by avinashbehera on 24/08/16.
 */
public class Constants {

    public static final String TAG_UserFBId = "fb_id";
    public static final String TAG_UserSaberaId = "userid";
    public static final String TAG_Name = "fullname";
    public static final String TAG_Email = "email";
    public static final String TAG_Gender = "sex";
    public static final String TAG_Birthday = "dob";
    public static final String TAG_CATEGORIES = "preferred_categories";
    public static final String TAG_QUESTIONS = "questions";
    public static final String TAG_User_Exists_Status = "status";
    public static final String TAG_PASSWORD = "password";
    public static final String TAG_LOGIN_MODE = "login_mode";

    public static final int REQUEST_CODE_CATEGORY = 1;

    public static final String REQUEST_CATEGORY_REGISTRATION="registration_category";
    public static final String REQUEST_CATEGORY_POSTQN="postqn_category";

    public static final String EXTRA_PICK_CATEGORY="PICK_CATEGORY";
    public static final String EXTRA_RESULT_CATEGORY="RESULT_CATEGORY";


    public static final String EXTRA_UserFBId = "fb_id";
    public static final String EXTRA_Name = "fullName";
    public static final String EXTRA_Email = "fb_email";
    public static final String EXTRA_Gender = "sex";
    public static final String EXTRA_Birthday = "birthday";

    public static final int toastLengthShort = Toast.LENGTH_SHORT;
    public static final int toastLengthLong = Toast.LENGTH_LONG;

    public static final boolean backendTest = true;



    public static final String FirstPageLoginButtonSendDataToServerURL = "http://192.168.0.189:9000/checkUserIfSaberaUserExists";
    public static final String RegisterButtonSendDataToServerURL = "http://192.168.0.189:9000/register";
    public static final String RegDetailDoneButtonSendDataToServer = "http://192.168.0.189:9000/storeUserProfile";
    public static final String FbLoginCheckURL = "http://192.168.0.189:9000/checkUserIfFbUserExists";
    public static final String sendPostQnToServerURL = "http://192.168.0.189:9000/postQuestion";


    public static final int POST_QN_TYPE_NONE = 0;
    public static final int POST_QN_TYPE_OBJECTIVE = 1;
    public static final int POST_QN_TYPE_SUBJECTIVE = 2;

    public static final String TAG_PostQn_UserSaberaID = "userid";
    public static final String TAG_PostQn_QnType = "qtype";
    public static final String TAG_PostQn_Qn_Text = "question";
    public static final String TAG_PostQn_Ans_Text = "proposed_answer";
    public static final String TAG_PostQn_Options = "options";
    public static final String TAG_PostQn_Correct_Options = "correct_options";
    public static final String TAG_PostQn_Timer = "timer";
    public static final String TAG_PostQn_Hint = "hints";
    public static final String TAG_PostQn_TimeStamp = "post_time";
    public static final String TAG_PostQn_Categories = "categories";
    public static final String TAG_PostQn_Option1 = "option1";
    public static final String TAG_PostQn_Option2 = "option2";
    public static final String TAG_PostQn_Option3 = "option3";
    public static final String TAG_PostQn_Option4 = "option4";
    public static final String TAG_PostQn_Option1_Status = "status1";
    public static final String TAG_PostQn_Option2_Status = "status2";
    public static final String TAG_PostQn_Option3_Status = "status3";
    public static final String TAG_PostQn_Option4_Status = "status4";
    public static final String TAG_PostQn_Keywords = "keywords";
    public static final String TAG_PostQn_Status = "status";
    public static final String VALUE_PostQn_Objective = "objective";
    public static final String VALUE_PostQn_Subjective = "subjective";





}
