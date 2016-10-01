package com.avinashbehera.sabera.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avinashbehera.sabera.R;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {

    public static final String TAG = IntroActivity.class.getSimpleName();

    private ViewPager viewPager;
    private Button skipButton;
    private ArrayList<Integer> listOfCards;
    private LinearLayout countLayout;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private int dotsCount;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initData();
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        private ArrayList<Integer> items;

        public MyViewPagerAdapter(ArrayList<Integer> listOfItems) {
            this.items = listOfItems;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Log.d(TAG,"MyViewPagerAdapter -instantiateItem");
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.galleryitem, container,false);

            ImageView galleimageview = (ImageView) view
                    .findViewById(R.id.gelleryimageview);
            galleimageview.setImageResource(items.get(position));

            ((ViewPager) container).addView(view);

            return view;
        }


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View)obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View)object;
            ((ViewPager) container).removeView(view);
        }
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

            Log.d(TAG,"viewPagerPageChangeListener - onPageSelected");

            for (int i = 0; i < dotsCount; i++) {
                dots[i].setTextColor(android.graphics.Color.GRAY);
                //dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            dots[position].setTextColor(android.graphics.Color.BLUE);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public void initData(){

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        skipButton = (Button)findViewById(R.id.btnSkip);
        countLayout = (LinearLayout)findViewById(R.id.image_count_homescreen);

        listOfCards = new ArrayList<>();
        listOfCards.add(R.drawable.screenshot0_512);
        listOfCards.add(R.drawable.screenshot2_512);
        listOfCards.add(R.drawable.screenshot3_512);

        myViewPagerAdapter = new MyViewPagerAdapter(listOfCards);

        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(viewPagerPageChangeListener);

        setUiPageViewController();

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(IntroActivity.this, LoginActivity.class);

                startActivity(homeIntent);

                finish();
            }
        });


    }

    private void setUiPageViewController() {
        Log.d(TAG,"setUiPageViewController()");
        dotsLayout = (LinearLayout)findViewById(R.id.image_count_homescreen);
        dotsCount = myViewPagerAdapter.getCount();
        dots = new TextView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
            dotsLayout.addView(dots[i]);
        }
        dots[0].setTextColor(android.graphics.Color.BLUE);
    }
}
