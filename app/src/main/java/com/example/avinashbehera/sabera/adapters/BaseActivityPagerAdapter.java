package com.example.avinashbehera.sabera.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.avinashbehera.sabera.fragments.MessageFragment;
import com.example.avinashbehera.sabera.fragments.MoreFragment;
import com.example.avinashbehera.sabera.fragments.PostQnFragment;
import com.example.avinashbehera.sabera.fragments.SeeQnFragment;

/**
 * Created by avinashbehera on 26/08/16.
 */
public class BaseActivityPagerAdapter extends FragmentPagerAdapter {

    public BaseActivityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                SeeQnFragment f1 = new SeeQnFragment();
                return f1;
            case 1:
                PostQnFragment f2 = new PostQnFragment();
                return f2;
            case 2:
                MessageFragment f3 = new MessageFragment();
                return f3;
            case 3:
                MoreFragment f4 = new MoreFragment();
                return f4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SEE QNS";
            case 1:
                return "POST QN";
            case 2:
                return "MESSAGES";
            case 3:
                return "MORE";
        }
        return null;
    }
}

