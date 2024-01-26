package com.example.ashishappv2.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ashishappv2.Activity.PaymentGateway;
import com.example.ashishappv2.Domains.userData;
import com.example.ashishappv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    ImageView logo;
    TextView shopName,editBusiness;
    LinearLayout accountDetail,storeSetting,youOwnApp,forPc,Payment;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private StorageReference storageReference;
    public ProfileFragment() {
        // Required empty public constructor
    }
public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        //Page Elements
        logo=view.findViewById(R.id.showLogo);
        shopName=view.findViewById(R.id.ShopName);
        editBusiness=view.findViewById(R.id.EditBusiness);
        accountDetail=view.findViewById(R.id.accountDetails);
        storeSetting=view.findViewById(R.id.StoreSettings);
        youOwnApp=view.findViewById(R.id.yourown);
        forPc=view.findViewById(R.id.pc);
        Payment=view.findViewById(R.id.payment);

        Payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), PaymentGateway.class);
                startActivity(intent);
            }
        });
        //Firebase Elements
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("ShopLogo");
        String userId = mAuth.getCurrentUser().getUid();

        StorageReference userLogoRef = storageReference.child(userId).child("logo.jpg");
        userLogoRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(requireContext())
                    .load(uri)
                    .into(logo);
        }).addOnFailureListener(e -> {
            Log.d("AshuraDB",e.toString());
        });
        retrieveShopName();
        return view;
    }
    private void retrieveShopName() {
        // Get the current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Reference to the user's data in the database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserData").child(userId);

        // Retrieve shop name from the database
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userData user = dataSnapshot.getValue(userData.class);
                    if (user != null) {
                        String ShopName = user.getShopname();
                        // Set shop name in the TextView
                        shopName.setText(ShopName);
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

    private void setUpOnBackPressed(){
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(isEnabled()){
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
    }
}