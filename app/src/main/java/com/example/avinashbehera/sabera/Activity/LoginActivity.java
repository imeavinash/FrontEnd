package com.example.avinashbehera.sabera.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.content.pm.Signature;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.example.avinashbehera.sabera.R;
import com.example.avinashbehera.sabera.model.User;
import com.example.avinashbehera.sabera.util.Constants;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Set;


public class LoginActivity extends AppCompatActivity {

    LoginButton loginButton;
    CallbackManager callbackManager;
    User user;
    private static final String TAG = LoginActivity.class.getSimpleName();
    public AccessToken mAccessToken;


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

            Intent homeIntent = new Intent(LoginActivity.this, LogoutActivity.class);

            startActivity(homeIntent);

            finish();
        }


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        AppEventsLogger.activateApp(this);

//        if(mAccessToken != AccessToken.getCurrentAccessToken() && mAccessToken != null){
//            Log.d("LoginActivity","mAccessToken != AccesToken.getCurrentAccessToken");
//            mAccessToken = AccessToken.getCurrentAccessToken();
//            executeGraphRequest();
//        }



        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile","email","user_birthday"));

        loginButton.registerCallback(callbackManager, mCallBack);


    }



    private void executeGraphRequest(){

        Log.d("LoginActivity","in executeGraphRequest mAccessToken = " + mAccessToken.toString());

        GraphRequest request = GraphRequest.newMeRequest(
                mAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        Log.e("response: ", response + "");
                        try {


                            String facebookID = object.getString("id").toString();
                            Log.d("LoginActivity","in onCompleted graphRequest userId = " + facebookID);


//                                user = new User();
//                                user.setFacebookID(object.getString("id").toString());
//                                user.setEmail(object.getString("email").toString());
//                                user.setName(object.getString("name").toString());
//                                user.setGender(object.getString("gender").toString());
//                                PrefUtilsUser.setCurrentUser(user, LoginActivity.this);
                                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                                intent.putExtra(Constants.EXTRA_UserFBId,object.getString("id").toString());
                                intent.putExtra(Constants.EXTRA_Name,object.getString("name").toString());
                                intent.putExtra(Constants.EXTRA_Email,object.getString("email").toString());
                                intent.putExtra(Constants.EXTRA_Gender,object.getString("gender").toString());
                                startActivity(intent);
                                finish();



                            //user.birthday = object.getString("birthday").toString();
                            //Log.d("LoginActivity","in executeGraphRequest user.birthday = "+user.birthday);
                            //PrefUtilsUser.setCurrentUser(user, LoginActivity.this);

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


    public class checkIfUserExists extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.checkIfUserExistsURL,params[0]);
            if(jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            try{
                if(jsonObjRec != null && jsonObjRec.length() > 0){

                    String userExistsStatus = jsonObjRec.getString(Constants.TAG_User_Exists_Status);
                    if(userExistsStatus.equalsIgnoreCase("yes")){

                        JSONObject jsonObjSend = new JSONObject();

                        try {
                            // Add key/value pairs
                            jsonObjSend.put(Constants.TAG_UserFBId, mAccessToken.getUserId());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(jsonObjSend.length() > 0){
                            new loadUserDataFromeServer().execute(jsonObjSend);
                        }


                    }else{
                        executeGraphRequest();
                    }


                }
                else
                    Log.e(TAG,"jsonObjRec == null");
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class loadUserDataFromeServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.getUserDataFromServerURL,params[0]);
            if(jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            try{
                if(jsonObjRec != null && jsonObjRec.length() > 0){

                    user = new User();
                    user.setFacebookID(jsonObjRec.getString(Constants.TAG_UserFBId).toString());
                    user.setEmail(jsonObjRec.getString(Constants.TAG_Email).toString());
                    user.setName(jsonObjRec.getString(Constants.TAG_Name).toString());
                    user.setGender(jsonObjRec.getString(Constants.TAG_Gender).toString());
                    user.setBirthday(jsonObjRec.getString(Constants.TAG_Birthday).toString());
                    user.setSaberaId(jsonObjRec.get(Constants.TAG_UserSaberaId).toString());
                    com.example.avinashbehera.sabera.util.PrefUtilsUser.setCurrentUser(user, LoginActivity.this);
                    Intent intent = new Intent(LoginActivity.this, LogoutActivity.class);
                    startActivity(intent);
                    finish();



                }
                else{
                    Log.e(TAG,"jsonObjRec == null");
                }

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            Log.d("LoginActivity","onSuccess");


            mAccessToken = loginResult.getAccessToken();
            Set<String> declinedPermissionSet = mAccessToken.getDeclinedPermissions();
            Log.d(TAG,"declindedPermissions = "+declinedPermissionSet.toString());

            if(Constants.backendTest){

                JSONObject jsonObjSend = new JSONObject();

                try {
                    // Add key/value pairs
                    jsonObjSend.put(Constants.TAG_UserFBId, mAccessToken.getUserId());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(jsonObjSend.length() > 0){
                    new checkIfUserExists().execute(jsonObjSend);
                }


            }
            else{

                executeGraphRequest();
            }

            //Log.d("LoginActivity","in onSuccess mAccessToken = " + mAccessToken.toString());
            //Log.d("LoginActivity","in onSuccess userId = " + mAccessToken.getUserId());





            // App code

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
