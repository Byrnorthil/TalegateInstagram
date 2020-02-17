package com.example.talegateinstagram.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.talegateinstagram.R;
import com.example.talegateinstagram.activities.MainActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    private SharedPreferences pref;

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String prevUsername = pref.getString("username", "n/a");
        String prevPassword = pref.getString("password", "n/a");
        if (!(prevUsername.equals("n/a") || prevPassword.equals("n/a"))) {
            login(prevUsername, prevPassword);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                login(username, password);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    private void signUp() {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    private void login(final String username, final String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    saveUser(username, password);
                    etUsername.setText("");
                    etPassword.setText("");
                    goMainActivity();
                } else {
                    displayMessage(e.getMessage());
                }
            }
        });
    }

    private void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void saveUser(String username, String password) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("username", username);
        edit.putString("password", password);
        edit.apply();
    }
}
