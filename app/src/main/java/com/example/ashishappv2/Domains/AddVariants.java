package com.example.ashishappv2.Domains;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashishappv2.Activity.Add_Category;
import com.example.ashishappv2.Adapter.chooseCategoryAdapter;
import com.example.ashishappv2.R;

import java.util.ArrayList;
import java.util.List;

public class AddVariants extends AppCompatActivity {

    private LinearLayout sizeOpenClose,colorOpenClose,colorButtonLayout,sizeButtonLayout,addSizesLayout,addColorLayout;
    private TextView size,color;
    private List<Sizes> sizeList;
    private List<String> ColorList;
    private ImageView sizeArrow,colorArrow;
    private Button saveAndContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sizeList=new ArrayList<>();
        ColorList=new ArrayList<>();
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

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addColorDialog();
            }
        });

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

    private void addColorDialog() {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_color_dialog);
        LinearLayout selectBlack = dialog.findViewById(R.id.selectBlack);
        LinearLayout selectOrange = dialog.findViewById(R.id.selectOrange);
        LinearLayout selectRed = dialog.findViewById(R.id.selectRed);
        LinearLayout selectPink = dialog.findViewById(R.id.selectPink);

        // Find Views inside the second LinearLayout
        LinearLayout selectPurple = dialog.findViewById(R.id.selectPurple);
        LinearLayout selectDarkPurple = dialog.findViewById(R.id.selectDarkPurple);
        LinearLayout selectIndigo = dialog.findViewById(R.id.selectIndigio);
        LinearLayout selectDarkBlue = dialog.findViewById(R.id.selectDarkBlue);

        // Find Views inside the third LinearLayout
        LinearLayout selectBlue = dialog.findViewById(R.id.selectBlue);
        LinearLayout selectLightBlue = dialog.findViewById(R.id.selectLightBlue);
        LinearLayout selectdarkGreen = dialog.findViewById(R.id.selectdarkGreen);
        LinearLayout selectGreen = dialog.findViewById(R.id.selectGreen);

        // Find Views inside the fourth LinearLayout
        LinearLayout selectLightGreen = dialog.findViewById(R.id.selectLightGreen);
        LinearLayout selectHalkaGreen = dialog.findViewById(R.id.selectHalkaGreen);
        LinearLayout selectYellow = dialog.findViewById(R.id.selectYellow);
        LinearLayout selectGoldenYellow = dialog.findViewById(R.id.selectGoldenYellow);

        // Find Views inside the fifth LinearLayout
        LinearLayout selectDarkYellow = dialog.findViewById(R.id.selectDarkYellow);
        LinearLayout selectBrown = dialog.findViewById(R.id.selectBrown);
        LinearLayout selectDarkGrey = dialog.findViewById(R.id.selectDarkGrey);
        LinearLayout selectGrey = dialog.findViewById(R.id.selectGrey);
        LinearLayout[] colorLayouts = new LinearLayout[]{
                selectOrange, selectRed, selectPink,selectBrown,selectBlack,selectBlue,selectGrey,selectDarkBlue,
                selectdarkGreen,selectDarkGrey,selectDarkPurple,selectDarkYellow,selectGoldenYellow,selectGreen,
                selectHalkaGreen,selectIndigo,selectLightBlue,selectLightGreen,selectYellow,selectPurple
        };
        selectBlack.setBackgroundColor(getResources().getColor(R.color.green));

        selectBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBlack.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectBlack,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectBlack);
                dialog.dismiss();
            }
        });
selectBrown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBrown.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectBrown,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectBrown);
                dialog.dismiss();
            }
        });
selectBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectBlue.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectBlue,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectBlue);
                dialog.dismiss();
            }
        });
selectdarkGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectdarkGreen.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectdarkGreen,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectdarkGreen);
                dialog.dismiss();
            }
        });
selectDarkGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDarkGrey.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectDarkGrey,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectDarkGrey);
                dialog.dismiss();
            }
        });
selectDarkBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDarkBlue.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectDarkBlue,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectDarkBlue);
                dialog.dismiss();
            }
        });
selectDarkPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDarkPurple.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectDarkPurple,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectDarkPurple);
                dialog.dismiss();
            }
        });
selectDarkYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDarkYellow.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectDarkYellow,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectDarkYellow);
                dialog.dismiss();
            }
        });
selectGoldenYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGoldenYellow.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectGoldenYellow,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectGoldenYellow);
                dialog.dismiss();
            }
        });
selectGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGreen.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectGreen,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectGreen);
                dialog.dismiss();
            }
        });
selectGrey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectGrey.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectGrey,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectGrey);
                dialog.dismiss();
            }
        });
selectHalkaGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHalkaGreen.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectHalkaGreen,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectHalkaGreen);
                dialog.dismiss();
            }
        });
selectLightGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLightGreen.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectLightGreen,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectLightGreen);
                dialog.dismiss();
            }
        });
selectIndigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIndigo.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectIndigo,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectIndigo);
                dialog.dismiss();
            }
        });
selectLightBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLightBlue.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectLightBlue,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectLightBlue);
                dialog.dismiss();
            }
        });
selectOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOrange.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectOrange,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectOrange);
                dialog.dismiss();
            }
        });
selectPink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPink.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectPink,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectPink);
                dialog.dismiss();
            }
        });
selectPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPurple.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectPurple,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectPurple);
                dialog.dismiss();
            }
        });
selectRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectRed.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectRed,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectRed);
                dialog.dismiss();
            }
        });
selectYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectYellow.setBackgroundColor(getResources().getColor(R.color.green));
                // Clear background color of other color layouts
                clearOtherColors(selectYellow,colorLayouts);
                // Add the XML layout in addColorLayout
                addXmlLayoutToColorLayout(selectYellow);
                dialog.dismiss();
            }
        });

        dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void addXmlLayoutToColorLayout(LinearLayout selectedColor) {
        // Inflate the XML layout
        View itemView = LayoutInflater.from(this).inflate(R.layout.color_input_layout, addColorLayout, false);
        ImageView colorImage = itemView.findViewById(R.id.colorBox);
        TextView colorName = itemView.findViewById(R.id.colorName);
        // Assuming selectedColor.getContentDescription() returns the name of the color resource as a String
        String colorNameString = selectedColor.getContentDescription().toString();

        // Retrieve the color resource identifier
        int colorResourceId = getResources().getIdentifier(colorNameString, "color", getPackageName());

        // Get the actual color value associated with the resource identifier
        int colorCode = ContextCompat.getColor(this, colorResourceId);

        // Set the background color to the colorImage ImageView
        colorImage.setBackgroundColor(colorCode);
        colorName.setText(colorNameString.toUpperCase());
        TextView deleteButton = itemView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click action
                ((ViewGroup)itemView).removeView(itemView);
                updateAddColorText();
            }
        });
        // Add the inflated layout to the parent layout
        addColorLayout.addView(itemView);
        updateAddColorText();
    }

    private void updateAddColorText() {
        if (addColorLayout.getChildCount() > 0) {
            color.setText("Add Another Color");
        } else {
            size.setText("Add Color");
        }
    }

    private void clearOtherColors(LinearLayout selectedColorLayout,LinearLayout[] colorLayouts) {
        // Iterate over color layouts and clear background color if it's not the selected one
        for (LinearLayout colorLayout : colorLayouts) {
            if (colorLayout != selectedColorLayout) {
                colorLayout.setBackgroundColor(Color.TRANSPARENT);
            }
        }
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
        ColorList.clear(); // Clear the list to avoid duplicate entries


        for (int i = 0; i < addColorLayout.getChildCount(); i++) {
            View itemView = addColorLayout.getChildAt(i);

            TextView colorNameText = itemView.findViewById(R.id.colorName);

            String colorName = colorNameText.getText().toString();
            ColorList.add(colorName);
        }

            sizeList.clear(); // Clear the list to avoid duplicate entries

            boolean isValidInput=true;
            for (int j = 0; j < addSizesLayout.getChildCount(); j++) {
            View itemView2 = addSizesLayout.getChildAt(j);

            // Extract data from the view
            // For demonstration, assuming EditTexts with ids sizeText, priceTextVariant, and sellingPriceText
            EditText sizeNameEditTextView = itemView2.findViewById(R.id.sizeText);
            EditText sizePriceEditTextView = itemView2.findViewById(R.id.priceTextVariant);
            EditText sizeSellingPriceEditTextView = itemView2.findViewById(R.id.sellingPriceText);

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
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("sizeList", new ArrayList<>(sizeList));
        intent.putStringArrayListExtra("colorList", new ArrayList<>(ColorList));
        setResult(RESULT_OK, intent);
        finish();
    }
}