package com.avinashbehera.sabera.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avinashbehera.sabera.adapters.ChatActivityAdapter;
import com.avinashbehera.sabera.model.MatchedUser;
import com.avinashbehera.sabera.model.Message;
import com.avinashbehera.sabera.model.MsgFrgmChatHead;
import com.avinashbehera.sabera.model.User;
import com.avinashbehera.sabera.network.HttpClient;
import com.avinashbehera.sabera.util.Constants;
import com.avinashbehera.sabera.util.PrefUtilsUser;
import com.avinashbehera.sabera.R;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {


    private String TAG = ChatActivity.class.getSimpleName();

    public static String userId="";
    private RecyclerView recyclerView;
    private ChatActivityAdapter mAdapter;
    private ArrayList<Message> messageList;
    private EditText inputEdtTxt;
    private Button btnSend;
    private String selfUserId;
    private BroadcastReceiver mPushNotBroadcastReceiver;
    private TextView chatUserNameTxtView;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"ChatActivity - onCreate");
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputEdtTxt = (EditText) findViewById(R.id.message);
        btnSend = (Button) findViewById(R.id.btn_send);
        chatUserNameTxtView = (TextView) findViewById(R.id.chatUserNameTxtView);

        Intent intent = getIntent();
        userId = intent.getStringExtra(Constants.EXTRA_Chat_UserId);
        Log.d(TAG,"userId from extra = "+userId);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor;
        editor = sharedPreferences.edit();
        editor.putString("currentChatUserId",userId);
        editor.apply();

        if (userId == null || userId.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "UserId not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        name = intent.getStringExtra(Constants.EXTRA_Chat_Name);
        chatUserNameTxtView.setText(name);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        messageList = new ArrayList<>();

        selfUserId = PrefUtilsUser.getCurrentUser(this).getSaberaId();
        Log.d(TAG,"selfUserId = "+selfUserId);

        mAdapter = new ChatActivityAdapter(this,messageList,selfUserId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        chatUserNameTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this,MatchedUserProfileActivity.class);
                intent.putExtra(Constants.EXTRA_Chat_UserId,userId);
                startActivity(intent);
            }
        });

        fetchChatThread();

        mPushNotBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d(TAG,"mPushNotBroadcastReceiver - onReceive");
                if(intent.getStringExtra(Constants.EXTRA_Push_Not_Type).equals(Constants.VALUE_Push_Not_Type_Chat)){
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChatActivity.this);
                    String key = "currentChatUserId";
                    String currentUserId =  sharedPreferences.getString(key,"xxx");
                    String senderId = intent.getStringExtra(Constants.EXTRA_Push_Not_Chat_Userid);
                    Log.d(TAG,"senderID = "+senderId);
                    Log.d(TAG,"userId = "+userId);
                    Log.d(TAG,"currentUserId = "+currentUserId);
                    if(senderId.equals(currentUserId)){
                        Log.d(TAG,"mPushNotBroadcastReceiver - onReceive - userId equals current userId");
                        handleNewChat(intent.getStringExtra(Constants.EXTRA_Push_Not_Data));
                    }
                }



            }
        };



    }

    public void handleNewChat(String jsonStringChat){

        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObjChat = (JSONObject)parser.parse(jsonStringChat);
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
                        ArrayList<Message> msgList = mUser.getMessagesList();
                        msgList.add(message);
                        messageList.add(message);
                        mAdapter.notifyDataSetChanged();
                        mUser.setMessagesList(msgList);
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

            }else{
                Log.e(TAG,"jsonObjChat = null or size = 0");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void fetchChatThread(){

        User user = PrefUtilsUser.getCurrentUser(this);

        ArrayList<MatchedUser> mUserList = user.getMatchedUserList();
        if(mUserList!=null){
            for(int i=0;i<mUserList.size();i++){
                MatchedUser mUser = mUserList.get(i);
                if(mUser.getUserId().equals(userId)){
                    messageList.clear();
                    messageList.addAll(mUser.getMessagesList());
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void sendMessage(){

        final String message = this.inputEdtTxt.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObjectSend = new JSONObject();
        jsonObjectSend.put(Constants.TAG_Send_Chat_Msg_Txt,message);
        jsonObjectSend.put(Constants.TAG_Send_Chat_SenderId,selfUserId);
        jsonObjectSend.put(Constants.TAG_Send_Chat_ReceiverId,userId);
        jsonObjectSend.put(Constants.TAG_Send_Chat_Timestamp,getTimestamp());

        if (jsonObjectSend != null && jsonObjectSend.size() > 0) {

            new SendChatToServer().execute(jsonObjectSend);


        }else{
            Log.e(TAG,"jsonObjectSend = null or size = 0");
        }

        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        inputEdtTxt.getText().clear();

    }

    public class SendChatToServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.sendChatToServerURL, params[0]);
            if (jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            if (jsonObjRec != null && jsonObjRec.size() > 0) {

                Log.d(TAG, "SendChatToServer = " + jsonObjRec + toString());
                String response = jsonObjRec.get(Constants.TAG_Send_Chat_Response).toString();
                if(response.equalsIgnoreCase("success")){

                    Message message = new Message();
                    message.setMessageId(jsonObjRec.get(Constants.TAG_Send_Chat_Msg_Id).toString());
                    message.setMsgTxt(jsonObjRec.get(Constants.TAG_Send_Chat_Msg_Txt).toString());
                    message.setTimeStamp(jsonObjRec.get(Constants.TAG_Send_Chat_Timestamp).toString());
                    message.setSenderId(selfUserId);
                    message.setRecvrId(userId);
                    messageList.add(message);
                    mAdapter.notifyDataSetChanged();
                    User user = PrefUtilsUser.getCurrentUser(ChatActivity.this);
                    ArrayList<MatchedUser> mUserList = user.getMatchedUserList();
                    MatchedUser mUser;
                    if(mUserList!=null){
                        for(int i=0;i<mUserList.size();i++){
                            if(mUserList.get(i).getUserId().equals(userId)){
                                mUser = mUserList.get(i);
                                ArrayList<Message> messagesArList = mUser.getMessagesList();
                                messagesArList.add(message);
                                mUser.setMessagesList(messagesArList);
                                mUserList.set(i,mUser);
                                user.setMatchedUserList(mUserList);
                                PrefUtilsUser.setCurrentUser(user,ChatActivity.this);
                                break;

                            }
                        }
                    }else{
                        Log.e(TAG,"sendMessage - mUserList = null");
                    }

                    user = PrefUtilsUser.getCurrentUser(ChatActivity.this);
                    ArrayList<MsgFrgmChatHead> chatHeadsList = user.getChatHeadsArrayList();
                    for(int i=0;i<chatHeadsList.size();i++){
                        MsgFrgmChatHead chatHead = chatHeadsList.get(i);
                        if(chatHead.getUserId().equals(userId)){
                            chatHead.setLastMessage(message.getMsgTxt());
                            chatHead.setTimeStamp(message.getTimeStamp());
                            chatHeadsList.set(i,chatHead);
                            user.setChatHeadsArrayList(chatHeadsList);
                            PrefUtilsUser.setCurrentUser(user,ChatActivity.this);
                            break;
                        }
                    }


                }else{
                    Log.e(TAG,"SendChatToServer - response = failure");
                }


            } else
                Log.e(TAG, "SendChatToServer - jsonObjRec == null");
        }
    }

    public String getTimestamp(){

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = s.format(new Date());
        return format;

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPushNotBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mPushNotBroadcastReceiver,
                new IntentFilter(Constants.Intent_Push_Notification));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor;
        editor = sharedPreferences.edit();
        editor.putString("currentChatUserId","");
        editor.apply();
        userId="";
    }
}
