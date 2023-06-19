package com.example.bikproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bikproject.R;
import com.example.bikproject.activities.DetailsActivity;
import com.example.bikproject.adapters.ListAdapter;
import com.example.bikproject.models.dto.ListData;
import com.example.bikproject.models.enums.ProductEnum;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


public class HomeFragment extends Fragment {
    public static final String COLLECTION_PATH = "product";
    public static final String PRODUCTS = "products/";
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    ListAdapter listAdapter;
    Map<String, Object> productData;


    public HomeFragment() {
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<ListData> dataArrayList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Context context = container.getContext();
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        FirebaseApp.initializeApp(context);
        firestore = FirebaseFirestore.getInstance();
        ListView listView = view.findViewById(R.id.listview);
        CollectionReference productRef = firestore.collection(COLLECTION_PATH);
        productRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot result = task.getResult();
                for (DocumentSnapshot r : result.getDocuments()) {
                    ListData listData = new ListData();
                    productData = r.getData();
                    listData.setName(getText(ProductEnum.NAME));
                    listData.setPrice(Integer.parseInt(getText(ProductEnum.PRICE)));
                    String photoName = getText(ProductEnum.PHOTO_NAME);
                    StorageReference child = storageRef.child(PRODUCTS + photoName);
                    child.getDownloadUrl().addOnCompleteListener(uriTask -> {
                        if (uriTask.isSuccessful()) {
                            listData.setImage(uriTask.getResult());
                        }
                    });
                    dataArrayList.add(listData);
                }
                listAdapter = new ListAdapter(context, dataArrayList);
                listView.setAdapter(listAdapter);
                listView.setClickable(true);
                listView.setOnItemClickListener((parent, view1, position, id) -> {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name", dataArrayList.get(position).getName());
                    intent.putExtra("price", dataArrayList.get(position).getPrice().toString());
                    startActivity(intent);
                });
            } else {
                Exception exception = task.getException();
                Log.e("[HOME]", exception.getMessage());
            }
        });


        return view;
    }

    private String getText(ProductEnum productEnum) {
        return productData.get(productEnum.getField()).toString();
    }

}