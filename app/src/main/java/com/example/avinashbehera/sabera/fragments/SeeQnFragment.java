package com.example.avinashbehera.sabera.fragments;


import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
import com.example.avinashbehera.sabera.util.CustomViewPager;
import com.example.avinashbehera.sabera.util.OnSwipeTouchListener;
import com.example.avinashbehera.sabera.util.PrefUtilsUser;

import org.json.JSONException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Text;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by avinashbehera on 25/08/16.
 */
public class SeeQnFragment extends Fragment {

    private TextView seeQnTxtView;
    private static ArrayList<UserSeeQn> qnArrayList;
    private static ArrayList<LinearLayout> qnLLArrayList;
    public FrameLayout qnContainer;
    private static MyCountDownTimer countDownTimer;
    private Button btnLoadQns;
    private boolean isVisible = false;
    private long currentTime=0;
    private LinearLayout timerLL;
    private TextView minTxtView;
    private TextView secTxtView;
    private TextView minSecSeparator;
    private AlphaAnimation animation1;


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
        btnLoadQns = (Button)rootView.findViewById(R.id.btnLoadQns);
        minTxtView = (TextView)rootView.findViewById(R.id.minTxtView);
        secTxtView = (TextView)rootView.findViewById(R.id.secTxtView);
        minSecSeparator = (TextView)rootView.findViewById(R.id.minSecSeparator);
        timerLL = (LinearLayout)rootView.findViewById(R.id.timerLL);

        btnLoadQns.setOnClickListener(loadQnsOnClickListener);

        if(currentTime==0){
            btnLoadQns.setVisibility(View.VISIBLE);
        }

        animation1 = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(300);
        animation1.setRepeatMode(Animation.REVERSE);
        animation1.setRepeatCount(Animation.INFINITE);




        return rootView;
    }

    public View.OnClickListener loadQnsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            User user1 = PrefUtilsUser.getCurrentUser(getContext());
            ArrayList<UserSeeQn> qnArr = user1.getQuestionArray();
            if (qnArr!=null && qnArr.size()>0) {
                addQnsToLayout();
            }else{
                JSONObject jsonObjectSend = new JSONObject();
                jsonObjectSend.put(Constants.TAG_UserSaberaId,user1.getSaberaId());
                if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                    new LoadQnsFromServer().execute(jsonObjectSend);
                }


            }

        }
    };

    public class LoadQnsFromServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.loadQnsRequestURL, params[0]);
            if (jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            if (jsonObjRec != null && jsonObjRec.size() > 0) {

                Log.d(TAG, "LoadQnsFromServerResponse = " + jsonObjRec + toString());
                setUserQns(jsonObjRec);
                addQnsToLayout();



            } else
                Log.e(TAG, "LoadQnsFromServer - jsonObjRec == null");
        }
    }

    public void setUserQns(JSONObject jsonObjRec){

        User user = PrefUtilsUser.getCurrentUser(getContext());

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
        PrefUtilsUser.setCurrentUser(user,getContext());

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG,"setUserVisibleHint - isVisibleToUser = "+isVisibleToUser);
        isVisible=isVisibleToUser;
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
    }

    public  void addQnsToLayout(){

        Log.d(TAG,"addQnsToLayout");

        animation1.cancel();
        timerLL.clearAnimation();
        timerLL.setVisibility(View.GONE);

        //timerLL.invalidate();


        Log.d(TAG,"isVisible() = "+isVisible());
        Log.d(TAG,"isVisible variable = "+isVisible);
        if(!isVisible){
            btnLoadQns.setVisibility(View.VISIBLE);
            return;
        }




        //qnLLArrayList = new ArrayList<>();

        User user =PrefUtilsUser.getCurrentUser(getContext());
        qnArrayList = user.getQuestionArray();
        if(qnArrayList==null || qnArrayList.size()==0){
            btnLoadQns.setVisibility(View.VISIBLE);

            return;
        }
        btnLoadQns.setVisibility(View.GONE);
        //qnNos=qnArrayList.size();


        UserSeeQn qn = qnArrayList.get(qnArrayList.size()-1);
        int nextTimer = Integer.parseInt(qn.getTimer());
        nextTimer = nextTimer*1000;
        currentTime = nextTimer;
        countDownTimer=new MyCountDownTimer(nextTimer,1000);
        countDownTimer.start();
            if (qn.getQnType().equalsIgnoreCase("objective")) {

                LayoutInflater inflater1 = LayoutInflater.from(getContext());
                final LinearLayout qn1 = (LinearLayout)inflater1.inflate(R.layout.see_question_objective,null);
                qnContainer.addView(qn1);
                Log.d(TAG,"addQnsToLayout - timerLL - setVisibility - visible");
                timerLL.setVisibility(View.VISIBLE);



                //qnLLArrayList.add(qn1);

                //((BaseActivity)getActivity()).getViewPager().setPagingEnabled(false);


                TextView qnTxt = (TextView)qn1.findViewById(R.id.seeQnObjQnText);
                TextView option1Txt = (TextView)qn1.findViewById(R.id.option1txtview);
                TextView option2Txt = (TextView)qn1.findViewById(R.id.option2txtview);
                TextView option3Txt = (TextView)qn1.findViewById(R.id.option3txtview);
                TextView option4Txt = (TextView)qn1.findViewById(R.id.option4txtview);
                Button submitbtn = (Button)qn1.findViewById(R.id.btnSeeQnObjSubmit);
                Button passbtn = (Button)qn1.findViewById(R.id.btnSeeQnObjPass);
                passbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPass();
                    }
                });

                qnTxt.setText(qn.getQnText());
                option1Txt.setText(qn.getOption1());
                option2Txt.setText(qn.getOption2());
                option3Txt.setText(qn.getOption3());
                option4Txt.setText(qn.getOption4());


                qn1.setOnTouchListener(new OnSwipeTouchListener(getContext()){
                    public void onSwipeLeft() {
                        Log.d(TAG,"onSwipeLeft");
                        onPass();
                        Toast.makeText(getContext(), "Swipe left", Toast.LENGTH_SHORT).show();
                    }
                });



                submitbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        countDownTimer.cancel();
                        currentTime=0;

                        User user1 = PrefUtilsUser.getCurrentUser(getContext());
                        ArrayList<UserSeeQn> qnArrayList = user1.getQuestionArray();


                        UserSeeQn qnAnswered = qnArrayList.get(qnArrayList.size()-1);

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

                        //qnArrayList.remove(qnNos-1);

                        qnArrayList.remove(qnArrayList.size()-1);
                        user1.setQuestionArray(qnArrayList);
                        PrefUtilsUser.setCurrentUser(user1,getContext());
                        addQnsToLayout();


