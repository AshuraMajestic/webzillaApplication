package com.example.ashishappv2.Fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashishappv2.Domains.userData;
import com.example.ashishappv2.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class PlusFragment extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference database;
    TextInputLayout  shopname, mail, phonenumber,pass;
    Button register;
    TextView loggo;

    public PlusFragment() {
        // Required empty public constructor
    }

    public static PlusFragment newInstance() {
        PlusFragment plusFragment = new PlusFragment();
        return plusFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_plus, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        register = view.findViewById(R.id.button2);
        shopname = view.findViewById(R.id.shopNameRegister);
        mail = view.findViewById(R.id.mailRegister);
        pass=view.findViewById(R.id.passRegister);
        phonenumber = view.findViewById(R.id.numberRegister);
        loggo= view.findViewById(R.id.loggo);
        loggo.setOnClickListener(v->{
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout,new LoginFragment());
            transaction.commit();
        });
        register.setOnClickListener(v -> {
            String shopName = Objects.requireNonNull(shopname.getEditText()).getText().toString();
            String email = Objects.requireNonNull(mail.getEditText()).getText().toString().trim();
            String phoneNumber = Objects.requireNonNull(phonenumber.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(pass.getEditText()).getText().toString();
            String link = shopName.toLowerCase().replaceAll("\\s", "");

            if (shopName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
                makeToast("Please fill in all fields");
                return;
            }
            if (!isValidEmail(email)) {
                makeToast("Please enter a valid email address");
                return;
            }
            if (!isValidPhoneNumber(phoneNumber)) {
                makeToast("Please enter a valid phone number");
                return;
            }
            if (!isValidPassword(password)) {
                makeToast("Please enter a valid password (minimum 6 characters)");
                return;
            }

            userData user = new userData(email, password, shopName, phoneNumber, link, false);
            createAccount(user);
        });
    }
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Validate phone number format
    private boolean isValidPhoneNumber(String phoneNumber) {
        return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
    }

    // Validate password length
    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }
    private void createAccount(userData user) {
        auth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(task->{
            if(task.isSuccessful()){
                makeToast("Account Created Successfully");
                saveUserData(user);
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,new LoginFragment());
                transaction.commit();
            }
            else{
                makeToast("Account not created");
                makeLog(task.getException().toString());
            }
        });
    }

    private void saveUserData(userData user) {
        if(auth.getCurrentUser()!=null){
            String user1 = auth.getCurrentUser().getUid();
            database.child("UserData").child(user1).setValue(user);
        }
        else{
            makeLog("user is null");
        }

    }

    private void makeLog(String string) {
        Log.d("AshuraDB",string);
    }

    private void makeToast(String s){
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
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