package com.example.ashishappv2.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.ashishappv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class AddVideo extends AppCompatActivity {
    private static final int PICK_VIDEO_REQUEST_CODE = 1;
    private Button btn;
    private VideoView videoView;
    private Uri selectedVideoUri;

    private EditText productname, productcategory, price, pieces;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        productname = findViewById(R.id.editTextText);
        productcategory = findViewById(R.id.editTextText2);
        price = findViewById(R.id.editTextText4);
        pieces = findViewById(R.id.editTextText5);
        videoView = findViewById(R.id.videoView);
        imageView = findViewById(R.id.takeVideo);
        btn = findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("productvideo");
        btn.setOnClickListener(view -> saveToFirebase());
        imageView.setOnClickListener(view -> selectVideoFromGallery());
    }

    public void selectVideoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_VIDEO_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedVideoUri = data.getData();
            displaySelectedVideo();
        }
    }

    private void displaySelectedVideo() {
        if (selectedVideoUri != null) {
            videoView.setVideoURI(selectedVideoUri);
            videoView.start();
        }
    }
    @Override
    public void onBackPressed() {
        // Handle the back button press
        super.onBackPressed();

        // Navigate back to the previous activity (MainActivity)
        Intent intent = new Intent(AddVideo.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveToFirebase() {
        if (selectedVideoUri == null) {
            showToast("Please select a video first");
            return;
        }
        if (productname.getText().toString().isEmpty() || productcategory.getText().toString().isEmpty() || price.getText().toString().isEmpty() || pieces.getText().toString().isEmpty()) {
            showToast("Please fill in all the fields");
            return;
        }

        String productId = database.getReference().child("productvideo").push().getKey();
        String userEmail = mAuth.getCurrentUser().getEmail();

        DatabaseReference productRef = database.getReference().child("productvideo").child(productId);

        // Upload video to Firebase Storage
        String videoName = productId; // You can change this to a unique identifier if needed
        StorageReference videoRef = storageReference.child(videoName);

        videoRef.putFile(selectedVideoUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Get the download URL of the uploaded video
                videoRef.getDownloadUrl().addOnCompleteListener(uriTask -> {
                    if (uriTask.isSuccessful()) {
                        // Save the video URL to the database
                        productRef.child("video").setValue(Objects.requireNonNull(uriTask.getResult()).toString());
                    }
                });
                // Save other product details to the database
                productRef.child("name").setValue(productname.getText().toString());
                productRef.child("category").setValue(productcategory.getText().toString());
                productRef.child("price").setValue(price.getText().toString());
                productRef.child("pieces").setValue(pieces.getText().toString());
                productRef.child("userEmail").setValue(userEmail);

                showToast("Product with video added successfully");

                // Optionally, start a new activity (e.g., MainActivity)
                Intent intent = new Intent(AddVideo.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional: finish the current activity
            } else {
                showToast("Failed to upload video to Firebase Storage");
            }
        });
    }


    private  void showMessage(String message){
        Log.d("AshuraDB",message);
    }
    private  void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}