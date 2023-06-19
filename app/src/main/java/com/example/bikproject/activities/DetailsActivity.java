package com.example.bikproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.bikproject.R;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = this.getIntent();
        TextView priceView = findViewById(R.id.price);
        TextView nameProductView = findViewById(R.id.nameProduct);
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String price = intent.getStringExtra("price");
            nameProductView.setText(name);
            priceView.setText(price);

        }
        // TODO: 6/19/2023 add button buy with delay 10 sec

    }
}