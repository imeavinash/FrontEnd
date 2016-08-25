package com.example.avinashbehera.sabera.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avinashbehera.sabera.R;
import com.example.avinashbehera.sabera.model.User;
import com.example.avinashbehera.sabera.util.Constants;
import com.example.avinashbehera.sabera.util.PrefUtilsUser;
import com.example.avinashbehera.sabera.network.HttpClient;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class LogoutActivity extends AppCompatActivity {

    private TextView btnLogout;
    User user;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView genderTextView;
    private ProfilePictureView profilePictureView;
    private Bitmap bitmap;
    private ImageView mImageView;
    private Button userIdButtton;
    private TextView userSaberaIdTextView;
    private TextView birthdayTextView;
    private TextView categoriesTextView;




    private static final String TAG = LogoutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        user= PrefUtilsUser.getCurrentUser(LogoutActivity.this);

        btnLogout = (TextView)findViewById(R.id.btnLogout);
        nameTextView = (TextView)findViewById(R.id.nameTextView);
        emailTextView = (TextView)findViewById(R.id.emailTextView);
        genderTextView = (TextView)findViewById(R.id.genderTextView);
        userSaberaIdTextView = (TextView)findViewById(R.id.userIdTextView);
        birthdayTextView = (TextView)findViewById(R.id.birthdayTextView);
        //mImageView=(ImageView)findViewById(R.id.profilePicture);
        userIdButtton=(Button) findViewById(R.id.userIdButton);
        profilePictureView = (ProfilePictureView)findViewById(R.id.profilePicture);
        categoriesTextView=(TextView) findViewById(R.id.categoryTextView);

        if(user != null){

            profilePictureView.setProfileId(user.getFacebookID());
            profilePictureView.setPresetSize(ProfilePictureView.LARGE);
            nameTextView.setText(user.getName());
            emailTextView.setText(user.getEmail());
            genderTextView.setText(user.getGender());
            birthdayTextView.setText(user.getBirthday());
            userSaberaIdTextView.setText(user.getSaberaId());
            categoriesTextView.setText(user.getCategoryList().toString());

            //new setProfilePicture().execute();


        }

        userIdButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG,"userIdButton - onClick");
                JSONObject jsonObjSend = buildJsonToSend();
                if(jsonObjSend.length() > 0)
                    new SendPostRequest().execute(jsonObjSend);
                else
                    Log.e(TAG,"jsonObjSend length = 0");

                // Send the HttpPostRequest and receive a JSONObject in return
                //JSONObject jsonObjRecv = HttpClient.SendHttpPostUsingUrlConnection(URL, jsonObjSend);
//                try{
//                    if(jsonObjRecv != null)
//                        userIdTextView.setText(jsonObjRecv.getString(TAG_UserSaberaId));
//                    else
//                        Log.e(TAG,"jsonObjRec == null");
//                }catch (JSONException e) {
//                    e.printStackTrace();
//                }


            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.example.avinashbehera.sabera.util.PrefUtilsUser.clearCurrentUser(LogoutActivity.this);


                // We can logout from facebook by calling following method
                LoginManager.getInstance().logOut();


                Intent i= new Intent(LogoutActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public class SendPostRequest extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.getSaberaIdURL,params[0]);
            if(jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            try{
                if(jsonObjRec != null)
                    userSaberaIdTextView.setText(jsonObjRec.getString(Constants.TAG_UserSaberaId));
                else
                    Log.e(TAG,"jsonObjRec == null");
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private JSONObject buildJsonToSend() {

        JSONObject jsonObjSend = new JSONObject();

        try {
            // Add key/value pairs
            jsonObjSend.put(Constants.TAG_Name, user.getName());
            jsonObjSend.put(Constants.TAG_Email, user.getEmail());
            jsonObjSend.put(Constants.TAG_Gender, user.getGender());
            jsonObjSend.put(Constants.TAG_UserFBId, user.getFacebookID());


            // Add a nested JSONObject (e.g. for header information)
            //JSONObject header = new JSONObject();
            //header.put("deviceType","Android"); // Device type
            //header.put("deviceVersion","2.0"); // Device OS version
            //header.put("language", "es-es");	// Language of the Android client
            //jsonObjSend.put("header", header);

            // Output the JSON object we're sending to Logcat:
            //Log.i(TAG, jsonObjSend.toString(2));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObjSend;

    }



    private class setProfilePicture extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            URL imageURL = null;
            try {
                imageURL = new URL("https://graph.facebook.com/" + user.getFacebookID() + "/picture?type=large");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                bitmap  = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //mImageView.setImageBitmap(bitmap);
        }
    }
}
