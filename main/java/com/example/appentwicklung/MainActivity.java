package com.example.appentwicklung;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Optional: Falls du das Begrüßungs-UI auch bei Rückkehr zur Activity aktualisieren willst
    @Override
    protected void onResume() {
        super.onResume();
        replaceNameInputWithGreeting();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceNameInputWithGreeting();
    }

    /**
     * Sucht das (frühere) Namens-Eingabefeld und ersetzt es durch:
     *  - "Hallo, <Name>!"
     *  - "<random motivierender Satz>"
     * Funktioniert auch, wenn das EditText in einer TextInputLayout liegt.
     */
    private void replaceNameInputWithGreeting() {
        View root = findViewById(android.R.id.content);
        if (!(root instanceof ViewGroup)) return;

        // 1) Versuch über bekannte IDs
        EditText nameInput = tryFindNameInputByCommonIds();

        // 2) Fallback: erstes EditText im View-Hierarchy suchen (oben auf dem Screen)
        if (nameInput == null) {
            nameInput = findFirstEditTextInHierarchy((ViewGroup) ((ViewGroup) root).getChildAt(0));
        }
        if (nameInput == null) return; // Nichts zu tun

        // Eltern bestimmen und ggf. ganze TextInputLayout entfernen
        View containerToRemove = nameInput; // standard: nur das EditText
        ViewGroup insertionParent = null;
        int insertionIndex = -1;

        ViewParent p1 = nameInput.getParent();
        if (p1 instanceof ViewGroup) {
            ViewGroup directParent = (ViewGroup) p1;

            // ist der direkte Parent eine Material TextInputLayout?
            String parentClass = directParent.getClass().getName().toLowerCase();
            boolean isTextInputLayout = parentClass.contains("textinputlayout");

            if (isTextInputLayout) {
                // wir fügen nicht in die TextInputLayout ein, sondern eine Ebene darüber
                ViewParent p2 = directParent.getParent();
                if (p2 instanceof ViewGroup) {
                    insertionParent = (ViewGroup) p2;
                    containerToRemove = directParent; // ganze Hülle entfernen
                    insertionIndex = insertionParent.indexOfChild(containerToRemove);
                }
            } else {
                insertionParent = directParent;
                insertionIndex = insertionParent.indexOfChild(nameInput);
            }
        }

        if (insertionParent == null || insertionIndex < 0) {
            // Fallback: an den Anfang des Root-Layouts setzen
            insertionParent = (ViewGroup) ((ViewGroup) root).getChildAt(0);
            insertionIndex = 0;
        }

        // Entfernen (wenn möglich), sonst nur ausblenden
        if (containerToRemove.getParent() == insertionParent) {
            insertionParent.removeView(containerToRemove);
        } else {
            containerToRemove.setVisibility(View.GONE);
        }

        // Benutzernamen holen (oder "Gast")
        String userName = UserPrefs.getUserName(this);
        if (userName == null || userName.trim().isEmpty()) userName = "Gast";

        // Name-TextView
        TextView nameView = new TextView(this);
        nameView.setText("Hallo, " + userName + "!");
        nameView.setTextSize(24f);
        nameView.setTypeface(nameView.getTypeface(), Typeface.BOLD);
        nameView.setPadding(dp(8), dp(8), dp(8), dp(4));

        // Zufalls-Motivationssatz
        TextView quoteView = new TextView(this);
        quoteView.setText(Motivation.randomQuote());
        quoteView.setTextSize(16f);
        quoteView.setPadding(dp(8), dp(0), dp(8), dp(16));

        // Einfügen: exakt an Stelle des entfernten Feldes
        insertionParent.addView(nameView, insertionIndex);
        insertionParent.addView(quoteView, insertionIndex + 1);
    }

    private EditText tryFindNameInputByCommonIds() {
        int[] possibleIds = new int[] {
                getId("editTextName"),
                getId("nameInput"),
                getId("inputName"),
                getId("username"),
                getId("editTextTextPersonName")
        };
        for (int id : possibleIds) {
            if (id != 0) {
                View v = findViewById(id);
                if (v instanceof EditText) return (EditText) v;
            }
        }
        return null;
    }

    private int getId(String name) {
        return getResources().getIdentifier(name, "id", getPackageName());
    }

    private EditText findFirstEditTextInHierarchy(ViewGroup root) {
        if (root == null) return null;
        for (int i = 0; i < root.getChildCount(); i++) {
            View c = root.getChildAt(i);
            if (c instanceof EditText) return (EditText) c;
            if (c instanceof ViewGroup) {
                EditText deeper = findFirstEditTextInHierarchy((ViewGroup) c);
                if (deeper != null) return deeper;
            }
        }
        return null;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}
