package com.letsseetech.data.simplenotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;


public class sharedpreference {


    public sharedpreference(){

    }
    public static boolean resetshred( Context context)
    {
        SharedPreferences pref = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit(); // commit changes
        return  true;
    }

    public static boolean saveUserId(String userId, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("name", userId);
        myEdit.commit();
        return true;
    }

    public static String getuserId(Context context) {
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_APPEND);
        String s1 = sh.getString("name", "");
       //if(s1 != null) return true;

        return  s1;
    }
}
