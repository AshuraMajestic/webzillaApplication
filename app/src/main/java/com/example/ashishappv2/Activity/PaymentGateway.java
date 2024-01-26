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

public class PaymentGateway extends AppCompatActivity {
    private AppCompatRadioButton upiRadioButton;
    private AppCompatRadioButton bankDetailsRadioButton;

    private ImageView image;
    private EditText upiIdEditText;
    private AppCompatButton verifyUpiButton;
    private CheckBox checkBox;
    private TextView terms;

    private EditText bankAccountNumberEditText;
    private EditText ifscCodeEditText;
    private EditText userNameEditText;
    private AppCompatButton submitBankDetailsButton;

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
                        saveUpiIdToFirebase(upiId);
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
                    // Check for existing UPI ID and bank details only when the bank details button is clicked
                    checkExistingBankDetails();
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
                // Handle click on UPI radio button
                showUpiViews();
            }
        });

        bankDetailsRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle click on Bank Details radio button
                showBankDetailsViews();
            }
        });
    }

    private void checkExistingBankDetails() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference upiRef = FirebaseDatabase.getInstance()
                    .getReference("Payment method/Upi/" + userId);

            DatabaseReference bankDetailsRef = FirebaseDatabase.getInstance()
                    .getReference("Payment method/BankDetails/" + userId);

            bankDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        showToast("You cannot have mulitple bank accounts");
                        openMainActivityAndReplaceProfileFragment();
                    } else {
                        // Proceed to save bank details
                        saveBankDetails();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast("Failed to check bank details. Please try again.");
                }
            });
        }
    }

    private void saveUpiIdToFirebase(final String upiId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            final String userId = currentUser.getUid();
            final String userEmail = currentUser.getEmail();

            final DatabaseReference userUpiRef = FirebaseDatabase.getInstance()
                    .getReference("Payment method/Upi/" + userId);

            userUpiRef.child("upiId").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getValue(String.class).equals(upiId)) {
                        showToast("You cannot have multiple upi");
                        openMainActivityAndReplaceProfileFragment();
                    } else {
                        saveUpiId(userUpiRef, upiId, userEmail);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast("Failed to check UPI ID. Please try again.");
                }
            });
        }
    }

    private void saveUpiId(final DatabaseReference userUpiRef, final String upiId, final String userEmail) {
        userUpiRef.child("upiId").setValue(upiId);
        userUpiRef.child("email").setValue(userEmail)
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

    private void saveBankDetails() {
        String accountNumber = bankAccountNumberEditText.getText().toString();
        String ifscCode = ifscCodeEditText.getText().toString();
        String userName = userNameEditText.getText().toString();

        saveBankDetailsToFirebase(accountNumber, ifscCode, userName);
    }

    private void saveBankDetailsToFirebase(String accountNumber, String ifscCode, String userName) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String userEmail = currentUser.getEmail();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference("Payment method/BankDetails/" + userId);

            databaseReference.orderByChild("accountNumber").equalTo(accountNumber)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                showToast("Bank details with this account number already exist.");
                            } else {
                                saveBankDetails(accountNumber, ifscCode, userName, userEmail);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            showToast("Error checking account number existence.");
                        }
                    });
        }
    }

    private void saveBankDetails(String accountNumber, String ifscCode, String userName, String userEmail) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userBankDetailsRef = FirebaseDatabase.getInstance()
                    .getReference("Payment method/BankDetails/" + userId);

            String key = userBankDetailsRef.push().getKey();

            userBankDetailsRef.child(key).child("accountNumber").setValue(accountNumber);
            userBankDetailsRef.child(key).child("ifscCode").setValue(ifscCode);
            userBankDetailsRef.child(key).child("userName").setValue(userName);
            userBankDetailsRef.child(key).child("email").setValue(userEmail)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast("Bank details saved successfully.");
                            openMainActivityAndReplaceProfileFragment();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("Failed to save bank details. Please try again.");
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
