package com.avinashbehera.sabera.fragments;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avinashbehera.sabera.Activity.BaseActivity;
import com.avinashbehera.sabera.Activity.CircleView;
import com.avinashbehera.sabera.model.User;
import com.avinashbehera.sabera.model.UserSeeQn;
import com.avinashbehera.sabera.network.HttpClient;
import com.avinashbehera.sabera.util.Constants;
import com.avinashbehera.sabera.util.OnSwipeTouchListener;
import com.avinashbehera.sabera.util.PrefUtilsUser;
import com.avinashbehera.sabera.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by avinashbehera on 25/08/16.
 */
public class SeeQnFragment extends Fragment {

    //private TextView seeQnTxtView;
    private static ArrayList<UserSeeQn> qnArrayList;
    private static ArrayList<LinearLayout> qnLLArrayList;
    public FrameLayout qnContainer;
    private static MyCountDownTimer countDownTimer;
    private Button btnLoadQns;
    private Button passButton;
    private Button submitButton;
    private boolean isVisible = false;
    private long currentTime=0;
    private LinearLayout timerLL;
    private TextView minTxtView;
    private TextView secTxtView;
    private TextView minSecSeparator;
    private AlphaAnimation animation1;
    private CardView qnCardView;
    private LinearLayout passSubmitLL;
    private LinearLayout qn1;
    private EditText ansEditTxt;
    private float cardViewInitElevation;

    private float cardViewX, cardViewY;
    private float cardViewWidth, cardViewHeight;
    private float cardViewPivotX, cardViewPivotY;
    private float dragStartX, dragStartY;
    private float rotateAngle;
    private View rootView;
    private RelativeLayout parentLayout;


    public static final String TAG = SeeQnFragment.class.getSimpleName();

    public SeeQnFragment(){

    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_see_qns, container, false);
        //seeQnTxtView = (TextView)rootView.findViewById(R.id.seeQnTxtView);
        User user = PrefUtilsUser.getCurrentUser(getContext());
        //seeQnTxtView.setText(user.getQuestions());

        qnContainer = (FrameLayout)rootView.findViewById(R.id.container);
        btnLoadQns = (Button)rootView.findViewById(R.id.btnLoadQns);
        passButton = (Button)rootView.findViewById(R.id.btnSeeQnPass);
        submitButton = (Button)rootView.findViewById(R.id.btnSeeQnSubmit);
        minTxtView = (TextView)rootView.findViewById(R.id.minTxtView);
        secTxtView = (TextView)rootView.findViewById(R.id.secTxtView);
        minSecSeparator = (TextView)rootView.findViewById(R.id.minSecSeparator);
        timerLL = (LinearLayout)rootView.findViewById(R.id.timerLL);
        qnCardView = (CardView)rootView.findViewById(R.id.qnCard);
        passSubmitLL = (LinearLayout)rootView.findViewById(R.id.passSumbitLL);

