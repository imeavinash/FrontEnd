package com.example.avinashbehera.sabera.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avinashbehera.sabera.Activity.BaseActivity;
import com.example.avinashbehera.sabera.R;
import com.example.avinashbehera.sabera.model.User;
import com.example.avinashbehera.sabera.model.UserSeeQn;
import com.example.avinashbehera.sabera.network.HttpClient;
import com.example.avinashbehera.sabera.util.Constants;
import com.example.avinashbehera.sabera.util.PrefUtilsUser;

import org.json.JSONException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by avinashbehera on 25/08/16.
 */
public class SeeQnFragment extends Fragment {

    private TextView seeQnTxtView;
    private static int qnNos;
    private static int currentQnNo;
    private static ArrayList<UserSeeQn> qnArrayList;
    private static ArrayList<LinearLayout> qnLLArrayList;
    public FrameLayout qnContainer;
    private TextView timerTxtView;
    private static MyCountDownTimer countDownTimer;

    public static final String TAG = SeeQnFragment.class.getSimpleName();

    public SeeQnFragment(){

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_see_qns, container, false);
        seeQnTxtView = (TextView)rootView.findViewById(R.id.seeQnTxtView);
        User user = PrefUtilsUser.getCurrentUser(getContext());
        //seeQnTxtView.setText(user.getQuestions());

        qnContainer = (FrameLayout)rootView.findViewById(R.id.container);
        timerTxtView = (TextView)rootView.findViewById(R.id.timertxtview);


        qnArrayList = user.getQuestionArray();
        if(qnArrayList != null && qnArrayList.size()>0){
            qnNos = qnArrayList.size();
            currentQnNo = qnArrayList.size()-1;
            addQnsToLayout();
        }







