package com.avinashbehera.sabera.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avinashbehera.sabera.model.MatchedUserQn;
import com.avinashbehera.sabera.R;

import java.util.ArrayList;

/**
 * Created by avinashbehera on 07/09/16.
 */
public class MUserProfileQnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private ArrayList<MatchedUserQn> qnList;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView qnTxtView, pAnsTxtView, attAnsTxtView;

        public ViewHolder(View view) {
            super(view);
            qnTxtView = (TextView) itemView.findViewById(R.id.qnTxtView);
            pAnsTxtView = (TextView) itemView.findViewById(R.id.pAnsTxtView);
            attAnsTxtView = (TextView) itemView.findViewById(R.id.attAnsTxtView);
        }
    }

    public MUserProfileQnAdapter(Context mContext, ArrayList<MatchedUserQn> qnList) {
        this.mContext = mContext;
        this.qnList = qnList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.muser_qn_row,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        MatchedUserQn mQn = qnList.get(position);
        ArrayList<String> proposed_ans = mQn.getProposed_answer();
        ArrayList<String> attempted_ans = mQn.getProposed_answer();
        String pAns="";
        String aAns="";
        for(int i=0;i<proposed_ans.size();i++){
            pAns=pAns+" "+proposed_ans.get(i);
        }
        for(int i=0;i<attempted_ans.size();i++){
            aAns=aAns+" "+attempted_ans.get(i);
        }

        ((ViewHolder) holder).qnTxtView.setText(mQn.getQnTxt());



        ((ViewHolder) holder).pAnsTxtView.setText(pAns);
        ((ViewHolder) holder).attAnsTxtView.setText(aAns);
    }

    @Override
    public int getItemCount() {
        return qnList.size();
    }
}
