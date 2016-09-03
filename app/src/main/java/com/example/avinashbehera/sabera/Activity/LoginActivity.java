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
import com.example.avinashbehera.sabera.model.User;
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
import org.json.simple.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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

                    User user = new User();
                    user.setEmail(jsonObjRec.get(Constants.TAG_Email).toString());
                    user.setName(jsonObjRec.get(Constants.TAG_Name).toString());
                    user.setGender(jsonObjRec.get(Constants.TAG_Gender).toString());
                    user.setBirthday(jsonObjRec.get(Constants.TAG_Birthday).toString());
                    user.setCategories(jsonObjRec.get(Constants.TAG_CATEGORIES).toString());
                    user.setSaberaId(jsonObjRec.get(Constants.TAG_UserSaberaId).toString());
                    user.setQuestions(jsonObjRec.get(Constants.TAG_QUESTIONS).toString());
                    user.setQnJsonArray((org.json.simple.JSONArray)jsonObjRec.get(Constants.TAG_QUESTIONS));
                    PrefUtilsUser.setCurrentUser(user,LoginActivity.this);
                    Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                    startActivity(intent);
                    finish();

                }


            }
            else
                Log.e(TAG,"jsonObjRec == null");
        }
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
            Log.e(TAG,"regButtonClickListener - a - new User");
            tempUser.setEmail(email);
            Log.e(TAG,"regButtonClickListener - b - set Email");
            tempUser.setPwd(pwd);
            Log.e(TAG,"regButtonClickListener - c - set pwd");
            PrefUtilsTempUser.setCurrentTempUser(tempUser,LoginActivity.this);
            Log.e(TAG,"regButtonClickListener - d - set currentTempUser");
            Intent intent = new Intent(LoginActivity.this,Registration1Activity.class);
            Log.e(TAG,"regButtonClickListener - e - new Intent");
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

                            //String facebookID = object.get("id").toString();
                            //Log.d("LoginActivity","in onCompleted graphRequest userId = " + facebookID);
                            String email = object.get("email").toString();

                            String name = object.get("name").toString();
                            String gender = object.get("gender").toString();
                            User tempUser = new User();
                            tempUser.setEmail(email);
                            tempUser.setGender(gender);
                            tempUser.setName(name);
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

                    User user = new User();
                    user.setEmail(jsonObjRec.get(Constants.TAG_Email).toString());
                    user.setName(jsonObjRec.get(Constants.TAG_Name).toString());
                    user.setGender(jsonObjRec.get(Constants.TAG_Gender).toString());
                    user.setBirthday(jsonObjRec.get(Constants.TAG_Birthday).toString());
                    user.setCategories(jsonObjRec.get(Constants.TAG_CATEGORIES).toString());
                    user.setSaberaId(jsonObjRec.get(Constants.TAG_UserSaberaId).toString());
                    user.setQuestions(jsonObjRec.get(Constants.TAG_QUESTIONS).toString());
                    user.setQnJsonArray((org.json.simple.JSONArray)jsonObjRec.get(Constants.TAG_QUESTIONS));
                    PrefUtilsUser.setCurrentUser(user,LoginActivity.this);
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
