package com.example.appentwicklung;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText userEt, passEt, pass2Et;
    Button finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userEt = findViewById(R.id.editTextNewUsername);
        passEt = findViewById(R.id.editTextNewPassword);
        pass2Et = findViewById(R.id.editTextConfirmPassword);
        finishBtn = findViewById(R.id.buttonFinishRegister);

        String prefill = getIntent().getStringExtra("prefillUsername");
        if (prefill != null && !prefill.trim().isEmpty()) {
            userEt.setText(prefill.trim());
        }

        finishBtn.setOnClickListener(v -> {
            String u = userEt.getText().toString().trim();
            String p = passEt.getText().toString();
            String p2 = pass2Et.getText().toString();

            if (u.isEmpty() || p.isEmpty() || p2.isEmpty()) {
                Toast.makeText(this, "Bitte alle Felder ausfüllen.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!p.equals(p2)) {
                pass2Et.setError("Passwörter stimmen nicht überein");
                return;
            }
            if (UserStore.isUsernameTaken(this, u)) {
                userEt.setError("Benutzer ist schon vergeben");
                Toast.makeText(this, "Benutzer ist schon vergeben.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (UserStore.register(this, u, p)) {
                Toast.makeText(this, "Registrierung erfolgreich. Bitte anmelden.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Registrierung fehlgeschlagen.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
