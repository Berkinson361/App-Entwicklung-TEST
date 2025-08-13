package com.example.appentwicklung;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class UserStore {
    private static final String PREF = "users_prefs";
    private static final String KEY_USERMAP = "user_map"; // JSON: {"user":"pass",...}

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    private static JSONObject readMap(Context ctx) {
        String raw = prefs(ctx).getString(KEY_USERMAP, "{}");
        try {
            return new JSONObject(raw != null ? raw : "{}");
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    private static void writeMap(Context ctx, JSONObject obj) {
        prefs(ctx).edit().putString(KEY_USERMAP, obj.toString()).apply();
    }

    public static boolean isUsernameTaken(Context ctx, String username) {
        if (username == null) return false;
        String u = username.trim();
        if (u.isEmpty()) return false;
        return readMap(ctx).has(u);
    }

    public static boolean register(Context ctx, String username, String password) {
        if (username == null || password == null) return false;
        String u = username.trim();
        if (u.isEmpty() || password.isEmpty()) return false;
        try {
            JSONObject map = readMap(ctx);
            if (map.has(u)) return false;
            map.put(u, password);
            writeMap(ctx, map);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    public static boolean validate(Context ctx, String username, String password) {
        if (username == null || password == null) return false;
        String u = username.trim();
        return password.equals(readMap(ctx).optString(u, ""));
    }
}
