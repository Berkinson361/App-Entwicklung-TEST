package com.example.appentwicklung;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // Farbe anwenden (Statusbar/Hintergrund/Buttons)
        ThemeApplier.applyToActivity(this);
        // Menü-Kugel oben rechts einblenden
        MenuOverlay.attach(this);
    }
}