        return rootView;
    }

    public  void addQnsToLayout(){




        //qnLLArrayList = new ArrayList<>();


            UserSeeQn qn = qnArrayList.get(currentQnNo);
            if (qn.getQnType().equalsIgnoreCase("objective")) {

                LayoutInflater inflater1 = LayoutInflater.from(getContext());
                final LinearLayout qn1 = (LinearLayout)inflater1.inflate(R.layout.see_question_objective,null);
                qnContainer.addView(qn1);
                //qnLLArrayList.add(qn1);


                TextView qnTxt = (TextView)qn1.findViewById(R.id.seeQnObjQnText);
                TextView option1Txt = (TextView)qn1.findViewById(R.id.option1txtview);
                TextView option2Txt = (TextView)qn1.findViewById(R.id.option2txtview);
                TextView option3Txt = (TextView)qn1.findViewById(R.id.option3txtview);
                TextView option4Txt = (TextView)qn1.findViewById(R.id.option4txtview);
                Button submitbtn = (Button)qn1.findViewById(R.id.btnSeeQnObjSubmit);

                qnTxt.setText(qn.getQnText());
                option1Txt.setText(qn.getOption1());
                option2Txt.setText(qn.getOption2());
                option3Txt.setText(qn.getOption3());
                option4Txt.setText(qn.getOption4());



                submitbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        countDownTimer.cancel();


                        UserSeeQn qnAnswered = qnArrayList.get(currentQnNo);

                        CheckBox chkbox1 = (CheckBox)qn1.findViewById(R.id.opt1chkbox);
                        CheckBox chkbox2 = (CheckBox)qn1.findViewById(R.id.opt2chkbox);
                        CheckBox chkbox3 = (CheckBox)qn1.findViewById(R.id.opt3chkbox);
                        CheckBox chkbox4 = (CheckBox)qn1.findViewById(R.id.opt4chkbox);
                        JSONObject jsonObjectSend = new JSONObject();


                            jsonObjectSend.put(Constants.TAG_AnsQn_Qn_ID,qnAnswered.getqId());
                            jsonObjectSend.put(Constants.TAG_AnsQn_UserId_Qnr,qnAnswered.getuId());
                            String currentUserID = PrefUtilsUser.getCurrentUser(getContext()).getSaberaId();
                            jsonObjectSend.put(Constants.TAG_AnsQn_UserId_Answr,currentUserID);
                            JSONArray answers_q = new JSONArray();
                            JSONArray answers_ans = new JSONArray();

                        jsonObjectSend.put(Constants.TAG_AnsQn_Qn_Type,qnAnswered.getQnType());

                        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String format = s.format(new Date());
                        jsonObjectSend.put(Constants.TAG_AnsQn_timestamp,format);


                                if(qnAnswered.isStatus1()){
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption1());
                                    answers_q.add(jsonObject);
                                }

                                if(qnAnswered.isStatus2()){
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption2());
                                    answers_q.add(jsonObject);
                                }

                                if(qnAnswered.isStatus3()){
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption3());
                                    answers_q.add(jsonObject);
                                }

                                if(qnAnswered.isStatus4()){
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption4());
                                    answers_q.add(jsonObject);
                                }

                                if(chkbox1.isChecked()){
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption1());
                                    answers_ans.add(jsonObject);
                                }

                                if(chkbox2.isChecked()){
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption2());
                                    answers_ans.add(jsonObject);
                                }

                                if(chkbox3.isChecked()){
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption3());
                                    answers_ans.add(jsonObject);
                                }

                                if(chkbox4.isChecked()){
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption4());
                                    answers_ans.add(jsonObject);
                                }


                            jsonObjectSend.put(Constants.TAG_AnsQn_Ans_Qnr,answers_q);
                            jsonObjectSend.put(Constants.TAG_AnsQn_Ans_Answr,answers_ans);


                        if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                            new sendAnsToServer().execute(jsonObjectSend);
                        }

                        qnContainer.removeAllViews();

                        currentQnNo--;
                        if(currentQnNo>=0){
                            int nextTimer = Integer.parseInt(qnArrayList.get(currentQnNo).getTimer());
                            nextTimer = nextTimer*1000;
                            countDownTimer=new MyCountDownTimer(nextTimer,1000);
                            countDownTimer.start();
                            addQnsToLayout();
                        }
                    }
                });

                Log.d(TAG,"qn = "+qn.getQnText());


            }
            //if qn is subjective

            else{

                LayoutInflater inflater1 = LayoutInflater.from(getContext());
                final LinearLayout qn1 = (LinearLayout)inflater1.inflate(R.layout.see_qn_subjective,null);
                qnContainer.addView(qn1);
                //qnLLArrayList.add(qn1);


                TextView qnTxt = (TextView)qn1.findViewById(R.id.seeQnSubjQnText);


                Button submitbtn = (Button)qn1.findViewById(R.id.btnSeeQnSubjSubmit);

                qnTxt.setText(qn.getQnText());

                submitbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        countDownTimer.cancel();






                        UserSeeQn qnAnswered = qnArrayList.get(currentQnNo);
                        //LinearLayout qnLL = qnLLArrayList.get(currentQnNo);
                        EditText ansTxt = (EditText)qn1.findViewById(R.id.ansEditText);
                        String ans = ansTxt.getText().toString();
                        if(ans==null || ans.equalsIgnoreCase("")){
                            Toast.makeText(getContext(),"Please enter answer",Toast.LENGTH_LONG).show();
                            return;
                        }

                        //countDownTimer.cancel();

                        JSONObject jsonObjectSend = new JSONObject();


                        jsonObjectSend.put(Constants.TAG_AnsQn_Qn_ID,qnAnswered.getqId());
                        jsonObjectSend.put(Constants.TAG_AnsQn_UserId_Qnr,qnAnswered.getuId());
                        String currentUserID = PrefUtilsUser.getCurrentUser(getContext()).getSaberaId();
                        jsonObjectSend.put(Constants.TAG_AnsQn_UserId_Answr,currentUserID);
                        JSONArray answers_q = new JSONArray();
                        JSONArray answers_ans = new JSONArray();

                        jsonObjectSend.put(Constants.TAG_AnsQn_Qn_Type,qnAnswered.getQnType());

                        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String format = s.format(new Date());
                        jsonObjectSend.put(Constants.TAG_AnsQn_timestamp,format);


                        ArrayList<String> keywords = qnAnswered.getKeywords();
                        if(keywords!=null && keywords.size()>0){
                            for(int i=0;i<keywords.size();i++){
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put(Constants.TAG_AnsQn_answers,keywords.get(i));
                                answers_q.add(jsonObject);
                            }
                        }

                        String[] keywrds = ans.split("\\\\s+");
                        if (keywrds.length > 0) {
                            for (int i = 0; i < keywrds.length; i++){
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put(Constants.TAG_AnsQn_answers,keywrds[i]);
                                answers_ans.add(jsonObject);
                            }

                        }


                        jsonObjectSend.put(Constants.TAG_AnsQn_Ans_Qnr,answers_q);
                        jsonObjectSend.put(Constants.TAG_AnsQn_Ans_Answr,answers_ans);


                        if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                            new sendAnsToServer().execute(jsonObjectSend);
                        }
                        //qnContainer.removeViewAt(currentQnNo);
                        qnContainer.removeAllViews();
                        currentQnNo--;
                        if(currentQnNo>=0){
                            int nextTimer = Integer.parseInt(qnArrayList.get(currentQnNo).getTimer());
                            nextTimer = nextTimer*1000;
                            countDownTimer=new MyCountDownTimer(nextTimer,1000);
                            countDownTimer.start();
                            addQnsToLayout();
                        }
                    }
                });

                Log.d(TAG,"qn = "+qn.getQnText());

            }

        if(currentQnNo == qnNos-1){
            int firstQnTimer = Integer.parseInt(qnArrayList.get(currentQnNo).getTimer());
            firstQnTimer=firstQnTimer*1000;
            countDownTimer=new MyCountDownTimer(firstQnTimer,1000);
            countDownTimer.start();
        }



        //user.setQnLLArray(qnLLArrayList);
        //PrefUtilsUser.setCurrentUser(user,getContext());



    }

    public class MyCountDownTimer extends CountDownTimer {

        //public static final String TAG = MyCountDownTimer.class.getSimpleName();

        public long currentTime = 0;

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }



        @Override
        public void onFinish() {

            Log.d(TAG,"MyCountDownTimer - onFinish");

            timerTxtView.setText("0");
            if(currentTime==1){
                qnContainer.removeAllViews();
                //qnContainer.removeViewAt(currentQnNo);
                currentQnNo--;
                if(currentQnNo>=0){
                    int nextTimer = Integer.parseInt(qnArrayList.get(currentQnNo).getTimer());
                    nextTimer = nextTimer*1000;
                    countDownTimer=new MyCountDownTimer(nextTimer,1000);
                    countDownTimer.start();
                    addQnsToLayout();
                }
            }
        }



        @Override
        public void onTick(long millisUntilFinished) {

            currentTime = millisUntilFinished/1000;

            if(currentTime==5){
                String hint = qnArrayList.get(currentQnNo).getHintText();
                Toast.makeText(getContext(),"Hint : "+hint,Toast.LENGTH_LONG).show();
            }

            timerTxtView.setText(String.valueOf(millisUntilFinished/1000));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public class sendAnsToServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.sendAnswerToServerURL, params[0]);
            if (jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            if (jsonObjRec != null && jsonObjRec.size() > 0) {

                Log.d(TAG, "sendAnsToServerResponse = " + jsonObjRec + toString());
                String response = jsonObjRec.get(Constants.TAG_PostQn_Status).toString();


            } else
                Log.e(TAG, "jsonObjRec == null");
        }
    }
}