//                        currentQnNo--;
//                        if(currentQnNo>=0){
//                            int nextTimer = Integer.parseInt(qnArrayList.get(currentQnNo).getTimer());
//                            nextTimer = nextTimer*1000;
//                            countDownTimer=new MyCountDownTimer(nextTimer,1000);
//                            countDownTimer.start();
//                            addQnsToLayout();
//                        }
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

                Log.d(TAG,"addQnsToLayout - timerLL - setVisibility - visible");

                timerLL.setVisibility(View.VISIBLE);



                TextView qnTxt = (TextView)qn1.findViewById(R.id.seeQnSubjQnText);


                Button submitbtn = (Button)qn1.findViewById(R.id.btnSeeQnSubjSubmit);

                qnTxt.setText(qn.getQnText());

                Button passbtn = (Button)qn1.findViewById(R.id.btnSeeQnSubjPass);
                passbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPass();
                    }
                });

                qn1.setOnTouchListener(new OnSwipeTouchListener(getContext()){
                    public void onSwipeLeft() {
                        Log.d(TAG,"onSwipeLeft");
                        onPass();
                        Toast.makeText(getContext(), "Swipe left", Toast.LENGTH_SHORT).show();
                    }
                });

                submitbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        countDownTimer.cancel();
                        currentTime = 0;





                        User user1 = PrefUtilsUser.getCurrentUser(getContext());
                        ArrayList<UserSeeQn> qnArrayList = user1.getQuestionArray();

                        UserSeeQn qnAnswered = qnArrayList.get(qnArrayList.size()-1);
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
                        //qnArrayList.remove(qnNos-1);

                        qnArrayList.remove(qnArrayList.size()-1);
                        user1.setQuestionArray(qnArrayList);
                        PrefUtilsUser.setCurrentUser(user1,getContext());
                        addQnsToLayout();
//                        qnArrayList.remove(currentQnNo);
//                        currentQnNo--;
//
//                        if(currentQnNo>=0){
//                            int nextTimer = Integer.parseInt(qnArrayList.get(currentQnNo).getTimer());
//                            nextTimer = nextTimer*1000;
//                            countDownTimer=new MyCountDownTimer(nextTimer,1000);
//                            countDownTimer.start();
//                            addQnsToLayout();
//                        }
                    }
                });

                Log.d(TAG,"qn = "+qn.getQnText());

            }

