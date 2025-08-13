package com.example.appentwicklung;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ZielKalenderActivity extends AppCompatActivity {

    private final SimpleDateFormat isoFmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private String selectedIsoDate = null;
    private String username = "anonymous";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ziel_kalender);

        String u = getIntent().getStringExtra("username");
        if (u != null && !u.trim().isEmpty()) username = u.trim();

        CalendarView calendarView = findViewById(R.id.calendarView);
        TextView selectedDateTv = findViewById(R.id.textViewSelectedDate);
        EditText zielEt = findViewById(R.id.editTextZielBeschreibung);
        Button saveBtn = findViewById(R.id.buttonZielSpeichern);
        Button backBtn = findViewById(R.id.buttonZurueck);

        // Start: Beschreibung und Speichern versteckt
        zielEt.setVisibility(View.GONE);
        saveBtn.setVisibility(View.GONE);

        backBtn.setOnClickListener(v -> finish()); // Zurück

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfMonth, 0, 0, 0);
            cal.set(Calendar.MILLISECOND, 0);

            selectedIsoDate = isoFmt.format(cal.getTime());
            selectedDateTv.setText("Ausgewähltes Datum: " + dayOfMonth + "." + (month + 1) + "." + year);

            // Sichtbar schalten und vorhandenes Ziel laden
            zielEt.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.VISIBLE);
            zielEt.setText(GoalStore.getGoal(this, username, selectedIsoDate));
        });

        saveBtn.setOnClickListener(v -> {
            if (selectedIsoDate == null) {
                Toast.makeText(this, "Bitte erst ein Datum auswählen.", Toast.LENGTH_SHORT).show();
                return;
            }
            String text = zielEt.getText().toString();
            GoalStore.setGoal(this, username, selectedIsoDate, text);
            Toast.makeText(this, "Ziel gespeichert.", Toast.LENGTH_SHORT).show();
        });
    }
}
