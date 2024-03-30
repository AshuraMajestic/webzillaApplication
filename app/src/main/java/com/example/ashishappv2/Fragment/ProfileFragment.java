package com.example.ashishappv2.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ashishappv2.Activity.MainActivity;
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

import java.util.Objects;

public class ProfileFragment extends Fragment {

    ImageView logo;
    TextView shopName,username;
    LinearLayout Payment,security,notification,support,logout;

    public ProfileFragment() {
        // Required empty public constructor
    }
public static ProfileFragment newInstance() {
    ProfileFragment profileFragment = new ProfileFragment();
    return profileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //Page Elements
        logo = view.findViewById(R.id.shopLogo);
        shopName = view.findViewById(R.id.shopName);
        username=view.findViewById(R.id.userName);
        security=view.findViewById(R.id.privacyandSecurityLayout);
        notification=view.findViewById(R.id.notificationLayout);
        support=view.findViewById(R.id.supportLayout);
        logout=view.findViewById(R.id.logoutLayout);
//        Payment = view.findViewById(R.id.payment);


        //Firebase Elements
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("UserData").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userData user = dataSnapshot.getValue(userData.class);
                    if (user != null) {
                        // Get the link dynamically from the user data
                        String link = user.getLink();
                        String shopname=user.getShopname();
                        String userName= user.getUsername();
                        shopName.setText(shopname);
                        username.setText("@"+userName);
                        // Now use the dynamically retrieved link to fetch the logo
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Shops").child(link);
                        StorageReference userLogoRef = storageReference.child("ShopLogo").child("logo.jpg");

                        userLogoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Glide.with(requireContext())
                                    .load(uri)
                                    .into(logo);
                        }).addOnFailureListener(e -> {
                            Log.d("AshuraDB", e.toString());
                        });

                        retrieveShopName();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving data: " + databaseError.getMessage());
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                // Redirect the user to the login screen or any other desired destination
                // For example:
                Intent intent = new Intent(requireContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                requireActivity().finish(); // Close the current activity
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Payment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Payment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        try {
//                            Intent intent = new Intent(getContext(), PaymentGateway.class);
//                            if (getContext() != null) {
//                                startActivity(intent);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.e("PaymentClick", "Error in onClick: " + e.getMessage());
//                        }
//                    }
//                });
//            }
//        });
    }

    private void retrieveShopName() {
        // Get the current user's ID
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

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