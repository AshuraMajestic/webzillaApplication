package com.example.ashishappv2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.ashishappv2.R;

public class AddVideo extends AppCompatActivity {
    private Button btn;
    private VideoView videoView;

    private EditText productname, productcategory, price, pieces;

    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        productname = findViewById(R.id.editTextText);
        productcategory = findViewById(R.id.editTextText2);
        price = findViewById(R.id.editTextText4);
        pieces = findViewById(R.id.editTextText5);
        videoView = findViewById(R.id.videoView);
        imageView = findViewById(R.id.takeVideo);
        btn = findViewById(R.id.button);
    }
}