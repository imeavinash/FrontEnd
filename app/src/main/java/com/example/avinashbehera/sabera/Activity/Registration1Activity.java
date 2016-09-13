package com.example.avinashbehera.sabera.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.avinashbehera.sabera.R;
import com.example.avinashbehera.sabera.model.User;
import com.example.avinashbehera.sabera.network.HttpClient;
import com.example.avinashbehera.sabera.util.Constants;
import com.example.avinashbehera.sabera.util.PrefUtilsTempUser;
import com.example.avinashbehera.sabera.util.PrefUtilsUser;

//import org.json.simple.JSONException;
import org.json.JSONException;
import org.json.simple.JSONObject;

public class Registration1Activity extends AppCompatActivity {

    private EditText emailEdtTxt;
    private EditText pwdEdtTxt;
    private EditText retypePwdEdtTxt;
    private EditText passcodeEdtTxt;
    private Button cancelButton;
    private Button regButton;
    private Button resendPCbutton;

    private LinearLayout passcodeLL;
    private LinearLayout pwdLL;
    private LinearLayout retypePwdLL;

    private static final String TAG = Registration1Activity.class.getSimpleName();

    private String passcode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.e(TAG,"regButtonClickListener - f - onCreate");
        setContentView(R.layout.activity_registration1);

        emailEdtTxt=(EditText)findViewById(R.id.emailReg1EdtTxt);
        pwdEdtTxt = (EditText)findViewById(R.id.pwdReg1EdtTxt);
        retypePwdEdtTxt = (EditText)findViewById(R.id.retypePwdReg1EdtTxt);
        passcodeEdtTxt = (EditText) findViewById(R.id.passcodeEdtTxt);
        cancelButton = (Button) findViewById(R.id.btnReg1Cancel);
        regButton = (Button) findViewById(R.id.btnReg1Register);
        resendPCbutton = (Button) findViewById(R.id.resendPCbtn);

        passcodeLL = (LinearLayout) findViewById(R.id.passcodeLL);
        pwdLL = (LinearLayout) findViewById(R.id.pwdLL);
        retypePwdLL = (LinearLayout) findViewById(R.id.retypePwdLL);

