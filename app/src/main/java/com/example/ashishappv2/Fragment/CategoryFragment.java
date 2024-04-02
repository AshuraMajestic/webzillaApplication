package com.example.ashishappv2.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ashishappv2.Activity.Add_Category;
import com.example.ashishappv2.Adapter.CategoryAdapter;
import com.example.ashishappv2.Domains.CategoryInventory;
import com.example.ashishappv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements  CategoryAdapter.OnSwitchChangeListener2{

    private RecyclerView recyclerView;
    private AppCompatButton button;
    private CategoryAdapter categoryAdapter;
    private List<CategoryInventory> CategoryList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String shopName;
    public CategoryFragment() {
        // Required empty public constructor
    }
    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_category, container, false);;
        mAuth = FirebaseAuth.getInstance();
        button=view.findViewById(R.id.buttonAddCategory);
        database = FirebaseDatabase.getInstance();
        recyclerView = view.findViewById(R.id.recyclerViewCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(CategoryList, getContext(),this);
        recyclerView.setAdapter(categoryAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), Add_Category.class);
                startActivity(intent);
            }
        });
        getShopName(); // Initiating the data retrieval
        return view;

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
    private void fetchUserData() {
        DatabaseReference CategoryRef = database.getReference().child("Shops").child(shopName).child("Category");
        CategoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CategoryList.clear();  // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CategoryInventory category = snapshot.getValue(CategoryInventory.class);
                    if (category != null) {
                        CategoryList.add(category);
                    }
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AshuraDB", error.toString());
            }
        });
    }
    private void showToast(String shopNameIsNull) {
        Toast.makeText(getContext(), shopNameIsNull, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSwitchChanged(String categoryName, boolean isChecked) {
        DatabaseReference categoryRef = database.getReference().child("Shops").child(shopName).child("Category");

        categoryRef.orderByChild("name").equalTo(categoryName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String categoryId = snapshot.getKey();
                        categoryRef.child(categoryId).child("active").setValue(isChecked);
                    }
                } else {
                    showToast("Category not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AshuraDB", error.toString());
            }
        });
    }

}
