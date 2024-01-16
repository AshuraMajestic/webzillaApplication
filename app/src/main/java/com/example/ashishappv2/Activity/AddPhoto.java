package com.example.ashishappv2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ashishappv2.R;

public class AddPhoto extends AppCompatActivity {
    private ImageView imageView;
    private EditText productname,productcateogory,price,pieces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        productname = findViewById(R.id.editTextText);
        productcateogory = findViewById(R.id.editTextText2);
        price = findViewById(R.id.editTextText4);
        pieces = findViewById(R.id.editTextText5);
        imageView = findViewById(R.id.takeImage);
    }
    private  void showMessage(String message){
        Log.d("AshuraDB",message);
    }
    private  void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}