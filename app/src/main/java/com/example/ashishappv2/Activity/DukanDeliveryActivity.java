package com.example.ashishappv2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashishappv2.Domains.DeliveryData;
import com.example.ashishappv2.Domains.userData;
import com.example.ashishappv2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DukanDeliveryActivity extends AppCompatActivity {

    private String[] items={"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa",
            "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala",
            "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland",
            "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
            "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands",
            "Chandigarh", "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Lakshadweep",
            "Puducherry", "Jammu and Kashmir", "Ladakh"};
    private AutoCompleteTextView state;
    ArrayAdapter<String> arrayAdapter;
    private EditText warehouseNameEditText;
    private EditText contactNameEditText;
    private EditText mobileNumberEditText;
    private EditText flatNumberEditText;
    private EditText areaEditText;
    private EditText pinCodeEditText;
    private EditText cityEditText;
    private EditText gstNumberEditText;
    private CheckBox acceptTermsCheckBox;
    private AppCompatButton update;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String ShopName,selectedState;
    private DatabaseReference userRef;
    TextView terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dukan_delivery);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        getshopName();
        state=findViewById(R.id.state);
        arrayAdapter=new ArrayAdapter<String>(this,R.layout.list_item,items);
        state.setAdapter(arrayAdapter);
        warehouseNameEditText = findViewById(R.id.warehouseName);
        contactNameEditText = findViewById(R.id.contactName);
        mobileNumberEditText = findViewById(R.id.mobileNumber);
        flatNumberEditText = findViewById(R.id.flatNumber);
        areaEditText = findViewById(R.id.Area);
        pinCodeEditText = findViewById(R.id.pinCode);
        cityEditText = findViewById(R.id.City);
        gstNumberEditText = findViewById(R.id.gstNumber);
        acceptTermsCheckBox = findViewById(R.id.checkBoxAcceptTerms);
        update=findViewById(R.id.updateData);
        terms=findViewById(R.id.terms);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTermsDialog();
            }
        });

        state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedState = (String) parent.getItemAtPosition(position);

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

    }

    private void updateData() {
        String warehouseName = warehouseNameEditText.getText().toString().trim();
        String contactName = contactNameEditText.getText().toString().trim();
        String mobileNumber = mobileNumberEditText.getText().toString().trim();
        String flatNumber = flatNumberEditText.getText().toString().trim();
        String area = areaEditText.getText().toString().trim();
        String pinCode = pinCodeEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String selectedState = state.getText().toString().trim();
        String gstNumber = gstNumberEditText.getText().toString().trim();
        if (!acceptTermsCheckBox.isChecked()) {
            Toast.makeText(this, "Please accept the terms & conditions", Toast.LENGTH_SHORT).show();
            return;
        }
        if(area.isEmpty()){
            area="";
        }
        if (warehouseName.isEmpty() || contactName.isEmpty() || mobileNumber.isEmpty() ||
                flatNumber.isEmpty()  || pinCode.isEmpty() || city.isEmpty() ||
                gstNumber.isEmpty() || selectedState.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        } else {
            DatabaseReference deliveryRef = FirebaseDatabase.getInstance()
                    .getReference("Shops")
                    .child(ShopName)
                    .child("delivery");

            // Create a data object to upload
            DeliveryData deliveryData = new DeliveryData(
                    warehouseNameEditText.getText().toString(),
                    contactNameEditText.getText().toString(),
                    mobileNumberEditText.getText().toString(),
                    flatNumberEditText.getText().toString(),
                    areaEditText.getText().toString(),
                    pinCodeEditText.getText().toString(),
                    cityEditText.getText().toString(),
                    selectedState,
                    gstNumberEditText.getText().toString()
            );

            // Upload data
            deliveryRef.setValue(deliveryData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DukanDeliveryActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            makeLog(e+"");
                        }
                    });
        }
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

    private void getshopName() {
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("UserData").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userData user = dataSnapshot.getValue(userData.class);
                    if (user != null) {
                        ShopName = user.getLink();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving data: " + databaseError.getMessage());
            }
        });
    }

    private void makeLog(String s) {
        Log.d("AshuraDB",s);
    }
}