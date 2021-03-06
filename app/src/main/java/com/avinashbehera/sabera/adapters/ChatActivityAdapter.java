package com.avinashbehera.sabera.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avinashbehera.sabera.model.MatchedUser;
import com.avinashbehera.sabera.model.Message;
import com.avinashbehera.sabera.model.User;
import com.avinashbehera.sabera.util.PrefUtilsUser;
import com.avinashbehera.sabera.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by avinashbehera on 05/09/16.
 */
public class ChatActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String userId;
    private int SELF = 1000;
    private static String today;

    private Context mContext;
    private ArrayList<Message> messageArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTxtView, timestamp;

        public ViewHolder(View view) {
            super(view);
            messageTxtView = (TextView) itemView.findViewById(R.id.message);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        }
    }

    public ChatActivityAdapter(Context mContext, ArrayList<Message> messageArrayList, String userId) {
        this.mContext = mContext;
        this.messageArrayList = messageArrayList;
        this.userId = userId;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
        } else {
            // others message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other, parent, false);
        }


        return new ViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if (message.getSenderId().equals(userId)) {
            return SELF;
        }

        return position;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Message message = messageArrayList.get(position);
        ((ViewHolder) holder).messageTxtView.setText(message.getMsgTxt());

        String timestamp = getTimeStamp(message.getTimeStamp());
        String name="";

        if (message.getSenderId() != null && !message.getSenderId().equalsIgnoreCase("")){
            User user = PrefUtilsUser.getCurrentUser(mContext);
            if(message.getSenderId().equals(user.getSaberaId()))
                name=user.getName();
            else{

                ArrayList<MatchedUser> mUserList = user.getMatchedUserList();
                for(int i=0;i<mUserList.size();i++){
                    MatchedUser mUser = mUserList.get(i);
                    if(mUser.getUserId().equals(message.getSenderId())){
                        name = mUser.getName();
                        break;
                    }
                }


            }


        }
            timestamp = name + ", " + timestamp;

        ((ViewHolder) holder).timestamp.setText(timestamp);
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
}
