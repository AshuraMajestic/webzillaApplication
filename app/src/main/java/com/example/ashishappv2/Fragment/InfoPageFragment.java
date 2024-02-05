package com.example.ashishappv2.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ashishappv2.Domains.userData;
import com.example.ashishappv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class InfoPageFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private TextView shopname,shopLink;
    public InfoPageFragment() {
        // Required empty public constructor
    }

    public static InfoPageFragment newInstance() {
        InfoPageFragment fragment = new InfoPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_info_page, container, false);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        shopname = view.findViewById(R.id.name);
        shopLink=view.findViewById(R.id.link);

        userRef = FirebaseDatabase.getInstance().getReference().child("UserData").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userData user = dataSnapshot.getValue(userData.class);
                    if (user != null) {
                        String ShopName = user.getShopname();
                        String link = user.getLink();
                        // Set shop name in the TextView
                        shopname.setText(ShopName);
                        shopLink.setText("ashishweb-jv5n.onrender.com/"+link);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving data: " + databaseError.getMessage());
            }
        });
        return  view;
    }

    private void makeLog(String s) {
        Log.d("AshuraDB", s);
    }
}