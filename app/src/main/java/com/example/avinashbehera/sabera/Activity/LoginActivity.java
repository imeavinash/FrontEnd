package com.example.avinashbehera.sabera.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.content.pm.Signature;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.avinashbehera.sabera.R;
import com.example.avinashbehera.sabera.model.MatchedUser;
import com.example.avinashbehera.sabera.model.MatchedUserQn;
import com.example.avinashbehera.sabera.model.Message;
import com.example.avinashbehera.sabera.model.MsgFrgmChatHead;
import com.example.avinashbehera.sabera.model.User;
import com.example.avinashbehera.sabera.model.UserSeeQn;
import com.example.avinashbehera.sabera.util.Constants;
import com.example.avinashbehera.sabera.util.PrefUtilsTempUser;
import com.example.avinashbehera.sabera.util.PrefUtilsUser;
import com.example.avinashbehera.sabera.network.HttpClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

//import org.json.simple.simple.*;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;


public class LoginActivity extends AppCompatActivity {

    LoginButton fbLoginButton;
    CallbackManager callbackManager;
    User user;
    private static final String TAG = LoginActivity.class.getSimpleName();
    public AccessToken mAccessToken;
    private EditText emailEdtTxt;
    private EditText pwdEdtTxt;
    private Button loginButton;
    private Button regButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("LoginActivity","TAG = "+TAG);


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.avinashbehera.sabera",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }




        user = PrefUtilsUser.getCurrentUser(LoginActivity.this);

        if (user != null && user.getName()!=null) {

            Log.d("LoginActivity","user!=null && user.getName() != null");

            Intent homeIntent = new Intent(LoginActivity.this, BaseActivity.class);

            startActivity(homeIntent);

            finish();
        }


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        AppEventsLogger.activateApp(this);

        emailEdtTxt=(EditText)findViewById(R.id.emailEdtTxt);
        pwdEdtTxt = (EditText)findViewById(R.id.pwdEdtTxt);
        loginButton = (Button) findViewById(R.id.btnLogin);
        regButton = (Button) findViewById(R.id.btnRegister);

        regButton.setOnClickListener(regButtonClickListener);
        loginButton.setOnClickListener(loginButtonClickListener);




        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        fbLoginButton.setReadPermissions(Arrays.asList(
                "public_profile","email","user_birthday"));

