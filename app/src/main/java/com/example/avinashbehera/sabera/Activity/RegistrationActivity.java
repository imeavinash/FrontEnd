package com.example.avinashbehera.sabera.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.avinashbehera.sabera.R;
import com.example.avinashbehera.sabera.model.User;
import com.example.avinashbehera.sabera.util.Constants;
import com.example.avinashbehera.sabera.util.PrefUtilsTempUser;
import com.example.avinashbehera.sabera.util.PrefUtilsUser;
import com.example.avinashbehera.sabera.util.Utility;
import com.example.avinashbehera.sabera.network.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private EditText birthdayEditText;
    private Button proceedButton;
    private Button cancelButton;

    String fbId;
    String name;
    String email;
    String gender;
    String birthday;

    User tempUser;

    Calendar myCalendar;

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameEditText = (EditText)findViewById(R.id.nameRegEditText);
        emailEditText = (EditText)findViewById(R.id.emaillRegEditText);
        birthdayEditText = (EditText)findViewById(R.id.birthdayRegEditText);
        genderRadioGroup = (RadioGroup) findViewById(R.id.genderRegRadioGroup);
        maleRadioButton = (RadioButton) findViewById(R.id.radio_reg_male);
        femaleRadioButton = (RadioButton) findViewById(R.id.radio_reg_female);
        proceedButton = (Button)findViewById(R.id.proceedRegButton);
        cancelButton = (Button)findViewById(R.id.cancelRegButton);


        Intent intent = getIntent();
        fbId = intent.getStringExtra(Constants.EXTRA_UserFBId);
        name = intent.getStringExtra(Constants.EXTRA_Name);
        email = intent.getStringExtra(Constants.EXTRA_Email);
        gender = intent.getStringExtra(Constants.EXTRA_Gender);

        nameEditText.setText(name);
        emailEditText.setText(email);
        if(gender.equalsIgnoreCase("male")){
            maleRadioButton.setChecked(true);
        }else {
            femaleRadioButton.setChecked(true);
        }

        Log.d(TAG,"birthday text = "+birthdayEditText.getText());

        myCalendar = Calendar.getInstance();





        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                birthdayEditText.setText(sdf.format(myCalendar.getTime()));


            }

        };

        birthdayEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegistrationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(noEditTextIsEmpty()){


                        tempUser = new User();
                    tempUser.setFacebookID(fbId);
                    tempUser.setEmail(emailEditText.getText().toString());
                    tempUser.setName(nameEditText.getText().toString());
                    tempUser.setGender(gender);
                    tempUser.setBirthday(birthdayEditText.getText().toString());
                        //user.setSaberaId(jsonObjRec.get(Constants.TAG_UserSaberaId).toString());
                        PrefUtilsTempUser.setCurrentUser(tempUser, RegistrationActivity.this);
                        Intent intent = new Intent(RegistrationActivity.this, CategoryActivity.class);
                        startActivity(intent);
                        finish();




                }else{
                    Utility.makeToast(RegistrationActivity.this,"Please fill all fields",Constants.toastLengthLong);
                }
            }
        });




    }

    public void sendDataToServer(){

        JSONObject jsonObjSend = new JSONObject();
        try{
            jsonObjSend.put(Constants.TAG_UserFBId,fbId);
            jsonObjSend.put(Constants.TAG_Name,nameEditText.getText().toString());
            jsonObjSend.put(Constants.TAG_Email,emailEditText.getText().toString());
            jsonObjSend.put(Constants.TAG_Gender,gender);
            jsonObjSend.put(Constants.TAG_Birthday,birthdayEditText.getText().toString());
        }catch(JSONException e){
            e.printStackTrace();
        }

        if(jsonObjSend!=null && jsonObjSend.length()>0){
            new sendDataToServerForRegistration().execute(jsonObjSend);
        }

    }

    public class sendDataToServerForRegistration extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.sendDataToServerForRegistrationURL,params[0]);
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

                    User user = new User();
                    user.setFacebookID(jsonObjRec.getString(Constants.TAG_UserFBId).toString());
                    user.setEmail(jsonObjRec.getString(Constants.TAG_Email).toString());
                    user.setName(jsonObjRec.getString(Constants.TAG_Name).toString());
                    user.setGender(jsonObjRec.getString(Constants.TAG_Gender).toString());
                    user.setBirthday(jsonObjRec.get(Constants.TAG_Birthday).toString());
                    user.setSaberaId(jsonObjRec.get(Constants.TAG_UserSaberaId).toString());
                    PrefUtilsUser.setCurrentUser(user, RegistrationActivity.this);
                    Intent intent = new Intent(RegistrationActivity.this, LogoutActivity.class);
                    startActivity(intent);
                    finish();


                }
                else
                    Log.e(TAG,"jsonObjRec == null");
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean noEditTextIsEmpty(){



        if(nameEditText.getText().toString() == null || nameEditText.getText().toString().equalsIgnoreCase("") ||
                emailEditText.getText().toString() == null || emailEditText.getText().toString().equalsIgnoreCase("") ||
                birthdayEditText.getText().toString() == null || birthdayEditText.getText().toString().equalsIgnoreCase("")){
            return false;
        }

        return true;
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_reg_male:
                if (checked)
                    gender = "male";
                    break;
            case R.id.radio_reg_female:
                if (checked)
                    gender = "female";
                    break;
        }
    }


}
