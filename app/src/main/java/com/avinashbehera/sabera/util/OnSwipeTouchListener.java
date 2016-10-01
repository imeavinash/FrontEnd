package com.avinashbehera.sabera.util;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.avinashbehera.sabera.Activity.BaseActivity;

/**
 * Created by avinashbehera on 13/09/16.
 */
public class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;

    public static final String TAG = OnSwipeTouchListener.class.getSimpleName();
    static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;
    private Context context;

    public OnSwipeTouchListener (Context ctx){
        context = ctx;
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Log.d(TAG,"onTouch");
        //gestureDetector.onTouchEvent(event);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                Log.d(TAG,"ACTION DOWN");
                downX = event.getX();
                downY = event.getY();
                ((BaseActivity)context).getViewPager().setPagingEnabled(false);
                return false;
            }

            case MotionEvent.ACTION_MOVE:{
                //Log.d(TAG,"ACTION MOVE");
                return false;
            }
            case MotionEvent.ACTION_UP: {
                Log.d(TAG,"ACTION UP");
                ((BaseActivity)context).getViewPager().setPagingEnabled(true);
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if(Math.abs(deltaX) > MIN_DISTANCE){
                    // left or right
                    if(deltaX < 0) { this.onSwipeRight(); return false; }
                    if(deltaX > 0) { this.onSwipeLeft(); return false; }
                }
                else {
                    Log.i(TAG, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                }

                // swipe vertical?
                if(Math.abs(deltaY) > MIN_DISTANCE){
                    // top or down
                    if(deltaY < 0) { this.onSwipeTop(); return false; }
                    if(deltaY > 0) { this.onSwipeBottom(); return false; }
                }
                else {
                    Log.i(TAG, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                    v.performClick();
                }

            }
        }
        return false;
    }





    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d(TAG,"onDown");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG,"onFling");
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
        Log.d(TAG,"onSwipeRight");
    }

    public void onSwipeLeft() {
        Log.d(TAG,"onSwipeLeft");
    }

    public void onSwipeTop() {
        Log.d(TAG,"onSwipeTop");
    }

    public void onSwipeBottom() {
        Log.d(TAG,"onSwipeBottom");
    }
}

