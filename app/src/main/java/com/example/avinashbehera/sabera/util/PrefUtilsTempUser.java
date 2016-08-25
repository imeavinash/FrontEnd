package com.example.avinashbehera.sabera.util;

import android.content.Context;

import com.example.avinashbehera.sabera.model.User;

/**
 * Created by avinashbehera on 25/08/16.
 */
public class PrefUtilsTempUser {

    public static void setCurrentUser(User currentUser, Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "temp_user_prefs", 0);
        complexPreferences.putObject("current_user_value", currentUser);
        complexPreferences.commit();
    }

    public static User getCurrentUser(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "temp_user_prefs", 0);
        User currentUser = complexPreferences.getObject("current_user_value", User.class);
        return currentUser;
    }

    public static void clearCurrentUser( Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "temp_user_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }
}
