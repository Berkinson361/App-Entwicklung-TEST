package com.example.appentwicklung;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Method;

public class ThemeApplier {

    public static void applyToActivity(Activity activity) {
        int primary = ThemePrefs.getPrimaryColor(activity);

        Window w = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            w.setStatusBarColor(darken(primary));
            w.setNavigationBarColor(darken(primary));
        }

        View content = activity.findViewById(android.R.id.content);
        if (content instanceof ViewGroup) {
            ViewGroup root = (ViewGroup) content;
            View first = root.getChildCount() > 0 ? root.getChildAt(0) : root;
            first.setBackgroundColor(lighten(primary));
            tintTree(first, primary);
        }
    }

    private static void tintTree(View view, int primary) {
        if (view instanceof Button) {
            Button b = (Button) view;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                b.setBackgroundTintList(ColorStateList.valueOf(primary));
                b.setTextColor(0xFFFFFFFF);
            } else {
                b.setBackgroundColor(primary);
                b.setTextColor(0xFFFFFFFF);
            }
        }
        if (view.getClass().getName().equals("com.google.android.material.button.MaterialButton")) {
            try {
                Method setBgTint = view.getClass().getMethod("setBackgroundTintList", ColorStateList.class);
                setBgTint.invoke(view, ColorStateList.valueOf(primary));
                Method setTxtColor = TextView.class.getMethod("setTextColor", int.class);
                setTxtColor.invoke(view, 0xFFFFFFFF);
            } catch (Exception ignored) {}
        }
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                tintTree(vg.getChildAt(i), primary);
            }
        }
    }

    private static int lighten(int color) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        r = (int) (r + (255 - r) * 0.90f);
        g = (int) (g + (255 - g) * 0.90f);
        b = (int) (b + (255 - b) * 0.90f);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int darken(int color) {
        int a = (color >> 24) & 0xFF;
        int r = Math.max(0, (int) (((color >> 16) & 0xFF) * 0.75f));
        int g = Math.max(0, (int) (((color >> 8) & 0xFF) * 0.75f));
        int b = Math.max(0, (int) ((color & 0xFF) * 0.75f));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
