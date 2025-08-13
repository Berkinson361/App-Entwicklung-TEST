package com.example.appentwicklung;

import android.content.Context;
import android.content.SharedPreferences;

public class ThemePrefs {
    private static final String PREFS = "theme_prefs";
    private static final String KEY_PRIMARY = "primary_color";
    private static final int DEFAULT_PRIMARY = 0xFF6A1B9A;

    public static int getPrimaryColor(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return sp.getInt(KEY_PRIMARY, DEFAULT_PRIMARY);
    }

    public static void setPrimaryColor(Context ctx, int color) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_PRIMARY, color).apply();
    }
}
