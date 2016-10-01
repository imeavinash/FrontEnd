package com.avinashbehera.sabera.Activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by avinashbehera on 01/09/16.
 */
public class CircleView extends View{

    private Paint mTextPaint;
    private Paint mCirclePaint;
    private float centerX;
    private float centerY;
    private int radius;

    private int circleColor;

    private Paint.Style paintStyle;


    public CircleView(Context context, float centerX, float centerY) {
        super(context);
        this.centerX = centerX;
        this.centerY = centerY;
        this.circleColor = Color.GREEN;
        this.radius = 5;



        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(circleColor);


    }



    public CircleView(Context context, AttributeSet attrs, int centerX) {
        super(context, attrs);
        this.centerX = centerX;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawCircle(centerX,centerY,radius,mCirclePaint);

    }
}
