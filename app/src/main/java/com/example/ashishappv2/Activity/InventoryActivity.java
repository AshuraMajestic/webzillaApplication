package com.example.ashishappv2.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.example.ashishappv2.Adapter.ViewPagerAdpater;
import com.example.ashishappv2.R;
import com.google.android.material.tabs.TabLayout;

public class InventoryActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdpater viewPagerAdpater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        tabLayout=findViewById(R.id.UpperTab);
        viewPager2=findViewById(R.id.viewPager);
        viewPagerAdpater=new ViewPagerAdpater(this);
        viewPager2.setAdapter(viewPagerAdpater);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                    case 1:
                        tabLayout.getTabAt(position).select();
                }
                super.onPageSelected(position);
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Handle the back button press
        super.onBackPressed();

        // Navigate back to the previous activity (MainActivity)
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}