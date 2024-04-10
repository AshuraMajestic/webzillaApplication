package com.example.ashishappv2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ashishappv2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class EditProfile extends AppCompatActivity {
    private ImageButton backButton;
    private TextView updateImage;
    private TextInputEditText storeLink,storeName,storeCategory,storeMobileNumber,storeMail;
    private ImageView logo;
    private TextView deleteButton,SaveData;
    Uri selectedImageUri;
    private  FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    private static final int GALLERY_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        backButton=findViewById(R.id.backButton);
        updateImage = findViewById(R.id.updateImage);
        storeLink=findViewById(R.id.storeLink);
        storeName=findViewById(R.id.storeName);
        storeCategory=findViewById(R.id.storeCategory);
        storeMobileNumber = findViewById(R.id.storeMobileNumber);
        storeMail= findViewById(R.id.storeMail);
        deleteButton=findViewById(R.id.deleteAccount);
        SaveData=findViewById(R.id.saveData);
        logo=findViewById(R.id.shopLogo);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String userLink = storeLink.getText().toString().trim();

                        DatabaseReference storageBaseRef = FirebaseDatabase.getInstance().getReference().child("Shops").child(userLink);
                        storageBaseRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // "Shops" node successfully deleted from the Realtime Database

                                // Now, delete data from Firebase Storage
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Shops").child(userLink);
                                storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Data successfully deleted from Firebase Storage
                                        // You may now show a success message or perform any other necessary action
                                        Toast.makeText(EditProfile.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                        // Redirect user to login page or do any other action
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failure to delete data from Firebase Storage
                                        Toast.makeText(EditProfile.this, "Failed to delete account. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure to delete "Shops" node from the Realtime Database
                                Toast.makeText(EditProfile.this, "Failed to delete account. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure to delete data from the Realtime Database
                        Toast.makeText(EditProfile.this, "Failed to delete account. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        SaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = storeName.getText().toString().trim();
                String newMobileNumber = storeMobileNumber.getText().toString().trim();
                String newEmail = storeMail.getText().toString().trim();
                String newLink = storeLink.getText().toString().trim();

                // Validation for name, mobile number, email, and link
                if (newName.isEmpty() || newMobileNumber.isEmpty() || newEmail.isEmpty() || newLink.isEmpty()) {
                    // Handle empty fields
                    // You can show an error message or toast
                    return;
                }

                // Validation for mobile number using regular expression
                if (!isValidMobileNumber(newMobileNumber)) {
                    Toast.makeText(EditProfile.this, "Please Give a suitable Phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validation for email using regular expression
                if (!isValidEmail(newEmail)) {
                    Toast.makeText(EditProfile.this, "Please Give a suitable email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save the image to Firebase Storage
                StorageReference imageRef = storageReference.child("ShopLogo/logo.jpg");
                imageRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the image
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Update data in Firebase Database
                                mDatabase.child("shopname").setValue(newName);
                                mDatabase.child("number").setValue(newMobileNumber);
                                mDatabase.child("email").setValue(newEmail);
                                mDatabase.child("link").setValue(newLink);

                                // Show a success message or toast
                                Toast.makeText(EditProfile.this, "Data saved successfully", Toast.LENGTH_SHORT).show();

                                // Finish the activity
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                    }
                });
            }
        });


        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("UserData").child(user.getUid());


            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = Objects.requireNonNull(dataSnapshot.child("shopname").getValue()).toString();
                        String mobileNumber = Objects.requireNonNull(dataSnapshot.child("number").getValue()).toString();
                        String email = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                        String link = Objects.requireNonNull(dataSnapshot.child("link").getValue()).toString();
                        storageReference = FirebaseStorage.getInstance().getReference().child("Shops").child(link);
                        StorageReference userLogoRef = storageReference.child("ShopLogo").child("logo.jpg");

                        userLogoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            selectedImageUri=uri;
                            Glide.with(getApplicationContext())
                                    .load(uri)
                                    .into(logo);
                        }).addOnFailureListener(e -> {
                            Log.d("AshuraDB", e.toString());
                        });


                        // Set retrieved data into EditTexts
                        storeName.setText(name);
                        storeCategory.setText("General");
                        storeMobileNumber.setText(mobileNumber);
                        storeMail.setText(email);
                        storeLink.setText(link);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            Glide.with(this)
                    .load(selectedImageUri)
                    .into(logo);
        }
    }
    // Regular expression for mobile number validation
    private boolean isValidMobileNumber(String mobileNumber) {
        // Regex for a valid mobile number (India)
        String regex = "^[6-9]\\d{9}$";
        return mobileNumber.matches(regex);
    }

    // Regular expression for email validation
    private boolean isValidEmail(String email) {
        // Regex for a valid email address
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regex);
    }

}