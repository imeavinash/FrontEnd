package com.example.avinashbehera.sabera.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.avinashbehera.sabera.R;

/**
 * Created by avinashbehera on 25/08/16.
 */
public class SeeQnFragment extends Fragment {

    public SeeQnFragment(){

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_see_qns, container, false);
        return rootView;
    }
}
