package com.example.bikproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bikproject.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPageActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Intent firstPageIntent;
    private Intent registerPageIntent;
    private EditText etEmail;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        FirebaseApp.initializeApp(LoginPageActivity.this);

        Button loginButton = findViewById(R.id.logInButton);
        etEmail = findViewById(R.id.emailSingUpTextEdit);
        etPassword = findViewById(R.id.reEnterPasswordSignUpTextEdit);
        Button registerPageButton = findViewById(R.id.registerPageBtn);

        firstPageIntent = new Intent(this, MainPageActivity.class);
        registerPageIntent = new Intent(this, RegisterPageActivity.class);

        firebaseAuth = FirebaseAuth.getInstance();

        registerPageButton.setOnClickListener(view -> startActivity(registerPageIntent));
        loginButton.setOnClickListener(view -> loginUser());
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(firstPageIntent);
            finish();
        }
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.login_can_not_be_empty));
            etEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.password_can_not_be_empty));
            etPassword.requestFocus();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(firstPageIntent);
                            finish();
                        } else {
                            etPassword.setError(getString(R.string.login_or_password_error));
                        }
                    });
        }
    }
}
