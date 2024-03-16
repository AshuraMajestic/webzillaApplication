package com.example.ashishappv2.Domains;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Gravity;
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
import com.example.ashishappv2.Activity.InventoryActivity;
import com.example.ashishappv2.Activity.MainActivity;
import com.example.ashishappv2.Adapter.CategoryAdapter;
import com.example.ashishappv2.Adapter.chooseCategoryAdapter;
import com.example.ashishappv2.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Add_Product extends AppCompatActivity  implements chooseCategoryAdapter.OnCategoryClickListener{
    private ImageView imageView;
    private TextView pricetext,dicounttext,pertext,unittext;
    private Button btn;
    private chooseCategoryAdapter chooseCategoryAdapter;
    private List<Sizes> sizeListFromVariants = new ArrayList<>();
    private List<String> colorListFromVarinats=new ArrayList<>();
    private List<CategoryInventory> CategoryList;

    private final List<Uri> selectedImageUris = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private LinearLayout variants;
    private String ShopName;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private TextInputEditText productName,categoryName,productPrice,discountedPrice,ProductUnit,per,productDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        imageView = findViewById(R.id.takeImage);
        imageView.setOnClickListener(this::selectImage);
        btn=findViewById(R.id.button);
        productName=findViewById(R.id.productName);
        categoryName=findViewById(R.id.productCategory);
        productPrice=findViewById(R.id.productPrice);
        discountedPrice=findViewById(R.id.productDiscount);
        ProductUnit=findViewById(R.id.productUnit);
        per=findViewById(R.id.per);
        variants=findViewById(R.id.Variants);
        productDetail=findViewById(R.id.productDetail);
        pricetext=findViewById(R.id.pricetext);
        dicounttext=findViewById(R.id.discounttext);
        pertext=findViewById(R.id.pertext);
        unittext=findViewById(R.id.unit);


        variants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddVariants.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_VARIANTS); // Start activity for result
            }
        });
        per.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    showPieceDialog();
                }
            }
        });

        categoryName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDialog();
                }
            }
        });
        productPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String priceText = s.toString();
                if (!priceText.isEmpty()) {
                    pricetext.setText("₹" + priceText);
                } else {
                    pricetext.setText(" "); // Reset the text if the price is empty
                }
            }
        });
        ProductUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String priceText = s.toString();
                if (!priceText.isEmpty()) {
                    pertext.setText(priceText);
                } else {
                    pertext.setText(" "); // Reset the text if the price is empty
                }
            }
        });
        per.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String priceText = s.toString();
                if (!priceText.isEmpty()) {
                    unittext.setText(priceText);
                } else {
                    unittext.setText(" "); // Reset the text if the price is empty
                }
            }
        });
        ProductUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String priceText = s.toString();
                if (!priceText.isEmpty()) {
                    pertext.setText(priceText);
                } else {
                    pricetext.setText(" "); // Reset the text if the price is empty
                }
            }
        });
        discountedPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String PriceText = s.toString();
                if (!PriceText.isEmpty()) {
                    dicounttext.setText("₹" + PriceText);
                    setStrikeThroughText(pricetext, productPrice.getText().toString());
                } else {
                    dicounttext.setText(""); // Reset the text if the price is empty
                    pricetext.setText("₹" + productPrice.getText().toString()); // Set pricetext to the original price
                }
            }
        });


        //Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        getShopName();
        btn.setOnClickListener(v->{
            savetodb();
        });

    }

    private void showPieceDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.unit_choose_dialog);
        ImageView cancelButton=dialog.findViewById(R.id.cancelButton);
        TextView pieceTextView = dialog.findViewById(R.id.piece);
        TextView kgTextView = dialog.findViewById(R.id.kg);
        TextView mlTextView = dialog.findViewById(R.id.ml);
        TextView gmTextView = dialog.findViewById(R.id.gm);
        TextView mmTextView = dialog.findViewById(R.id.mm);
        TextView feetTextView = dialog.findViewById(R.id.feet);
        TextView meterTextView = dialog.findViewById(R.id.meter);
        TextView sqftTextView = dialog.findViewById(R.id.sqft);
        TextView sqmeterTextView = dialog.findViewById(R.id.sqmeter);
        TextView kmTextView = dialog.findViewById(R.id.km);
        TextView setTextView = dialog.findViewById(R.id.set);
        TextView hourTextView = dialog.findViewById(R.id.hour);
        TextView dayTextView = dialog.findViewById(R.id.day);
        TextView bunchTextView = dialog.findViewById(R.id.bunch);
        TextView bundleTextView = dialog.findViewById(R.id.bundle);
        TextView monthTextView = dialog.findViewById(R.id.month);
        TextView yearTextView = dialog.findViewById(R.id.year);
        TextView serviceTextView = dialog.findViewById(R.id.service);
        TextView workTextView = dialog.findViewById(R.id.work);
        TextView packetTextView = dialog.findViewById(R.id.packet);
        TextView boxTextView = dialog.findViewById(R.id.box);
        TextView poundTextView = dialog.findViewById(R.id.pound);
        TextView dozenTextView = dialog.findViewById(R.id.dozen);
        TextView guntaTextView = dialog.findViewById(R.id.gunta);
        TextView pairTextView = dialog.findViewById(R.id.pair);
        TextView minuteTextView = dialog.findViewById(R.id.minute);
        TextView quintalTextView = dialog.findViewById(R.id.quintal);
        TextView tonTextView = dialog.findViewById(R.id.ton);
        TextView capsuleTextView = dialog.findViewById(R.id.capsule);
        TextView tabletTextView = dialog.findViewById(R.id.tablet);
        TextView plateTextView = dialog.findViewById(R.id.plate);
        TextView inchTextView = dialog.findViewById(R.id.inch);
        pieceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(pieceTextView.getText().toString());
                dialog.dismiss();
            }
        });

        kgTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(kgTextView.getText().toString());
                dialog.dismiss();
            }
        });

        mlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(mlTextView.getText().toString());
                dialog.dismiss();
            }
        });

        gmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(gmTextView.getText().toString());
                dialog.dismiss();
            }
        });

        mmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(mmTextView.getText().toString());
                dialog.dismiss();
            }
        });

        feetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(feetTextView.getText().toString());
                dialog.dismiss();
            }
        });

        meterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(meterTextView.getText().toString());
                dialog.dismiss();
            }
        });

        sqftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(sqftTextView.getText().toString());
                dialog.dismiss();
            }
        });

        sqmeterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(sqmeterTextView.getText().toString());
                dialog.dismiss();
            }
        });

        kmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(kmTextView.getText().toString());
                dialog.dismiss();
            }
        });

        setTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(setTextView.getText().toString());
                dialog.dismiss();
            }
        });

        hourTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(hourTextView.getText().toString());
                dialog.dismiss();
            }
        });

        dayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(dayTextView.getText().toString());
                dialog.dismiss();
            }
        });

        bunchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(bunchTextView.getText().toString());
                dialog.dismiss();
            }
        });

        bundleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(bundleTextView.getText().toString());
                dialog.dismiss();
            }
        });

        monthTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(monthTextView.getText().toString());
                dialog.dismiss();
            }
        });

        yearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(yearTextView.getText().toString());
                dialog.dismiss();
            }
        });

        serviceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(serviceTextView.getText().toString());
                dialog.dismiss();
            }
        });

        workTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(workTextView.getText().toString());
                dialog.dismiss();
            }
        });

        packetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(packetTextView.getText().toString());
                dialog.dismiss();
            }
        });

        boxTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(boxTextView.getText().toString());
                dialog.dismiss();
            }
        });

        poundTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(poundTextView.getText().toString());
                dialog.dismiss();
            }
        });

        dozenTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(dozenTextView.getText().toString());dialog.dismiss();
            }
        });

        guntaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(guntaTextView.getText().toString());dialog.dismiss();
            }
        });

        pairTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(pairTextView.getText().toString());dialog.dismiss();
            }
        });

        minuteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(minuteTextView.getText().toString());dialog.dismiss();
            }
        });

        quintalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(quintalTextView.getText().toString());dialog.dismiss();
            }
        });

        tonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(tonTextView.getText().toString());dialog.dismiss();
            }
        });

        capsuleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(capsuleTextView.getText().toString());dialog.dismiss();
            }
        });

        tabletTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(tabletTextView.getText().toString());dialog.dismiss();
            }
        });

        plateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(plateTextView.getText().toString());dialog.dismiss();
            }
        });

        inchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                per.setText(inchTextView.getText().toString());dialog.dismiss();
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

    private void showDialog() {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.category_daialog);
            RecyclerView chooseCatgeory= dialog.findViewById(R.id.chooseCategory);
            Button btn=dialog.findViewById(R.id.AddCategoryButton);
            ImageView cancelButton=dialog.findViewById(R.id.cancelButton);
            chooseCatgeory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            CategoryList = new ArrayList<>();
            chooseCategoryAdapter = new chooseCategoryAdapter(CategoryList, getApplicationContext(),this,dialog);
            chooseCatgeory.setAdapter(chooseCategoryAdapter);
            fetchUserData();

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Add_Category.class);
                    startActivity(intent);
                }
            });
            dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void fetchUserData() {

        DatabaseReference CategoryRef = database.getReference().child("Shops").child(ShopName).child("Category");
        CategoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CategoryList.clear();  // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CategoryInventory category = snapshot.getValue(CategoryInventory.class);
                    if (category != null) {
                        CategoryList.add(category);
                    }
                }
                chooseCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AshuraDB", error.toString());
            }
        });
    }

    private void setStrikeThroughText(TextView textView, String price) {
        SpannableString spannablePrice = new SpannableString("₹" + price);
        spannablePrice.setSpan(new StrikethroughSpan(), 0, spannablePrice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannablePrice);
    }
    private void getShopName() {
        String userId = mAuth.getCurrentUser().getUid();

        DatabaseReference userRef = database.getReference("UserData").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ShopName = dataSnapshot.child("link").getValue(String.class).trim().toLowerCase();
                    storageReference = storage.getReference().child("Shops");
                } else {
                    showToast("User data not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Error retrieving user data");
            }
        });
    }
    private void savetodb() {


        if (productName.getText().toString().isEmpty() || categoryName.getText().toString().isEmpty() || productPrice.getText().toString().isEmpty() ||  ProductUnit.getText().toString().isEmpty()  ) {
            showToast("Please fill in all the fields");
            return;
        }
        if (!discountedPrice.getText().toString().isEmpty() && !productPrice.getText().toString().isEmpty()) {
            double price = Double.parseDouble(productPrice.getText().toString());
            double discounted = Double.parseDouble(discountedPrice.getText().toString());

            if (discounted > price) {
                // Show toast
                showToast("Discounted price cannot be greater than the actual price");
                return; // Stop further execution
            }
        }
        String ProductName = productName.getText().toString().trim().toLowerCase();
        // Create a reference to the product in the database
        DatabaseReference shopRef = database.getReference().child("Shops").child(ShopName);
        DatabaseReference productRef = shopRef.child("Products").child(ProductName);

        int totalUploads = selectedImageUris.size();
        AtomicInteger uploadsComplete = new AtomicInteger(0);

        // Upload images to Firebase Storage
        for (int i = 0; i < totalUploads; i++) {
            Uri imageUri = selectedImageUris.get(i);
            String imageName = "image_" + i + ".jpg";
            StorageReference imageRef = storageReference.child(ShopName).child("Products").child(ProductName).child(imageName);
            int finalI=i;
            // Upload image to Firebase Storage
            imageRef.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Get the download URL of the uploaded image
                    imageRef.getDownloadUrl().addOnCompleteListener(uriTask -> {
                        if (uriTask.isSuccessful()) {
                            // Save the image URL to the database
                            productRef.child("images").child("image_" + finalI).setValue(uriTask.getResult().toString());

                            // Increment the counter for successful uploads
                            int count = uploadsComplete.incrementAndGet();

                            // Check if all uploads are complete
                            if (count == totalUploads) {
                                // All uploads are complete, proceed to save other product details
                                saveOtherProductDetails(productRef,shopRef);
                            }
                        }
                    });
                }
            });
        }

        Intent intent = new Intent(Add_Product.this, InventoryActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveOtherProductDetails(DatabaseReference productRef,DatabaseReference shopRef) {

        // Save other product details to the database
        productRef.child("name").setValue(productName.getText().toString());
        productRef.child("category").setValue(categoryName.getText().toString());

        productRef.child("price").setValue(productPrice.getText().toString());
        if(discountedPrice.getText().toString().isEmpty()){
            productRef.child("DiscountedPrice").setValue("");
        }
        else{
            productRef.child("DiscountedPrice").setValue(discountedPrice.getText().toString());
        }
        productRef.child("unit").setValue(ProductUnit.getText().toString());
        productRef.child("per").setValue(per.getText().toString());
        boolean b=true;
        productRef.child("available").setValue(b);
        productRef.child("sizes").setValue(sizeListFromVariants);
        productRef.child("color").setValue(colorListFromVarinats);
        DatabaseReference categoryRef=shopRef.child("Category").child(categoryName.getText().toString().toLowerCase());
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long currentItemCount = dataSnapshot.child("itemCount").getValue(Long.class);
                    categoryRef.child("itemCount").setValue(currentItemCount + 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog(databaseError+"");
            }
        });
        showToast("Product added successfully");

    }

    private void makeLog(String s) {
        Log.d("AshuraDB",s);
    }


    public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getData() != null) {
                // Single image selected
                Uri selectedImageUri = data.getData();
                selectedImageUris.add(selectedImageUri);
            } else if (data.getClipData() != null) {
                // Multiple images selected
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri selectedImageUri = clipData.getItemAt(i).getUri();
                    selectedImageUris.add(selectedImageUri);
                }
            }
            // Update image views
            updateImageViews();
        }else if (requestCode == REQUEST_CODE_ADD_VARIANTS && resultCode == RESULT_OK && data != null) {
            // Handling for adding variants
            ArrayList<Sizes> sizesList = data.getParcelableArrayListExtra("sizeList");
            ArrayList<String> colorList = data.getStringArrayListExtra("colorList");
            if (sizesList != null) {
                sizeListFromVariants = sizesList;
            }
            if (colorList != null) {
                colorListFromVarinats=colorList;
            }
        } else if (requestCode == REQUEST_CODE_ADD_VARIANTS && resultCode == RESULT_CANCELED) {
            // Handling for canceled result
            makeLog(resultCode+"");
        }
    }

    private void updateImageViews() {
        // Clear existing images
        imageView.setImageResource(0);

        // Display the first selected image (if any)
        if (!selectedImageUris.isEmpty()) {
            imageView.setImageURI(selectedImageUris.get(0));
        }
    }
    @Override
    public void onBackPressed() {
        // Handle the back button press
        super.onBackPressed();

        // Navigate back to the previous activity
        Intent intent = new Intent(Add_Product.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private  void showMessage(String message){
        Log.d("AshuraDB",message);
    }
    private  void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryClick(String category) {
        categoryName.setText(category);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    private static final int REQUEST_CODE_ADD_VARIANTS = 1001;
}