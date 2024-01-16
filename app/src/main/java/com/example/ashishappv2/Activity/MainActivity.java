package com.example.ashishappv2.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ashishappv2.Fragment.HomeFragment;
import com.example.ashishappv2.Fragment.PlusFragment;
import com.example.ashishappv2.Fragment.ProfileFragment;
import com.example.ashishappv2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomBar;
    private PopupWindow popupWindow;

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
                    replace(new HomeFragment());
                    return true;
                } else if (id == R.id.plus) {
                    if (!isUserRegistered()) {
                        replace(new PlusFragment());
                        return true;
                    } else {
                        showPopUp();
                        return true;
                    }
                } else if (id == R.id.profile) {
                    replace(new ProfileFragment());
                    return true;
                }
                return false;
            }
        });
    }

    private boolean isUserRegistered() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser() != null;
    }
    private void showPopUp() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ResourceType") View popupView = inflater.inflate(R.menu.custom_menu_layout, null);

        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true // Set this to true to make the PopupWindow outside touchable
        );

        // Set background drawable with a transparent color to close the popup when clicked outside
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LinearLayout addPhotoButton = popupView.findViewById(R.id.imageButton1);
        LinearLayout addVideoButton = popupView.findViewById(R.id.imageButton2);

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageButton1Click();
                popupWindow.dismiss();
            }
        });

        addVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageButton2Click();
                popupWindow.dismiss();
            }
        });

        // Adjust the y-coordinate and gravity to position the PopupWindow above the BottomNavigationView
        int[] location = new int[2];
        bottomBar.getLocationOnScreen(location);
        int y = location[1] - popupWindow.getHeight();

        popupWindow.showAtLocation(bottomBar, Gravity.NO_GRAVITY, 0, y);
    }


    private void onImageButton1Click() {
        Intent intent=new Intent(getApplicationContext(),AddPhoto.class);
        startActivity(intent);
    }

    private void onImageButton2Click() {
        Intent intent=new Intent(getApplicationContext(),AddVideo.class);
        startActivity(intent);
    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
