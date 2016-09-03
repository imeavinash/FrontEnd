package com.example.avinashbehera.sabera.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Button cancelButton;
    private Button regButton;

    private static final String TAG = Registration1Activity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"regButtonClickListener - f - onCreate");
        setContentView(R.layout.activity_registration1);

        emailEdtTxt=(EditText)findViewById(R.id.emailReg1EdtTxt);
        pwdEdtTxt = (EditText)findViewById(R.id.pwdReg1EdtTxt);
        retypePwdEdtTxt = (EditText)findViewById(R.id.retypePwdReg1EdtTxt);
        cancelButton = (Button) findViewById(R.id.btnReg1Cancel);
        regButton = (Button) findViewById(R.id.btnReg1Register);

        cancelButton.setOnClickListener(cancelButtonClickListener);
        regButton.setOnClickListener(regButtonOnClickListener);

        User tempUser = new User();
        tempUser = PrefUtilsTempUser.getCurrentTempUser(this);
        emailEdtTxt.setText(tempUser.getEmail());
        pwdEdtTxt.setText(tempUser.getPwd());
    }

    View.OnClickListener cancelButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            emailEdtTxt.getText().clear();
            pwdEdtTxt.getText().clear();
            retypePwdEdtTxt.getText().clear();
            onBackPressed();

        }
    };

    View.OnClickListener regButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String email = emailEdtTxt.getText().toString();
            String pwd = pwdEdtTxt.getText().toString();
            String retypePwd = retypePwdEdtTxt.getText().toString();

            if(email == null || email.equalsIgnoreCase("")){
                Toast.makeText(Registration1Activity.this,"Please enter email",Toast.LENGTH_LONG).show();
                return;
            }
            if(pwd == null || pwd.equalsIgnoreCase("")){
                Toast.makeText(Registration1Activity.this,"Please enter password",Toast.LENGTH_LONG).show();
                return;
            }

            if(!(pwd.equals(retypePwd))){
                Toast.makeText(Registration1Activity.this,"Passwords should match",Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject jsonObjectSend = new JSONObject();
            jsonObjectSend.put(Constants.TAG_Email,email);
            jsonObjectSend.put(Constants.TAG_PASSWORD,pwd);
            if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                new RegisterButtonSendDataToServer().execute(jsonObjectSend);
            }

        }
    };

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



                        User user = new User();
                        user.setEmail(jsonObjRec.get(Constants.TAG_Email).toString());
                        user.setSaberaId(jsonObjRec.get(Constants.TAG_UserSaberaId).toString());
                        PrefUtilsUser.setCurrentUser(user,Registration1Activity.this);
                        Intent intent = new Intent(Registration1Activity.this, RegistrationDetailActivity.class);
                        startActivity(intent);
                        finish();




                }
                else
                    Log.e(TAG,"jsonObjRec == null");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
