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
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

//import org.json.simple.JSONException;
import org.json.simple.JSONObject;

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
            categoriesTextView.setText(user.getCategories());

            //new setProfilePicture().execute();


        }


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.example.avinashbehera.sabera.util.PrefUtilsUser.clearCurrentUser(LogoutActivity.this);


                // We can logout from facebook by calling following method
                if(FacebookSdk.isInitialized())
                    LoginManager.getInstance().logOut();


                Intent i= new Intent(LogoutActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
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
