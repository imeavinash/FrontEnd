package com.avinashbehera.sabera.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avinashbehera.sabera.R;

import java.util.ArrayList;

/**
 * Created by avinashbehera on 15/09/16.
 */
public class PickKeywordsAdapter extends RecyclerView.Adapter<PickKeywordsAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<String> givenKeywords;
    private ArrayList<Boolean> status;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView keywordTextView;

        public ViewHolder(View view) {
            super(view);
            keywordTextView = (TextView) view.findViewById(R.id.keywordTxtView);

        }

    }

    public PickKeywordsAdapter(Context context, ArrayList<String> givenKeywords,ArrayList<Boolean> statusArray){

        this.mContext = context;
        this.givenKeywords = givenKeywords;
        this.status = statusArray;



    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pick_keywords_row,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String keyword = givenKeywords.get(position);
        holder.keywordTextView.setText(keyword);
        if(status.get(position)){
            holder.keywordTextView.setBackgroundResource(R.drawable.bg_bubble_blue);
            holder.keywordTextView.setTextColor(Color.WHITE);
        }else{
            holder.keywordTextView.setBackgroundResource(R.drawable.bg_bubble_gray);
            holder.keywordTextView.setTextColor(Color.BLACK);
        }

    }

    @Override
    public int getItemCount() {
        return givenKeywords.size();
    }



    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private PickKeywordsAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final PickKeywordsAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
