package com.example.ashishappv2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.ashishappv2.Domains.OrderDetailActivity;
import com.example.ashishappv2.Fragment.OrderFragment;
import com.example.ashishappv2.R;

public class OrderPlacedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(OrderPlacedActivity.this, OrderFragment.class);
                startActivity(mainIntent);
                finish();
            }
        }, 2000);
    }
}