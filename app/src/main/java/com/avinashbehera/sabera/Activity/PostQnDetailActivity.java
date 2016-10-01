package com.avinashbehera.sabera.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avinashbehera.sabera.model.UserPostQn;
import com.avinashbehera.sabera.network.HttpClient;
import com.avinashbehera.sabera.util.Constants;
import com.avinashbehera.sabera.util.PrefUtilsPostQn;
import com.avinashbehera.sabera.util.PrefUtilsUser;
import com.avinashbehera.sabera.R;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//import org.json.simple.JSONException;

public class PostQnDetailActivity extends AppCompatActivity {

    private Button sumbitButton;
    private Button postQnPickCtgryButton;
    private Button pickKeywordsButton;
    private TextView categoryTxtView;
    private TextView keywordsTxtView;
    private TextView qnTxtView;
    private EditText ansEdtTxt;
    private EditText timerEdtTxt;
    private EditText minEditText;
    private EditText secEditText;
    private EditText hintEdtTxt;
    private EditText option1EdtTxt;
    private EditText option2EdtTxt;
    private EditText option3EdtTxt;
    private EditText option4EdtTxt;
    private CheckBox opt1checkBox;
    private CheckBox opt2checkBox;
    private CheckBox opt3checkBox;
    private CheckBox opt4checkBox;
    private String qnType;
    private static UserPostQn postQn;
    private LinearLayout postQnDetailObjLL;
    private LinearLayout postQnDetailSubjLL;
    private ArrayList<String> optionsArray;
    private ArrayList<Integer> correctOptionsArray;
    private ArrayList<String> proposed_answer;
    private ArrayList<String> keywordsArray;
    private ArrayList<String> categoriesArray;
    private static final String TAG = PostQnDetailActivity.class.getSimpleName();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE_CATEGORY) {
                Log.d(TAG, "requestCode = REQUEST_CODE_CATEGORY");
                ArrayList<String> catArray = data.getStringArrayListExtra(Constants.EXTRA_RESULT_CATEGORY);
                postQn.setCategories(catArray);
                String category = "";
                if (postQn.getCategories() != null && postQn.getCategories().size() > 0) {
                    Log.d(TAG, "postQn.getCategories !=null");
                    category = category + postQn.getCategories().get(0);
                    for (int i = 1; i < postQn.getCategories().size(); i++)
                        category = category + ", " + postQn.getCategories().get(i);
                }
                Log.d(TAG, "category = " + category);
                categoryTxtView.setText(category);

            }

            if(requestCode == Constants.REQUEST_CODE_PICK_KEYWORDS){
                ArrayList<String> selectedKeywords = data.getStringArrayListExtra(Constants.EXTRA_RESULT_PICK_KEYWORDS);
                postQn.setKeywords(selectedKeywords);
                String keywords="";
                if (selectedKeywords.size() > 0) {
                    keywords = keywords + selectedKeywords.get(0);
                    for (int i = 1; i < selectedKeywords.size(); i++)
                        keywords = keywords + ", " + selectedKeywords.get(i);
                    postQn.setKeywordString(keywords);
                    keywordsTxtView.setText(keywords);
                }else{
                    Log.d(TAG,"onActivityResult - selectedKeywordsLength = 0");
                }

            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_qn_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

        optionsArray = new ArrayList<>();
        correctOptionsArray = new ArrayList<>();
        proposed_answer = new ArrayList<>();
        keywordsArray = new ArrayList<>();
        categoriesArray = new ArrayList<>();

        sumbitButton = (Button) findViewById(R.id.btnSubmitPostQnObjDetail);
        postQnPickCtgryButton = (Button) findViewById(R.id.btnPostQnPickCtgry);
        pickKeywordsButton = (Button) findViewById(R.id.btnPickKeywords);

        categoryTxtView = (TextView) findViewById(R.id.postQnCtgryTxtView);
        keywordsTxtView = (TextView) findViewById(R.id.postQnKeywordsTxtView);
        qnTxtView = (TextView) findViewById(R.id.qnTxtView);

        ansEdtTxt = (EditText) findViewById(R.id.postQnAnsEdtTxt);

        minEditText = (EditText) findViewById(R.id.minEdtText);
        secEditText = (EditText) findViewById(R.id.secEdtText);
        hintEdtTxt = (EditText) findViewById(R.id.postQnHintEdtTxt);

        option1EdtTxt = (EditText) findViewById(R.id.option1edttxt);
        option2EdtTxt = (EditText) findViewById(R.id.option2edttxt);
        option3EdtTxt = (EditText) findViewById(R.id.option3edttxt);
        option4EdtTxt = (EditText) findViewById(R.id.option4edttxt);

        opt1checkBox = (CheckBox) findViewById(R.id.opt1chkbox);
        opt2checkBox = (CheckBox) findViewById(R.id.opt2chkbox);
        opt3checkBox = (CheckBox) findViewById(R.id.opt3chkbox);
        opt4checkBox = (CheckBox) findViewById(R.id.opt4chkbox);

        postQnDetailObjLL = (LinearLayout) findViewById(R.id.postObjQnDetailLL);
        postQnDetailObjLL.setVisibility(View.GONE);
        postQnDetailSubjLL = (LinearLayout) findViewById(R.id.postQnSubjDetailLL);
        postQnDetailSubjLL.setVisibility(View.GONE);

        sumbitButton.setOnClickListener(sumbitButtonClickListener);


        ansEdtTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG,"ansEdtTxt - onFocusChange");
                if(hasFocus){
                    Log.d(TAG,"ansEdtTxt - onFocusChange - hasFocus");
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                        //v.setElevation(50);

                    }


                    //((EditText) v).setShadowLayer(50,0,0,Color.GREEN);

                }
            }
        });
        postQnPickCtgryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostQnDetailActivity.this, CategoryActivity.class);
                //intent.putExtra(Constants.EXTRA_PICK_CATEGORY,Constants.REQUEST_CATEGORY_POSTQN);
                startActivityForResult(intent, Constants.REQUEST_CODE_CATEGORY);
            }
        });

        pickKeywordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostQnDetailActivity.this,PickKeywordsActivity.class);
                String ans = ansEdtTxt.getText().toString();
                intent.putExtra("answerText",ans);
                startActivityForResult(intent, Constants.REQUEST_CODE_PICK_KEYWORDS);
            }
        });

        postQn = PrefUtilsPostQn.getCurrentPostQn(this);
        if (postQn == null) {
            Log.d(TAG, "onCreate - postQn = null");
        }
        qnType = postQn.getQnType();
        qnTxtView.setText(postQn.getQnTxt());
        if (postQn.getQnType().equalsIgnoreCase(Constants.VALUE_PostQn_Objective)) {
            Log.d(TAG, "onCreate - postQnType == objective");
            postQnDetailObjLL.setVisibility(View.VISIBLE);

        } else {
            Log.d(TAG, "onCreate - postQnType == subjective");
            postQnDetailSubjLL.setVisibility(View.VISIBLE);
        }



    }

    View.OnClickListener sumbitButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String min = minEditText.getText().toString();
            String sec = secEditText.getText().toString();
            if((min==null || min.equalsIgnoreCase("") ) && (sec==null || sec.equalsIgnoreCase("") )){
                Toast.makeText(PostQnDetailActivity.this, "Set a timer for Qn", Toast.LENGTH_LONG).show();
                return;
            }

            if (qnType.equalsIgnoreCase(Constants.VALUE_PostQn_Objective)) {
                String option1 = option1EdtTxt.getText().toString();
                String option2 = option2EdtTxt.getText().toString();
                String option3 = option3EdtTxt.getText().toString();
                String option4 = option4EdtTxt.getText().toString();
                if (allOptionsEmpty()) {
                    Toast.makeText(PostQnDetailActivity.this, "Please enter at least one option", Toast.LENGTH_LONG).show();
                    return;
                }

                optionsArray.add(option1);
                optionsArray.add(option2);
                optionsArray.add(option3);
                optionsArray.add(option4);
                postQn.setOptions(optionsArray);

                if (noOptionSelected()) {

                    Toast.makeText(PostQnDetailActivity.this, "Please check at least one option as correct option", Toast.LENGTH_LONG).show();
                    return;

                }

                if (opt1checkBox.isChecked()) {
                    correctOptionsArray.add(1);
                    proposed_answer.add(option1);
                }
                if (opt2checkBox.isChecked()) {
                    correctOptionsArray.add(2);
                    proposed_answer.add(option2);
                }
                if (opt3checkBox.isChecked()) {
                    correctOptionsArray.add(3);
                    proposed_answer.add(option3);
                }
                if (opt4checkBox.isChecked()) {
                    correctOptionsArray.add(4);
                    proposed_answer.add(option4);
                }
                postQn.setCorrectOptns(correctOptionsArray);
                postQn.setProposed_answer(proposed_answer);


            } else {
                String ans = ansEdtTxt.getText().toString();
                if (ans == null || ans.equalsIgnoreCase("")) {

                    Toast.makeText(PostQnDetailActivity.this, "Please enter answer", Toast.LENGTH_LONG).show();
                    return;
                }
                postQn.setAnsTxt(ans);
                if(postQn.getKeywords()==null){
                    Toast.makeText(PostQnDetailActivity.this, "Please select keywords", Toast.LENGTH_LONG).show();
                    return;

                }

                if(postQn.getKeywords().size()==0){
                    Toast.makeText(PostQnDetailActivity.this, "Please select keywords", Toast.LENGTH_LONG).show();
                    return;

                }
//                ArrayList<String> keywordsArray = new ArrayList<>();
//                String[] keywrds = ans.split("\\\\s+");
//                if (keywrds.length > 0) {
//                    for (int i = 0; i < keywrds.length; i++)
//                        keywordsArray.add(keywrds[i]);
//                }
//
//                postQn.setKeywords(keywordsArray);
            }

            int minutes = Integer.parseInt(min);
            int seconds = Integer.parseInt(sec);
            int timer = minutes*60+seconds;

            postQn.setTimer(Integer.toString(timer));
            postQn.setHintTxt(hintEdtTxt.getText().toString());

            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = s.format(new Date());
            Log.d(TAG, "postQn time = " + format);
            postQn.setTimeStamp(format);


            sendPostQnToServer();

            finish();


        }
    };

    public boolean noOptionSelected() {

        if (!(opt1checkBox.isChecked() || opt2checkBox.isChecked() || opt3checkBox.isChecked()
                || opt4checkBox.isChecked())) {
            return true;
        }

        return false;
    }

    public boolean allOptionsEmpty() {

        String option1 = option1EdtTxt.getText().toString();
        String option2 = option2EdtTxt.getText().toString();
        String option3 = option3EdtTxt.getText().toString();
        String option4 = option4EdtTxt.getText().toString();
        if ((option1 == null || option1.equalsIgnoreCase("")) && (option2 == null || option2.equalsIgnoreCase(""))
                && (option3 == null || option3.equalsIgnoreCase("")) && (option4 == null || option4.equalsIgnoreCase(""))) {

            return true;

        }

        return false;

    }

    public void sendPostQnToServer() {
        JSONObject jsonObjSend = new JSONObject();
        jsonObjSend.put(Constants.TAG_PostQn_UserSaberaID, PrefUtilsUser.getCurrentUser(this).getSaberaId());
        jsonObjSend.put(Constants.TAG_PostQn_Qn_Text, postQn.getQnTxt());
        jsonObjSend.put(Constants.TAG_PostQn_QnType, postQn.getQnType());
        jsonObjSend.put(Constants.TAG_PostQn_Timer, postQn.getTimer());
        jsonObjSend.put(Constants.TAG_PostQn_Hint, postQn.getHintTxt());
        jsonObjSend.put(Constants.TAG_PostQn_TimeStamp, postQn.getTimeStamp());
        jsonObjSend.put(Constants.TAG_PostQn_Categories, postQn.getCategories());
        JSONArray pAnsJArray = new JSONArray();
        if (postQn.getQnType().equalsIgnoreCase(Constants.VALUE_PostQn_Objective)) {

            jsonObjSend.put(Constants.TAG_PostQn_Option1, postQn.getOptions().get(0));
            jsonObjSend.put(Constants.TAG_PostQn_Option2, postQn.getOptions().get(1));
            jsonObjSend.put(Constants.TAG_PostQn_Option3, postQn.getOptions().get(2));
            jsonObjSend.put(Constants.TAG_PostQn_Option4, postQn.getOptions().get(3));
            jsonObjSend.put(Constants.TAG_PostQn_Option1_Status, true);

            ArrayList<String> cOptions = postQn.getProposed_answer();
            for(int i=0;i<cOptions.size();i++){
                JSONObject jObj = new JSONObject();
                jObj.put(Constants.TAG_M_User_QA_answers,cOptions.get(i));
                pAnsJArray.add(jObj);
            }

            if (postQn.getCorrectOptns().contains(1))
                jsonObjSend.put(Constants.TAG_PostQn_Option1_Status, "TRUE");
            else
                jsonObjSend.put(Constants.TAG_PostQn_Option1_Status, "FALSE");

            if (postQn.getCorrectOptns().contains(2))
                jsonObjSend.put(Constants.TAG_PostQn_Option2_Status, "TRUE");
            else
                jsonObjSend.put(Constants.TAG_PostQn_Option2_Status, "FALSE");

            if (postQn.getCorrectOptns().contains(3))
                jsonObjSend.put(Constants.TAG_PostQn_Option3_Status, "TRUE");
            else
                jsonObjSend.put(Constants.TAG_PostQn_Option3_Status, "FALSE");

            if (postQn.getCorrectOptns().contains(4))
                jsonObjSend.put(Constants.TAG_PostQn_Option4_Status, "TRUE");
            else
                jsonObjSend.put(Constants.TAG_PostQn_Option4_Status, "FALSE");


        } else {
            jsonObjSend.put(Constants.TAG_PostQn_Ans_Text, postQn.getAnsTxt());
            String keywords = "";
            ArrayList<String> keywordsArray = postQn.getKeywords();
            if (keywordsArray.size() > 0) {
                keywords = keywords + keywordsArray.get(0);
                for (int i = 1; i < keywordsArray.size(); i++)
                    keywords = keywords + "," + keywordsArray.get(i);
                for(int i=0;i<keywordsArray.size();i++){
                    JSONObject jObj = new JSONObject();
                    jObj.put(Constants.TAG_M_User_QA_answers,keywordsArray.get(i));
                    pAnsJArray.add(jObj);
                }
            }
            jsonObjSend.put(Constants.TAG_PostQn_Keywords, postQn.getKeywordString());
        }

        jsonObjSend.put(Constants.TAG_M_User_QA_pAns,pAnsJArray);

        if (jsonObjSend != null && jsonObjSend.size() > 0) {
            Log.d(TAG, "PostQnJsonSend = " + jsonObjSend.toString());
            if (Constants.backendTest) {
                new sendPostQnToServer().execute(jsonObjSend);
            }

        }


    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    public void onStop() {
        super.onStop();


    }

    public class sendPostQnToServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.sendPostQnToServerURL, params[0]);
            if (jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            if (jsonObjRec != null && jsonObjRec.size() > 0) {

                Log.d(TAG, "PostQnServerResponse = " + jsonObjRec + toString());
                String response = jsonObjRec.get(Constants.TAG_PostQn_Status).toString();
                Toast.makeText(PostQnDetailActivity.this,"Your Question was posted successfully",Toast.LENGTH_LONG).show();


            } else
                Log.e(TAG, "jsonObjRec == null");
        }
    }

}
