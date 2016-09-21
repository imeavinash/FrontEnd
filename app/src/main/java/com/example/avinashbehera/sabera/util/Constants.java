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
    public static final String TAG_Image_String = "image_string";
    public static final String TAG_Passcode = "passcode";

    public static final int REQUEST_CODE_CATEGORY = 1;
    public static final int REQUEST_SELECT_PICTURE = 5;
    public static final int REQUEST_CODE_PICK_KEYWORDS = 6;

    public static final String REQUEST_CATEGORY_REGISTRATION="registration_category";
    public static final String REQUEST_CATEGORY_POSTQN="postqn_category";

    public static final String EXTRA_PICK_CATEGORY="PICK_CATEGORY";
    public static final String EXTRA_RESULT_CATEGORY="RESULT_CATEGORY";
    public static final String EXTRA_RESULT_PICK_KEYWORDS="RESULT_KEYWORDS";


    public static final String EXTRA_UserFBId = "fb_id";
    public static final String EXTRA_Name = "fullName";
    public static final String EXTRA_Email = "fb_email";
    public static final String EXTRA_Gender = "sex";
    public static final String EXTRA_Birthday = "birthday";
    public static final String EXTRA_Chat_UserId = "chat_user_id";
    public static final String EXTRA_Chat_Name = "chat_name";

    public static final String Intent_Push_Notification = "push_notification";
    public static final String EXTRA_Push_Not_Type = "push_not_type";
    public static final String VALUE_Push_Not_Type_Match = "match";
    public static final String VALUE_Push_Not_Type_Chat = "chat";
    public static final String EXTRA_Push_Not_Data = "push_not_data";
    public static final String EXTRA_Push_Not_Chat_Userid = "userid";


    public static final int toastLengthShort = Toast.LENGTH_SHORT;
    public static final int toastLengthLong = Toast.LENGTH_LONG;

    public static final boolean backendTest = true;

    //public static final String baseURL = "http://52.66.47.149:9000/";
    public static final String baseURL = "http://192.168.0.189:9000/";



    public static final String FirstPageLoginButtonSendDataToServerURL = baseURL+"checkUserIfSaberaUserExists";
    public static final String RegisterButtonSendDataToServerURL = baseURL+"register";
    public static final String RegDetailDoneButtonSendDataToServer = baseURL+"storeUserProfile";
    public static final String FbLoginCheckURL = baseURL+"checkUserIfFbUserExists";
    public static final String sendPostQnToServerURL = baseURL+"postQuestion";
    public static final String sendAnswerToServerURL = baseURL+"postAnswer";
    public static final String sendPassAnswerToServerURL = baseURL+"passQuestion";
    public static final String sendGcmTokenLoginToServerURL = baseURL+"storeDeviceToken";
    public static final String sendGcmTokenLogoutToServerURL = baseURL+"logout";
    public static final String sendChatToServerURL = baseURL+"storeChat";
    public static final String sendPicToServerURL = baseURL+"updateProfilePicture";
    public static final String loadQnsRequestURL = baseURL+"loadMoreQuestions";
    public static final String sendEmailRegURL = baseURL+"checkRegistration";
    public static final String resendPasscodeURL = baseURL+"resendPassCode";


    public static final int POST_QN_TYPE_NONE = 0;
    public static final int POST_QN_TYPE_OBJECTIVE = 1;
    public static final int POST_QN_TYPE_SUBJECTIVE = 2;

    public static final String TAG_FCM_Token = "device_token";

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

    public static final String TAG_SEEQN_QN_ID = "qid";
    public static final String TAG_SEEQN_QNR_ID = "uid";
    public static final String TAG_SEEQN_QN_TEXT = "qstring";
    public static final String TAG_SEEQN_QN_TYPE = "qtypes";
    public static final String TAG_SEEQN_OPTION1 = "option1";
    public static final String TAG_SEEQN_OPTION2 = "option2";
    public static final String TAG_SEEQN_OPTION3 = "option3";
    public static final String TAG_SEEQN_OPTION4 = "option4";
    public static final String TAG_SEEQN_Option1_Status = "status1";
    public static final String TAG_SEEQN_Option2_Status = "status2";
    public static final String TAG_SEEQN_Option3_Status = "status3";
    public static final String TAG_SEEQN_Option4_Status = "status4";
    public static final String TAG_SEEQN_Ans_Text = "proposed_answer";
    public static final String TAG_SEEQN_Keywords = "proposed_keywords";
    public static final String TAG_SEEQN_Timer = "timer";
    public static final String TAG_SEEQN_Hint = "hints";
    public static final String VALUE_SEEQN_Objective = "objective";
    public static final String VALUE_SEEQN_Subjective = "subjective";

    public static final String TAG_AnsQn_Qn_ID = "qid";
    public static final String TAG_AnsQn_UserId_Qnr = "uid_questioner";
    public static final String TAG_AnsQn_UserId_Answr = "uid_answerer";
    public static final String TAG_AnsQn_Qn_Type = "qtype";
    public static final String TAG_AnsQn_Ans_Qnr = "answer_questioner";
    public static final String TAG_AnsQn_Ans_Answr = "answer_answerer";
    public static final String TAG_AnsQn_answers = "answer";
    public static final String TAG_AnsQn_timestamp = "answer_time";

    public static final String TAG_SendRegToken_token = "device_token";
    public static final String TAG_SendRegToken_userId = "uid";

    public static final String TAG_M_User = "matched_users";
    public static final String TAG_M_User_QA = "QandA";
    public static final String TAG_M_User_QA_qTxt = "qstring";
    public static final String TAG_M_User_QA_qnrId = "quesid";
    public static final String TAG_M_User_QA_ansId = "ansid";
    public static final String TAG_M_User_QA_pAns = "proposed_keywords";
    public static final String TAG_M_User_QA_aAns = "attempted_answer";
    public static final String TAG_M_User_QA_keywords = "answer";
    public static final String TAG_M_User_QA_answers = "answer";
    public static final String TAG_M_User_Msgs = "chats";
    public static final String TAG_M_User_Msgs_Id = "chat_id";
    public static final String TAG_M_User_Msgs_Txt = "message";
    public static final String TAG_M_User_Msgs_time = "time_stamp";
    public static final String TAG_M_User_Msgs_senderId = "uid_sender";
    public static final String TAG_M_User_Msgs_receiverId = "uid_receiver";

    public static final String TAG_Send_Chat_Msg_Txt = "message";
    public static final String TAG_Send_Chat_SenderId = "uid_sender";
    public static final String TAG_Send_Chat_ReceiverId = "uid_receiver";
    public static final String TAG_Send_Chat_Timestamp = "time_stamp";
    public static final String TAG_Send_Chat_Response = "status";
    public static final String TAG_Send_Chat_Msg_Id = "chat_id";


    public static final String TAG_Push_Not_Match_Qnr = "questioner";
    public static final String TAG_Push_Not_Match_Answr = "answerer";
    public static final String TAG_Push_Not_Match_timestamp = "timestamp";
    public static final String TAG_Push_Not_Chat_UserId = "chat_id";
    public static final String TAG_Push_Not_Chat_MsgTxt = "message";
    public static final String TAG_Push_Not_Chat_timestamp = "time_stamp";







}
