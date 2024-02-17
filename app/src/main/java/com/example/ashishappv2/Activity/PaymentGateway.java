package com.example.ashishappv2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.example.ashishappv2.Domains.BankDetails;
import com.example.ashishappv2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class PaymentGateway extends AppCompatActivity {
    private AppCompatRadioButton upiRadioButton;
    private AppCompatRadioButton bankDetailsRadioButton;

    private ImageView image;
    private EditText upiIdEditText;
    private AppCompatButton verifyUpiButton;
    private CheckBox checkBox;
    private TextView terms;
    private String ShopName;
    private EditText bankAccountNumberEditText;
    private EditText ifscCodeEditText;
    private EditText userNameEditText;
    private AppCompatButton submitBankDetailsButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private TextInputLayout upiIdTextInputLayout;
    private TextInputLayout bankAccountNumberTextInputLayout;
    private TextInputLayout ifscCodeTextInputLayout;
    private TextInputLayout userNameTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        upiRadioButton = findViewById(R.id.upiRadioButton);
        bankDetailsRadioButton = findViewById(R.id.bankDetailsRadioButton);

        upiIdEditText = findViewById(R.id.upiIdEditText);
        image = findViewById(R.id.image);
        verifyUpiButton = findViewById(R.id.verifyUpiButton);

        bankAccountNumberEditText = findViewById(R.id.bankAccountNumberEditText);
        ifscCodeEditText = findViewById(R.id.ifscCodeEditText);
        userNameEditText = findViewById(R.id.userNameEditText);
        submitBankDetailsButton = findViewById(R.id.submitBankDetailsButton);

        upiIdTextInputLayout = findViewById(R.id.upiIdTextInputLayout);
        bankAccountNumberTextInputLayout = findViewById(R.id.bankAccountNumberTextInputLayout);
        ifscCodeTextInputLayout = findViewById(R.id.ifscCodeTextInputLayout);
        userNameTextInputLayout = findViewById(R.id.userNameTextInputLayout);
        checkBox = findViewById(R.id.checkBox);
        terms = findViewById(R.id.terms);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        getShopName();

        // Set click listeners
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTermsDialog();
            }
        });

        verifyUpiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    if (upiIdEditText.getText().toString().trim().isEmpty()) {
                        showToast("Enter UPI ID");
                    } else {
                        String upiId = upiIdEditText.getText().toString().trim();
                        saveUpiId(upiId);
                    }
                } else {
                    showToast("Please agree to the Terms & Conditions");
                }
            }
        });

        submitBankDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bankAccountNumberEditText.getText().toString().trim().isEmpty() ||
                        ifscCodeEditText.getText().toString().trim().isEmpty() ||
                        userNameEditText.getText().toString().isEmpty()) {
                    showToast("Fill all details");
                } else {
                   saveBankDetails();
                }
            }
        });
        if (upiRadioButton.isChecked()) {
            showUpiViews();
        } else {
            showBankDetailsViews();
        }
        upiRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpiViews();
            }
        });

        bankDetailsRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBankDetailsViews();
            }
        });
    }

    private void saveBankDetails() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {


            DatabaseReference bankDetailRef = FirebaseDatabase.getInstance()
                    .getReference("Shops").child(ShopName)
                    .child("Payment method/BankAccount");

            bankDetailRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Bank details already exist
                        showToast("Bank details already exist.");
                        openMainActivityAndReplaceProfileFragment();
                    } else {
                        // Bank details do not exist, save the provided one
                        saveNewBankDetail(bankDetailRef);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast("Failed to check existing UPI ID. Please try again.");
                }
            });
        }
    }

    private void saveNewBankDetail(DatabaseReference bankDetailRef) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String accountNumber=bankAccountNumberEditText.getText().toString().trim();
                    String ifscCode=ifscCodeEditText.getText().toString().trim();
                    String userName=userNameEditText.getText().toString();
            bankDetailRef.child("accountNumber").setValue(accountNumber);
            bankDetailRef.child("ifscCode").setValue(ifscCode);
            bankDetailRef.child("userName").setValue(userName)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast("Bank details saved successfully: " );
                            openMainActivityAndReplaceProfileFragment();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("Failed to save UPI ID. Please try again.");
                        }
                    });
        }

    }

    private void getShopName() {
        String userId = mAuth.getCurrentUser().getUid();

        DatabaseReference userRef = database.getReference("UserData").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ShopName = dataSnapshot.child("link").getValue(String.class).trim().toLowerCase();
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

    private void saveUpiId(String upiId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {


            DatabaseReference userUpiRef = FirebaseDatabase.getInstance()
                    .getReference("Shops").child(ShopName)
                    .child("Payment method/Upi");

            userUpiRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // UPI ID already exists
                        showToast("UPI ID already exists.");
                        openMainActivityAndReplaceProfileFragment();
                    } else {
                        // UPI ID does not exist, save the provided one
                        saveNewUpiId(userUpiRef, upiId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast("Failed to check existing UPI ID. Please try again.");
                }
            });
        }
    }

    private void saveNewUpiId(DatabaseReference userUpiRef, String upiId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {

            userUpiRef.child("upiId").setValue(upiId)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast("UPI ID saved successfully: " + upiId);
                            openMainActivityAndReplaceProfileFragment();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("Failed to save UPI ID. Please try again.");
                        }
                    });
        }
    }


    private void openMainActivityAndReplaceProfileFragment() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showTermsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.termsdialog, null);
        builder.setView(dialogView);

        TextView dialogContent = dialogView.findViewById(R.id.termsTextView);
        Button dialogCloseButton = dialogView.findViewById(R.id.closeButton);

        dialogContent.setText(getString(R.string.termsandcondition));

        AlertDialog dialog = builder.create();

        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showUpiViews() {
        upiIdTextInputLayout.setVisibility(View.VISIBLE);
        image.setVisibility(View.VISIBLE);
        verifyUpiButton.setVisibility(View.VISIBLE);
        checkBox.setVisibility(View.VISIBLE);
        terms.setVisibility(View.VISIBLE);
        bankAccountNumberTextInputLayout.setVisibility(View.GONE);
        ifscCodeTextInputLayout.setVisibility(View.GONE);
        userNameTextInputLayout.setVisibility(View.GONE);
        submitBankDetailsButton.setVisibility(View.GONE);
    }

    private void showBankDetailsViews() {
        upiIdTextInputLayout.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
        verifyUpiButton.setVisibility(View.GONE);
        checkBox.setVisibility(View.GONE);
        terms.setVisibility(View.GONE);

        bankAccountNumberTextInputLayout.setVisibility(View.VISIBLE);
        ifscCodeTextInputLayout.setVisibility(View.VISIBLE);
        userNameTextInputLayout.setVisibility(View.VISIBLE);
        submitBankDetailsButton.setVisibility(View.VISIBLE);
    }
}
