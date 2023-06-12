package com.example.bikproject.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bikproject.R;
import com.example.bikproject.models.enums.UserInfoEnum;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterPageActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    EditText etFIO;
    EditText etBirthDate;
    Spinner spCities;
    EditText etPhoneNumber;
    EditText etEmailEnter;
    EditText etPasswordEnter;
    Button btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        FirebaseApp.initializeApp(this);
        firestore = FirebaseFirestore.getInstance();
        etFIO = (EditText) findViewById(R.id.editTextFIO);
        etBirthDate = (EditText) findViewById(R.id.editTextDateOfBirth);
        spCities = (Spinner) findViewById(R.id.spinnerCities);
        etPhoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        etEmailEnter = (EditText) findViewById(R.id.emailSingUpTextEdit);
        etPasswordEnter = (EditText) findViewById(R.id.enterPasswordSignUpTextEdit);
        btnSignUp = (Button) findViewById(R.id.signUpButton);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCities.setAdapter(adapter);


        firebaseAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(view -> signUpUser());

    }


    private void signUpUser() {
        String fio = etFIO.getText().toString();
        String birthDate = etBirthDate.getText().toString();
        String city = spCities.getSelectedItem().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String password = etPasswordEnter.getText().toString();
        String email = etEmailEnter.getText().toString();
        String fieldCanNotBeEmpty = getString(R.string.field_can_not_be_empty);


        if (TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailEnter.setError(fieldCanNotBeEmpty);
        } else if (TextUtils.isEmpty(password)) {
            etPasswordEnter.setError(fieldCanNotBeEmpty);
        } else if (TextUtils.isEmpty(fio)) {
            etFIO.setError(fieldCanNotBeEmpty);
        } else if (TextUtils.isEmpty(birthDate)) {
            etBirthDate.setError(fieldCanNotBeEmpty);
        } else if (TextUtils.isEmpty(city)) {
            TextView errorText = (TextView) spCities.getSelectedView();
            errorText.setError(fieldCanNotBeEmpty);
        } else if (TextUtils.isEmpty(phoneNumber) && !Patterns.PHONE.matcher(phoneNumber).matches()) {
            etPhoneNumber.setError(fieldCanNotBeEmpty);
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    saveAdditionalInfo(fio, birthDate, city, phoneNumber, email, task);
                } else {
                    Toast.makeText(RegisterPageActivity.this, getString(R.string.error_can_not_perfom_sign_up), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void saveAdditionalInfo(String fio, String birthDate, String city, String phoneNumber, String email, Task<AuthResult> task) {
        String uid = Objects.requireNonNull(task.getResult().getUser()).getUid();
        DocumentReference userRef = firestore.collection("users").document(uid);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("fio", fio);
        userInfo.put("dateBirth", birthDate);
        userInfo.put("city", city);
        userInfo.put("phone", phoneNumber);
        userInfo.put("email", email);
        userRef.set(userInfo).addOnSuccessListener(aVoid -> {
                    createDialog().show();
                    finish();
                })
                .addOnFailureListener(e -> Log.e("REG", e.getMessage()));
    }


    private Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.sign_up_succefull_messasges)
                .setPositiveButton(R.string.sign_up_succefull_messasges_ok_button, (dialog, id) ->
                        startActivity(new Intent(RegisterPageActivity.this, LoginPageActivity.class)));
        return builder.create();

    }


}