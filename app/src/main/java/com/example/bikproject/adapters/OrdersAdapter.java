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
import com.example.bikproject.models.dto.OrdersDto;

import java.util.ArrayList;

public class OrdersAdapter extends ArrayAdapter<OrdersDto> {


    public OrdersAdapter(@NonNull Context context, ArrayList<OrdersDto> dataArrayList) {
        super(context, R.layout.list_orders, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        OrdersDto listData = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_orders, parent, false);
        }
        TextView orderName = view.findViewById(R.id.orderName);
        TextView dateOrder = view.findViewById(R.id.dateOrder);
        TextView bonusAmount = view.findViewById(R.id.bonusOrderAmount);
        orderName.setText(listData.getName());
        dateOrder.setText(listData.getDate());
        bonusAmount.setText(listData.getPoints().toString());
        return view;
    }


}
