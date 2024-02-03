package com.example.ashishappv2.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashishappv2.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    TextInputLayout mail, pass;
    Button loggo;
    TextView register;
    FirebaseAuth auth;
    DatabaseReference database;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        register = view.findViewById(R.id.registernow);
        mail = view.findViewById(R.id.mailLogin);
        pass=view.findViewById(R.id.passLogin);
        loggo= view.findViewById(R.id.logBtn);
        register.setOnClickListener(v->{
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new PlusFragment());
            transaction.commit();
        });
        loggo.setOnClickListener(v->{
            String email = Objects.requireNonNull(mail.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(pass.getEditText()).getText().toString().trim();
            if ( email.isEmpty() || password.isEmpty()) {
                makeToast("Please fill in all fields");
                return;
            }
            else{
                siginUser(email,password);
            }
        });
    }

    private void siginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                makeToast("Log in successful");
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,new HomeFragment());
                transaction.commit();
            }
            else{
                makeToast("User not found");
                return;
            }
        });
    }
    private void onBackPressed(){
        FragmentManager fm=getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }
    private void makeLog(String string) {
        Log.d("AshuraDB",string);
    }

    private void makeToast(String s){
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }


}