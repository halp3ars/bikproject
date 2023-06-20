package com.example.bikproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bikproject.R;
import com.example.bikproject.models.dto.ItemsData;

import java.util.ArrayList;

public class ItemsAdapter extends ArrayAdapter<ItemsData> {


    public ItemsAdapter(@NonNull Context context, ArrayList<ItemsData> dataArrayList) {
        super(context, R.layout.list_item, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ItemsData itemsData = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        ImageView listImage = view.findViewById(R.id.listImage);
        TextView listName = view.findViewById(R.id.listName);
        TextView listPrice = view.findViewById(R.id.listPrice);
        listImage.setImageURI(itemsData.getImage());
        listName.setText(itemsData.getName());
        listPrice.setText(itemsData.getPrice().toString());
        return view;
    }


}
