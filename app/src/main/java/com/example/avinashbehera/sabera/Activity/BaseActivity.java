package com.example.avinashbehera.sabera.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.avinashbehera.sabera.R;
import com.example.avinashbehera.sabera.adapters.BaseActivityPagerAdapter;
import com.example.avinashbehera.sabera.fragments.MessageFragment;
import com.example.avinashbehera.sabera.fragments.MoreFragment;
import com.example.avinashbehera.sabera.fragments.PostQnFragment;
import com.example.avinashbehera.sabera.fragments.SeeQnFragment;
import com.example.avinashbehera.sabera.gcm.GCMRegistrationIntentService;
import com.example.avinashbehera.sabera.model.User;
import com.example.avinashbehera.sabera.model.UserSeeQn;
import com.example.avinashbehera.sabera.network.HttpClient;
import com.example.avinashbehera.sabera.util.Constants;
import com.example.avinashbehera.sabera.util.PrefUtilsUser;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.simple.JSONArray;
//import org.json.simple.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

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
    private ViewPager mViewPager;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<UserSeeQn> qnArrayList;
    public static final String TAG = BaseActivity.class.getSimpleName();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mBaseActivityPagerAdapter = new BaseActivityPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mBaseActivityPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    Log.d(TAG,"gcm token ="+token);

                    User user = PrefUtilsUser.getCurrentUser(BaseActivity.this);
                    user.setGcmRegToken(token);
                    JSONObject jsonObjectSend = new JSONObject();

                        jsonObjectSend.put(Constants.TAG_SendRegToken_token,token);
                        jsonObjectSend.put(Constants.TAG_SendRegToken_userId,PrefUtilsUser.getCurrentUser(getApplication()).getSaberaId());


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
            Intent intent = new Intent(this, GCMRegistrationIntentService.class);
            startService(intent);
        }

        qnArrayList = new ArrayList<>();
        User user = PrefUtilsUser.getCurrentUser(this);
        String jsonString = user.getQnJsonArray().toJSONString();
        JSONParser parser = new JSONParser();
        JSONArray arr = null;
        try {
            arr = (JSONArray) parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //JSONArray qnJsonArray = user.getQnJsonArray();
        for (int i = 0; i < arr.size(); i++) {
            Log.d(TAG, "BaseActivity - onCreate - qnJsonArray loop - i = " + i);
            UserSeeQn qn = new UserSeeQn();
            //Log.d(TAG, qnJsonArray.toJSONString());
            JSONObject jsonObject = (JSONObject) arr.get(i);
            qn.setqId(jsonObject.get(Constants.TAG_SEEQN_QN_ID).toString());
            qn.setqId(jsonObject.get(Constants.TAG_SEEQN_QNR_ID).toString());
            qn.setQnText(jsonObject.get(Constants.TAG_SEEQN_QN_TEXT).toString());
            qn.setHintText(jsonObject.get(Constants.TAG_SEEQN_Hint).toString());
            qn.setTimer(jsonObject.get(Constants.TAG_SEEQN_Timer).toString());
            qn.setQnType(jsonObject.get(Constants.TAG_SEEQN_QN_TYPE).toString());
            if (qn.getQnType().equalsIgnoreCase(Constants.VALUE_SEEQN_Objective)) {

                qn.setOption1(jsonObject.get(Constants.TAG_SEEQN_OPTION1).toString());
                qn.setOption2(jsonObject.get(Constants.TAG_SEEQN_OPTION2).toString());
                qn.setOption3(jsonObject.get(Constants.TAG_SEEQN_OPTION3).toString());
                qn.setOption4(jsonObject.get(Constants.TAG_SEEQN_OPTION4).toString());

                if (jsonObject.get(Constants.TAG_SEEQN_Option1_Status).toString().equalsIgnoreCase("true"))
                    qn.setStatus1(true);
                else
                    qn.setStatus1(false);

                if (jsonObject.get(Constants.TAG_SEEQN_Option2_Status).toString().equalsIgnoreCase("true"))
                    qn.setStatus2(true);
                else
                    qn.setStatus2(false);

                if (jsonObject.get(Constants.TAG_SEEQN_Option3_Status).toString().equalsIgnoreCase("true"))
                    qn.setStatus3(true);
                else
                    qn.setStatus3(false);

                if (jsonObject.get(Constants.TAG_SEEQN_Option4_Status).toString().equalsIgnoreCase("true"))
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

        user.setQuestionArray(qnArrayList);
        PrefUtilsUser.setCurrentUser(user, this);



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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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


    }

    @Override
    public void onStop() {
        super.onStop();


    }
}
