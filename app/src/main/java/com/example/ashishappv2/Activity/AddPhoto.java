package com.example.ashishappv2.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ashishappv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class AddPhoto extends AppCompatActivity {
    private ImageView imageView;
    private Button btn;
    private EditText productname,productcateogory,price,pieces;
    private final List<Uri> selectedImageUris = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        productname = findViewById(R.id.editTextText);
        productcateogory = findViewById(R.id.editTextText2);
        price = findViewById(R.id.editTextText4);
        pieces = findViewById(R.id.editTextText5);
        imageView = findViewById(R.id.takeImage);
        imageView.setOnClickListener(this::selectImage);
        btn=findViewById(R.id.button);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("productimage");

        btn.setOnClickListener(v->{
            savetodb();
        });

    }

    private void savetodb() {
        if (productname.getText().toString().isEmpty() || productcateogory.getText().toString().isEmpty() || price.getText().toString().isEmpty() || pieces.getText().toString().isEmpty()) {
            showToast("Please fill in all the fields");
            return;
        }

        String productId = database.getReference().child("productimage").push().getKey();

        // Create a reference to the product in the database
        DatabaseReference productRef = database.getReference().child("productimage").child(productId);
        String userEmail = mAuth.getCurrentUser().getEmail();
        // Upload images to Firebase Storage
        for (int i = 0; i < selectedImageUris.size(); i++) {
            Uri imageUri = selectedImageUris.get(i);
            String imageName = "image_" + i + ".jpg";
            StorageReference imageRef = storageReference.child(productId).child(imageName);

            // Upload image to Firebase Storage
            int finalI = i;
            imageRef.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Get the download URL of the uploaded image
                    imageRef.getDownloadUrl().addOnCompleteListener(uriTask -> {
                        if (uriTask.isSuccessful()) {
                            // Save the image URL to the database
                            productRef.child("images").child("image_" + finalI).setValue(uriTask.getResult().toString());
                        }
                    });
                }
            });
        }

        // Save other product details to the database
        productRef.child("name").setValue(productname.getText().toString());
        productRef.child("category").setValue(productcateogory.getText().toString());
        productRef.child("price").setValue(price.getText().toString());
        productRef.child("pieces").setValue(pieces.getText().toString());
        productRef.child("userEmail").setValue(userEmail);

        showToast("Product added successfully");
        Intent intent = new Intent(AddPhoto.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getData() != null) {
                // Single image selected
                Uri selectedImageUri = data.getData();
                selectedImageUris.add(selectedImageUri);
            } else if (data.getClipData() != null) {
                // Multiple images selected
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri selectedImageUri = clipData.getItemAt(i).getUri();
                    selectedImageUris.add(selectedImageUri);
                }
            }

            updateImageViews();
        }
    }

    private void updateImageViews() {
        // Clear existing images
        imageView.setImageResource(0);

        // Display the first selected image (if any)
        if (!selectedImageUris.isEmpty()) {
            imageView.setImageURI(selectedImageUris.get(0));
        }
    }

    private  void showMessage(String message){
        Log.d("AshuraDB",message);
    }
    private  void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}