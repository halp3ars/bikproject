package com.example.bikproject.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.bikproject.R;
import com.example.bikproject.models.enums.UserInfoEnum;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    public static final String COLLECTION_PATH_USER = "users";
    public static final String COLLECTION_PATH_ORDERS = "orders";
    public static final String COLLECTION_ALL_ORDERS = "allOrders";
    private Map<String, Object> userData;
    private String price;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(DetailsActivity.this);
        Intent intent = this.getIntent();
        TextView priceView = findViewById(R.id.price);
        TextView nameProductView = findViewById(R.id.nameProduct);
        Button buyBtn = findViewById(R.id.buttonBuy);

        if (intent != null) {
            String name = intent.getStringExtra("name");
            price = intent.getStringExtra("price");
            nameProductView.setText(name);
            priceView.setText(price);
        }

        buyBtn.setOnClickListener(v -> {
                    buyBtn.setEnabled(false);
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        saveBonus(firestore, currentUser);
                    }
                    new Handler().postDelayed(() -> buyBtn.setEnabled(true), 5000);
                }
        );

    }

    private void saveBonus(FirebaseFirestore firestore, FirebaseUser currentUser) {
        String uid = currentUser.getUid();
        DocumentReference userRef = firestore.collection(COLLECTION_PATH_USER).document(uid);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userData = task.getResult().getData();
                Object balance = userData.get(UserInfoEnum.POINTS.getField());
                double bonusAmount = getBonusAmount(Double.parseDouble(balance.toString()));
                userData.put(UserInfoEnum.POINTS.getField(), bonusAmount);
                userRef.update(userData).addOnSuccessListener(aVoid -> createDialog().show())
                        .addOnFailureListener(e -> Log.e("DETAILS", e.getMessage()));
            }
        });
    }

    private double getBonusAmount(double balance) {
        double priceDouble = Double.parseDouble(price);
        double bonus = priceDouble % 3;
        return balance + bonus;
    }


    private void saveOrder(String uid) {
        CollectionReference collection = firestore.collection(COLLECTION_PATH_ORDERS);
        DocumentReference document = collection.document(uid);
        Task<DocumentSnapshot> documentSnapshotTask = document.get();
        documentSnapshotTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                CollectionReference allOrders = document.collection(COLLECTION_ALL_ORDERS);
                allOrders.get().addOnCompleteListener(orders -> {
                    if (orders.isSuccessful()) {

                    }
                });
            } else if (task.isComplete()) {
            }

        });
    }

    private Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.succec_buy)
                .setPositiveButton(R.string.sign_up_succefull_messasges_ok_button, (dialog, id) ->
                        startActivity(new Intent(DetailsActivity.this, MainPageActivity.class)));
        return builder.create();

    }
}