        fbLoginButton.registerCallback(callbackManager, mCallBack);
        if(checkGooglePlayServices()){

            Toast.makeText(this,"Google Play services is updated",Toast.LENGTH_LONG).show();

        }


    }

    private boolean checkGooglePlayServices() {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            Log.e(TAG, GooglePlayServicesUtil.getErrorString(status));

            // ask user to update google play services.
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, 1);
            dialog.show();
            return false;
        } else {
            Log.i(TAG, GooglePlayServicesUtil.getErrorString(status));
            // google play services is updated.
            //your code goes here...
            return true;
        }
    }

    View.OnClickListener loginButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String email = emailEdtTxt.getText().toString();
            String pwd = pwdEdtTxt.getText().toString();

            if(email == null || email.equalsIgnoreCase("")){
                Toast.makeText(LoginActivity.this,"Please enter email",Toast.LENGTH_LONG).show();
                return;
            }
            if(pwd == null || pwd.equalsIgnoreCase("")){
                Toast.makeText(LoginActivity.this,"Please enter password",Toast.LENGTH_LONG).show();
                return;
            }
            if(pwd.length()<6){
                Toast.makeText(LoginActivity.this,"Password must be at least 6 characters",Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject jsonObjectSend = new JSONObject();
            jsonObjectSend.put(Constants.TAG_Email,email);
            jsonObjectSend.put(Constants.TAG_PASSWORD,pwd);
            jsonObjectSend.put(Constants.TAG_LOGIN_MODE,"non_fb");
            if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                new firstPageLoginButtonSendDataToServer().execute(jsonObjectSend);
            }

        }
    };

    public class firstPageLoginButtonSendDataToServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.FirstPageLoginButtonSendDataToServerURL,params[0]);
            if(jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            if(jsonObjRec != null && jsonObjRec.size() > 0){

                if(jsonObjRec.get(Constants.TAG_User_Exists_Status).toString().equalsIgnoreCase("new")){

                    Toast.makeText(LoginActivity.this,"The username password doesn't exist.Please Register",Toast.LENGTH_LONG).show();

                }else{

                    setUserOld(jsonObjRec);


                    Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                    startActivity(intent);
                    finish();

                }


            }
            else
                Log.e(TAG,"jsonObjRec == null");
        }
    }

    public void setUserOld(JSONObject jsonObjRec) {

        User user = new User();
        user.setEmail(jsonObjRec.get(Constants.TAG_Email).toString());
        user.setName(jsonObjRec.get(Constants.TAG_Name).toString());
        user.setGender(jsonObjRec.get(Constants.TAG_Gender).toString());
        user.setBirthday(jsonObjRec.get(Constants.TAG_Birthday).toString());
        user.setCategories(jsonObjRec.get(Constants.TAG_CATEGORIES).toString());
        user.setSaberaId(jsonObjRec.get(Constants.TAG_UserSaberaId).toString());
        user.setEncodedImage(jsonObjRec.get(Constants.TAG_Image_String).toString());

        PrefUtilsUser.setCurrentUser(user,this);
        setUserOldQns(jsonObjRec);
        setUserOldMatchedUserDetails(jsonObjRec);

    }

    public void setUserOldMatchedUserDetails(JSONObject jsonObjRec) {

        User user = PrefUtilsUser.getCurrentUser(this);
        ArrayList<MatchedUser> matchedUserList = new ArrayList<>();
        //HashMap<String,MatchedUser> matchedUserHashMap = new HashMap<>();
        //HashMap<String,ArrayList<Message>> matchedUserMessageMap = new HashMap<>();
        ArrayList<MsgFrgmChatHead> chatHeadsArrayList = new ArrayList<>();
        JSONArray matchedUserJArray = (org.json.simple.JSONArray)jsonObjRec.get(Constants.TAG_M_User);
        if(matchedUserJArray!=null && matchedUserJArray.size()>0){

            for(int i=0;i<matchedUserJArray.size();i++){

                MatchedUser mUser = new MatchedUser();
                ArrayList<MatchedUserQn> qnsAnswered = new ArrayList<>();
                ArrayList<MatchedUserQn> qnsAsked = new ArrayList<>();

                JSONObject mUserJObject = (JSONObject)matchedUserJArray.get(i);
                mUser.setUserId(mUserJObject.get(Constants.TAG_UserSaberaId).toString());
                mUser.setEmail(mUserJObject.get(Constants.TAG_Email).toString());
                mUser.setName(mUserJObject.get(Constants.TAG_Name).toString());
                mUser.setDob(mUserJObject.get(Constants.TAG_Birthday).toString());
                mUser.setGender(mUserJObject.get(Constants.TAG_Gender).toString());
                mUser.setCategories(mUserJObject.get(Constants.TAG_CATEGORIES).toString());


                JSONArray qandAArray = (org.json.simple.JSONArray)mUserJObject.get(Constants.TAG_M_User_QA);
                if(qandAArray!=null && qandAArray.size()>0){

                    for(int j=0;j<qandAArray.size();j++){

                        MatchedUserQn mQn = new MatchedUserQn();
                        JSONObject mQnJsonObj = (JSONObject)qandAArray.get(j);
                        mQn.setQnTxt(mQnJsonObj.get(Constants.TAG_M_User_QA_qTxt).toString());
                        mQn.setQnrId(mQnJsonObj.get(Constants.TAG_M_User_QA_qnrId).toString());
                        mQn.setAnswererId(mQnJsonObj.get(Constants.TAG_M_User_QA_ansId).toString());
                        ArrayList<String> proposed_answer = new ArrayList<>();
                        ArrayList<String> attempted_answer = new ArrayList<>();
                        JSONArray pAnsJArray = (org.json.simple.JSONArray)mQnJsonObj.get(Constants.TAG_M_User_QA_pAns);
                        JSONArray aAnsJArray = (org.json.simple.JSONArray)mQnJsonObj.get(Constants.TAG_M_User_QA_aAns);
                        if(pAnsJArray!=null && pAnsJArray.size()>0){

                            for(int k=0;k<pAnsJArray.size();k++){
                                JSONObject pAnsJObj = (JSONObject)pAnsJArray.get(k);
                                proposed_answer.add(pAnsJObj.get(Constants.TAG_M_User_QA_keywords).toString());
                            }

                        }else{
                            Log.e(TAG,"pAnsJArray = null or size = 0");
                        }

                        if(aAnsJArray!=null && aAnsJArray.size()>0){

                            for(int k=0;k<aAnsJArray.size();k++){
                                JSONObject aAnsJObj = (JSONObject)pAnsJArray.get(k);
                                attempted_answer.add(aAnsJObj.get(Constants.TAG_M_User_QA_answers).toString());
                            }

                        }else{
                            Log.e(TAG,"aAnsJArray = null or size = 0");
                        }

                        mQn.setProposed_answer(proposed_answer);
                        mQn.setAttempted_answer(attempted_answer);

                        if(mQn.getQnrId().equals(user.getSaberaId())){
                            qnsAnswered.add(mQn);
                        }else{
                            qnsAsked.add(mQn);
                        }



                    }

                }else{
                    Log.e(TAG,"qandAArray = null or size = 0");
                }

                mUser.setQnsAnswered(qnsAnswered);
                mUser.setQnsAsked(qnsAsked);

                ArrayList<Message> messagesList = new ArrayList<>();

                JSONArray chatsArray = (org.json.simple.JSONArray)mUserJObject.get(Constants.TAG_M_User_Msgs);
                if(chatsArray!=null && chatsArray.size()>0){

                    for(int j=0;j<chatsArray.size();j++){

                        Message message = new Message();
                        JSONObject msgJsonObj = (JSONObject)chatsArray.get(j);
                        message.setMessageId(msgJsonObj.get(Constants.TAG_M_User_Msgs_Id).toString());
                        message.setMsgTxt(msgJsonObj.get(Constants.TAG_M_User_Msgs_Txt).toString());
                        message.setTimeStamp(msgJsonObj.get(Constants.TAG_M_User_Msgs_time).toString());
                        message.setSenderId(msgJsonObj.get(Constants.TAG_M_User_Msgs_senderId).toString());
                        message.setRecvrId(msgJsonObj.get(Constants.TAG_M_User_Msgs_receiverId).toString());
                        messagesList.add(message);
                    }

                }else{
                    Log.e(TAG,"chatsArray = null or size = 0");
                }

                mUser.setMessagesList(messagesList);

                MsgFrgmChatHead chatHead = new MsgFrgmChatHead();
                chatHead.setUserId(mUser.getUserId());
                chatHead.setName(mUser.getName());
                int messageCount = mUser.getMessagesList().size();
                if(messageCount!=0){
                    Message lastMessage = mUser.getMessagesList().get(messageCount-1);
                    chatHead.setLastMessage(lastMessage.getMsgTxt());
                    chatHead.setTimeStamp(lastMessage.getTimeStamp());
                }else{
                    chatHead.setLastMessage("");
                    chatHead.setTimeStamp("");
                }

                chatHead.setUnreadMsgCount(0);


                matchedUserList.add(mUser);
                //matchedUserHashMap.put(mUser.getUserId(),mUser);
                //matchedUserMessageMap.put(mUser.getUserId(),mUser.getMessagesList());
                chatHeadsArrayList.add(chatHead);

            }//end of matchedUserJArray loop

        }else{
            Log.e(TAG,"matchedUserJArray = null or size = 0");
        }

        user.setMatchedUserList(matchedUserList);
        //user.setMatchedUserHashMap(matchedUserHashMap);
        //user.setMatchedUserMessageMap(matchedUserMessageMap);
        user.setChatHeadsArrayList(chatHeadsArrayList);
        PrefUtilsUser.setCurrentUser(user,this);


    }

    public void setUserOldQns(JSONObject jsonObjRec){

        User user = PrefUtilsUser.getCurrentUser(this);

        user.setQuestions(jsonObjRec.get(Constants.TAG_QUESTIONS).toString());
        user.setQnJsonArray((org.json.simple.JSONArray)jsonObjRec.get(Constants.TAG_QUESTIONS));
        ArrayList<UserSeeQn> qnArrayList = new ArrayList<>();

        JSONArray jArray =  user.getQnJsonArray();
        JSONArray arr = null;
        if(jArray != null){
            String jsonString = jArray.toJSONString();
            JSONParser parser = new JSONParser();

            try {
                arr = (JSONArray) parser.parse(jsonString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            Log.d(TAG,"jArray = null");
        }

        if(arr!=null){
            for (int i = 0; i < arr.size(); i++) {
                Log.d(TAG, "qnJsonArray loop - i = " + i);
                UserSeeQn qn = new UserSeeQn();
                //Log.d(TAG, qnJsonArray.toJSONString());
                JSONObject jsonObject = (JSONObject) arr.get(i);
                qn.setqId(jsonObject.get(Constants.TAG_SEEQN_QN_ID).toString());
                qn.setuId(jsonObject.get(Constants.TAG_SEEQN_QNR_ID).toString());
                qn.setQnText(jsonObject.get(Constants.TAG_SEEQN_QN_TEXT).toString());
                qn.setHintText(jsonObject.get(Constants.TAG_SEEQN_Hint).toString());
                qn.setTimer(jsonObject.get(Constants.TAG_SEEQN_Timer).toString());
                qn.setQnType(jsonObject.get(Constants.TAG_SEEQN_QN_TYPE).toString());
                if (qn.getQnType().equalsIgnoreCase(Constants.VALUE_SEEQN_Objective)) {

                    qn.setOption1(jsonObject.get(Constants.TAG_SEEQN_OPTION1).toString());
                    qn.setOption2(jsonObject.get(Constants.TAG_SEEQN_OPTION2).toString());
                    qn.setOption3(jsonObject.get(Constants.TAG_SEEQN_OPTION3).toString());
                    qn.setOption4(jsonObject.get(Constants.TAG_SEEQN_OPTION4).toString());

                    if (jsonObject.get(Constants.TAG_SEEQN_Option1_Status).toString().equalsIgnoreCase("1"))
                        qn.setStatus1(true);
                    else
                        qn.setStatus1(false);

                    if (jsonObject.get(Constants.TAG_SEEQN_Option2_Status).toString().equalsIgnoreCase("1"))
                        qn.setStatus2(true);
                    else
                        qn.setStatus2(false);

                    if (jsonObject.get(Constants.TAG_SEEQN_Option3_Status).toString().equalsIgnoreCase("1"))
                        qn.setStatus3(true);
                    else
                        qn.setStatus3(false);

                    if (jsonObject.get(Constants.TAG_SEEQN_Option4_Status).toString().equalsIgnoreCase("1"))
                        qn.setStatus4(true);
                    else
                        qn.setStatus4(false);


                } else {

                    qn.setAnsText(jsonObject.get(Constants.TAG_SEEQN_Ans_Text).toString());
                    String keywords = jsonObject.get(Constants.TAG_SEEQN_Keywords).toString();
                    String[] keyw = keywords.split(",");
                    ArrayList<String> keywordsArray = new ArrayList<>();
                    for(int j=0;j<keyw.length;j++)
                        keywordsArray.add(keyw[j]);
                    qn.setKeywords(keywordsArray);

                }
                qnArrayList.add(qn);

            }
        }else{
            Log.d(TAG,"arr = null");
        }





        user.setQuestionArray(qnArrayList);
        PrefUtilsUser.setCurrentUser(user,LoginActivity.this);

    }

    View.OnClickListener regButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String email = emailEdtTxt.getText().toString();
            String pwd = pwdEdtTxt.getText().toString();

            if(email == null || email.equalsIgnoreCase("")){
                Toast.makeText(LoginActivity.this,"Please enter email",Toast.LENGTH_LONG).show();
                return;
            }
            if(pwd == null || pwd.equalsIgnoreCase("")){
                Toast.makeText(LoginActivity.this,"Please enter password",Toast.LENGTH_LONG).show();
                return;
            }
            if(pwd.length()<6){
                Toast.makeText(LoginActivity.this,"Password must be at least 6 characters",Toast.LENGTH_LONG).show();
                return;
            }

            User tempUser = new User();
            //Log.e(TAG,"regButtonClickListener - a - new User");
            tempUser.setEmail(email);
            //Log.e(TAG,"regButtonClickListener - b - set Email");
            tempUser.setPwd(pwd);
            //Log.e(TAG,"regButtonClickListener - c - set pwd");
            PrefUtilsTempUser.setCurrentTempUser(tempUser,LoginActivity.this);
            //Log.e(TAG,"regButtonClickListener - d - set currentTempUser");
            Intent intent = new Intent(LoginActivity.this,Registration1Activity.class);
            //Log.e(TAG,"regButtonClickListener - e - new Intent");
            startActivity(intent);

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        user = PrefUtilsUser.getCurrentUser(LoginActivity.this);

        if (user != null && user.getName()!=null) {

            Log.d("LoginActivity","user!=null && user.getName() != null");

            Intent homeIntent = new Intent(LoginActivity.this, BaseActivity.class);

            startActivity(homeIntent);

            finish();
        }


    }

    private void executeGraphRequest(){

        Log.d("LoginActivity","in executeGraphRequest mAccessToken = " + mAccessToken.toString());

        GraphRequest request = GraphRequest.newMeRequest(
                mAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(
                            org.json.JSONObject object,
                            GraphResponse response) {

                        Log.e("response: ", response + "");
                        try {

                            String facebookID = object.get("id").toString();
                            String profilePicUrl;
                            Log.d(TAG,"facebookId :"+facebookID);
                            String stringURL = null;
                            try {
                                stringURL = "http://graph.facebook.com/" + URLEncoder.encode(facebookID, "UTF-8") + "?fields=" + URLEncoder.encode("picture", "UTF-8");
                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            }
                            Log.d(TAG,"stringURL :"+stringURL);
                            //Log.d("LoginActivity","in onCompleted graphRequest userId = " + facebookID);
                            org.json.JSONObject data = response.getJSONObject();
                            if(data.has("picture")){

                                profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");

                            }else{
                                Log.d(TAG,"data doesn't have picture");
                            }
                            String email = object.get("email").toString();

                            String name = object.get("name").toString();
                            String gender = object.get("gender").toString();
                            URL url = new URL("http://graph.facebook.com/"+facebookID+"/picture?type=large");
                            //profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            //Log.d(TAG,"profilePicUrl :"+profilePicUrl);
                            User tempUser = new User();
                            tempUser.setEmail(email);
                            tempUser.setGender(gender);
                            tempUser.setName(name);
                            tempUser.setImageUrl(url);
                            PrefUtilsTempUser.setCurrentTempUser(tempUser,LoginActivity.this);

                            JSONObject jsonObjectSend = new JSONObject();
                            jsonObjectSend.put(Constants.TAG_Email,email);
                            jsonObjectSend.put(Constants.TAG_LOGIN_MODE,"fb");
                            new fbLoginCheck().execute(jsonObjectSend);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                       // Utility.makeToast(LoginActivity.this,"welcome " + user.name, Constants.toastLengthLong);
                        //Toast.makeText(LoginActivity.this, "welcome " + user.name, Toast.LENGTH_LONG).show();


                    }

                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();

    }

    public class fbLoginCheck extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.FbLoginCheckURL,params[0]);
            if(jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            if(jsonObjRec != null && jsonObjRec.size() > 0){

                String status = jsonObjRec.get(Constants.TAG_User_Exists_Status).toString();
                String saberaID = jsonObjRec.get(Constants.TAG_UserSaberaId).toString();
                if(status.equalsIgnoreCase("new")){

                    User user = new User();
                    User tempUser = PrefUtilsTempUser.getCurrentTempUser(LoginActivity.this);
                    user.setSaberaId(saberaID);
                    user.setEmail(tempUser.getEmail());
                    user.setName(tempUser.getName());
                    user.setGender(tempUser.getGender());
                    PrefUtilsUser.setCurrentUser(user,LoginActivity.this);
                    PrefUtilsTempUser.clearCurrentTempUser(LoginActivity.this);
                    Intent intent = new Intent(LoginActivity.this,RegistrationDetailActivity.class);
                    startActivity(intent);
                    finish();

                }else{

                    setUserOld(jsonObjRec);
                    PrefUtilsTempUser.clearCurrentTempUser(LoginActivity.this);
                    Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                    startActivity(intent);
                    finish();




                }



            }
            else
                Log.e(TAG,"jsonObjRec == null");
        }
    }




    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            Log.d("LoginActivity","onSuccess");


            mAccessToken = loginResult.getAccessToken();
            executeGraphRequest();

        }

        @Override
        public void onCancel() {

            Log.d("LoginActivity","onCancel");

        }

        @Override
        public void onError(FacebookException e) {
            Log.d("LoginActivity","onError");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Log.e("LoginActivity", "Exception: "+Log.getStackTraceString(e));
            //Log.e("LoginActivity", "Facebook error = "+sw.toString());
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }
}
