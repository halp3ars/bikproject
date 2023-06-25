package com.example.bikproject.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import com.example.bikproject.R;
import com.example.bikproject.models.adapters.ProductCart;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class DetailsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private CollectionReference cartCollection;
    private String name;
    private String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(DetailsActivity.this);

        Intent intent = getIntent();
        TextView priceView = findViewById(R.id.price);
        TextView nameProductView = findViewById(R.id.nameProduct);
        Button addInCartButton = findViewById(R.id.buttonAddInCart);

        if (intent != null) {
            name = intent.getStringExtra("name");
            price = intent.getStringExtra("price");
            nameProductView.setText(name);
            priceView.setText(price);
        }

        addInCartButton.setOnClickListener(v -> {
            addInCartButton.setEnabled(false);
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                addInCart(currentUser);
            }
            new Handler().postDelayed(() -> addInCartButton.setEnabled(true), 5000);
        });
    }

    private void addInCart(FirebaseUser currentUser) {
        cartCollection = firestore.collection("cart");
        DocumentReference cartRef = cartCollection.document(currentUser.getUid());

        String productId = generateUUID();
        DocumentReference productRef = cartRef.collection("products").document(productId);

        ProductCart product = new ProductCart(name, Double.parseDouble(price));
        productRef.set(product)
                .addOnSuccessListener(aVoid -> createDialog());
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.succec_buy)
                .setPositiveButton(R.string.sign_up_succefull_messasges_ok_button, (dialog, id) ->
                        startActivity(new Intent(DetailsActivity.this, MainPageActivity.class)))
                .create()
                .show();
    }
}
