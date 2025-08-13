package com.example.appentwicklung;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class GoalStore {
    private static final String PREF = "goals_prefs";

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    private static String keyForUser(String user) {
        return "goals_user_" + (user == null ? "anonymous" : user.trim());
    }

    private static JSONObject readUserMap(Context ctx, String user) {
        String raw = prefs(ctx).getString(keyForUser(user), "{}");
        try {
            return new JSONObject(raw != null ? raw : "{}");
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    private static void writeUserMap(Context ctx, String user, JSONObject obj) {
        prefs(ctx).edit().putString(keyForUser(user), obj.toString()).apply();
    }

    public static void setGoal(Context ctx, String user, String dateIso, String text) {
        try {
            JSONObject map = readUserMap(ctx, user);
            if (text == null || text.trim().isEmpty()) {
                map.remove(dateIso);
            } else {
                map.put(dateIso, text);
            }
            writeUserMap(ctx, user, map);
        } catch (JSONException ignored) {}
    }

    public static String getGoal(Context ctx, String user, String dateIso) {
        return readUserMap(ctx, user).optString(dateIso, "");
    }
}
