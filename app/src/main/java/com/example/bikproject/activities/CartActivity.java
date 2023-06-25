package com.example.bikproject.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bikproject.R;
import com.example.bikproject.adapters.CartAdapter;
import com.example.bikproject.models.adapters.ProductCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private CollectionReference cartCollection;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private CartAdapter cartAdapter;
    private ListView listView;
    private TextView sumTextView;
    private Button buttonPay;
    private double sumOfCart = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        listView = findViewById(R.id.listCart);
        sumTextView = findViewById(R.id.sum_of_all_in_cart_text);
        buttonPay = findViewById(R.id.buttonPay);

        buttonPay.setOnClickListener(v -> clearCart(firebaseAuth.getCurrentUser()));

        productList(firebaseAuth.getCurrentUser());
    }

    private void productList(FirebaseUser currentUser) {
        cartCollection = db.collection("cart");
        DocumentReference userCartRef = cartCollection.document(currentUser.getUid());
        CollectionReference productsCollection = userCartRef.collection("products");
        ArrayList<ProductCart> productCartList = new ArrayList<>();

        productsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    ProductCart product = document.toObject(ProductCart.class);
                    productCartList.add(product);
                    sumOfCart += product.getPrice();
                }
                sumTextView.setText(String.valueOf(sumOfCart));
                cartAdapter = new CartAdapter(getBaseContext(), productCartList);
                listView.setAdapter(cartAdapter);
                listView.setClickable(false);
            } else {
                Exception exception = task.getException();
                Log.e("[CART]", exception.getMessage());
            }
        });
    }

    private void clearCart(FirebaseUser currentUser) {
        DocumentReference userCartRef = cartCollection.document(currentUser.getUid());
        CollectionReference productsCollection = userCartRef.collection("products");

        DocumentReference userRef = db.collection("users").document(currentUser.getUid());
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                double currentPoints = documentSnapshot.getDouble("points");

                productsCollection.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            productsCollection.document(document.getId()).delete();
                        }
                        sumTextView.setText("0.0");
                        cartAdapter.clear();

                        double currentBonus = calculateBonus(sumOfCart);
                        double updatedPoints = currentPoints + currentBonus;

                        userRef.update("points", updatedPoints)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(CartActivity.this, "Все 'оплаченно' успешно", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(CartActivity.this, "Не получилось зачислить баллы", Toast.LENGTH_SHORT).show();
                                    Log.e("[UPDATE_POINTS]", e.getMessage());
                                });
                    } else {
                        Exception exception = task.getException();
                        Log.e("[CLEAR_CART]", exception.getMessage());
                    }
                });
            } else {
                Log.e("[CLEAR_CART]", "User document does not exist");
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(CartActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
            Log.e("[RETRIEVE_POINTS]", e.getMessage());
        });
    }

    private double calculateBonus(double sum) {
        return sum * 0.03;
    }
}
