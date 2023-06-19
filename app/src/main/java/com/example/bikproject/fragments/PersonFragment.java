package com.example.bikproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.bikproject.R;
import com.example.bikproject.models.enums.UserInfoEnum;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


public class PersonFragment extends Fragment {

    public static final String COLLECTION_PATH = "users";
    TextView fioTextView;
    FirebaseFirestore firestore;
    TextView birthdayTextView;
    TextView cityTextView;
    TextView pointsTextView;
    Map<String, Object> userData;


    public PersonFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        fioTextView = view.findViewById(R.id.fioTextViewProfile);
        birthdayTextView = view.findViewById(R.id.birthdayTextViewProfile);
        cityTextView = view.findViewById(R.id.cityTextViewProfile);
        pointsTextView = view.findViewById(R.id.pointsTextViewProfile);

        Context context = container.getContext();
        FirebaseApp.initializeApp(context);
        firestore = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null && !uid.isEmpty()) {
            DocumentReference userRef = firestore.collection(COLLECTION_PATH).document(uid);
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot result = task.getResult();
                    if (result.exists()) {
                        userData = result.getData();

                        fioTextView.setText(getText(UserInfoEnum.FIO));
                        birthdayTextView.append(" " + getText(UserInfoEnum.BIRTHDAY));
                        cityTextView.append(" " + getText(UserInfoEnum.CITY));
                        pointsTextView.append(" " + getText(UserInfoEnum.POINTS));

                    } else {
                        showToastError(context);
                    }
                } else {
                    Exception exception = task.getException();
                    Log.e("[PROFILE]", exception.getMessage());
                }
            });
        } else {
            showToastError(context);
            return view;
        }


        return view;
    }

    // TODO: 6/19/2023 add number of card


    private String getText(UserInfoEnum userInfoEnum) {
        return userData.get(userInfoEnum.getField()).toString();
    }

    private void showToastError(Context context) {
        Toast.makeText(context, getString(R.string.error_can_not_perfom_sign_up), Toast.LENGTH_SHORT).show();
    }
}