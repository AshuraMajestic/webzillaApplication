package com.example.ashishappv2.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ashishappv2.Activity.AddPhoto;
import com.example.ashishappv2.Adapter.ProductAdapter;
import com.example.ashishappv2.Domains.ProductInventory;
import com.example.ashishappv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyproductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppCompatButton button;
    private ProductAdapter productAdapter;
    private List<ProductInventory> productList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String shopName;

    public MyproductsFragment() {
        // Required empty public constructor
    }

    public static MyproductsFragment newInstance() {
        MyproductsFragment fragment = new MyproductsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myproducts, container, false);
        mAuth = FirebaseAuth.getInstance();
        button=view.findViewById(R.id.button3);
        database = FirebaseDatabase.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, getContext());
        recyclerView.setAdapter(productAdapter);

        getShopName(); // Initiating the data retrieval
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), AddPhoto.class);
                startActivity(intent);
            }
        });
    }

    private void getShopName() {
        String userId = mAuth.getCurrentUser().getUid();

        DatabaseReference userRef = database.getReference("UserData").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    shopName = dataSnapshot.child("link").getValue(String.class);
                    if (shopName != null) {
                        shopName = shopName.trim().toLowerCase();
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

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void fetchUserData() {
        DatabaseReference productRef = database.getReference().child("Shops").child(shopName).child("Products");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();  // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductInventory product = snapshot.getValue(ProductInventory.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                productAdapter.notifyDataSetChanged();  // Notify adapter that data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AshuraDB", error.toString());
            }
        });
    }
}
