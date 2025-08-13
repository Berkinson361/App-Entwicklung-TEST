package com.example.appentwicklung;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends BaseActivity {

    private RadioGroup radioGroup;
    private int[] colors = new int[] {
            0xFF6A1B9A, // Lila
            0xFF00796B, // Türkis
            0xFF1565C0, // Blau
            0xFF2E7D32, // Grün
            0xFFAD1457,  // Pink
            0xfffefae0,
            0xffdda15e
    };
    private String[] names = new String[] { "Lila", "Türkis", "Blau", "Grün", "Pink","Test","TEst 2Berk" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(16), dp(16), dp(16));
        root.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TextView title = new TextView(this);
        title.setText("Einstellungen");
        title.setTextSize(22f);
        title.setTypeface(title.getTypeface(), android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, dp(12));
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("App-Farbe auswählen:");
        subtitle.setTextSize(16f);
        subtitle.setPadding(0, 0, 0, dp(8));
        root.addView(subtitle);

        radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        int saved = ThemePrefs.getPrimaryColor(this);

        for (int i = 0; i < colors.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(names[i] + "  (#" + Integer.toHexString(colors[i]).toUpperCase() + ")");
            rb.setTextSize(15f);
            rb.setTag(colors[i]);
            rb.setPadding(0, dp(6), 0, dp(6));
            if (colors[i] == saved) rb.setChecked(true);
            radioGroup.addView(rb);
        }
        root.addView(radioGroup);

        Button save = new Button(this);
        save.setText("Speichern");
        save.setAllCaps(false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(16);
        root.addView(save, lp);

        setContentView(root);

        save.setOnClickListener(v -> {
            int checkedId = radioGroup.getCheckedRadioButtonId();
            if (checkedId == -1) {
                Toast.makeText(this, "Bitte eine Farbe wählen.", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton checked = radioGroup.findViewById(checkedId);
            Object tag = checked.getTag();
            int color = (tag instanceof Integer) ? (Integer) tag : Color.parseColor("#6A1B9A");
            ThemePrefs.setPrimaryColor(this, color);
            ThemeApplier.applyToActivity(this);
            Toast.makeText(this, "Farbe aktualisiert.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private int dp(int v) { return (int) (v * getResources().getDisplayMetrics().density); }
}
