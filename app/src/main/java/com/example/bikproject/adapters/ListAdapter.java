package com.example.bikproject.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bikproject.R;
import com.example.bikproject.models.dto.ListData;

import java.net.URI;
import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ListData> {


    public ListAdapter(@NonNull Context context, ArrayList<ListData> dataArrayList) {
        super(context, R.layout.list_item, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ListData listData = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        ImageView listImage = view.findViewById(R.id.listImage);
        TextView listName = view.findViewById(R.id.listName);
        TextView listPrice = view.findViewById(R.id.listPrice);
        listImage.setImageURI(listData.getImage());
        listName.setText(listData.getName());
        listPrice.setText(listData.getPrice().toString());
        return view;
    }


}
