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

public class LoginPageActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Intent firstPage;
    private Intent registerPage;
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
        Button etRegisterPage = findViewById(R.id.registerPageBtn);

//        firstPage = new Intent(this, GroupChat.class);
        registerPage = new Intent(this, RegisterPageActivity.class);

        firebaseAuth = FirebaseAuth.getInstance();

        etRegisterPage.setOnClickListener(view -> startActivity(registerPage));
        loginButton.setOnClickListener(view -> loginUser());
    }


    @Override
    protected void onStart() {
        super.onStart();
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        if (currentUser != null) {
//            startActivity(firstPage);
//            finish();
//        }
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
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(firstPage);
                            finish();
                        } else if (task.isComplete()) {
                            etPassword.setError(getString(R.string.login_or_password_error));
                        }
                    }
            );
        }


    }
}
