package com.avinashbehera.sabera.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avinashbehera.sabera.model.User;
import com.avinashbehera.sabera.network.HttpClient;
import com.avinashbehera.sabera.util.Constants;
import com.avinashbehera.sabera.util.PrefUtilsUser;
import com.avinashbehera.sabera.util.Utility;
import com.avinashbehera.sabera.R;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

//import org.json.simple.JSONException;

public class LogoutActivity extends AppCompatActivity {

    private TextView btnLogout;
    User user;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView genderTextView;
    private ImageView mImageView;

    private TextView userSaberaIdTextView;
    private TextView birthdayTextView;
    private TextView categoriesTextView;
    private Button btnUploadPicture;




    private static final String TAG = LogoutActivity.class.getSimpleName();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult");
        if(resultCode==RESULT_OK){


            if(requestCode == Constants.REQUEST_SELECT_PICTURE){
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = Utility.getBitmapFromUri(selectedImageUri,this);
                    Bitmap bm = Utility.getRoundedShape(bitmap);
                    //Bitmap bm = Bitmap.createScaledBitmap(bitmap,50,50,false);
                    mImageView.setImageBitmap(bm);
                    String encodedImgString = Utility.getImgEncString(bm);
                    User user = PrefUtilsUser.getCurrentUser(this);
                    user.setEncodedImage(encodedImgString);
                    PrefUtilsUser.setCurrentUser(user,this);
                    JSONObject jsonObjectSend = new JSONObject();
                    jsonObjectSend.put(Constants.TAG_UserSaberaId,user.getSaberaId());
                    jsonObjectSend.put(Constants.TAG_Image_String,user.getEncodedImage());
                    if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                        new SendPicToServer().execute(jsonObjectSend);
                    }
//                    User user = PrefUtilsUser.getCurrentUser(this);
//                    user.setEncodedImage(encodedImage);
//                    PrefUtilsUser.setCurrentUser(user,this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public class SendPicToServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.sendPicToServerURL,params[0]);
            if(jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            if(jsonObjRec != null && jsonObjRec.size() > 0){

                Log.d(TAG,"SendPicToServer - jsonObjRec - "+jsonObjRec.toJSONString());




            }
            else
                Log.e(TAG,"SendPicToServer - jsonObjRec == null");
        }
    }

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
        mImageView=(ImageView)findViewById(R.id.profilePicture);

        //profilePictureView = (ProfilePictureView)findViewById(R.id.profilePicture);
        categoriesTextView=(TextView) findViewById(R.id.categoryTextView);
        btnUploadPicture = (Button) findViewById(R.id.btnUploadPicture);

        if(user != null){

            //profilePictureView.setProfileId(user.getFacebookID());
            //profilePictureView.setPresetSize(ProfilePictureView.LARGE);
            nameTextView.setText(user.getName());
            emailTextView.setText(user.getEmail());
            genderTextView.setText(user.getGender());
            birthdayTextView.setText(user.getBirthday());
            userSaberaIdTextView.setText(user.getSaberaId());
            categoriesTextView.setText(user.getCategories());
            String encodedImageString = user.getEncodedImage();
            if(encodedImageString!=null && !encodedImageString.equalsIgnoreCase("")){
                Bitmap bm = Utility.getBmFromEncString(encodedImageString);
                mImageView.setImageBitmap(bm);
            }

            //new setProfilePicture().execute();


        }

        btnUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), Constants.REQUEST_SELECT_PICTURE);
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //com.avinashbehera.sabera.util.PrefUtilsUser.clearCurrentUser(LogoutActivity.this);


                // We can logout from facebook by calling following method
                if(FacebookSdk.isInitialized())
                    LoginManager.getInstance().logOut();

                User user = PrefUtilsUser.getCurrentUser(LogoutActivity.this);

                JSONObject jsonObjectSend = new JSONObject();
                jsonObjectSend.put(Constants.TAG_UserSaberaId,user.getSaberaId());
                jsonObjectSend.put(Constants.TAG_FCM_Token,user.getGcmRegToken());

                if (jsonObjectSend != null && jsonObjectSend.size() > 0) {
                    Log.d(TAG, "jsonObjectSend = " + jsonObjectSend.toString());
                    if (Constants.backendTest) {
                        new sendFCMTOkenLogoutToServer().execute(jsonObjectSend);
                    }

                }

                PrefUtilsUser.clearCurrentUser(LogoutActivity.this);


                Intent i= new Intent(LogoutActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public class sendFCMTOkenLogoutToServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.sendGcmTokenLogoutToServerURL, params[0]);
            if (jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            if (jsonObjRec != null && jsonObjRec.size() > 0) {

                Log.d(TAG, "sendFCMTOkenLogoutToServer = " + jsonObjRec + toString());
                String response = jsonObjRec.get(Constants.TAG_PostQn_Status).toString();


            } else
                Log.e(TAG, "jsonObjRec == null");
        }
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
                Bitmap bitmap  = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

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
