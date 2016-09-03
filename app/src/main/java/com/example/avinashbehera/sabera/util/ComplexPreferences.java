package com.example.avinashbehera.sabera.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.simple.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by avinashbehera on 20/08/16.
 */
public class ComplexPreferences {

    private static ComplexPreferences complexPreferences;
    private static Gson GSON = new Gson();
    Type typeOfObject = new TypeToken<Object>() {
    }.getType();
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static final String TAG = ComplexPreferences.class.getSimpleName();

    private ComplexPreferences(Context context, String namePreferences, int mode) {
        Log.e(TAG,"regButtonClickListener - j1");
        this.context = context;
        Log.e(TAG,"regButtonClickListener - j2");
        if (namePreferences == null || namePreferences.equals("")) {
            Log.e(TAG,"regButtonClickListener - j3");
            namePreferences = "complex_preferences";
        }
        Log.e(TAG,"regButtonClickListener - j4");
        preferences = context.getSharedPreferences(namePreferences, mode);
        Log.e(TAG,"regButtonClickListener - j5");
        editor = preferences.edit();
        Log.e(TAG,"regButtonClickListener - j6");
    }

    public static ComplexPreferences getComplexPreferences(Context context, String namePreferences, int mode) {

        Log.e(TAG,"regButtonClickListener - f1");

     // if (complexPreferences == null) {
        complexPreferences = new ComplexPreferences(context,
                namePreferences, mode);
      //}

        Log.e(TAG,"regButtonClickListener - f2");

        return complexPreferences;
    }

    public void putObject(String key, Object object) {
        Log.e(TAG,"regButtonClickListener - g1");
        if (object == null) {
            Log.e(TAG,"regButtonClickListener - g2");
            throw new IllegalArgumentException("object is null");
        }

        if (key.equals("") || key == null) {
            Log.e(TAG,"regButtonClickListener - g3");
            throw new IllegalArgumentException("key is empty or null");
        }

        Log.e(TAG,"regButtonClickListener - g4");
        JSONObject obj = new JSONObject();

        editor.putString(key, GSON.toJson(object));
        Log.e(TAG,"regButtonClickListener - g5");
    }

    public void commit() {

        Log.e(TAG,"regButtonClickListener - h1");
        editor.commit();
    }

    public void clearObject() {
        editor.clear();
    }

    public <T> T getObject(String key, Class<T> a) {

        String gson = preferences.getString(key, null);
        if (gson == null) {
            return null;
        } else {
            try {
                return GSON.fromJson(gson, a);
            } catch (Exception e) {
                throw new IllegalArgumentException("Object storaged with key " + key + " is instanceof other class");
            }
        }
    }

}



