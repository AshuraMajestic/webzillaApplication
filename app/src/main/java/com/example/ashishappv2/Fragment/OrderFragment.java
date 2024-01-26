package com.example.ashishappv2.Fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ashishappv2.Adapter.OrderAdapter;
import com.example.ashishappv2.Domains.ProductWithImage;
import com.example.ashishappv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<ProductWithImage> productList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        recyclerView = view.findViewById(R.id.orderView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<>();
        orderAdapter = new OrderAdapter(getContext(), productList);
        recyclerView.setAdapter(orderAdapter);

//        fetchUserData();  // Moved this line after initializing the adapter

        return view;
    }
    private void setUpOnBackPressed(){
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(isEnabled()){
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
    }
    private void fetchUserData() {
        String userEmail = mAuth.getCurrentUser().getEmail();

        DatabaseReference productRef = database.getReference().child("productimage");

        // Query the database to get products for the current user
        productRef.orderByChild("userEmail").equalTo(userEmail)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        productList.clear();  // Clear existing data
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ProductWithImage product = snapshot.getValue(ProductWithImage.class);
                            Log.d("AshuraDB", "Product: " + product.toString());
                            productList.add(product);
                        }
                        orderAdapter.notifyDataSetChanged();  // Notify adapter that data has changed
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("AshuraDB", error.toString());
                    }
                });
    }

}