package com.example.bikproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bikproject.R;
import com.example.bikproject.models.adapters.ProductCart;

import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<ProductCart> {


    public CartAdapter(@NonNull Context context, ArrayList<ProductCart> dataArrayList) {
        super(context, R.layout.list_orders, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ProductCart listData = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_orders, parent, false);
        }
        TextView orderName = view.findViewById(R.id.orderName);
        TextView priceCart = view.findViewById(R.id.price_cart);
        orderName.setText(listData.getName());
        priceCart.setText(listData.getPrice().toString());
        return view;
    }


}
