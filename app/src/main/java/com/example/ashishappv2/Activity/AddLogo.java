package com.example.ashishappv2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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
    private AppCompatButton selectLogo, selectBanner, updateButton;
    private ImageView logoImageView, bannerImageView;
    private String ShopName;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_logo);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        getShopName();

        selectLogo = findViewById(R.id.selectlogo);
        selectBanner = findViewById(R.id.selectBanner);
        updateButton = findViewById(R.id.updateData);
        logoImageView = findViewById(R.id.logo);
        bannerImageView = findViewById(R.id.bannerImage);

        selectLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(PICK_IMAGE_REQUEST);
            }
        });

        selectBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(PICK_IMAGE_REQUEST + 1);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadLogoAndBanner();
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
                    ShopName = dataSnapshot.child("link").getValue(String.class).trim().toLowerCase();
                    storageReference = storage.getReference().child("Shops").child(ShopName).child("ShopLogo");
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

    private void uploadLogoAndBanner() {
        if (ShopName == null || ShopName.isEmpty()) {
            showToast("ShopName is null or empty");
            return;
        }
        if (logoImageView.getDrawable() == null || bannerImageView.getDrawable() == null) {
            showToast("Please select both logo and banner");
            return;
        }

        // Convert logo image to byte array
        logoImageView.setDrawingCacheEnabled(true);
        logoImageView.buildDrawingCache();
        Bitmap logoBitmap = logoImageView.getDrawingCache();
        ByteArrayOutputStream logoBaos = new ByteArrayOutputStream();
        logoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, logoBaos);
        byte[] logoData = logoBaos.toByteArray();

        // Convert banner image to byte array and resize
        bannerImageView.setDrawingCacheEnabled(true);
        bannerImageView.buildDrawingCache();
        Bitmap bannerBitmap = bannerImageView.getDrawingCache();

        // Resize the banner image
        bannerBitmap = Bitmap.createScaledBitmap(bannerBitmap, 1440, 627, true);

        ByteArrayOutputStream bannerBaos = new ByteArrayOutputStream();
        bannerBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bannerBaos);
        byte[] bannerData = bannerBaos.toByteArray();

        // Get current user's ID
        String email = mAuth.getCurrentUser().getEmail();

        // Check if the shop already has a logo and banner in the database
        DatabaseReference shopRef = database.getReference("Shops").child(ShopName);
        shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Shop already has a logo and banner, update the existing entry
                    updateExistingLogoAndBanner(email, logoData, bannerData);
                } else {
                    // Shop doesn't have a logo and banner, create a new entry
                    createNewLogoAndBannerEntry(email, logoData, bannerData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Error checking shop data");
            }
        });
    }


    private void updateExistingLogoAndBanner(String email, byte[] logoData, byte[] bannerData) {
        if (ShopName == null || ShopName.isEmpty()) {
            showToast("ShopName is null or empty");
            return;
        }
        // Upload the new logo image to Firebase Storage under the shop's name folder
        StorageReference logoFolderRef = storageReference.child("logo.jpg");
        UploadTask logoUploadTask = logoFolderRef.putBytes(logoData);

        // Handle the upload success and failure for logo
        logoUploadTask.addOnSuccessListener(logoTaskSnapshot -> {
            // Get the download URL of the uploaded logo image
            logoFolderRef.getDownloadUrl().addOnSuccessListener(logoUri -> {
                // Upload the new banner image to Firebase Storage under the shop's name folder
                StorageReference bannerFolderRef = storageReference.child("banner.jpg");
                UploadTask bannerUploadTask = bannerFolderRef.putBytes(bannerData);

                // Handle the upload success and failure for banner
                bannerUploadTask.addOnSuccessListener(bannerTaskSnapshot -> {
                    // Get the download URL of the uploaded banner image
                    bannerFolderRef.getDownloadUrl().addOnSuccessListener(bannerUri -> {
                        // Update the existing entry with the new logo and banner image URLs
                        DatabaseReference shopRef = database.getReference("Shops").child(ShopName).child("ShopLogo");
                        Map<String, Object> logoBannerData = new HashMap<>();
                        logoBannerData.put("logoUrl", logoUri.toString());
                        logoBannerData.put("bannerUrl", bannerUri.toString());
                        logoBannerData.put("email",email);
                        shopRef.updateChildren(logoBannerData);

                        showToast("Logo and banner updated successfully");
                        // Redirect to the main activity or perform other actions as needed
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }).addOnFailureListener(e -> showToast("Error uploading banner"));
            });
        }).addOnFailureListener(e -> showToast("Error uploading logo"));
    }

    private void createNewLogoAndBannerEntry(String email, byte[] logoData, byte[] bannerData) {
        if (ShopName == null || ShopName.isEmpty()) {
            showToast("ShopName is null or empty");
            return;
        }
        // Upload the new logo image to Firebase Storage under the shop's name folder
        StorageReference logoFolderRef = storageReference.child("logo.jpg");
        UploadTask logoUploadTask = logoFolderRef.putBytes(logoData);

        // Handle the upload success and failure for logo
        logoUploadTask.addOnSuccessListener(logoTaskSnapshot -> {
            // Get the download URL of the uploaded logo image
            logoFolderRef.getDownloadUrl().addOnSuccessListener(logoUri -> {
                // Upload the new banner image to Firebase Storage under the shop's name folder
                StorageReference bannerFolderRef = storageReference.child("banner.jpg");
                UploadTask bannerUploadTask = bannerFolderRef.putBytes(bannerData);

                // Handle the upload success and failure for banner
                bannerUploadTask.addOnSuccessListener(bannerTaskSnapshot -> {
                    // Get the download URL of the uploaded banner image
                    bannerFolderRef.getDownloadUrl().addOnSuccessListener(bannerUri -> {
                        // Create a new entry in the database with the logo and banner image URLs
                        DatabaseReference shopRef = database.getReference("Shops").child(ShopName).child("ShopLogo");
                        Map<String, Object> logoBannerData = new HashMap<>();
                        logoBannerData.put("logoUrl", logoUri.toString());
                        logoBannerData.put("bannerUrl", bannerUri.toString());
                        logoBannerData.put("email",email);
                        shopRef.setValue(logoBannerData);

                        showToast("Logo and banner uploaded successfully");
                        // Redirect to the main activity or perform other actions as needed
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }).addOnFailureListener(e -> showToast("Error uploading banner"));
            });
        }).addOnFailureListener(e -> showToast("Error uploading logo"));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                if (requestCode == PICK_IMAGE_REQUEST) {
                    logoImageView.setImageBitmap(bitmap);
                } else if (requestCode == PICK_IMAGE_REQUEST + 1) {
                    bannerImageView.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {
        // Handle the back button press
        super.onBackPressed();

        // Navigate back to the previous activity (MainActivity)
        Intent intent = new Intent(AddLogo.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
