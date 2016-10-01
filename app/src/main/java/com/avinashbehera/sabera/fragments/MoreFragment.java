package com.avinashbehera.sabera.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.avinashbehera.sabera.Activity.LogoutActivity;
import com.avinashbehera.sabera.R;

/**
 * Created by avinashbehera on 25/08/16.
 */
public class MoreFragment extends Fragment {

    private Button seeProfileButton;

    public MoreFragment(){

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_more, container, false);
        seeProfileButton = (Button)rootView.findViewById(R.id.seeProfileButton);
        seeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LogoutActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
