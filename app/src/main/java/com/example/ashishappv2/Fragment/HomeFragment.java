package com.example.ashishappv2.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CompoundButton;
import android.widget.LinearLayout;

import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ashishappv2.Domains.OrderList;

import com.example.ashishappv2.Domains.userData;
import com.example.ashishappv2.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private LinearLayout sessionLayout,salesLayout,orderLayout;
    private LinearLayout salesShowLayout,SessionshowLayout,orderShowLayout;
    private FirebaseDatabase database;
    private String ShopName;
    private Switch online;

    private TextView shopname, shopLink, siteVisitCount, totalSales, totalOrder;

    private AppCompatButton allTime, Last30, Last7, today;
    private View view2;
    BarChart barChart,barChart2,barChart3;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        allTime = view.findViewById(R.id.alltime);
        Last30 = view.findViewById(R.id.last30days);
        Last7 = view.findViewById(R.id.last7days);
        today = view.findViewById(R.id.today);
        sessionLayout=view.findViewById(R.id.siteSessionLayout);
        shopname = view.findViewById(R.id.name);
        shopLink = view.findViewById(R.id.link);
        siteVisitCount = view.findViewById(R.id.SiteSession);
        totalSales = view.findViewById(R.id.TotalSales);
        totalOrder = view.findViewById(R.id.TotalOrders);
        salesLayout=view.findViewById(R.id.salesLayout);
        orderLayout=view.findViewById(R.id.orderLayout);
        online=view.findViewById(R.id.online);
        view2=view.findViewById(R.id.view);

        SessionshowLayout=view.findViewById(R.id.siteShow);
        salesShowLayout=view.findViewById(R.id.salesShow);
        orderShowLayout=view.findViewById(R.id.orderShow);

        barChart = view.findViewById(R.id.lineChart);
        barChart2 = view.findViewById(R.id.lineChart2);
        barChart3 = view.findViewById(R.id.lineChart3);
        disableButtons();
        getShopName();
        sessionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SessionshowLayout.getVisibility() == View.VISIBLE) {
                    SessionshowLayout.setVisibility(View.GONE);
                } else {
                    SessionshowLayout.setVisibility(View.VISIBLE);


                }
            }
        });
        orderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderShowLayout.getVisibility() == View.VISIBLE) {
                    orderShowLayout.setVisibility(View.GONE);
                } else {
                    orderShowLayout.setVisibility(View.VISIBLE);


                }
            }
        });


        salesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (salesShowLayout.getVisibility() == View.VISIBLE) {
                    salesShowLayout.setVisibility(View.GONE);
                } else {
                    salesShowLayout.setVisibility(View.VISIBLE);
                }
            }
        });


        return view;
    }
    private void getShopName() {
        String userId = mAuth.getCurrentUser().getUid();

        DatabaseReference userRef = database.getReference("UserData").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ShopName = dataSnapshot.child("link").getValue(String.class).trim().toLowerCase();
                    String shopName=dataSnapshot.child("shopname").getValue(String.class);
                    boolean isOnline = dataSnapshot.child("online").getValue(Boolean.class);
                    shopname.setText(shopName);
                    shopLink.setText("https://ashishweb-jv5n.onrender.com/" + ShopName);
                    if (isOnline) {
                        online.setChecked(true);
                        online.setText("Online");
                        view2.setBackgroundResource(R.color.green);
                    } else {
                        online.setChecked(false);
                        online.setText("Offline");
                        view2.setBackgroundResource(R.color.red);
                    }

                    setAllTime();
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

    private void showToast(String userDataNotFound) {
        Toast.makeText(getContext(), userDataNotFound, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shopLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl(shopLink.getText().toString());
            }
        });
        allTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllTime();
            }
        });

        Last30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            setLast30Days();
            }
        });
        Last7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLast7days();
            }
            });

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToday();
            }
        });
        online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateOnlineStatus(isChecked);
            }
        });
    }
    private void updateOnlineStatus(boolean isOnline) {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = database.getReference("UserData").child(userId).child("online");

        // Update online status in Firebase Realtime Database
        userRef.setValue(isOnline)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showToast("Online status updated successfully");
                        if (isOnline) {
                            online.setChecked(true);
                            online.setText("Online");
                            view2.setBackgroundResource(R.color.green);
                        } else {
                            online.setChecked(false);
                            online.setText("Offline");
                            view2.setBackgroundResource(R.color.red);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Failed to update online status");
                    }
                });
    }
    private void setToday() {
        disableButtons();
        allTime.setBackgroundResource(R.drawable.background_last_normal);
        allTime.setTextColor(Color.GRAY);
        Last30.setBackgroundResource(R.drawable.background_last_normal);
        Last30.setTextColor(Color.GRAY);
        Last7.setBackgroundResource(R.drawable.background_last_normal);
        Last7.setTextColor(Color.GRAY);
        today.setBackgroundResource(R.drawable.background_last_pressed);
        today.setTextColor(Color.BLACK);

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime(); // Current date

        // Convert the current date to the format used in the database
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String currentDateString = dateFormat.format(currentDate);

        // Query the database for visitor counts for today
        DatabaseReference visitorRef = database.getReference().child("VisitorCounts").child(ShopName).child(currentDateString);
        visitorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long totalVisitorCount = 0;
                Long count = dataSnapshot.getValue(Long.class);
                if (count != null) {
                    totalVisitorCount += count;
                }
                // Set the total visitor count for today
                siteVisitCount.setText(String.valueOf(totalVisitorCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving visitor counts: " + databaseError.getMessage());
            }
        });

        // Query the database for orders for today
        DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
        makeLog(currentDateString);
        orderRef.orderByChild("date").equalTo(currentDateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalOrderCount = 0;
                double totalSalesAmount = 0;
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderList order = orderSnapshot.getValue(OrderList.class);
                        totalOrderCount++;
                    if (order != null && order.getAccepted().equals("shipped")) {
                        totalSalesAmount += order.getTotalPrice();
                    }
                }
                // Set the total order count and total sales amount for today
                totalOrder.setText(String.valueOf(totalOrderCount));
                totalSales.setText("\u20B9" + " " + String.format("%.2f", totalSalesAmount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving orders: " + databaseError.getMessage());
            }
        });
        enableButtons();
        salesShowLayout.setVisibility(View.GONE);
        SessionshowLayout.setVisibility(View.GONE);
        orderShowLayout.setVisibility(View.GONE);
        sessionLayout.setEnabled(false);
        salesLayout.setEnabled(false);
        orderLayout.setEnabled(false);

    }

    private void setLast7days() {
        disableButtons();
        allTime.setBackgroundResource(R.drawable.background_last_normal);
        allTime.setTextColor(Color.GRAY);
        Last30.setBackgroundResource(R.drawable.background_last_normal);
        Last30.setTextColor(Color.GRAY);
        Last7.setBackgroundResource(R.drawable.background_last_pressed);
        Last7.setTextColor(Color.BLACK);
        today.setBackgroundResource(R.drawable.background_last_normal);
        today.setTextColor(Color.GRAY);

        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime(); // Current date

        // Subtract 6 days from the current date to get the start date (last 7 days)
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        Date startDate = calendar.getTime();

        // Convert the dates to the format used in the database
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);

        // Query the database for visitor counts within the last 7 days
        DatabaseReference visitorRef = database.getReference().child("VisitorCounts").child(ShopName);
        visitorRef.orderByKey().startAt(startDateString).endAt(endDateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long totalVisitorCount = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Long count = snapshot.getValue(Long.class);
                    if (count != null) {
                        totalVisitorCount += count;
                    }
                }
                // Set the total visitor count
                siteVisitCount.setText(String.valueOf(totalVisitorCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving visitor counts: " + databaseError.getMessage());
            }
        });

        // Query the database for orders within the last 7 days
        DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
        orderRef.orderByChild("date").startAt(startDateString).endAt(endDateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalOrderCount = 0;
                double totalSalesAmount = 0;
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderList order = orderSnapshot.getValue(OrderList.class);
                    if (order != null) {
                        totalOrderCount++;
                        if(order.getAccepted().equals("shipped")) {
                            totalSalesAmount += order.getTotalPrice();
                        }
                    }
                }
                // Set the total order count and total sales amount
                totalOrder.setText(String.valueOf(totalOrderCount));
                totalSales.setText("\u20B9" + " " + String.format("%.2f", totalSalesAmount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving orders: " + databaseError.getMessage());
            }
        });
        setLastDaysSessionGraph(-6);
        setLastDaysOrderGraph(-6);
        setLastDaysSalesGraph(-6);
        enableButtons();
    }


    private void setLast30Days() {
        disableButtons();
        allTime.setBackgroundResource(R.drawable.background_last_normal);
        allTime.setTextColor(Color.GRAY);
        Last30.setBackgroundResource(R.drawable.background_last_pressed);
        Last30.setTextColor(Color.BLACK);
        Last7.setBackgroundResource(R.drawable.background_last_normal);
        Last7.setTextColor(Color.GRAY);
        today.setBackgroundResource(R.drawable.background_last_normal);
        today.setTextColor(Color.GRAY);

        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime(); // Current date

        // Subtract 30 days from the current date
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date startDate = calendar.getTime();

        // Convert the dates to the format used in the database
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);

        // Query the database for visitor counts within the last 30 days
        DatabaseReference visitorRef = database.getReference().child("VisitorCounts").child(ShopName);
        visitorRef.orderByKey().startAt(startDateString).endAt(endDateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long totalVisitorCount = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Long count = snapshot.getValue(Long.class);
                    if (count != null) {
                        totalVisitorCount += count;
                    }
                }
                // Set the total visitor count
                siteVisitCount.setText(String.valueOf(totalVisitorCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving visitor counts: " + databaseError.getMessage());
            }
        });

        // Query the database for orders within the last 30 days
        DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
        orderRef.orderByChild("date").startAt(startDateString).endAt(endDateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalOrderCount = 0;
                double totalSalesAmount = 0;
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderList order = orderSnapshot.getValue(OrderList.class);
                    if (order != null) {
                        totalOrderCount++;
                        if(order.getAccepted().equals("shipped")) {
                            totalSalesAmount += order.getTotalPrice();
                        }
                    }
                }
                // Set the total order count and total sales amount
                totalOrder.setText(String.valueOf(totalOrderCount));
                totalSales.setText("\u20B9" + " " + String.format("%.2f", totalSalesAmount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving orders: " + databaseError.getMessage());
            }
        });
        setLastDaysSessionGraph(-29);
        setLastDaysOrderGraph(-29);
        setLastDaysSalesGraph(-29);
        enableButtons();
    }
    private void setLastDaysSalesGraph(Integer i) {
        // Get the start and end dates for the last 30 days
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime(); // Current date
        calendar.add(Calendar.DAY_OF_MONTH, i); // Subtract 29 days to get the start date
        Date startDate = calendar.getTime();
        // Convert the dates to the format used in the database
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);

        // Query the database for orders within the last 30 days
        DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
        orderRef.orderByChild("date").startAt(startDateString).endAt(endDateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Initialize TreeMap to store sales amounts for each day
                TreeMap<String, Double> salesMap = new TreeMap<>();

                // Iterate through each day and initialize amounts to 0
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                while (!calendar.getTime().after(endDate)) {
                    String dateString = dateFormat.format(calendar.getTime());
                    salesMap.put(dateString, 0.0);
                    calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
                }

                // Update sales amounts with retrieved data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dateString = snapshot.child("date").getValue(String.class);
                    String acceptedStatus = snapshot.child("accepted").getValue(String.class);
                    double totalPrice = snapshot.child("totalPrice").getValue(Double.class);
                    if (dateString != null && acceptedStatus != null && acceptedStatus.equals("shipped") &&  !Double.isNaN(totalPrice)) {
                        double amount = salesMap.getOrDefault(dateString, 0.0);
                        salesMap.put(dateString, amount + totalPrice);
                    }
                }

                // Prepare data for chart
                ArrayList<BarEntry> dataVals = new ArrayList<>();
                ArrayList<String> xLabels = new ArrayList<>(salesMap.keySet()); // to store x-axis labels

                // Iterate through TreeMap to populate dataVals
                int index = 0; // Counter to keep track of x-axis index
                for (Map.Entry<String, Double> entry : salesMap.entrySet()) {
                    double salesAmount = entry.getValue();

                    // Add entry to dataVals
                    dataVals.add(new BarEntry(index++, (float) salesAmount));
                }

                // Create BarDataSet and BarData
                BarDataSet barDataSet = new BarDataSet(dataVals, "Total Sales");
                int barColor = ContextCompat.getColor(getContext(), R.color.green);
                barDataSet.setColor(barColor);
                BarData data = new BarData(barDataSet);

                // Set data to the chart
                barChart2.setData(data);

                // Customize x-axis labels
                XAxis xAxis = barChart2.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels)); // Set custom value formatter for x-axis
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position
                xAxis.setLabelRotationAngle(45);

                // Customize y-axis labels (optional)
                YAxis yAxis = barChart2.getAxisLeft();
                yAxis.setValueFormatter(new DefaultAxisValueFormatter(0)); // Set y-axis to display integer values

                // Hide right y-axis
                YAxis rightYAxis = barChart2.getAxisRight();
                rightYAxis.setEnabled(false);

                barChart3.getDescription().setEnabled(false); // Hide description

                barChart3.invalidate(); // Refresh chart
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving sales data: " + databaseError.getMessage());
            }
        });
    }

    private void setLastDaysOrderGraph(Integer i) {
        // Get the start and end dates for the last 30 days
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime(); // Current date
        calendar.add(Calendar.DAY_OF_MONTH, i); // Subtract 29 days to get the start date
        Date startDate = calendar.getTime();
        // Convert the dates to the format used in the database
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);
        // Query the database for orders within the last 30 days
        DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
        orderRef.orderByChild("date").startAt(startDateString).endAt(endDateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Initialize TreeMap to store order counts for each day
                TreeMap<String, Integer> orderMap = new TreeMap<>();

                // Iterate through each day and initialize counts to 0
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                while (!calendar.getTime().after(endDate)) {
                    String dateString = dateFormat.format(calendar.getTime());
                    orderMap.put(dateString, 0);
                    calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
                }

                // Update order counts with retrieved data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dateString = snapshot.child("date").getValue(String.class);
                    if (dateString != null) {
                        int count = orderMap.getOrDefault(dateString, 0);
                        orderMap.put(dateString, count + 1);
                    }
                }

                // Prepare data for chart
                ArrayList<BarEntry> dataVals = new ArrayList<>();
                ArrayList<String> xLabels = new ArrayList<>(orderMap.keySet()); // to store x-axis labels

                // Iterate through TreeMap to populate dataVals
                int index = 0; // Counter to keep track of x-axis index
                for (Map.Entry<String, Integer> entry : orderMap.entrySet()) {
                    int orderCount = entry.getValue();

                    // Add entry to dataVals
                    dataVals.add(new BarEntry(index++, orderCount));
                }

                // Create BarDataSet and BarData
                BarDataSet barDataSet = new BarDataSet(dataVals, "Total Orders");
                int barColor = ContextCompat.getColor(getContext(), R.color.green);
                barDataSet.setColor(barColor);
                BarData data = new BarData(barDataSet);

                // Set data to the chart
                barChart3.setData(data);

                // Customize x-axis labels
                XAxis xAxis = barChart3.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels)); // Set custom value formatter for x-axis
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position
                xAxis.setLabelRotationAngle(45);

                // Customize y-axis labels (optional)
                YAxis yAxis = barChart3.getAxisLeft();
                yAxis.setValueFormatter(new DefaultAxisValueFormatter(0)); // Set y-axis to display integer values

                // Hide right y-axis
                YAxis rightYAxis = barChart3.getAxisRight();
                rightYAxis.setEnabled(false);

                barChart3.getDescription().setEnabled(false); // Hide description

                barChart3.invalidate(); // Refresh chart
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving order counts: " + databaseError.getMessage());
            }
        });
    }

    private void setLastDaysSessionGraph(Integer i) {
        // Get the start and end dates for the last 30 days
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime(); // Current date
        calendar.add(Calendar.DAY_OF_MONTH, i); // Subtract 29 days to get the start date
        Date startDate = calendar.getTime();
        // Convert the dates to the format used in the database
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);

        // Query the database for visitor counts within the last 30 days
        DatabaseReference visitorRef = database.getReference().child("VisitorCounts").child(ShopName);
        visitorRef.orderByKey().startAt(startDateString).endAt(endDateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Initialize TreeMap to store visitor counts for each day
                TreeMap<String, Long> visitorMap = new TreeMap<>();

                // Iterate through each day and initialize counts to 0
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                while (!calendar.getTime().after(endDate)) {
                    String dateString = dateFormat.format(calendar.getTime());
                    visitorMap.put(dateString, 0L);
                    calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
                }

                // Update visitor counts with retrieved data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dateString = snapshot.getKey();
                    Long count = snapshot.getValue(Long.class);
                    if (dateString != null && count != null) {
                        visitorMap.put(dateString, count);
                    }
                }

                // Prepare data for chart
                ArrayList<BarEntry> dataVals = new ArrayList<>();
                ArrayList<String> xLabels = new ArrayList<>(visitorMap.keySet()); // to store x-axis labels

                // Iterate through TreeMap to populate dataVals
                int index = 0; // Counter to keep track of x-axis index
                for (Map.Entry<String, Long> entry : visitorMap.entrySet()) {
                    long visitorCount = entry.getValue();

                    // Add entry to dataVals
                    dataVals.add(new BarEntry(index++, visitorCount));
                }

                // Create BarDataSet and BarData
                BarDataSet barDataSet = new BarDataSet(dataVals, "Site Sessions");
                int barColor = ContextCompat.getColor(getContext(), R.color.green);
                barDataSet.setColor(barColor);
                BarData data = new BarData(barDataSet);

                // Set data to the chart
                barChart.setData(data);

                // Customize x-axis labels
                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels)); // Set custom value formatter for x-axis
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position
                xAxis.setLabelRotationAngle(45);

                // Customize y-axis labels (optional)
                YAxis yAxis = barChart.getAxisLeft();
                yAxis.setValueFormatter(new DefaultAxisValueFormatter(0)); // Set y-axis to display integer values

                // Hide right y-axis
                YAxis rightYAxis = barChart.getAxisRight();
                rightYAxis.setEnabled(false);

                barChart.getDescription().setEnabled(false); // Hide description

                barChart.invalidate(); // Refresh chart
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving visitor counts: " + databaseError.getMessage());
            }
        });

    }

    class MonthYearComparator implements Comparator<String> {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yyyy", Locale.US);

        @Override
        public int compare(String s1, String s2) {
            try {
                Date date1 = sdf.parse(s1);
                Date date2 = sdf.parse(s2);
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }




    private void setAllTime() {
        disableButtons();
        allTime.setBackgroundResource(R.drawable.background_last_pressed);
        allTime.setTextColor(Color.BLACK);
        Last30.setBackgroundResource(R.drawable.background_last_normal);
        Last30.setTextColor(Color.GRAY);
        Last7.setBackgroundResource(R.drawable.background_last_normal);
        Last7.setTextColor(Color.GRAY);
        today.setBackgroundResource(R.drawable.background_last_normal);
        today.setTextColor(Color.GRAY);
        DatabaseReference visitorRef = database.getReference().child("VisitorCounts").child(ShopName);
        DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
        visitorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long totalVisitorCount = 0;
                // Iterate through each child node
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the visitor count for each hour
                    Long count = snapshot.getValue(Long.class);
                    if (count != null) {
                        totalVisitorCount += count;
                    }
                }
                // Set total visitor count
                siteVisitCount.setText(String.valueOf(totalVisitorCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving visitor counts: " + databaseError.getMessage());
            }
        });
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalSalesAmount = 0;
                long totalOrderCount=dataSnapshot.getChildrenCount();
                // Sum up all-time total sales
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderList order = orderSnapshot.getValue(OrderList.class);
                    if (order != null) {
                        if (order.getAccepted().equals("shipped")) {
                            totalSalesAmount += order.getTotalPrice();
                        }
                    }
                }
                // Set total sales and total order count
                totalOrder.setText(String.valueOf(totalOrderCount));
                totalSales.setText("\u20B9" + " " + String.format("%.2f", totalSalesAmount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving orders: " + databaseError.getMessage());
            }
        });
        //graph
        setSiteSessionGraph();
        setSalesGraph();
        setTotalOrdersGraph();
        enableButtons();

    }
    private void setSalesGraph() {
        DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TreeMap<String, Double> salesMap = new TreeMap<>(new MonthYearComparator()); // TreeMap to store sales amount for each month

                // Iterate through each order and aggregate sales amount by month
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderList order = orderSnapshot.getValue(OrderList.class);
                    double totalPrice=0;
                    if (order != null) {
                        String dateString = order.getDate(); // Assuming the order object has a date field
                        if(order.getAccepted().equals("shipped")) {
                            totalPrice = order.getTotalPrice();
                        }

                        // Extract year and month from the date string (assuming yyyy-mm-dd format)
                        String[] dateParts = dateString.split("-");
                        String monthYearString = dateParts[0] + "-" + dateParts[1];

                        // Add sales amount to the map, aggregating if month-year already exists
                        salesMap.put(monthYearString, salesMap.getOrDefault(monthYearString, 0.0) + totalPrice);
                    }
                }

                // Prepare data for chart
                ArrayList<BarEntry> dataVals = new ArrayList<>();
                ArrayList<String> xLabels = new ArrayList<>(); // to store x-axis labels

                int i = 0;
                for (Map.Entry<String, Double> entry : salesMap.entrySet()) {
                    String monthYear = entry.getKey();

                    double salesAmount = entry.getValue();

                    // Add entry to dataVals
                    dataVals.add(new BarEntry(i++, (float) salesAmount));
                    xLabels.add(monthYear); // Add month-year to x-axis labels
                }

                // Create BarDataSet and BarData
                BarDataSet barDataSet = new BarDataSet(dataVals, "Total Sales");
                int barColor = ContextCompat.getColor(getContext(), R.color.green);
                barDataSet.setColor(barColor);
                BarData data = new BarData(barDataSet);

                // Set data to the chart
                barChart2.setData(data);
                // Customize x-axis labels
                XAxis xAxis = barChart2.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels)); // Set custom value formatter for x-axis
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position
                xAxis.setLabelRotationAngle(45);

                // Customize y-axis labels (optional)
                YAxis yAxis = barChart2.getAxisLeft();
                yAxis.setValueFormatter(new DefaultAxisValueFormatter(2)); // Set y-axis to display decimal values with 2 decimal places

                // Hide right y-axis
                YAxis rightYAxis = barChart2.getAxisRight();
                rightYAxis.setEnabled(false);

                // Hide description
                barChart2.getDescription().setEnabled(false);

                // Refresh chart
                barChart2.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving orders: " + databaseError.getMessage());
            }
        });
    }

    private void setSiteSessionGraph() {
        // Query the database for all visitor counts
        DatabaseReference visitorRef = database.getReference().child("VisitorCounts").child(ShopName);
        visitorRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TreeMap<String, Long> visitorMap = new TreeMap<>(new MonthYearComparator()); // TreeMap to store visitor counts for each month

                // Iterate through each snapshot and aggregate visitor counts by month
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dateString = snapshot.getKey();
                    Long count = snapshot.getValue(Long.class);
                    if (dateString != null && count != null) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            Date dateObj = sdf.parse(dateString);
                            if (dateObj != null) {
                                String monthYear = getMonthYearFromDate(dateObj);
                                // Add visitor count to the map, aggregating if month-year already exists
                                visitorMap.put(monthYear, visitorMap.getOrDefault(monthYear, 0L) + count);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Prepare data for chart
                ArrayList<BarEntry> dataVals = new ArrayList<>();
                ArrayList<String> xLabels = new ArrayList<>(visitorMap.keySet()); // to store x-axis labels

                // Iterate through TreeMap to populate dataVals
                int index = 0; // Counter to keep track of x-axis index
                for (Map.Entry<String, Long> entry : visitorMap.entrySet()) {
                    long visitorCount = entry.getValue();

                    // Add entry to dataVals
                    dataVals.add(new BarEntry(index++, visitorCount));
                }

                // Create BarDataSet and BarData
                BarDataSet barDataSet = new BarDataSet(dataVals, "Site Sessions");
                int barColor = ContextCompat.getColor(getContext(), R.color.green);
                barDataSet.setColor(barColor);
                BarData data = new BarData(barDataSet);

                // Set data to the chart
                barChart.setData(data);

                // Customize x-axis labels
                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels)); // Set custom value formatter for x-axis
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position
                xAxis.setLabelRotationAngle(45);

                // Customize y-axis labels (optional)
                YAxis yAxis = barChart.getAxisLeft();
                yAxis.setValueFormatter(new DefaultAxisValueFormatter(0)); // Set y-axis to display integer values

                // Hide right y-axis
                YAxis rightYAxis = barChart.getAxisRight();
                rightYAxis.setEnabled(false);

                barChart.getDescription().setEnabled(false); // Hide description

                barChart.invalidate(); // Refresh chart
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving visitor counts: " + databaseError.getMessage());
            }
        });
    }


    private void setTotalOrdersGraph() {
        DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TreeMap<String, Integer> totalOrdersMap = new TreeMap<>(new MonthYearComparator()); // TreeMap to store total orders for each month

                // Iterate through each order and aggregate total orders by month
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderList order = orderSnapshot.getValue(OrderList.class);
                    if (order != null) {
                        String dateString = order.getDate(); // Assuming the order object has a date field

                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            Date dateObj = sdf.parse(dateString);
                            if (dateObj != null) {
                                String monthYearString = getMonthYearFromDate(dateObj);
                                // Add total orders to the map, aggregating if month-year already exists
                                totalOrdersMap.put(monthYearString, totalOrdersMap.getOrDefault(monthYearString, 0) + 1);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Prepare data for chart
                ArrayList<BarEntry> dataVals = new ArrayList<>();
                ArrayList<String> xLabels = new ArrayList<>(totalOrdersMap.keySet()); // to store x-axis labels

                // Iterate through TreeMap to populate dataVals
                int i = 0; // Counter to keep track of x-axis index
                for (Map.Entry<String, Integer> entry : totalOrdersMap.entrySet()) {
                    int totalOrders = entry.getValue();

                    // Add entry to dataVals
                    dataVals.add(new BarEntry(i++, totalOrders));
                }

                // Create BarDataSet and BarData
                BarDataSet barDataSet = new BarDataSet(dataVals, "Total Orders");
                int barColor = ContextCompat.getColor(getContext(), R.color.green);
                barDataSet.setColor(barColor);
                BarData data = new BarData(barDataSet);

                // Set data to the chart
                barChart3.setData(data);

                // Customize x-axis labels
                XAxis xAxis = barChart3.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels)); // Set custom value formatter for x-axis
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position
                xAxis.setLabelRotationAngle(45);

                // Customize y-axis labels (optional)
                YAxis yAxis = barChart3.getAxisLeft();
                yAxis.setValueFormatter(new DefaultAxisValueFormatter(0)); // Set y-axis to display integer values

                // Hide right y-axis
                YAxis rightYAxis = barChart3.getAxisRight();
                rightYAxis.setEnabled(false);

                barChart3.getDescription().setEnabled(false); // Hide description

                barChart3.invalidate(); // Refresh chart
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving orders: " + databaseError.getMessage());
            }
        });
    }



    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }


    // Helper function to extract month and year from date string
    private String getMonthYearFromDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yyyy", Locale.US);
        return sdf.format(date);
    }

    // Custom Comparator to sort TreeMap by month and year


    private void enableButtons() {
        allTime.setEnabled(true);
        Last30.setEnabled(true);
        Last7.setEnabled(true);
        today.setEnabled(true);
        sessionLayout.setEnabled(true);
        salesLayout.setEnabled(true);
        orderLayout.setEnabled(true);
    }

    private void disableButtons() {
        allTime.setEnabled(false);
        Last30.setEnabled(false);
        Last7.setEnabled(false);
        today.setEnabled(false);
        sessionLayout.setEnabled(false);
        salesLayout.setEnabled(false);
        orderLayout.setEnabled(false);

    }
    private void makeLog(String s) {
        Log.d("AshuraDB", s);
    }
}
