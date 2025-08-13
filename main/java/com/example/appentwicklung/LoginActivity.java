package com.example.appentwicklung;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText usernameInput, passwordInput;
    Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.editTextUsername);
        passwordInput = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        registerButton = findViewById(R.id.buttonRegister);

        // Login prüft gegen Mehrbenutzer-Speicher
        loginButton.setOnClickListener(v -> {
            String inputUsername = usernameInput.getText().toString().trim();
            String inputPassword = passwordInput.getText().toString();

            if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, "Bitte Benutzername und Passwort eingeben.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (UserStore.validate(this, inputUsername, inputPassword)) {
                Toast.makeText(this, "Anmeldung erfolgreich", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, EingabeActivity.class);
                intent.putExtra("username", inputUsername);
                startActivity(intent);
                finish();
            } else if (!UserStore.isUsernameTaken(this, inputUsername)) {
                Toast.makeText(this, "Benutzer existiert nicht.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Falsche Zugangsdaten", Toast.LENGTH_SHORT).show();
            }
        });

        // Bei Registrierung warnen, wenn Name bereits vergeben ist
        registerButton.setOnClickListener(v -> {
            String desired = usernameInput.getText().toString().trim();
            if (!desired.isEmpty() && UserStore.isUsernameTaken(this, desired)) {
                usernameInput.setError("Benutzer ist schon vergeben");
                Toast.makeText(this, "Benutzer ist schon vergeben.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("prefillUsername", desired);
            startActivity(intent);
        });
    }
}
