package com.example.avinashbehera.sabera.util;

import android.content.Context;

import com.example.avinashbehera.sabera.model.User;

/**
 * Created by avinashbehera on 25/08/16.
 */
public class PrefUtilsTempUser {

    public static void setCurrentTempUser(User currentTempUser, Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "temp_user_prefs", 0);
        complexPreferences.putObject("current_temp_user_value", currentTempUser);
        complexPreferences.commit();
    }

    public static User getCurrentTempUser(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "temp_user_prefs", 0);
        User currentTempUser = complexPreferences.getObject("current_temp_user_value", User.class);
        return currentTempUser;
    }

    public static void clearCurrentTempUser( Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "temp_user_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }
}
