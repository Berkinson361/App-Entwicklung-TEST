package com.example.appentwicklung;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class MenuOverlay {

    public static void attach(@NonNull Activity activity) {
        View content = activity.findViewById(android.R.id.content);
        if (!(content instanceof ViewGroup)) return;

        final ViewGroup root = (ViewGroup) content;

        // schon vorhanden? (Doppelte vermeiden)
        if (root.findViewWithTag("overflowMenuKugel") != null) return;

        final int primary = ThemePrefs.getPrimaryColor(activity);

        // Runder Hintergrund
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(primary);
        bg.setShape(GradientDrawable.OVAL);

        // "⋮" als Text — funktioniert ohne Icons/Drawables
        TextView dotBtn = new TextView(activity);
        dotBtn.setTag("overflowMenuKugel");
        dotBtn.setText("⋮");
        dotBtn.setTextSize(22f);
        dotBtn.setTypeface(Typeface.DEFAULT_BOLD);
        dotBtn.setTextColor(0xFFFFFFFF);
        dotBtn.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dotBtn.setElevation(dp(activity, 6));
        }
        int size = dp(activity, 44);
        dotBtn.setWidth(size);
        dotBtn.setHeight(size);
        dotBtn.setBackground(bg);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, size);
        lp.gravity = Gravity.END | Gravity.TOP;
        lp.topMargin = dp(activity, 12);
        lp.rightMargin = dp(activity, 12);

        root.addView(dotBtn, lp);

        dotBtn.setOnClickListener(v -> {
            PopupMenu pm = new PopupMenu(activity, v);
            pm.getMenu().add(0, 1, 0, "Einstellungen");
            pm.getMenu().add(0, 2, 1, "Ausloggen");
            pm.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == 1) {
                    activity.startActivity(new Intent(activity, SettingsActivity.class));
                    return true;
                } else if (item.getItemId() == 2) {
                    UserPrefs.clearUserName(activity);
                    Intent i = new Intent(activity, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(i);
                    activity.finish();
                    return true;
                }
                return false;
            });
            pm.show();
        });
    }

    private static int dp(Activity a, int v) {
        return (int) (v * a.getResources().getDisplayMetrics().density);
    }
}
