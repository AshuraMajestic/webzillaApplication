package com.example.ashishappv2.Domains;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashishappv2.R;

import java.util.ArrayList;
import java.util.List;

public class AddVariants extends AppCompatActivity {

    private LinearLayout sizeOpenClose,colorOpenClose,colorButtonLayout,sizeButtonLayout,addSizesLayout,addColorLayout;
    private TextView size,color;
    private List<Sizes> sizeList;
    private ImageView sizeArrow,colorArrow;
    private Button saveAndContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sizeList=new ArrayList<>();
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
        saveAndContinue=findViewById(R.id.button);

        saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSizeList();
            }
        });
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
    private void updateSizeList() {
        sizeList.clear(); // Clear the list to avoid duplicate entries
        boolean isValidInput = true; // Flag to track if all inputs are valid

        for (int i = 0; i < addSizesLayout.getChildCount(); i++) {
            View itemView = addSizesLayout.getChildAt(i);

            // Extract data from the view
            // For demonstration, assuming EditTexts with ids sizeText, priceTextVariant, and sellingPriceText
            EditText sizeNameEditTextView = itemView.findViewById(R.id.sizeText);
            EditText sizePriceEditTextView = itemView.findViewById(R.id.priceTextVariant);
            EditText sizeSellingPriceEditTextView = itemView.findViewById(R.id.sellingPriceText);

            String sizeName = sizeNameEditTextView.getText().toString();
            String sizePriceText = sizePriceEditTextView.getText().toString();
            String sizeSellingPriceText = sizeSellingPriceEditTextView.getText().toString();

            if (!sizePriceText.isEmpty() && !sizeSellingPriceText.isEmpty()) {
                try {
                    long sizePrice = Long.parseLong(sizePriceText);
                    long sizeSellingPrice = Long.parseLong(sizeSellingPriceText);

                    // Create Sizes object and add to the list
                    Sizes size = new Sizes(sizeName, sizePrice, sizeSellingPrice);
                    sizeList.add(size);
                } catch (NumberFormatException e) {
                    // Handle the case where the entered values are not valid longs
                    Toast.makeText(this, "Please enter valid price and selling price", Toast.LENGTH_SHORT).show();
                    isValidInput = false;
                    break; // Exit the loop if there's an invalid input
                }
            } else {
                // Handle the case where the fields are empty
                Toast.makeText(this, "Please enter price and selling price", Toast.LENGTH_SHORT).show();
                isValidInput = false;
                break; // Exit the loop if there's an empty field
            }
        }

        if (isValidInput) {
            saveAndContinue(); // Call saveAndContinue() only if all inputs are valid
        }
    }
    private void saveAndContinue() {
        // Prepare intent to pass data back to previous activity
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("sizeList", new ArrayList<>(sizeList));
        setResult(RESULT_OK, intent);
        finish();
    }
}