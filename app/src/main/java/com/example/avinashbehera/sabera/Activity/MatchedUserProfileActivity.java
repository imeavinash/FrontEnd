package com.example.avinashbehera.sabera.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avinashbehera.sabera.R;
import com.example.avinashbehera.sabera.adapters.ChatHeadAdapter;
import com.example.avinashbehera.sabera.adapters.MUserProfileQnAdapter;
import com.example.avinashbehera.sabera.model.MatchedUser;
import com.example.avinashbehera.sabera.model.MatchedUserQn;
import com.example.avinashbehera.sabera.model.User;
import com.example.avinashbehera.sabera.util.Constants;
import com.example.avinashbehera.sabera.util.PrefUtilsUser;
import com.example.avinashbehera.sabera.util.SimpleDividerItemDecoration;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

public class MatchedUserProfileActivity extends AppCompatActivity {

    public static final String TAG = MatchedUserProfileActivity.class.getSimpleName();

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView genderTextView;
    private ImageView mImageView;

    private TextView userIdTextView;
    private TextView birthdayTextView;
    private TextView categoriesTextView;
    private String userId;

    private RecyclerView qnAskedRecyclerView;
    private RecyclerView qnAnsweredRecyclerView;
    private MUserProfileQnAdapter qnAskedAdpater;
    private MUserProfileQnAdapter qnAnsweredAdpater;

    private ArrayList<MatchedUserQn> qnAskedList;
    private ArrayList<MatchedUserQn> qnAnsweredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_matched_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameTextView = (TextView)findViewById(R.id.nameTextView);
        emailTextView = (TextView)findViewById(R.id.emailTextView);
        genderTextView = (TextView)findViewById(R.id.genderTextView);
        userIdTextView = (TextView)findViewById(R.id.userIdTextView);
        birthdayTextView = (TextView)findViewById(R.id.birthdayTextView);
        mImageView=(ImageView)findViewById(R.id.profilePicture);

        categoriesTextView=(TextView) findViewById(R.id.categoryTextView);

        qnAskedList = new ArrayList<>();
        qnAskedAdpater = new MUserProfileQnAdapter(this,qnAskedList);
        qnAnsweredList = new ArrayList<>();
        qnAnsweredAdpater = new MUserProfileQnAdapter(this,qnAnsweredList);

        qnAskedRecyclerView=(RecyclerView)findViewById(R.id.qns_asked_recycler_view);
        qnAnsweredRecyclerView=(RecyclerView)findViewById(R.id.qns_ans_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        qnAskedRecyclerView.setLayoutManager(layoutManager);
        qnAskedRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                this.getApplicationContext()
        ));
        qnAskedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        qnAskedRecyclerView.setAdapter(qnAskedAdpater);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        qnAnsweredRecyclerView.setLayoutManager(layoutManager2);
        qnAnsweredRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                this.getApplicationContext()
        ));
        qnAnsweredRecyclerView.setItemAnimator(new DefaultItemAnimator());
        qnAnsweredRecyclerView.setAdapter(qnAnsweredAdpater);

        Intent intent = getIntent();
        userId = intent.getStringExtra(Constants.EXTRA_Chat_UserId);



        updateUI();


    }

    public void updateUI(){

        User user= PrefUtilsUser.getCurrentUser(this);
        ArrayList<MatchedUser> mUserList = user.getMatchedUserList();
        for(int i=0;i<mUserList.size();i++){
            if(mUserList.get(i).getUserId().equals(userId)){
                MatchedUser mUser = mUserList.get(i);
                nameTextView.setText(mUser.getName());
                emailTextView.setText(mUser.getEmail());
                genderTextView.setText(mUser.getGender());
                birthdayTextView.setText(mUser.getDob());
                userIdTextView.setText(mUser.getUserId());
                categoriesTextView.setText(mUser.getCategories());

                qnAskedList.clear();
                qnAskedList.addAll(mUser.getQnsAsked());
                qnAnsweredList.clear();
                qnAnsweredList.addAll(mUser.getQnsAnswered());

                qnAskedAdpater.notifyDataSetChanged();
                qnAnsweredAdpater.notifyDataSetChanged();

                break;
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }
}
