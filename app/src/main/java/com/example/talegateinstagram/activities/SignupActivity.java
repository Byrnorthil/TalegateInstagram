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
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "SignupActivity";

    private EditText etUsername;
    private EditText etHandle;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirm;
    private Button btnSignup;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.etUsername);
        etHandle = findViewById(R.id.etHandle);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirm = findViewById(R.id.etConfirm);
        btnSignup = findViewById(R.id.btnSignup);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String handle = etHandle.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (!password.equals(etConfirm.getText().toString())) {
                    displayMessage("Password fields are not equal");
                    etConfirm.setText("");
                } else if(handle.length() > 8) {
                    displayMessage("Handle must be <= 8 characters long");
                } else {
                    signUp(username, handle, email, password);
                }
            }
        });
    }

    private void signUp(final String username, String handle, String email, final String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.put("handle", handle);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    saveUser(username, password);
                    goMainActivity();
                } else {
                    displayMessage(e.getMessage());
                    Log.e(TAG, "Parse exception thrown", e);
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
        finish();
    }

    private void saveUser(String username, String password) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("username", username);
        edit.putString("password", password);
        edit.apply();
    }
}
