package com.example.avinashbehera.sabera.fragments;


import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.avinashbehera.sabera.R;
import com.example.avinashbehera.sabera.util.Constants;
import com.example.avinashbehera.sabera.util.Utility;

/**
 * Created by avinashbehera on 25/08/16.
 */




    public class PostQnFragment extends Fragment {

    private static final int OPTION_OBJECTIVE = 1;
    private static final int OPTION_SUBJECTIVE = 2;
    private int OPTION_SELECTED = 1;
    private EditText postQnEditText;
    private Button proceedButton;
    private Button cancelButton;
    private RadioButton objectiveRadioButton;
    private RadioButton subjectiveRadioButton;
    private LinearLayout objSubjPostQnLinearLayout;
    private LinearLayout postObjQnDetail;

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
        postObjQnDetail = (LinearLayout) rootView.findViewById(R.id.postObjQnDetail);


        objectiveRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if (checked)
                    OPTION_SELECTED = OPTION_OBJECTIVE;
            }
        });

        subjectiveRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if (checked)
                    OPTION_SELECTED = OPTION_OBJECTIVE;
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                objectiveRadioButton.setChecked(true);
                postQnEditText.getText().clear();

            }
        });

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (postQnEditText.getText().toString() == null || postQnEditText.getText().toString().equalsIgnoreCase("")) {
                    Utility.makeToast(getContext(), "Please enter a question", Toast.LENGTH_LONG);
                } else {

                    objSubjPostQnLinearLayout.setVisibility(View.GONE);
                    postObjQnDetail.setVisibility(View.VISIBLE);
                    Button button = (Button)v;
                    button.setText("Submit");


                }

            }
        });


        return rootView;
    }



}