//        if(currentQnNo == qnNos-1){
//            int firstQnTimer = Integer.parseInt(qnArrayList.get(currentQnNo).getTimer());
//            firstQnTimer=firstQnTimer*1000;
//            countDownTimer=new MyCountDownTimer(firstQnTimer,1000);
//            countDownTimer.start();
//        }



        //user.setQnLLArray(qnLLArrayList);
        //PrefUtilsUser.setCurrentUser(user,getContext());



    }



    public void onPass(){
        Toast.makeText(getContext(),"Pass Qn",Toast.LENGTH_LONG).show();
        countDownTimer.cancel();
        currentTime=0;
        User user = PrefUtilsUser.getCurrentUser(getContext());
        ArrayList<UserSeeQn> qnArr = user.getQuestionArray();
        if(qnArr.size()>0){

            UserSeeQn currentQn = qnArr.get(qnArr.size()-1);
            JSONObject jsonObjectSend = new JSONObject();


            jsonObjectSend.put(Constants.TAG_AnsQn_Qn_ID,currentQn.getqId());
            jsonObjectSend.put(Constants.TAG_AnsQn_UserId_Qnr,currentQn.getuId());
            jsonObjectSend.put(Constants.TAG_AnsQn_UserId_Answr,user.getSaberaId());
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = s.format(new Date());
            jsonObjectSend.put(Constants.TAG_AnsQn_timestamp,format);
            if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                new sendPassAnsToServer().execute(jsonObjectSend);
            }
            qnContainer.removeAllViews();
            //qnArrayList.remove(qnNos-1);

            qnArr.remove(qnArr.size()-1);
            user.setQuestionArray(qnArr);
            PrefUtilsUser.setCurrentUser(user,getContext());
        }

        addQnsToLayout();

    }

    public class sendPassAnsToServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.sendPassAnswerToServerURL, params[0]);
            if (jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            if (jsonObjRec != null && jsonObjRec.size() > 0) {

                Log.d(TAG, "sendPassAnsToServer response = " + jsonObjRec + toString());
                //String response = jsonObjRec.get(Constants.TAG_PostQn_Status).toString();


            } else
                Log.e(TAG, "sendPassAnsToServer - jsonObjRec == null");
        }
    }

    public class MyCountDownTimer extends CountDownTimer {

        //public static final String TAG = MyCountDownTimer.class.getSimpleName();

        public long currentTime1 = 0;

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }



        @Override
        public void onFinish() {

            Log.d(TAG,"MyCountDownTimer - onFinish");
            Log.d(TAG,"currenTime1 = "+currentTime1);
            currentTime=0;

            minTxtView.setText("00");
            secTxtView.setText("00");
            if(currentTime1==1){
                Log.d(TAG,"timer onFinish currenTime1 = 1");
                animation1.cancel();
                timerLL.clearAnimation();
                timerLL.setVisibility(View.GONE);
                onPass();
            }

        }



        @Override
        public void onTick(long millisUntilFinished) {


            currentTime1 = millisUntilFinished/1000;
            currentTime = currentTime1;
            Log.d(TAG,"onTick - currentTime1 = "+currentTime1);

            if(currentTime1<=10){
                playNotificationSound();
                Vibrator vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
            }

            if(currentTime1==1){

            }


            if(currentTime1==5){
                User user1 = PrefUtilsUser.getCurrentUser(getContext());
                ArrayList<UserSeeQn> qnArrayList = user1.getQuestionArray();
                if(qnArrayList!=null){
                    if(qnArrayList.size()>0){
                        int currentQnNo = qnArrayList.size()-1;
                        String hint = qnArrayList.get(currentQnNo).getHintText();
                        Toast.makeText(getContext(),"Hint : "+hint,Toast.LENGTH_LONG).show();
                    }
                }

            }

            int currentTimeMin = (int)currentTime1/60;
            int currentTimeSec = (int)currentTime1%60;
            minTxtView.setText(String.valueOf(currentTimeMin));
            secTxtView.setText(String.valueOf(currentTimeSec));

            if(currentTime1==10){


                timerLL.startAnimation(animation1);
            }

        }

    }

    public void playNotificationSound() {
        try {
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getContext(), sound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG,"onResume");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
        if(currentTime!=0){
            onPass();
        }
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
