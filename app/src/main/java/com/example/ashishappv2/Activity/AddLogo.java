package com.example.ashishappv2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ashishappv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddLogo extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView logoImageView;
    private Button selectLogoButton;
    private Button changeLogoButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_logo);

        // Initialize views
        logoImageView = findViewById(R.id.imageView2);
        selectLogoButton = findViewById(R.id.button3);
        changeLogoButton = findViewById(R.id.button4);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("ShopLogo");

        // Set click listeners
        selectLogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the gallery to select an image
                openGallery();
            }
        });

        changeLogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadLogo();
            }
        });
    }

    private void uploadLogo() {
        if (logoImageView.getDrawable() == null) {
            showToast("Please select a logo");
            return;
        }

        // Convert the image to a byte array
        logoImageView.setDrawingCacheEnabled(true);
        logoImageView.buildDrawingCache();
        Bitmap bitmap = logoImageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Get the current user's ID and email
        String userId = mAuth.getCurrentUser().getUid();
        String userEmail = mAuth.getCurrentUser().getEmail();

        // Check if the user already has a logo in the database
        DatabaseReference userRef = database.getReference("ShopLogo").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User already has a logo, update the existing entry
                    updateExistingLogo(userId, userEmail, data);
                } else {
                    // User doesn't have a logo, create a new entry
                    createNewLogoEntry(userId, userEmail, data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Error checking user data");
            }
        });
    }

    private void updateExistingLogo(String userId, String userEmail, byte[] data) {
        // Upload the new image to Firebase Storage under the user's ID folder
        StorageReference userFolderRef = storageReference.child(userId).child("logo.jpg");
        UploadTask uploadTask = userFolderRef.putBytes(data);

        // Handle the upload success and failure
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Get the download URL of the uploaded image
            userFolderRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Update the existing entry with the new image URL
                DatabaseReference userRef = database.getReference("ShopLogo").child(userId);
                Map<String, Object> logoData = new HashMap<>();
                logoData.put("imageUrl", uri.toString());
                userRef.updateChildren(logoData);

                showToast("Logo updated successfully");
                // Redirect to the main activity or perform other actions as needed
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            });
        }).addOnFailureListener(e -> showToast("Error updating logo"));
    }

    private void createNewLogoEntry(String userId, String userEmail, byte[] data) {
        // Upload the new image to Firebase Storage under the user's ID folder
        StorageReference userFolderRef = storageReference.child(userId).child("logo.jpg");
        UploadTask uploadTask = userFolderRef.putBytes(data);

        // Handle the upload success and failure
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Get the download URL of the uploaded image
            userFolderRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Create a new entry in the database with the image URL and email
                DatabaseReference userRef = database.getReference("ShopLogo").child(userId);
                Map<String, Object> logoData = new HashMap<>();
                logoData.put("imageUrl", uri.toString());
                logoData.put("email", userEmail);
                userRef.setValue(logoData);

                showToast("Logo uploaded successfully");
                // Redirect to the main activity or perform other actions as needed
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            });
        }).addOnFailureListener(e -> showToast("Error uploading logo"));
    }






    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Get the selected image URI
            Uri selectedImageUri = data.getData();

            try {
                // Convert URI to Bitmap and set it on the ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                logoImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
