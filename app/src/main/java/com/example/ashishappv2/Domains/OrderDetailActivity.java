package com.example.ashishappv2.Domains;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashishappv2.Activity.OrderPlacedActivity;
import com.example.ashishappv2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView OrderNumber,dateTime,accepted,itemCount,itemTotal,Grandtotal;
    private  TextView name,email,phone,address,city,pinCode,state,paymentMethod,payementType;
    ImageView dot;
    AppCompatButton accept;
    TextView reject;
    private String ShopName,orderNumber;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference OrderRef;
    private RecyclerView recyclerViewCartItems;
    private CartItemAdapter cartItemAdapter;
    private List<Item> itemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        orderNumber = getIntent().getStringExtra("ORDER_NUMBER");
        OrderNumber=findViewById(R.id.textView);
        OrderNumber.setText("Order #"+orderNumber);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        accepted=findViewById(R.id.accepted);
        itemCount=findViewById(R.id.totalItem);
        dateTime=findViewById(R.id.dateTime);
        itemTotal=findViewById(R.id.itemTotal);
        Grandtotal=findViewById(R.id.TotalGrand);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phoneNumber);
        address=findViewById(R.id.address);
        city=findViewById(R.id.city);
        pinCode=findViewById(R.id.zipCode);
        state=findViewById(R.id.state);
        payementType=findViewById(R.id.paymentType);
        paymentMethod=findViewById(R.id.paymentMethod);
        dot=findViewById(R.id.dot);
        accept=findViewById(R.id.AcceptBtn);
        reject = findViewById(R.id.rejectOrderButton);
        getShopName();
        recyclerViewCartItems = findViewById(R.id.itemRecyler);
        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this));

        // Initialize item list
        itemList = new ArrayList<>();

        // Initialize adapter
        cartItemAdapter = new CartItemAdapter(this,itemList);
        recyclerViewCartItems.setAdapter(cartItemAdapter);

        // Call getShopName() to fetch data
        getShopName();


    }



    private void getShopName() {
        String userId = mAuth.getCurrentUser().getUid();

        DatabaseReference userRef = database.getReference("UserData").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ShopName = dataSnapshot.child("link").getValue(String.class).trim().toLowerCase();
                    OrderRef=database.getReference().child("Shops").child(ShopName).child("Orders").child(orderNumber+"");
                    setData();
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

    private void setData() {
        OrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    OrderList orderList=dataSnapshot.getValue(OrderList.class);
                    itemList.clear();
                    for (DataSnapshot itemSnapshot : dataSnapshot.child("cart").getChildren()) {
                        Item item = itemSnapshot.getValue(Item.class);
                        itemList.add(item);
                    }

                    // Update the RecyclerView adapter with the new itemList
                    cartItemAdapter.notifyDataSetChanged();

                    dateTime.setText(orderList.getDate()+"");
                    accepted.setText(orderList.getAccepted()+"");
                    itemCount.setText(orderList.getTotalQuantity()+ " ITEM");
                    itemTotal.setText("₹ "+orderList.getTotalPrice());
                    Grandtotal.setText("₹ "+orderList.getTotalPrice());

                    if (orderList.getAccepted().equals("accepted")){
                        dot.setImageResource(R.drawable.green_dot);
                        //Customer Details
                        name.setText(orderList.getFirstName()+" " + orderList.getLastName());
                        email.setText(orderList.getEmail()+"");
                        phone.setText(orderList.getNumber()+"");
                        address.setText(orderList.getAddress()+"");
                        city.setText(orderList.getCity()+"");
                        pinCode.setText(orderList.getZipCode()+"");
                        state.setText(orderList.getState()+"");
                        accept.setText("Ship Order");
                        reject.setText("Cancel Order");


                        reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                OrderRef.child("accepted").setValue("cancelled")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle any errors, show a toast message or log the error
                                                Toast.makeText(OrderDetailActivity.this, "Failed to accept order", Toast.LENGTH_SHORT).show();
                                                Log.e("OrderDetailActivity", "Failed to accept order", e);
                                            }
                                        });
                            }
                        });
                        accept.setBackgroundColor(getResources().getColor(R.color.btnorange));
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            showPopUp();
                            }
                        });
                    }else if(orderList.getAccepted().equals("pending")){
                        dot.setImageResource(R.drawable.orange_dot);
                        name.setText("**********");
                        email.setText("**********");
                        phone.setText("**********");
                        address.setText("**********");
                        city.setText("******");
                        pinCode.setText("******");
                        state.setText("******");
                        reject.setText("Reject Order");

                        reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                OrderRef.child("accepted").setValue("rejected")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle any errors, show a toast message or log the error
                                                Toast.makeText(OrderDetailActivity.this, "Failed to accept order", Toast.LENGTH_SHORT).show();
                                                Log.e("OrderDetailActivity", "Failed to accept order", e);
                                            }
                                        });
                                onBackPressed();
                            }
                        });

                        accept.setText("Accept Order");
                        accept.setBackgroundColor(getResources().getColor(R.color.btngreen));
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                OrderRef.child("accepted").setValue("accepted")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dot.setImageResource(R.drawable.green_dot);
                                                name.setText(orderList.getFirstName()+" " + orderList.getLastName());
                                                email.setText(orderList.getEmail()+"");
                                                phone.setText(orderList.getNumber()+"");
                                                address.setText(orderList.getAddress()+"");
                                                city.setText(orderList.getCity()+"");
                                                pinCode.setText(orderList.getZipCode()+"");
                                                state.setText(orderList.getState()+"");
                                                accept.setText("Ship Order");
                                                accept.setBackgroundColor(getResources().getColor(R.color.btnorange));
                                                reject.setText("Cancel Order");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle any errors, show a toast message or log the error
                                                Toast.makeText(OrderDetailActivity.this, "Failed to accept order", Toast.LENGTH_SHORT).show();
                                                Log.e("OrderDetailActivity", "Failed to accept order", e);
                                            }
                                        });
                            }
                        });
                    }
                    payementType.setText(orderList.getPaymentOption());
                    if(orderList.getPaymentOption().equals("Cash On Delivery")){
                        paymentMethod.setText("COD");
                    }
                    else{
                        paymentMethod.setText("UPI");
                    }

                } else {
                    showToast("Order data not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Error retrieving order data");
            }
        });
    }
    private void showPopUp() {
        final Dialog dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ship_dialog);
        ImageView cancelButton=dialog.findViewById(R.id.cancelButton);
        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        RadioButton radioButton1 = dialog.findViewById(R.id.radioButton1);
        RadioButton radioButton2 = dialog.findViewById(R.id.radioButton2);
        AppCompatButton btn = dialog.findViewById(R.id.proceedButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the ID of the selected radio button
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                // Perform action based on the selected radio button
                if (selectedRadioButtonId == radioButton1.getId()) {
                    // Action for radioButton1
                  showapiDialog();
                    // Add your code here to handle the action for radioButton1
                } else if (selectedRadioButtonId == radioButton2.getId()) {
                    OrderRef.child("accepted").setValue("shipped")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(getApplicationContext(), OrderPlacedActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle any errors, show a toast message or log the error
                                    Toast.makeText(OrderDetailActivity.this, "Failed to ship order", Toast.LENGTH_SHORT).show();
                                    Log.e("OrderDetailActivity", "Failed to ship order", e);
                                }
                            });

                } else {
                    Toast.makeText(getApplicationContext(), "Please select a shipping option", Toast.LENGTH_SHORT).show();
                }
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

    private void showapiDialog() {
        final Dialog dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.api_dialog);
        ImageView cancelButton=dialog.findViewById(R.id.cancelButton);
        TextInputLayout email=dialog.findViewById(R.id.mailAPI);
        TextInputLayout pass = dialog.findViewById(R.id.passAPI);
        AppCompatButton connect= dialog.findViewById(R.id.connectButton);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrderPlacedActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

    }


    private void showToast(String userDataNotFound) {
        Toast.makeText(this, userDataNotFound, Toast.LENGTH_SHORT).show();
    }
}