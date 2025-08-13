package com.example.appentwicklung;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.TextView;

public class EingabeActivity extends BaseActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eingabe);

        String u = getIntent() != null ? getIntent().getStringExtra("username") : null;
        if (u == null || u.trim().isEmpty()) u = UserPrefs.getUserName(this);
        username = (u != null && !u.trim().isEmpty()) ? u.trim() : "Gast";

        replaceNameInputWithGreeting();
    }

    private void replaceNameInputWithGreeting() {
        EditText nameInput = findViewById(getId("editTextName"));
        if (nameInput == null) return;

        View containerToRemove = nameInput;
        ViewParent parentRaw = nameInput.getParent();
        if (!(parentRaw instanceof ViewGroup)) {
            nameInput.setVisibility(View.GONE);
            ViewGroup root = findRootContainer();
            if (root != null) {
                int index = 0;
                root.addView(makeNameView(), index);
                root.addView(makeQuoteView(), index + 1);
            }
            return;
        }

        ViewGroup parent = (ViewGroup) parentRaw;
        int insertionIndex = parent.indexOfChild(nameInput);

        String parentClass = parent.getClass().getName().toLowerCase();
        if (parentClass.contains("textinputlayout")) {
            ViewParent p2 = parent.getParent();
            if (p2 instanceof ViewGroup) {
                insertionIndex = ((ViewGroup) p2).indexOfChild(parent);
                containerToRemove = parent;
                parent = (ViewGroup) p2;
            }
        }

        if (containerToRemove.getParent() == parent) {
            parent.removeView(containerToRemove);
        } else {
            containerToRemove.setVisibility(View.GONE);
        }

        parent.addView(makeNameView(), insertionIndex);
        parent.addView(makeQuoteView(), insertionIndex + 1);
    }

    private TextView makeNameView() {
        TextView tv = new TextView(this);
        tv.setText("Hallo, " + username + "!");
        tv.setTextSize(24f);
        tv.setPadding(dp(8), dp(8), dp(8), dp(4));
        tv.setTypeface(tv.getTypeface(), android.graphics.Typeface.BOLD);
        return tv;
    }

    private TextView makeQuoteView() {
        TextView tv = new TextView(this);
        tv.setText(Motivation.randomQuote());
        tv.setTextSize(16f);
        tv.setPadding(dp(8), 0, dp(8), dp(16));
        return tv;
    }

    private ViewGroup findRootContainer() {
        View rootContent = findViewById(android.R.id.content);
        if (rootContent instanceof ViewGroup) {
            View child0 = ((ViewGroup) rootContent).getChildAt(0);
            if (child0 instanceof ViewGroup) return (ViewGroup) child0;
            return (ViewGroup) rootContent;
        }
        return null;
    }

    private int getId(String name) {
        return getResources().getIdentifier(name, "id", getPackageName());
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}
