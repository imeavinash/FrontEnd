package com.example.avinashbehera.sabera.Activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.avinashbehera.sabera.R;
import com.example.avinashbehera.sabera.adapters.ChatHeadAdapter;
import com.example.avinashbehera.sabera.adapters.PickKeywordsAdapter;
import com.example.avinashbehera.sabera.model.MsgFrgmChatHead;
import com.example.avinashbehera.sabera.util.Constants;
import com.example.avinashbehera.sabera.util.SimpleDividerItemDecoration;

import org.apache.http.entity.StringEntity;

import java.util.ArrayList;

public class PickKeywordsActivity extends AppCompatActivity {

    public static final String TAG = PickKeywordsActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private Button doneButton;
    private ArrayList<String> givenKeywords;
    private ArrayList<String> selectedKeywords;
    private PickKeywordsAdapter mAdapter;
    private ArrayList<Boolean> statusArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_keywords);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.pick_keywords_recyclerview);
        doneButton = (Button) findViewById(R.id.doneBtn);

        givenKeywords = new ArrayList<>();
        selectedKeywords = new ArrayList<>();
        statusArray = new ArrayList<>();

        Intent intent = getIntent();
        String ans = intent.getStringExtra("answerText");
        String[] keywrds = ans.split("\\s+");
        Log.d(TAG,"keywrds array length = "+keywrds.length);
        if (keywrds.length > 0) {
            for (int i = 0; i < keywrds.length; i++)
                givenKeywords.add(keywrds[i]);
        }
        for(int i=0;i<givenKeywords.size();i++){
            statusArray.add(false);

        }

        mAdapter = new PickKeywordsAdapter(this,givenKeywords,statusArray);
        GridLayoutManager layoutManager = new GridLayoutManager(this,4, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new PickKeywordsAdapter.RecyclerTouchListener(this.getApplicationContext(), recyclerView, new PickKeywordsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity

                if(statusArray.get(position)){
                    statusArray.set(position,false);
                }else{
                    statusArray.set(position,true);
                }
                mAdapter.notifyItemChanged(position);


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        mAdapter.notifyDataSetChanged();

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                for(int i=0;i<statusArray.size();i++){
                    if(statusArray.get(i)){
                        flag=true;
                        selectedKeywords.add(givenKeywords.get(i));
                    }
                }
                if(!flag){
                    Toast.makeText(PickKeywordsActivity.this,"Please select at least one keyword",Toast.LENGTH_LONG).show();
                    return;
                }
                Intent returnIntent = new Intent();
                returnIntent.putStringArrayListExtra(Constants.EXTRA_RESULT_PICK_KEYWORDS, selectedKeywords);
                setResult(RESULT_OK, returnIntent);
                finish();

            }
        });


    }

}
