package com.avinashbehera.sabera.util;

import android.content.Context;

import com.avinashbehera.sabera.model.UserPostQn;

import java.util.ArrayList;

/**
 * Created by avinashbehera on 25/08/16.
 */
public class PrefUtilsPostQn {

    public static void setCurrentPostQn(UserPostQn currentPostQn, Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "post_qn_prefs", 0);
        complexPreferences.putObject("current_post_qn_value", currentPostQn);
        complexPreferences.commit();
    }

    public static UserPostQn getCurrentPostQn(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "post_qn_prefs", 0);
        UserPostQn currentPostQn = complexPreferences.getObject("current_post_qn_value", UserPostQn.class);
        return currentPostQn;
    }

    public static void clearCurrentPostQn( Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "post_qn_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }

    public static void updateCategory(Context ctx, ArrayList<String> categoriesList){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "post_qn_prefs", 0);
        UserPostQn currentPostQn = complexPreferences.getObject("current_post_qn_value", UserPostQn.class);
        currentPostQn.setCategories(categoriesList);

    }
}
