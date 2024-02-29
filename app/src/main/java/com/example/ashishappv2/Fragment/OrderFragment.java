package com.example.ashishappv2.Fragment;

import android.content.Intent;
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

import android.widget.Toast;

import com.example.ashishappv2.Adapter.OrderAdapter;
import com.example.ashishappv2.Domains.OrderDetailActivity;
import com.example.ashishappv2.Domains.OrderList;

import com.example.ashishappv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderFragment extends Fragment implements OrderAdapter.OrderClickListener {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<OrderList> orderList;
    private FirebaseAuth mAuth;
    String ShopName;
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
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(getContext(), orderList, this);
        recyclerView.setAdapter(orderAdapter);
        getShopName();
        return view;
    }

    private void getShopName() {
        String userId = mAuth.getCurrentUser().getUid();

        DatabaseReference userRef = database.getReference("UserData").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ShopName = dataSnapshot.child("link").getValue(String.class);
                    if (ShopName != null) {
                        ShopName = ShopName.trim().toLowerCase();
                        fetchUserData();
                    } else {
                        showToast("Shop name is null");
                    }
                } else {
                    showToast("User data not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Error retrieving user data");
            }
        });
    }

    private void fetchUserData() {
        DatabaseReference productRef = database.getReference().child("Shops").child(ShopName).child("Orders");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();  // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrderList product = snapshot.getValue(OrderList.class);
                    if (product != null) {
                        orderList.add(product);
                    }
                }
                Collections.reverse(orderList);
                orderAdapter.notifyDataSetChanged();  // Notify adapter that data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AshuraDB", error.toString());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setUpOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
    }

    @Override
    public void onOrderClick(OrderList order) {
        // Start new activity and pass order number

        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra("ORDER_NUMBER", order.getOrderNumber()+"");
        startActivity(intent);
    }
}
