package com.example.ashishappv2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ashishappv2.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Add_Category extends AppCompatActivity {
    private ImageView imageView;
    private Button btn;
    private TextInputEditText categoryName;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private String ShopName;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        categoryName=findViewById(R.id.categoryName);
        btn=findViewById(R.id.button);
        imageView=findViewById(R.id.takeImage);
        imageView.setOnClickListener(this::selectImage);
        getShopName();
        btn.setOnClickListener(v->{
            savetodb();
        });
    }
    private void getShopName() {
        String userId = mAuth.getCurrentUser().getUid();

        DatabaseReference userRef = database.getReference("UserData").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ShopName = dataSnapshot.child("link").getValue(String.class).trim().toLowerCase();
                    storageReference = storage.getReference().child("Shops").child(ShopName);
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
    public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            updateImageView();
        }
    }

    private void updateImageView() {
        // Clear existing image
        imageView.setImageURI(null);

        // Display the selected image
        imageView.setImageURI(selectedImageUri);
    }
    private void savetodb() {
        if (selectedImageUri == null) {
            showToast("Please select an image");
            return;
        }

        if (categoryName.getText() == null ) {
            showToast("Please fill in all the fields");
            return;
        }
        String CategoryName = categoryName.getText().toString().trim().toLowerCase();
        // Create a reference to the product in the database
        DatabaseReference shopRef = database.getReference().child("Shops").child(ShopName);
        StorageReference imageRef = storageReference.child("Category").child(CategoryName).child("image.jpg");
        DatabaseReference CategoryRef = shopRef.child("Category").child(CategoryName);


        imageRef.putFile(selectedImageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Get the download URL of the uploaded image
                imageRef.getDownloadUrl().addOnCompleteListener(uriTask -> {
                    if (uriTask.isSuccessful()) {
                        // Save the image URL to the database
                        String imageUrl = uriTask.getResult().toString();
                        CategoryRef.child("image").setValue(imageUrl);
                        CategoryRef.child("name").setValue(categoryName.getText().toString());
                        boolean b=true;
                        CategoryRef.child("active").setValue(b);
                        DatabaseReference productsRef = shopRef.child("Products");

                        productsRef.orderByChild("category").equalTo(CategoryName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                makeLog(dataSnapshot+"");
                                int collegeProductCount = (int) dataSnapshot.getChildrenCount();
                                CategoryRef.child("itemCount").setValue(collegeProductCount);
                                showToast("Category added successfully");
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                showToast("Error counting college products");
                            }
                        });
                        // Display success message
                        showToast("Image uploaded and product added successfully");
                        finish();
                    }
                });
            } else {
                showToast("Failed to upload image");
            }
        });
        // Save other product details to the database

        showToast("Cateogry added successfully");
        finish();
    }

    private void makeLog(String categoryName) {
        Log.d("AshuraDB", categoryName);
    }

    private void showToast(String userDataNotFound) {
        Toast.makeText(this, userDataNotFound, Toast.LENGTH_SHORT).show();
    }
    }