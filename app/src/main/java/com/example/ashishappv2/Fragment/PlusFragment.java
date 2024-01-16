package com.example.ashishappv2.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ashishappv2.Domains.userData;
import com.example.ashishappv2.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class PlusFragment extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference database;
    TextInputLayout username, shopname, address, mail, phonenumber,pass;
    Button register,loggo;

    public PlusFragment() {
        // Required empty public constructor
    }

    public static PlusFragment newInstance() {
        PlusFragment fragment = new PlusFragment();
        return fragment;
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
        username = view.findViewById(R.id.userRegister);
        shopname = view.findViewById(R.id.shopNameRegister);
        address = view.findViewById(R.id.addressRegister);
        mail = view.findViewById(R.id.mailRegister);
        pass=view.findViewById(R.id.passRegister);
        phonenumber = view.findViewById(R.id.numberRegister);
        loggo= view.findViewById(R.id.logBtn);
        loggo.setOnClickListener(v->{
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame,new LoginFragment());
            transaction.commit();
        });
        register.setOnClickListener(v->{
            String userName = Objects.requireNonNull(username.getEditText()).getText().toString().trim();
            String shopName = Objects.requireNonNull(shopname.getEditText()).getText().toString();
            String addressText = Objects.requireNonNull(address.getEditText()).getText().toString();
            String email = Objects.requireNonNull(mail.getEditText()).getText().toString().trim();
            String phoneNumber = Objects.requireNonNull(phonenumber.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(pass.getEditText()).getText().toString();
            if (userName.isEmpty() || shopName.isEmpty() || addressText.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
                makeToast("Please fill in all fields");
                return;
            }
            else{
                userData user= new userData(userName,email,password,shopName,addressText,phoneNumber);
                createAccount(user);
            }

        });
    }

    private void createAccount(userData user) {
        auth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(task->{
            if(task.isSuccessful()){
                makeToast("Account Created Successfully");
                saveUserData(user);
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame,new LoginFragment());
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



}