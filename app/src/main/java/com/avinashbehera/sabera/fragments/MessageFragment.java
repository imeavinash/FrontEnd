package com.avinashbehera.sabera.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avinashbehera.sabera.Activity.ChatActivity;
import com.avinashbehera.sabera.adapters.ChatHeadAdapter;
import com.avinashbehera.sabera.model.MsgFrgmChatHead;
import com.avinashbehera.sabera.model.User;
import com.avinashbehera.sabera.util.Constants;
import com.avinashbehera.sabera.util.PrefUtilsUser;
import com.avinashbehera.sabera.util.SimpleDividerItemDecoration;
import com.avinashbehera.sabera.R;

import java.util.ArrayList;

/**
 * Created by avinashbehera on 25/08/16.
 */
public class MessageFragment extends Fragment {

    private RecyclerView chatHeadRecyclerView;
    private ArrayList<MsgFrgmChatHead> chatHeadsList;
    private ChatHeadAdapter mAdapter;
    public static final String TAG = MessageFragment.class.getSimpleName();
    private boolean isVisible = false;

    public MessageFragment(){

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG,"onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        chatHeadRecyclerView = (RecyclerView)rootView.findViewById(R.id.chat_head_recycler_view);

        chatHeadsList = new ArrayList<>();
        mAdapter = new ChatHeadAdapter(getContext(),chatHeadsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatHeadRecyclerView.setLayoutManager(layoutManager);
        chatHeadRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));
        chatHeadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        chatHeadRecyclerView.setAdapter(mAdapter);

        chatHeadRecyclerView.addOnItemTouchListener(new ChatHeadAdapter.RecyclerTouchListener(getActivity().getApplicationContext(), chatHeadRecyclerView, new ChatHeadAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                // when chat is clicked, launch full chat thread activity
                Log.d(TAG,"RecyclerView onItemTouch - position = "+position);
                MsgFrgmChatHead chatHead = chatHeadsList.get(position);
                Intent intent = new Intent(getContext(), ChatActivity.class);
                Log.d(TAG,"chatHead userId = "+chatHead.getUserId());
                intent.putExtra(Constants.EXTRA_Chat_UserId, chatHead.getUserId());
                intent.putExtra(Constants.EXTRA_Chat_Name, chatHead.getName());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        updateChatHead();






        return rootView;
    }

    public void updateChatHead(){

        User user = PrefUtilsUser.getCurrentUser(getContext());
        if(user.getChatHeadsArrayList()!=null && user.getChatHeadsArrayList().size()>0){

            chatHeadsList.clear();
            chatHeadsList.addAll(user.getChatHeadsArrayList());

            if(chatHeadsList==null || chatHeadsList.size()==0){
                Log.e(TAG,"chatHeadsList = null or size = 0");
            }else{
                for(int i=0;i<chatHeadsList.size();i++){
                    Log.d(TAG,"chatHeadList - "+i+" name = "+chatHeadsList.get(i).getName());
                }
                Log.d(TAG,"chatHeadsList = "+chatHeadsList.toString());
            }
            //mAdapter = new ChatHeadAdapter(getContext(),chatHeadsList);
            //chatHeadRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }else{
            Log.e(TAG,"user - chatHeadsArrayList = null or size = 0");
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG,"setUserVisibleHint - isVisibleToUser = " + isVisibleToUser);
        if(isVisibleToUser && isResumed()){
            isVisible = true;
        }
    }

    public boolean isFragmentVisible(){
        return isVisible;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
        isVisible = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }
}
