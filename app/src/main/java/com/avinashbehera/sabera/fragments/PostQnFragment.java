package com.avinashbehera.sabera.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.avinashbehera.sabera.Activity.PostQnDetailActivity;
import com.avinashbehera.sabera.model.UserPostQn;
import com.avinashbehera.sabera.util.Constants;
import com.avinashbehera.sabera.util.PrefUtilsPostQn;
import com.avinashbehera.sabera.util.Utility;
import com.avinashbehera.sabera.R;

/**
 * Created by avinashbehera on 25/08/16.
 */




    public class PostQnFragment extends Fragment {


    private int OPTION_SELECTED = Constants.POST_QN_TYPE_NONE;
    private EditText postQnEditText;
    private Button proceedButton;
    private Button cancelButton;
    private RadioButton objectiveRadioButton;
    private RadioButton subjectiveRadioButton;
    private LinearLayout objSubjPostQnLinearLayout;


       public PostQnFragment(){

       }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_qn, container, false);
        postQnEditText = (EditText) rootView.findViewById(R.id.postQnTextBox);
        proceedButton = (Button) rootView.findViewById(R.id.proceedPostQnButton);
        cancelButton = (Button) rootView.findViewById(R.id.cancelPostQnButton);
        objectiveRadioButton = (RadioButton) rootView.findViewById(R.id.radio_objective);
        subjectiveRadioButton = (RadioButton) rootView.findViewById(R.id.radio_subjective);
        objSubjPostQnLinearLayout = (LinearLayout) rootView.findViewById(R.id.objSubjPostQnLinearLayout);



        objectiveRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if (checked)
                    OPTION_SELECTED = Constants.POST_QN_TYPE_OBJECTIVE;
            }
        });

        subjectiveRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if (checked)
                    OPTION_SELECTED = Constants.POST_QN_TYPE_SUBJECTIVE;
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                objectiveRadioButton.setChecked(false);
                subjectiveRadioButton.setChecked(false);
                postQnEditText.getText().clear();
                OPTION_SELECTED = Constants.POST_QN_TYPE_NONE;

            }
        });

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (postQnEditText.getText().toString() == null || postQnEditText.getText().toString().equalsIgnoreCase("")) {
                    Utility.makeToast(getContext(), "Please enter a question", Toast.LENGTH_LONG);
                } else {

                    if(!objectiveRadioButton.isChecked() && !subjectiveRadioButton.isChecked())
                        Utility.makeToast(getContext(),"Please select a question type",Toast.LENGTH_LONG);
                    else{

                        UserPostQn tempQn = new UserPostQn();
                        if(OPTION_SELECTED == Constants.POST_QN_TYPE_OBJECTIVE) {

                            tempQn.setQnType(Constants.VALUE_PostQn_Objective);
                        }
                        else {
                            tempQn.setQnType(Constants.VALUE_PostQn_Subjective);
                        }

                            tempQn.setQnTxt(postQnEditText.getText().toString());
                            PrefUtilsPostQn.setCurrentPostQn(tempQn,getContext());
                        postQnEditText.getText().clear();
                            Intent intent = new Intent(getContext(), PostQnDetailActivity.class);
                            startActivity(intent);



                    }


                }

            }
        });


        return rootView;
    }//end of onCreateView



}
