package com.avinashbehera.sabera.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.avinashbehera.sabera.adapters.BaseActivityPagerAdapter;
import com.avinashbehera.sabera.fragments.SeeQnFragment;
import com.avinashbehera.sabera.gcm.GCMRegistrationIntentService;
import com.avinashbehera.sabera.gcm.GCMTokenRefreshListenerService;
import com.avinashbehera.sabera.model.MatchedUser;
import com.avinashbehera.sabera.model.MatchedUserQn;
import com.avinashbehera.sabera.model.Message;
import com.avinashbehera.sabera.model.MsgFrgmChatHead;
import com.avinashbehera.sabera.model.User;
import com.avinashbehera.sabera.model.UserSeeQn;
import com.avinashbehera.sabera.network.HttpClient;
import com.avinashbehera.sabera.util.Constants;
import com.avinashbehera.sabera.util.CustomViewPager;
import com.avinashbehera.sabera.util.PrefUtilsUser;
import com.avinashbehera.sabera.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

//import org.json.simple.JSONException;

public class BaseActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private BaseActivityPagerAdapter mBaseActivityPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private CustomViewPager mViewPager;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<UserSeeQn> qnArrayList;
    public static final String TAG = BaseActivity.class.getSimpleName();
    private BroadcastReceiver mPushNotBroadcastReceiver;
    private int appBarHeight;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_base);
        appBarHeight = 0;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CoordinatorLayout mainLayout = (CoordinatorLayout)findViewById(R.id.main_content);
        ViewTreeObserver vto = mainLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                appBarHeight = getSupportActionBar().getHeight();
            }
        });
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mBaseActivityPagerAdapter = new BaseActivityPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mBaseActivityPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        mViewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                //Log.d(TAG,"mViewPager - onTouchListener");
//                return false;
//            }
//        });

        User user = PrefUtilsUser.getCurrentUser(BaseActivity.this);
        //String regToken = GCMTokenRefreshListenerService.refreshedToken;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String key = "firebaseToken";
        final String currentToken =  sharedPreferences.getString(key,"xxx");
        Log.d(TAG,"currentToken = "+currentToken);
        String userToken = user.getGcmRegToken();
        if(userToken==null || userToken.equalsIgnoreCase("") || !userToken.equals(currentToken)){
            Log.d(TAG,"user gcmToken = null or = blank or not equal to current token");
            userToken = currentToken;
            user.setGcmRegToken(userToken);
            PrefUtilsUser.setCurrentUser(user,this);
            JSONObject jsonObjectSend = new JSONObject();

            jsonObjectSend.put(Constants.TAG_SendRegToken_token,userToken);
            jsonObjectSend.put(Constants.TAG_SendRegToken_userId, PrefUtilsUser.getCurrentUser(getApplication()).getSaberaId());


            new sendRegTokenToServer().execute(jsonObjectSend);

        }




        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d(TAG,"mRegistrationBroadcastReceiver - onReceive");
                //If the broadcast has received with success
                //that means device is registered successfully
                if (intent.getAction().equals(GCMTokenRefreshListenerService.REGISTRATION_SUCCESS)) {
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
                    SharedPreferences.Editor editor;
                    editor = sharedPreferences.edit();
                    editor.putString("firebaseToken",token);
                    editor.apply();
                    Log.d(TAG,"gcm token ="+token);

                    User user = PrefUtilsUser.getCurrentUser(BaseActivity.this);
                    user.setGcmRegToken(token);
                    PrefUtilsUser.setCurrentUser(user,BaseActivity.this);
                    JSONObject jsonObjectSend = new JSONObject();

                        jsonObjectSend.put(Constants.TAG_SendRegToken_token,token);
                        jsonObjectSend.put(Constants.TAG_SendRegToken_userId, PrefUtilsUser.getCurrentUser(getApplication()).getSaberaId());


                    new sendRegTokenToServer().execute(jsonObjectSend);

                    //Displaying the token as toast
                    //Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                    //if the intent is not with success then displaying error messages
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            //Intent intent = new Intent(this, GCMTokenRefreshListenerService.class);
            //startService(intent);
        }

        mPushNotBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d(TAG,"mPushNotBroadcastReceiver - onReceive");

                if(intent.getStringExtra(Constants.EXTRA_Push_Not_Type).equals(Constants.VALUE_Push_Not_Type_Match)){
                    Log.d(TAG,"mPushNotBroadcastReceiver - onReceive - type - match");
                    handleNewMatch(intent.getStringExtra(Constants.EXTRA_Push_Not_Data));
                }else{
                    Log.d(TAG,"mPushNotBroadcastReceiver - onReceive - type - chat");
                    String senderId = intent.getStringExtra(Constants.EXTRA_Push_Not_Chat_Userid);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
                    String key = "currentChatUserId";
                    String currentUserId =  sharedPreferences.getString(key,"xxx");
                    Log.d(TAG,"currentUserId = "+currentUserId);
                    Log.d(TAG,"senderId = "+senderId);

                    //String currentChatUserId = ChatActivity.userId;
                    if(currentUserId!=null){
                        if(!senderId.equals(currentUserId)){
                            handleNewChat(intent.getStringExtra(Constants.EXTRA_Push_Not_Data));
                        }
                    }

                }
            }
        };

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            String pushType = extras.getString(Constants.EXTRA_Push_Not_Type,"");
            if(pushType!=null && !pushType.equalsIgnoreCase("")){

                if(pushType.equals(Constants.VALUE_Push_Not_Type_Match)){
                    Log.d(TAG,"pushType = match");
                    handleNewMatch(extras.getString(Constants.EXTRA_Push_Not_Data));
                }else{
                    Log.d(TAG,"pushType = chat");
                    String senderId = extras.getString(Constants.EXTRA_Push_Not_Chat_Userid);
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
                    key = "currentChatUserId";
                    String currentUserId =  sharedPreferences.getString(key,"xxx");
                    Log.d(TAG,"currentUserId = "+currentUserId);
                    Log.d(TAG,"senderId = "+senderId);
                    if(!senderId.equals(currentUserId)){
                        handleNewChat(extras.getString(Constants.EXTRA_Push_Not_Data));
                    }
                }

            }else{
                Log.d(TAG,"pushType = null or empty");
            }
        }


    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {



            //((SeeQnFragment)mBaseActivityPagerAdapter.getItem(position)).onPass();

        }

        @Override
        public void onPageSelected(int position) {

            Log.d(TAG,"onPageSelected - position = "+position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public int getAppBarHeight(){

        return appBarHeight;

    }

    public CustomViewPager getViewPager(){
        return mViewPager;
    }

    public void handleNewMatch(String jsonMatchString){

        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObjMatch = (JSONObject)parser.parse(jsonMatchString);
            Log.e(TAG,"jsonObjMatch = "+jsonObjMatch);
            if(jsonObjMatch!=null && jsonObjMatch.size()>0){

                User user = PrefUtilsUser.getCurrentUser(this);
                //MatchedUser mUser = new MatchedUser();
                ArrayList<MatchedUser> mUserList = user.getMatchedUserList();
                if(mUserList==null || mUserList.size()==0){
                    mUserList = new ArrayList<>();
                }

                JSONObject mUserJObj;
                //JSONObject mUserJObj = (JSONObject)jsonObjMatch.get(Constants.TAG_Push_Not_Match_Muser);
                JSONObject mQnJObj = (JSONObject)jsonObjMatch.get(Constants.TAG_M_User_QA);
                String qnrId = mQnJObj.get(Constants.TAG_M_User_QA_qnrId).toString();
                if(qnrId.equals(user.getSaberaId())){
                    mUserJObj = (JSONObject)jsonObjMatch.get(Constants.TAG_Push_Not_Match_Answr);
                }else{
                    mUserJObj = (JSONObject)jsonObjMatch.get(Constants.TAG_Push_Not_Match_Qnr);
                }
                String mUserId = mUserJObj.get(Constants.TAG_UserSaberaId).toString();
                int mUserIndex = -1;
                for(int i=0;i<mUserList.size();i++){
                    if(mUserList.get(i).getUserId().equals(mUserId)){
                        mUserIndex=i;
                        break;
                    }
                }
                //Old Matched User New Qn
                if(mUserIndex!=-1){

                    MatchedUser mUser = mUserList.get(mUserIndex);
                    ArrayList<MatchedUserQn> qnsAnswered = mUser.getQnsAnswered();
                    ArrayList<MatchedUserQn> qnsAsked = mUser.getQnsAsked();

                    mQnJObj = (JSONObject)jsonObjMatch.get(Constants.TAG_M_User_QA);
                    if(mQnJObj!=null && mQnJObj.size()>0){

                        MatchedUserQn mQn = new MatchedUserQn();
                        mQn.setQnTxt(mQnJObj.get(Constants.TAG_M_User_QA_qTxt).toString());
                        mQn.setQnrId(mQnJObj.get(Constants.TAG_M_User_QA_qnrId).toString());
                        mQn.setAnswererId(mQnJObj.get(Constants.TAG_M_User_QA_ansId).toString());
                        ArrayList<String> proposed_answer = new ArrayList<>();
                        ArrayList<String> attempted_answer = new ArrayList<>();
                        JSONArray pAnsJArray = (JSONArray)mQnJObj.get(Constants.TAG_M_User_QA_pAns);
                        JSONArray aAnsJArray = (JSONArray)mQnJObj.get(Constants.TAG_M_User_QA_aAns);
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
                            mUser.setQnsAnswered(qnsAnswered);
                        }else{
                            qnsAsked.add(mQn);
                            mUser.setQnsAsked(qnsAsked);
                        }

                        Toast.makeText(BaseActivity.this, "New match with "+mUser.getName(), Toast.LENGTH_LONG).show();

                        mUserList.set(mUserIndex,mUser);
                        user.setMatchedUserList(mUserList);
                        PrefUtilsUser.setCurrentUser(user,this);



                    }else{
                        Log.e(TAG,"mQnObj = null or size = 0");
                    }

                }
                //New Matched User
                else{

                    MatchedUser mUser = new MatchedUser();
                    ArrayList<MatchedUserQn> qnsAnswered = new ArrayList<>();
                    ArrayList<MatchedUserQn> qnsAsked = new ArrayList<>();

                    //JSONObject mUserJObject = (JSONObject)jsonObjMatch.get(Constants.TAG_Push_Not_Match_Muser);
                    mUser.setUserId(mUserJObj.get(Constants.TAG_UserSaberaId).toString());
                    mUser.setEmail(mUserJObj.get(Constants.TAG_Email).toString());
                    mUser.setName(mUserJObj.get(Constants.TAG_Name).toString());
                    mUser.setDob(mUserJObj.get(Constants.TAG_Birthday).toString());
                    mUser.setGender(mUserJObj.get(Constants.TAG_Gender).toString());
                    mUser.setCategories(mUserJObj.get(Constants.TAG_CATEGORIES).toString());
                    if(mUserJObj.get(Constants.TAG_Image_String)!=null){
                        mUser.setEncodedImage(mUserJObj.get(Constants.TAG_Image_String).toString());
                    }


                    MatchedUserQn mQn = new MatchedUserQn();
                    mQnJObj = (JSONObject)jsonObjMatch.get(Constants.TAG_M_User_QA);
                    if(mQnJObj!=null && mQnJObj.size()>0){

                        mQn.setQnTxt(mQnJObj.get(Constants.TAG_M_User_QA_qTxt).toString());
                        mQn.setQnrId(mQnJObj.get(Constants.TAG_M_User_QA_qnrId).toString());
                        mQn.setAnswererId(mQnJObj.get(Constants.TAG_M_User_QA_ansId).toString());
                        ArrayList<String> proposed_answer = new ArrayList<>();
                        ArrayList<String> attempted_answer = new ArrayList<>();
                        JSONArray pAnsJArray = (JSONArray)mQnJObj.get(Constants.TAG_M_User_QA_pAns);
                        JSONArray aAnsJArray = (JSONArray)mQnJObj.get(Constants.TAG_M_User_QA_aAns);
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

                    }else{

                        Log.e(TAG,"mQnObj = null or size = 0");

                    }






                    mUser.setQnsAnswered(qnsAnswered);
                    mUser.setQnsAsked(qnsAsked);

                    ArrayList<Message> messagesList = new ArrayList<>();
                    mUser.setMessagesList(messagesList);

                    mUserList.add(mUser);
                    user.setMatchedUserList(mUserList);

                    MsgFrgmChatHead chatHead = new MsgFrgmChatHead();
                    chatHead.setUserId(mUser.getUserId());
                    chatHead.setName(mUser.getName());
                    chatHead.setLastMessage("");
                    chatHead.setTimeStamp("");
                    chatHead.setUnreadMsgCount(0);

                    Toast.makeText(BaseActivity.this, "New match "+mUser.getName(), Toast.LENGTH_LONG).show();

                    ArrayList<MsgFrgmChatHead> chatHeadsList = user.getChatHeadsArrayList();
                    chatHeadsList.add(chatHead);
                    user.setChatHeadsArrayList(chatHeadsList);

                    PrefUtilsUser.setCurrentUser(user,this);

                }


            }else{
                Log.e(TAG,"handlNewMatch - jsonObjMatch = null or size = 0");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void handleNewChat(String jsonNewChatString){

        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObjChat = (JSONObject)parser.parse(jsonNewChatString);
            Log.e(TAG,"jsonObjChat = "+jsonObjChat);
            if(jsonObjChat!=null && jsonObjChat.size()>0){

                Message message = new Message();

                message.setMessageId(jsonObjChat.get(Constants.TAG_M_User_Msgs_Id).toString());
                message.setMsgTxt(jsonObjChat.get(Constants.TAG_M_User_Msgs_Txt).toString());
                message.setTimeStamp(jsonObjChat.get(Constants.TAG_M_User_Msgs_time).toString());
                message.setSenderId(jsonObjChat.get(Constants.TAG_M_User_Msgs_senderId).toString());
                message.setRecvrId(jsonObjChat.get(Constants.TAG_M_User_Msgs_receiverId).toString());

                User user = PrefUtilsUser.getCurrentUser(this);

                ArrayList<MatchedUser> mUserList = user.getMatchedUserList();
                for(int i=0;i<mUserList.size();i++){
                    MatchedUser mUser = mUserList.get(i);
                    if(mUser.getUserId().equals(message.getSenderId())){
                        ArrayList<Message> messagesList = mUser.getMessagesList();
                        messagesList.add(message);
                        mUser.setMessagesList(messagesList);
                        mUserList.set(i,mUser);
                        user.setMatchedUserList(mUserList);
                        ArrayList<MsgFrgmChatHead> chatHeadsList = user.getChatHeadsArrayList();
                        for(int j=0;j<chatHeadsList.size();j++){
                            MsgFrgmChatHead chatHead = chatHeadsList.get(j);
                            if(chatHead.getUserId().equals(message.getSenderId())){
                                chatHead.setTimeStamp(message.getTimeStamp());
                                chatHead.setLastMessage(message.getMsgTxt());
                                chatHeadsList.remove(j);
                                chatHeadsList.add(chatHead);
                                user.setChatHeadsArrayList(chatHeadsList);
                                break;
                            }
                        }
                        PrefUtilsUser.setCurrentUser(user,this);
                        break;
                    }
                }



                Fragment messageFragment = mBaseActivityPagerAdapter.getItem(2);
                if(messageFragment.isVisible()){
                    Log.d(TAG,"messageFragment - isVisible() is true");

                }


            }else{
                Log.e(TAG,"jsonObjChat = null or size = 0");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public class sendRegTokenToServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.sendGcmTokenLoginToServerURL, params[0]);
            if (jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            if (jsonObjRec != null && jsonObjRec.size() > 0) {

                Log.d(TAG, "sendRegTokenToServerResponse = " + jsonObjRec + toString());
                String response = jsonObjRec.get(Constants.TAG_PostQn_Status).toString();


            } else
                Log.e(TAG, "jsonObjRec == null");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPushNotBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMTokenRefreshListenerService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        LocalBroadcastManager.getInstance(this).registerReceiver(mPushNotBroadcastReceiver,
                new IntentFilter(Constants.Intent_Push_Notification));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        Log.d(TAG,"onTabSelected");

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
        Log.d(TAG,"onTabUnselected");

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Log.d(TAG,"onTabReselected");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
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


    }
}