        parentLayout = (RelativeLayout)rootView.findViewById(R.id.parentLayout);
        btnLoadQns.setOnClickListener(loadQnsOnClickListener);

        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPass();
            }
        });

        submitButton.setOnClickListener(submitButtonCliclListener);

        if(currentTime==0){
            btnLoadQns.setVisibility(View.VISIBLE);
        }

        animation1 = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(300);
        animation1.setRepeatMode(Animation.REVERSE);
        animation1.setRepeatCount(Animation.INFINITE);

        qnCardView.setTag("");


        qnContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG,"qnContainer - onLongClickListener");
                onCardViewLongClick(qnCardView);
                return false;
            }
        });

        qnCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG,"qnCardView - onLongClick");
                onCardViewLongClick(v);
                return false;
            }
        });

        qnCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG,"qnCardView - onTouch - (x,y) - ("+event.getX()+","+event.getY()+")");
                Log.d(TAG,"qnCardView - onTouch - Raw - (x,y) - ("+event.getRawX()+","+event.getRawY()+")");
                return false;
            }
        });

        rootView.setOnDragListener(new myDragEventListener());
        //qnCardView.setOnDragListener(new myDragEventListener());




        return rootView;
    }





    public void onCardViewLongClick(View v){


        ClipData.Item item = new ClipData.Item(v.getTag().toString());
        ClipData dragData = new ClipData((CharSequence)v.getTag(),new String[]{ ClipDescription.MIMETYPE_TEXT_PLAIN },item);
        View.DragShadowBuilder myShadow = new View.DragShadowBuilder();
        cardViewX = qnCardView.getX();
        cardViewY = qnCardView.getY();


        cardViewWidth = qnCardView.getWidth();
        cardViewHeight = qnCardView.getHeight();
        Rect r = new Rect();
        qnCardView.getGlobalVisibleRect(r);
        Log.d(TAG,"qnCardView - onLongClick - Global Rect - (left,right) = ("+r.left+","+r.right+")");
        Log.d(TAG,"qnCardView - onLongClick - Global Rect - (top,bottom) = ("+r.top+","+r.bottom+")");
        Log.d(TAG,"CardView - longClick - (x,y) = ("+cardViewX+", "+cardViewY+")");
        Log.d(TAG,"CardView - longClick - (top,bottom) = ("+qnCardView.getTop()+", "+qnCardView.getBottom()+")");
        Log.d(TAG,"cardViewWidth = "+cardViewWidth+" cardViewHeight = "+cardViewHeight);
        cardViewPivotX = cardViewX + cardViewWidth/2;
        cardViewPivotY = cardViewY + cardViewHeight/2;

        //Log.d(TAG,"cardViewPivotX = "+cardViewPivotX+" cardViewPivotY = "+cardViewPivotY);
        cardViewInitElevation = qnCardView.getCardElevation();
        qnCardView.setPivotX(cardViewX/2);
        qnCardView.setPivotY(cardViewY+cardViewHeight/2);
        qnCardView.setX(cardViewX);
        qnCardView.setY(cardViewY);
        //qnCardView.setRotation(30);

        CircleView circle = new CircleView(getContext(),cardViewX,cardViewY+cardViewHeight);
        //parentLayout.addView(circle);

        int appBarHeight = ((BaseActivity)getActivity()).getAppBarHeight();
        CircleView circle2 = new CircleView(getContext(),cardViewX,cardViewY+cardViewHeight+appBarHeight);
        //parentLayout.addView(circle2);




        //qnCardView.setRotation(90);

        //final RotateAnimation rotateAnim = new RotateAnimation(0,359,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,1);
        final RotateAnimation rotateAnim = new RotateAnimation(0,360,cardViewX/2,cardViewY+cardViewHeight/2);
        rotateAnim.setDuration(5000);
        rotateAnim.setInterpolator(new LinearInterpolator());
        //qnCardView.setAnimation(rotateAnim);
        //rotateAnim.start();






        //qnCardView.setRotation(90);
        Log.d(TAG,"CardView - longClick - after rotation - (x,y) = ("+qnCardView.getX()+", "+qnCardView.getY()+")");
        rotateAngle = 0;

        Log.d(TAG,"onCardViewLongClick");
        Log.d(TAG,"Before Drag Start cardview - (x,y) - "+(+qnCardView.getX()+","+qnCardView.getY()+")"));
        v.startDrag(dragData,  // the data to be dragged
                myShadow,  // the drag shadow builder
                null,      // no need to use local data
                0          // flags (not currently used, set to 0)
        );

    }




    protected class myDragEventListener implements View.OnDragListener {

        float x=0,y=0,deltaX=0,deltaY=0,changeY=0;
        int count = 0;
        boolean upperHalf = true;
        float startX=0,startY=0;
        // This is the method that the system calls when it dispatches a drag event to the
        // listener.
        public boolean onDrag(View v, DragEvent event) {

            // Defines a variable to store the action type for the incoming event
            final int action = event.getAction();
            deltaX = event.getX() - x;
            deltaY = event.getY() - y;
            x=event.getX();
            y=event.getY();

            // Handles each of the expected events
            switch(action) {

                case DragEvent.ACTION_DRAG_STARTED:

                    count++;

                    Log.d(TAG,"ACTION_DRAG_STARTED - (x,y) = "+"("+event.getX()+","+event.getY()+")");
                    dragStartX = event.getX();
                    dragStartY = event.getY();

                    if(event.getY()<=cardViewY+qnCardView.getHeight()/2){
                        changeY = cardViewY+qnCardView.getHeight() - event.getY();
                        upperHalf = true;
                    }else{
                        changeY = event.getY() - cardViewY;
                        upperHalf = false;
                    }
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                        Log.d(TAG,"qnCardView elevation = "+qnCardView.getElevation());
                        qnCardView.setCardElevation(20);

                    }
                    EditText ansTxt = (EditText)qn1.findViewById(R.id.ansEditText);
                    if(ansTxt!=null){
                        ansTxt.setEnabled(false);
                    }

                    // Determines if this View can accept the dragged data
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        Log.d(TAG,"ACTION_DRAG_STARTED - has mimetype_plain_text");

                        // As an example of what your application might do,
                        // applies a blue color tint to the View to indicate that it can accept
                        // data.
                        //v.getBackground().setColorFilter(Color.BLUE,PorterDuff.Mode.SRC);

                        // Invalidate the view to force a redraw in the new tint
                        //v.invalidate();

                        // returns true to indicate that the View can accept the dragged data.
                        return true;

                    }

                    // Returns false. During the current drag and drop operation, this View will
                    // not receive events again until ACTION_DRAG_ENDED is sent.
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:

                    ansTxt = (EditText)qn1.findViewById(R.id.ansEditText);
                    if(ansTxt!=null){
                        ansTxt.setEnabled(false);
                    }

                    Log.d(TAG,"ACTION_DRAG_ENTERED - (x,y) = "+"("+event.getX()+","+event.getY()+")");
                    //moveCardView(deltaX,deltaY);


                    // Applies a green tint to the View. Return true; the return value is ignored.

                    //v.getBackground().setColorFilter(Color.GREEN,PorterDuff.Mode.SRC);

                    // Invalidate the view to force a redraw in the new tint
                    //v.invalidate();

                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:

                    if(count==0){
                        count++;
                        dragStartX = event.getX();
                        dragStartY = event.getY();
                        if(event.getY()<=cardViewY+qnCardView.getHeight()/2){
                            changeY = cardViewY+qnCardView.getHeight() - event.getY();
                            upperHalf = true;
                        }else{
                            changeY = event.getY() - cardViewY;
                            upperHalf = false;
                        }
                        return true;

                    }

                    //Log.d(TAG,"ACTION_DRAG_LOCATION");
                    //Log.d(TAG,"ACTION_DRAG_LOCATION - (x,y) = "+"("+event.getX()+","+event.getY()+")");
                    //Rect r = new Rect();
                    //qnCardView.getGlobalVisibleRect(r);
                    //Log.d(TAG,"global visible rect = (left,top,right,bottom) = ("+r.left+","+r.top+","+r.right+","+r.bottom+")");
                    //Log.d(TAG,"global visible rect = "+r.toString());
                    moveCardView(deltaX,deltaY);
                    //rotateCardView(x,y,deltaX,deltaY,changeY,upperHalf);

                    // Ignore the event
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:

                    //moveCardView(deltaX,deltaY);

                    Log.d(TAG,"ACTION_DRAG_EXITED - (x,y) = "+"("+event.getX()+","+event.getY()+")");

                    // Re-sets the color tint to blue. Returns true; the return value is ignored.
                    //v.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC);

                    // Invalidate the view to force a redraw in the new tint
                   // v.invalidate();

                    return true;

                case DragEvent.ACTION_DROP:

                    count = 0;
                    changeY = 0;

                    ansTxt = (EditText)qn1.findViewById(R.id.ansEditText);
                    if(ansTxt!=null){
                        ansTxt.setEnabled(true);
                    }

                    Log.d(TAG,"ACTION_DRAG_LOCATION - (x,y) = "+"("+event.getX()+","+event.getY()+")");
                    //r = new Rect();
                    //qnCardView.getGlobalVisibleRect(r);
                    //Log.d(TAG,"global visible rect = (left,top,right,bottom) = ("+r.left+","+r.top+","+r.right+","+r.bottom+")");


                    Log.d(TAG,"ACTION_DRAG_DROP - (x,y) = "+"("+event.getX()+","+event.getY()+")");
                    Log.d(TAG,"ACTION_DRAG_DROP - cardViewX = "+cardViewX+", cardViewY = "+cardViewY);
                    qnCardView.setX(cardViewX);
                    qnCardView.setY(cardViewY);
                    qnCardView.setCardElevation(cardViewInitElevation);
                    rotateAngle = 0;
                    qnCardView.setPivotX(cardViewX);
                    qnCardView.setPivotY(cardViewY);
                    qnCardView.setRotation(0);

                    x=0;
                    y=0;
                    deltaY=0;
                    deltaX=0;

                    // Gets the item containing the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    // Gets the text data from the item.
                    String dragData = item.getText().toString();

                    // Displays a message containing the dragged data.
                    Toast.makeText(getContext(), "Dragged data is " + dragData, Toast.LENGTH_LONG);

                    // Turns off any color tints
                    //v.getBackground().clearColorFilter();


                    // Invalidates the view to force a redraw
                    //v.invalidate();

                    // Returns true. DragEvent.getResult() will return true.
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:

                    count = 0;
                    changeY = 0;
                    dragStartX = 0;
                    dragStartY = 0;
                    upperHalf = true;

                    ansTxt = (EditText)qn1.findViewById(R.id.ansEditText);
                    if(ansTxt!=null){
                        ansTxt.setEnabled(true);
                    }

                    Log.d(TAG,"ACTION_DRAG_ENDED - (x,y) = "+"("+event.getX()+","+event.getY()+")");
                    Log.d(TAG,"ACTION_DRAG_ENDED - cardViewX = "+cardViewX+", cardViewY = "+cardViewY);

                    qnCardView.setX(cardViewX);
                    qnCardView.setY(cardViewY);
                    qnCardView.setCardElevation(cardViewInitElevation);
                    rotateAngle = 0;
                    qnCardView.setPivotX(cardViewPivotX);
                    qnCardView.setPivotY(cardViewPivotY);
                    qnCardView.setRotation(0);

                    x=0;
                    y=0;
                    deltaY=0;
                    deltaX=0;
                    //v.invalidate();

                    // Does a getResult(), and displays what happened.
                    if (event.getResult()) {
                        Toast.makeText(getContext(), "The drop was handled.", Toast.LENGTH_LONG);

                    } else {
                        Toast.makeText(getContext(), "The drop didn't work.", Toast.LENGTH_LONG);

                    }

                    // returns true; the value is ignored.
                    return true;

                // An unknown action type was received.
                default:
                    Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                    break;
            }

            return false;
        }
    };

    public void rotateCardView(float x,float y,float deltaX, float deltaY,float changeY,boolean upperhalf){

        if(upperhalf){



        }else{

        }


    }

    public void moveCardView(float deltaX,float deltaY){

        Log.d(TAG,"moveCardView - deltaX = "+deltaX+", deltaY = "+deltaY);

        float newX = 0, newY = 0;



        //moved left
        if(deltaX < 0){
            newX = qnCardView.getX()-Math.abs(deltaX);
        }
        //moved right
        else {
            newX = qnCardView.getX() + Math.abs(deltaX);
        }

        //moved up
        if(deltaY < 0){
            newY = qnCardView.getY()-Math.abs(deltaY);
        }
        //moved down
        else {
            newY = qnCardView.getY() + Math.abs(deltaY);
        }

        qnCardView.setX(newX);
        qnCardView.setY(newY);
        //qnCardView.setPivotX(newX+cardViewWidth/2);
        //qnCardView.setPivotY(newY+cardViewHeight/2);
        Log.d(TAG,"newX = "+newX+" newY = "+newY);
        Log.d(TAG,"card - x,y = "+qnCardView.getX()+", "+qnCardView.getY());


    }

    public double calculateAngle(float x,float y){
        double angle=0;
        double tx = x-cardViewPivotX;
        double ty = y - cardViewPivotY;
        double t_length = Math.sqrt(tx*tx + ty*ty);
        angle = Math.acos(ty/t_length);


        return angle;
    }



    public View.OnClickListener loadQnsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Log.d(TAG,"loadQnsOnClickListener");

            User user1 = PrefUtilsUser.getCurrentUser(getContext());
            ArrayList<UserSeeQn> qnArr = user1.getQuestionArray();
            if (qnArr!=null && qnArr.size()>0) {
                addQnsToLayout();
            }else{
                JSONObject jsonObjectSend = new JSONObject();
                jsonObjectSend.put(Constants.TAG_UserSaberaId,user1.getSaberaId());
                if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                    new LoadQnsFromServer().execute(jsonObjectSend);
                }


            }

        }
    };

    public class LoadQnsFromServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.loadQnsRequestURL, params[0]);
            if (jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            if (jsonObjRec != null && jsonObjRec.size() > 0) {

                Log.d(TAG, "LoadQnsFromServerResponse = " + jsonObjRec + toString());
                setUserQns(jsonObjRec);
                addQnsToLayout();



            } else
                Log.e(TAG, "LoadQnsFromServer - jsonObjRec == null");
        }
    }

    public void setUserQns(JSONObject jsonObjRec){

        User user = PrefUtilsUser.getCurrentUser(getContext());

        user.setQuestions(jsonObjRec.get(Constants.TAG_QUESTIONS).toString());
        user.setQnJsonArray((JSONArray)jsonObjRec.get(Constants.TAG_QUESTIONS));
        ArrayList<UserSeeQn> qnArrayList = new ArrayList<>();

        JSONArray jArray =  user.getQnJsonArray();
        JSONArray arr = null;
        if(jArray != null){
            String jsonString = jArray.toJSONString();
            JSONParser parser = new JSONParser();

            try {
                arr = (JSONArray) parser.parse(jsonString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            Log.d(TAG,"jArray = null");
        }

        if(arr!=null){
            for (int i = 0; i < arr.size(); i++) {
                Log.d(TAG, "qnJsonArray loop - i = " + i);
                UserSeeQn qn = new UserSeeQn();
                //Log.d(TAG, qnJsonArray.toJSONString());
                JSONObject jsonObject = (JSONObject) arr.get(i);
                qn.setqId(jsonObject.get(Constants.TAG_SEEQN_QN_ID).toString());
                qn.setuId(jsonObject.get(Constants.TAG_SEEQN_QNR_ID).toString());
                qn.setQnText(jsonObject.get(Constants.TAG_SEEQN_QN_TEXT).toString());
                qn.setHintText(jsonObject.get(Constants.TAG_SEEQN_Hint).toString());
                qn.setTimer(jsonObject.get(Constants.TAG_SEEQN_Timer).toString());
                qn.setQnType(jsonObject.get(Constants.TAG_SEEQN_QN_TYPE).toString());
                if (qn.getQnType().equalsIgnoreCase(Constants.VALUE_SEEQN_Objective)) {

                    qn.setOption1(jsonObject.get(Constants.TAG_SEEQN_OPTION1).toString());
                    qn.setOption2(jsonObject.get(Constants.TAG_SEEQN_OPTION2).toString());
                    qn.setOption3(jsonObject.get(Constants.TAG_SEEQN_OPTION3).toString());
                    qn.setOption4(jsonObject.get(Constants.TAG_SEEQN_OPTION4).toString());

                    if (jsonObject.get(Constants.TAG_SEEQN_Option1_Status).toString().equalsIgnoreCase("1"))
                        qn.setStatus1(true);
                    else
                        qn.setStatus1(false);

                    if (jsonObject.get(Constants.TAG_SEEQN_Option2_Status).toString().equalsIgnoreCase("1"))
                        qn.setStatus2(true);
                    else
                        qn.setStatus2(false);

                    if (jsonObject.get(Constants.TAG_SEEQN_Option3_Status).toString().equalsIgnoreCase("1"))
                        qn.setStatus3(true);
                    else
                        qn.setStatus3(false);

                    if (jsonObject.get(Constants.TAG_SEEQN_Option4_Status).toString().equalsIgnoreCase("1"))
                        qn.setStatus4(true);
                    else
                        qn.setStatus4(false);


                } else {

                    qn.setAnsText(jsonObject.get(Constants.TAG_SEEQN_Ans_Text).toString());
                    String keywords = jsonObject.get(Constants.TAG_SEEQN_Keywords).toString();
                    String[] keyw = keywords.split(",");
                    ArrayList<String> keywordsArray = new ArrayList<>();
                    for(int j=0;j<keyw.length;j++)
                        keywordsArray.add(keyw[j]);
                    qn.setKeywords(keywordsArray);

                }
                qnArrayList.add(qn);

            }
        }else{
            Log.d(TAG,"arr = null");
        }





        user.setQuestionArray(qnArrayList);
        PrefUtilsUser.setCurrentUser(user,getContext());

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG,"setUserVisibleHint - isVisibleToUser = "+isVisibleToUser);
        isVisible=isVisibleToUser;
        if(!isVisibleToUser){
            if(currentTime!=0){
                onPass();
            }
        }
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
    }

    public View.OnClickListener submitButtonCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            User user1 = PrefUtilsUser.getCurrentUser(getContext());
            ArrayList<UserSeeQn> qnArrayList = user1.getQuestionArray();
            if(qnArrayList==null || qnArrayList.size()==0){
                Log.e(TAG,"qnArrayList is null and submit button is visible");
                return;
            }
            UserSeeQn qnAnswered = qnArrayList.get(qnArrayList.size()-1);
            countDownTimer.cancel();
            currentTime=0;
            if (qnAnswered.getQnType().equalsIgnoreCase("objective")) {

                CheckBox chkbox1 = (CheckBox)qn1.findViewById(R.id.opt1chkbox);
                CheckBox chkbox2 = (CheckBox)qn1.findViewById(R.id.opt2chkbox);
                CheckBox chkbox3 = (CheckBox)qn1.findViewById(R.id.opt3chkbox);
                CheckBox chkbox4 = (CheckBox)qn1.findViewById(R.id.opt4chkbox);
                if(!( ( chkbox1.isChecked() ) || (chkbox2.isChecked()) || (chkbox3.isChecked()) || (chkbox4.isChecked()))){

                    Toast.makeText(getContext(),"Check at least one option",Toast.LENGTH_SHORT).show();
                    return;

                }
                JSONObject jsonObjectSend = new JSONObject();


                jsonObjectSend.put(Constants.TAG_AnsQn_Qn_ID,qnAnswered.getqId());
                jsonObjectSend.put(Constants.TAG_AnsQn_UserId_Qnr,qnAnswered.getuId());
                String currentUserID = PrefUtilsUser.getCurrentUser(getContext()).getSaberaId();
                jsonObjectSend.put(Constants.TAG_AnsQn_UserId_Answr,currentUserID);
                JSONArray answers_q = new JSONArray();
                JSONArray answers_ans = new JSONArray();

                jsonObjectSend.put(Constants.TAG_AnsQn_Qn_Type,qnAnswered.getQnType());

                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = s.format(new Date());
                jsonObjectSend.put(Constants.TAG_AnsQn_timestamp,format);


                if(qnAnswered.isStatus1()){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption1());
                    answers_q.add(jsonObject);
                }

                if(qnAnswered.isStatus2()){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption2());
                    answers_q.add(jsonObject);
                }

                if(qnAnswered.isStatus3()){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption3());
                    answers_q.add(jsonObject);
                }

                if(qnAnswered.isStatus4()){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption4());
                    answers_q.add(jsonObject);
                }

                if(chkbox1.isChecked()){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption1());
                    answers_ans.add(jsonObject);
                }

                if(chkbox2.isChecked()){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption2());
                    answers_ans.add(jsonObject);
                }

                if(chkbox3.isChecked()){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption3());
                    answers_ans.add(jsonObject);
                }

                if(chkbox4.isChecked()){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.TAG_AnsQn_answers,qnAnswered.getOption4());
                    answers_ans.add(jsonObject);
                }


                jsonObjectSend.put(Constants.TAG_AnsQn_Ans_Qnr,answers_q);
                jsonObjectSend.put(Constants.TAG_AnsQn_Ans_Answr,answers_ans);


                if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                    new sendAnsToServer().execute(jsonObjectSend);
                }

                qnContainer.removeAllViews();
                qnCardView.setVisibility(View.GONE);

                //qnArrayList.remove(qnNos-1);

                qnArrayList.remove(qnArrayList.size()-1);
                user1.setQuestionArray(qnArrayList);
                PrefUtilsUser.setCurrentUser(user1,getContext());
                addQnsToLayout();

            }else
            //qnAnswered is subjective
            {

                EditText ansTxt = (EditText)qn1.findViewById(R.id.ansEditText);
                String ans = ansTxt.getText().toString();
                if(ans==null || ans.equalsIgnoreCase("")){
                    Toast.makeText(getContext(),"Please enter answer",Toast.LENGTH_LONG).show();
                    return;
                }

                //countDownTimer.cancel();

                JSONObject jsonObjectSend = new JSONObject();


                jsonObjectSend.put(Constants.TAG_AnsQn_Qn_ID,qnAnswered.getqId());
                jsonObjectSend.put(Constants.TAG_AnsQn_UserId_Qnr,qnAnswered.getuId());
                String currentUserID = PrefUtilsUser.getCurrentUser(getContext()).getSaberaId();
                jsonObjectSend.put(Constants.TAG_AnsQn_UserId_Answr,currentUserID);
                JSONArray answers_q = new JSONArray();
                JSONArray answers_ans = new JSONArray();

                jsonObjectSend.put(Constants.TAG_AnsQn_Qn_Type,qnAnswered.getQnType());

                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String format = s.format(new Date());
                jsonObjectSend.put(Constants.TAG_AnsQn_timestamp,format);


                ArrayList<String> keywords = qnAnswered.getKeywords();
                if(keywords!=null && keywords.size()>0){
                    for(int i=0;i<keywords.size();i++){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(Constants.TAG_AnsQn_answers,keywords.get(i));
                        answers_q.add(jsonObject);
                    }
                }

                String[] keywrds = ans.split("\\\\s+");
                if (keywrds.length > 0) {
                    for (int i = 0; i < keywrds.length; i++){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(Constants.TAG_AnsQn_answers,keywrds[i]);
                        answers_ans.add(jsonObject);
                    }

                }


                jsonObjectSend.put(Constants.TAG_AnsQn_Ans_Qnr,answers_q);
                jsonObjectSend.put(Constants.TAG_AnsQn_Ans_Answr,answers_ans);


                if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                    new sendAnsToServer().execute(jsonObjectSend);
                }


                qnContainer.removeAllViews();
                qnCardView.setVisibility(View.GONE);


                qnArrayList.remove(qnArrayList.size()-1);
                user1.setQuestionArray(qnArrayList);
                PrefUtilsUser.setCurrentUser(user1,getContext());
                addQnsToLayout();

            }

        }
    };

    public  void addQnsToLayout(){

        Log.d(TAG,"addQnsToLayout");

        animation1.cancel();
        timerLL.clearAnimation();
        timerLL.setVisibility(View.GONE);

        //timerLL.invalidate();


        Log.d(TAG,"isVisible() = "+isVisible());
        Log.d(TAG,"isVisible variable = "+isVisible);
        if(!isVisible){
            btnLoadQns.setVisibility(View.VISIBLE);
            qnCardView.setVisibility(View.GONE);
            passSubmitLL.setVisibility(View.GONE);
            return;
        }




        //qnLLArrayList = new ArrayList<>();

        User user = PrefUtilsUser.getCurrentUser(getContext());
        qnArrayList = user.getQuestionArray();
        if(qnArrayList==null || qnArrayList.size()==0){
            btnLoadQns.setVisibility(View.VISIBLE);
            qnCardView.setVisibility(View.GONE);
            passSubmitLL.setVisibility(View.GONE);

            return;
        }
        btnLoadQns.setVisibility(View.GONE);
        //qnNos=qnArrayList.size();


        UserSeeQn qn = qnArrayList.get(qnArrayList.size()-1);
        int nextTimer = Integer.parseInt(qn.getTimer());
        nextTimer = nextTimer*1000;
        currentTime = nextTimer;
        countDownTimer=new MyCountDownTimer(nextTimer,1000);
        countDownTimer.start();
            if (qn.getQnType().equalsIgnoreCase("objective")) {

                qnCardView.setVisibility(View.VISIBLE);
                cardViewX = qnCardView.getX();
                cardViewY = qnCardView.getY();
                Log.d(TAG,"cardViewX = "+cardViewX+", cardViewY = "+cardViewY);
                passSubmitLL.setVisibility(View.VISIBLE);

                LayoutInflater inflater1 = LayoutInflater.from(getContext());
                qn1 = (LinearLayout)inflater1.inflate(R.layout.see_question_objective,null);
                qnContainer.addView(qn1);
                Log.d(TAG,"addQnsToLayout - timerLL - setVisibility - visible");
                timerLL.setVisibility(View.VISIBLE);





                //qnLLArrayList.add(qn1);

                //((BaseActivity)getActivity()).getViewPager().setPagingEnabled(false);


                TextView qnTxt = (TextView)qn1.findViewById(R.id.seeQnObjQnText);
                TextView option1Txt = (TextView)qn1.findViewById(R.id.option1txtview);
                TextView option2Txt = (TextView)qn1.findViewById(R.id.option2txtview);
                TextView option3Txt = (TextView)qn1.findViewById(R.id.option3txtview);
                TextView option4Txt = (TextView)qn1.findViewById(R.id.option4txtview);


                qnTxt.setText(qn.getQnText());
                Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/comic_sans.ttf");
                qnTxt.setTypeface(custom_font);

                option1Txt.setText(qn.getOption1());
                option2Txt.setText(qn.getOption2());
                option3Txt.setText(qn.getOption3());
                option4Txt.setText(qn.getOption4());



                qn1.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.d(TAG,"qn1-onLongClickListener");
                        onCardViewLongClick(qnCardView);
                        return false;
                    }
                });


                qn1.setOnTouchListener(new OnSwipeTouchListener(getContext()){
                    public void onSwipeLeft() {
                        Log.d(TAG,"onSwipeLeft");
                        onPass();
                        Toast.makeText(getContext(), "Swipe left", Toast.LENGTH_SHORT).show();
                    }

                });





                Log.d(TAG,"qn = "+qn.getQnText());


            }
            //if qn is subjective

            else{

                qnCardView.setVisibility(View.VISIBLE);
                cardViewX = qnCardView.getX();
                cardViewY = qnCardView.getY();
                Log.d(TAG,"cardViewX = "+cardViewX+", cardViewY = "+cardViewY);
                passSubmitLL.setVisibility(View.VISIBLE);

                LayoutInflater inflater1 = LayoutInflater.from(getContext());
                qn1 = (LinearLayout)inflater1.inflate(R.layout.see_qn_subjective,null);
                qnContainer.addView(qn1);
                //qnLLArrayList.add(qn1);

                Log.d(TAG,"addQnsToLayout - timerLL - setVisibility - visible");

                timerLL.setVisibility(View.VISIBLE);



                TextView qnTxt = (TextView)qn1.findViewById(R.id.seeQnSubjQnText);

                Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/comic_sans.ttf");
                qnTxt.setTypeface(custom_font);


                qnTxt.setText(qn.getQnText());

                qn1.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.d(TAG,"qn1-onLongClickListener");
                        onCardViewLongClick(qnCardView);
                        return false;
                    }
                });


                qn1.setOnTouchListener(new OnSwipeTouchListener(getContext()){
                    public void onSwipeLeft() {
                        Log.d(TAG,"onSwipeLeft");
                        onPass();
                        Toast.makeText(getContext(), "Swipe left", Toast.LENGTH_SHORT).show();
                    }
                });



                Log.d(TAG,"qn = "+qn.getQnText());

            }

    }



    public void onPass(){
        Toast.makeText(getContext(),"Pass Qn",Toast.LENGTH_LONG).show();
        countDownTimer.cancel();
        currentTime=0;
        User user = PrefUtilsUser.getCurrentUser(getContext());
        ArrayList<UserSeeQn> qnArr = user.getQuestionArray();
        if(qnArr.size()>0){

            UserSeeQn currentQn = qnArr.get(qnArr.size()-1);
            JSONObject jsonObjectSend = new JSONObject();


            jsonObjectSend.put(Constants.TAG_AnsQn_Qn_ID,currentQn.getqId());
            jsonObjectSend.put(Constants.TAG_AnsQn_UserId_Qnr,currentQn.getuId());
            jsonObjectSend.put(Constants.TAG_AnsQn_UserId_Answr,user.getSaberaId());
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = s.format(new Date());
            jsonObjectSend.put(Constants.TAG_AnsQn_timestamp,format);
            if(jsonObjectSend!=null && jsonObjectSend.size()>0){
                new sendPassAnsToServer().execute(jsonObjectSend);
            }
            qnContainer.removeAllViews();
            //qnArrayList.remove(qnNos-1);

            qnArr.remove(qnArr.size()-1);
            user.setQuestionArray(qnArr);
            PrefUtilsUser.setCurrentUser(user,getContext());
        }

        addQnsToLayout();

    }

    public class sendPassAnsToServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.sendPassAnswerToServerURL, params[0]);
            if (jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            if (jsonObjRec != null && jsonObjRec.size() > 0) {

                Log.d(TAG, "sendPassAnsToServer response = " + jsonObjRec + toString());
                //String response = jsonObjRec.get(Constants.TAG_PostQn_Status).toString();


            } else
                Log.e(TAG, "sendPassAnsToServer - jsonObjRec == null");
        }
    }

    public class MyCountDownTimer extends CountDownTimer {

        //public static final String TAG = MyCountDownTimer.class.getSimpleName();

        public long currentTime1 = 0;

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }



        @Override
        public void onFinish() {

            Log.d(TAG,"MyCountDownTimer - onFinish");
            Log.d(TAG,"currenTime1 = "+currentTime1);
            currentTime=0;

            minTxtView.setText("00");
            secTxtView.setText("00");
            if(currentTime1==1){
                Log.d(TAG,"timer onFinish currenTime1 = 1");
                animation1.cancel();
                timerLL.clearAnimation();
                timerLL.setVisibility(View.GONE);
                onPass();
            }

        }



        @Override
        public void onTick(long millisUntilFinished) {


            currentTime1 = millisUntilFinished/1000;
            currentTime = currentTime1;
            //Log.d(TAG,"onTick - currentTime1 = "+currentTime1);

            if(currentTime1<=10){
                playNotificationSound();
                Vibrator vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
            }

            if(currentTime1==1){

            }


            if(currentTime1==5){
                User user1 = PrefUtilsUser.getCurrentUser(getContext());
                ArrayList<UserSeeQn> qnArrayList = user1.getQuestionArray();
                if(qnArrayList!=null){
                    if(qnArrayList.size()>0){
                        int currentQnNo = qnArrayList.size()-1;
                        String hint = qnArrayList.get(currentQnNo).getHintText();
                        Toast.makeText(getContext(),"Hint : "+hint,Toast.LENGTH_LONG).show();
                    }
                }

            }

            int currentTimeMin = (int)currentTime1/60;
            int currentTimeSec = (int)currentTime1%60;
            minTxtView.setText(String.valueOf(currentTimeMin));
            secTxtView.setText(String.valueOf(currentTimeSec));

            if(currentTime1==10){


                timerLL.startAnimation(animation1);
            }

        }

    }

    public void playNotificationSound() {
        try {
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getContext(), sound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG,"onResume");
        super.onResume();
        isVisible = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
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
        if(currentTime!=0){
            onPass();
        }
    }

    public class sendAnsToServer extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            JSONObject jsonObjRec = HttpClient.SendHttpPostUsingUrlConnection(Constants.sendAnswerToServerURL, params[0]);
            if (jsonObjRec != null)
                return jsonObjRec;
            else
                return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObjRec) {
            super.onPostExecute(jsonObjRec);
            if (jsonObjRec != null && jsonObjRec.size() > 0) {

                Log.d(TAG, "sendAnsToServerResponse = " + jsonObjRec + toString());
                String response = jsonObjRec.get(Constants.TAG_PostQn_Status).toString();


            } else
                Log.e(TAG, "jsonObjRec == null");
        }
    }
}