        cancelButton.setOnClickListener(cancelButtonClickListener);
        regButton.setOnClickListener(regButtonOnClickListener);
        resendPCbutton.setOnClickListener(resendPCOnClickListener);

//        User tempUser = new User();
//        tempUser = PrefUtilsTempUser.getCurrentTempUser(this);
//        emailEdtTxt.setText(tempUser.getEmail());
//        pwdEdtTxt.setText(tempUser.getPwd());
    }

    View.OnClickListener resendPCOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            User tempUser = PrefUtilsTempUser.getCurrentTempUser(Registration1Activity.this);
            JSONObject jsonObjectSend = new JSONObject();
            jsonObjectSend.put(Constants.TAG_Email,tempUser.getEmail());
            if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                new ResendPCReqToServer().execute(jsonObjectSend);
            }
            return;




        }
    };

    View.OnClickListener cancelButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            emailEdtTxt.getText().clear();
            pwdEdtTxt.getText().clear();
            retypePwdEdtTxt.getText().clear();
            passcodeEdtTxt.getText().clear();
            PrefUtilsTempUser.clearCurrentTempUser(Registration1Activity.this);
            onBackPressed();


        }
    };

    View.OnClickListener regButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String email = emailEdtTxt.getText().toString();
            String pwd = pwdEdtTxt.getText().toString();
            String retypePwd = retypePwdEdtTxt.getText().toString();
            String passcode = passcodeEdtTxt.getText().toString();

            if(email == null || email.equalsIgnoreCase("")){
                Toast.makeText(Registration1Activity.this,"Please enter email",Toast.LENGTH_LONG).show();
                return;
            }

            User tempUser = PrefUtilsTempUser.getCurrentTempUser(Registration1Activity.this);
            if(tempUser==null || tempUser.getPasscode()==null || tempUser.getPasscode().equalsIgnoreCase("")){
                JSONObject jsonObjectSend = new JSONObject();
                jsonObjectSend.put(Constants.TAG_Email,email);
                if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                    new SendEmailToServer().execute(jsonObjectSend);
                }
                return;
            }
            if(pwd == null || pwd.equalsIgnoreCase("")){
                Toast.makeText(Registration1Activity.this,"Please enter password",Toast.LENGTH_LONG).show();
                return;
            }

            if(pwd.length()<6){
                Toast.makeText(Registration1Activity.this,"Password must be at least 6 characters",Toast.LENGTH_LONG).show();
                return;
            }

            if(!(pwd.equals(retypePwd))){
                Toast.makeText(Registration1Activity.this,"Passwords should match",Toast.LENGTH_LONG).show();
                return;
            }

            if(!(passcode.equals(tempUser.getPasscode()))){
                Toast.makeText(Registration1Activity.this,"Please enter correct passcode",Toast.LENGTH_LONG).show();
                return;
            }



            JSONObject jsonObjectSend = new JSONObject();
            jsonObjectSend.put(Constants.TAG_Email,tempUser.getEmail());
            jsonObjectSend.put(Constants.TAG_PASSWORD,pwd);
            if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                new RegisterButtonSendDataToServer().execute(jsonObjectSend);
            }

        }
    };

    public class ResendPCReqToServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.resendPasscodeURL,params[0]);
            if(jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            try{
                if(jsonObjRec != null && jsonObjRec.size() > 0){

                    String passcode = jsonObjRec.get(Constants.TAG_Passcode).toString();
                    User tempUser = PrefUtilsTempUser.getCurrentTempUser(Registration1Activity.this);
                    tempUser.setPasscode(passcode);
                    PrefUtilsTempUser.setCurrentTempUser(tempUser,Registration1Activity.this);

                }
                else
                    Log.e(TAG,"SendEmailToServer - jsonObjRec == null");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class SendEmailToServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.sendEmailRegURL,params[0]);
            if(jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            try{
                if(jsonObjRec != null && jsonObjRec.size() > 0){

                    String status = jsonObjRec.get(Constants.TAG_User_Exists_Status).toString();
                    if(status.equalsIgnoreCase("old")){
                        Toast.makeText(Registration1Activity.this,"Email already exists.Please enter different email",Toast.LENGTH_LONG).show();
                    }else{
                        User tempUser = new User();
                        String email = jsonObjRec.get(Constants.TAG_Email).toString();
                        String passcode = jsonObjRec.get(Constants.TAG_Passcode).toString();
                        tempUser.setEmail(email);
                        tempUser.setPasscode(passcode);
                        PrefUtilsTempUser.setCurrentTempUser(tempUser,Registration1Activity.this);
                        passcodeLL.setVisibility(View.VISIBLE);
                        pwdLL.setVisibility(View.VISIBLE);
                        retypePwdLL.setVisibility(View.VISIBLE);
                        resendPCbutton.setVisibility(View.VISIBLE);
                        regButton.setText("Register");
                    }

                }
                else
                    Log.e(TAG,"SendEmailToServer - jsonObjRec == null");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class RegisterButtonSendDataToServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.RegisterButtonSendDataToServerURL,params[0]);
            if(jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            try{
                if(jsonObjRec != null && jsonObjRec.size() > 0){

                    PrefUtilsTempUser.clearCurrentTempUser(Registration1Activity.this);
                    User user = new User();
                    user.setEmail(jsonObjRec.get(Constants.TAG_Email).toString());
                    user.setSaberaId(jsonObjRec.get(Constants.TAG_UserSaberaId).toString());
                    PrefUtilsUser.setCurrentUser(user,Registration1Activity.this);
                    Intent intent = new Intent(Registration1Activity.this, RegistrationDetailActivity.class);
                    startActivity(intent);
                    finish();




                }
                else
                    Log.e(TAG,"RegisterButtonSendDataToServer - jsonObjRec == null");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
