package com.example.avinashbehera.sabera.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by avinashbehera on 24/08/16.
 */
public class Utility {

    public static void makeToast(Context ctx, String toastMessage, int toastLength){
        switch(toastLength){
            case Constants.toastLengthShort :
                Toast.makeText(ctx, toastMessage, Toast.LENGTH_SHORT).show();
                break;
            case Constants.toastLengthLong:
            default:
                Toast.makeText(ctx, toastMessage, Toast.LENGTH_LONG).show();
                break;


        }
    }
}
