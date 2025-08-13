package com.example.appentwicklung;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPrefs {
    private static final String PREFS = "user_prefs";
    private static final String KEY_USER_NAME = "user_name";

    public static String getUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getString(KEY_USER_NAME, null);
    }

    public static void setUserName(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_USER_NAME, name).apply();
    }

    public static void clearUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().remove(KEY_USER_NAME).apply();
    }
}
