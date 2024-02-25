package com.example.ashishappv2.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ashishappv2.Fragment.EmptyFragment;
import com.example.ashishappv2.Fragment.HomeFragment;
import com.example.ashishappv2.Fragment.OrderFragment;
import com.example.ashishappv2.Fragment.PlusFragment;
import com.example.ashishappv2.Fragment.ProfileFragment;
import com.example.ashishappv2.Fragment.SearchFragment;
import com.example.ashishappv2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBar = findViewById(R.id.bottomNavigationView);
        replace(new HomeFragment());

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home) {
                    if(!isUserRegistered()) {
                        replace(new PlusFragment());
                        return false;
                    } else {
                        replace(new HomeFragment());
                        return true;
                    }
                } else if (id == R.id.plus) {
                    if(!isUserRegistered()) {
                        replace(new PlusFragment());
                        return true;
                    } else {
                            showPopUp();
                            return false;
                    }
                } else if (id == R.id.profile) {
                    if(!isUserRegistered()) {
                        Toast.makeText(MainActivity.this, "Please Register First", Toast.LENGTH_SHORT).show();
                        replace(new PlusFragment());
                        return false;
                    } else {
                        replace(new ProfileFragment());
                        return true;
                    }
                }
                else if(id == R.id.Order){
                    if(!isUserRegistered()) {
                        Toast.makeText(MainActivity.this, "Please Register First", Toast.LENGTH_SHORT).show();
                        replace(new PlusFragment());
                        return false;
                    } else {
                        replace(new OrderFragment());
                        return true;
                    }
                }
                else if(id==R.id.empty){
                    replace(new EmptyFragment());
                    return true;
                }
                return false;
            }
        });
    }

    private void showPopUp() {
        final Dialog dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomdialog);
        LinearLayout imageLayout = dialog.findViewById(R.id.layoutImage);
        LinearLayout logoLayout = dialog.findViewById(R.id.layoutLogo);
        LinearLayout inventoryLayout=dialog.findViewById(R.id.layoutInventory);
        ImageView cancelButton=dialog.findViewById(R.id.cancelButton);

        inventoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),InventoryActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });

        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AddPhoto.class);
                startActivity(intent);
                finish();
                dialog.dismiss();


            }
        });
        logoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AddLogo.class);
                startActivity(intent);
                finish();
                dialog.dismiss();


            }
        });



        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private boolean isUserRegistered() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser() != null;
    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
