package com.example.bikproject.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bikproject.R;
import com.example.bikproject.models.enums.UserInfoEnum;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.common.value.qual.MatchesRegex;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterPageActivity extends AppCompatActivity {

    public static final String FIO_REGEXP = "^[А-ЯЁ][а-яё]+\\\\s[А-ЯЁ][а-яё]+\\\\s[А-ЯЁ][а-яё]+$";
    public static final String COLLECTION_PATH = "users";
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
        etFIO = findViewById(R.id.editTextFIO);
        etBirthDate = findViewById(R.id.editTextDateOfBirth);
        spCities = findViewById(R.id.spinnerCities);
        etPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        etEmailEnter = findViewById(R.id.emailSingUpTextEdit);
        etPasswordEnter = findViewById(R.id.enterPasswordSignUpTextEdit);
        btnSignUp = findViewById(R.id.signUpButton);


        etBirthDate.setOnClickListener(v1 -> {
            final Calendar c1 = Calendar.getInstance();

            int year12 = c1.get(Calendar.YEAR);
            int month1 = c1.get(Calendar.MONTH);
            int day1 = c1.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    RegisterPageActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> etBirthDate.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year1),

                    year12, month1, day1);

            datePickerDialog.show();
        });


        spCities.setAdapter(getDropDownAdapter());


        firebaseAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(view -> signUpUser());


    }

    @NonNull
    private ArrayAdapter<CharSequence> getDropDownAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(RegisterPageActivity.this,
                R.array.cities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
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
        DocumentReference userRef = firestore.collection(COLLECTION_PATH).document(uid);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put(UserInfoEnum.FIO.getField(), fio);
        userInfo.put(UserInfoEnum.BIRTHDAY.getField(), birthDate);
        userInfo.put(UserInfoEnum.CITY.getField(), city);
        userInfo.put(UserInfoEnum.PHONE.getField(), phoneNumber);
        userInfo.put(UserInfoEnum.EMAIL.getField(), email);
        userInfo.put(UserInfoEnum.POINTS.getField(), 0);
        userRef.set(userInfo)
                .addOnSuccessListener(aVoid -> {
                    showSuccessDialog();
                })
                .addOnFailureListener(e -> {
                    Log.e("REG", e.getMessage());
                });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.sign_up_succefull_messasges)
                .setPositiveButton(R.string.sign_up_succefull_messasges_ok_button, (dialog, id) -> {
                    Intent intent = new Intent(RegisterPageActivity.this, LoginPageActivity.class);
                    startActivity(intent);
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        finish();
    }



}