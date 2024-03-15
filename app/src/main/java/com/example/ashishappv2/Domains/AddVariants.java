package com.example.ashishappv2.Domains;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ashishappv2.R;

public class AddVariants extends AppCompatActivity {

    private LinearLayout sizeOpenClose,colorOpenClose,colorButtonLayout,sizeButtonLayout,addSizesLayout,addColorLayout;
    private TextView size,color;
    private ImageView sizeArrow,colorArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_variants);
        size=findViewById(R.id.addSizesButton);
        color=findViewById(R.id.addColor);
        sizeOpenClose=findViewById(R.id.sizeopenClose);
        colorOpenClose=findViewById(R.id.colorOpenClose);
        colorButtonLayout=findViewById(R.id.colorButtonLayout);
        sizeButtonLayout=findViewById(R.id.sizesButtonLayout);
        addSizesLayout=findViewById(R.id.addDifferentSizesLayout);
        addColorLayout=findViewById(R.id.addDifferentColorLayout);
        sizeArrow=findViewById(R.id.sizeArrow);
        colorArrow=findViewById(R.id.colorArrow);
        size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSizeTextInputLayout();
            }
        });
        sizeOpenClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sizeButtonLayout.getVisibility() == View.GONE) {
                    sizeButtonLayout.setVisibility(View.VISIBLE);
                    sizeArrow.animate().rotation(180f).start();
                } else {
                    sizeButtonLayout.setVisibility(View.GONE);
                    sizeArrow.animate().rotation(0f).start();
                }
            }
        });

        colorOpenClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(colorButtonLayout.getVisibility() == View.GONE) {
                    colorButtonLayout.setVisibility(View.VISIBLE);
                    colorArrow.animate().rotation(180f).start();
                } else {
                    colorButtonLayout.setVisibility(View.GONE);
                    colorArrow.animate().rotation(0f).start();
                }
            }
        });

    }
    private void addSizeTextInputLayout() {
        // Inflate the XML layout
        View itemView = LayoutInflater.from(this).inflate(R.layout.sizes_input_layout, addSizesLayout, false);

        // Access components in the inflated layout
        TextView deleteButton = itemView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click action
                ((ViewGroup)itemView.getParent()).removeView(itemView);
                updateAddSizeButtonText();
            }
        });

        // Add the inflated layout to the parent layout
        addSizesLayout.addView(itemView);
        updateAddSizeButtonText();
    }
    private void updateAddSizeButtonText() {
        if (addSizesLayout.getChildCount() > 0) {
            size.setText("Add Another Size");
        } else {
            size.setText("Add Size");
        }
    }
}