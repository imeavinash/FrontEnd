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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.avinashbehera.sabera.R;
import com.example.avinashbehera.sabera.model.User;
import com.example.avinashbehera.sabera.model.UserSeeQn;
import com.example.avinashbehera.sabera.util.Constants;
import com.example.avinashbehera.sabera.util.PrefUtilsUser;
import com.example.avinashbehera.sabera.util.Utility;
import com.example.avinashbehera.sabera.network.HttpClient;

//import org.json.simple.JSONException;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RegistrationDetailActivity extends AppCompatActivity {

    private EditText nameEditText;
    private TextView emailTxtView;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private EditText birthdayEditText;
    private Button doneButton;
    private Button cancelButton;
    private Button pickCtgryButton;
    private TextView categoryTxtView;
    private ImageView profilePicImgView;
    private Button btnUploadPicture;


    private ArrayList<String> mCategoriesList;
    private User user;

    String fbId;
    String name;
    String email;
    String gender;
    String birthday;

    User tempUser;

    Calendar myCalendar;

    private static final String TAG = RegistrationDetailActivity.class.getSimpleName();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult");
        if(resultCode==RESULT_OK){
            if(requestCode==Constants.REQUEST_CODE_CATEGORY){
                Log.d(TAG,"requestCode = REQUEST_CODE_CATEGORY");
                mCategoriesList = data.getStringArrayListExtra(Constants.EXTRA_RESULT_CATEGORY);

                String category = "";
                if(mCategoriesList!=null && mCategoriesList.size()>0){
                    Log.d(TAG,"mCategoriesList !=null");
                    category=category+mCategoriesList.get(0);
                    for(int i=1;i<mCategoriesList.size();i++)
                        category=category+","+mCategoriesList.get(i);
                }
                Log.d(TAG,"category = "+category);
                categoryTxtView.setText(category);
                User user = PrefUtilsUser.getCurrentUser(this);
                user.setCategories(category);
                PrefUtilsUser.setCurrentUser(user,RegistrationDetailActivity.this);


            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationdetail_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCategoriesList = new ArrayList<>();

        nameEditText = (EditText)findViewById(R.id.nameRegEditText);
        emailTxtView = (TextView) findViewById(R.id.emaillRegEditText);
        birthdayEditText = (EditText)findViewById(R.id.birthdayRegEditText);
        genderRadioGroup = (RadioGroup) findViewById(R.id.genderRegRadioGroup);
        maleRadioButton = (RadioButton) findViewById(R.id.radio_reg_male);
        femaleRadioButton = (RadioButton) findViewById(R.id.radio_reg_female);
        doneButton = (Button)findViewById(R.id.doneRegButton);
        cancelButton = (Button)findViewById(R.id.cancelRegButton);
        pickCtgryButton = (Button)findViewById(R.id.btnRegPickCtgry);
        categoryTxtView = (TextView) findViewById(R.id.regCtgryTxtView);
        profilePicImgView = (ImageView) findViewById(R.id.profilePicture);
        btnUploadPicture = (Button) findViewById(R.id.btnUploadPicture);

        


        user = PrefUtilsUser.getCurrentUser(this);
        //fbId = intent.getExtra(Constants.EXTRA_UserFBId);
        name = user.getName();
        email = user.getEmail();
        gender = user.getGender();
        if(gender!=null){
            if(gender.equalsIgnoreCase("male")){
                maleRadioButton.setChecked(true);
            }else {
                femaleRadioButton.setChecked(true);
            }
        }

        nameEditText.setText(name);
        emailTxtView.setText(email);


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
                new DatePickerDialog(RegistrationDetailActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        pickCtgryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationDetailActivity.this,CategoryActivity.class);
                //intent.putExtra(Constants.EXTRA_PICK_CATEGORY,Constants.REQUEST_CATEGORY_REGISTRATION);
                startActivityForResult(intent,Constants.REQUEST_CODE_CATEGORY);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG,"doneButton - onClickListener");

                if(noEditTextIsEmpty()){

                    if(Constants.backendTest){

                        User user = PrefUtilsUser.getCurrentUser(RegistrationDetailActivity.this);
                        user.setEmail(emailTxtView.getText().toString());
                        user.setName(nameEditText.getText().toString());
                        user.setGender(gender);
                        user.setBirthday(birthdayEditText.getText().toString());
                        user.setCategoryList(mCategoriesList);
                        PrefUtilsUser.setCurrentUser(user,RegistrationDetailActivity.this);



                        JSONObject jsonObjectSend = new JSONObject();
                        jsonObjectSend.put(Constants.TAG_UserSaberaId,user.getSaberaId());
                        jsonObjectSend.put(Constants.TAG_Name,user.getName());
                        jsonObjectSend.put(Constants.TAG_Gender,user.getGender());
                        jsonObjectSend.put(Constants.TAG_Birthday,user.getBirthday());
                        jsonObjectSend.put(Constants.TAG_CATEGORIES,user.getCategories());

                        if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                            new RegDetailDoneButtonSendDataToServer().execute(jsonObjectSend);
                        }

                    }else{


                    }

                }else{
                    Utility.makeToast(RegistrationDetailActivity.this,"Please fill all fields",Constants.toastLengthLong);
                }
            }
        });




    }

    public class RegDetailDoneButtonSendDataToServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.RegDetailDoneButtonSendDataToServer,params[0]);
            if(jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            if(jsonObjRec != null && jsonObjRec.size() > 0){

                setUserNewQns(jsonObjRec);
                Intent intent = new Intent(RegistrationDetailActivity.this,BaseActivity.class);
                startActivity(intent);
                finish();




            }
            else
                Log.e(TAG,"jsonObjRec == null");
        }
    }

    public void setUserNewQns(JSONObject jsonObjRec){

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
                Log.d(TAG, "BaseActivity - onCreate - qnJsonArray loop - i = " + i);
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


        //JSONArray qnJsonArray = user.getQnJsonArray();


        user.setQuestionArray(qnArrayList);
        PrefUtilsUser.setCurrentUser(user,RegistrationDetailActivity.this);

    }


    public boolean noEditTextIsEmpty(){



        if(nameEditText.getText().toString() == null || nameEditText.getText().toString().equalsIgnoreCase("") ||
                emailTxtView.getText().toString() == null || emailTxtView.getText().toString().equalsIgnoreCase("") ||
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
