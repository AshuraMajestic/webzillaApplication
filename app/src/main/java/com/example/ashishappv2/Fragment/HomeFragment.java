package com.example.ashishappv2.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;

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
    private LinearLayout salesShowLayout,SessionshowLayout;
    private FirebaseDatabase database;
    private String ShopName;

    private TextView shopname, shopLink, siteVisitCount, totalSales, totalOrder;

    private AppCompatButton allTime, Last30, Last7, today;
    BarChart barChart,barChart2;

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
        totalOrder = view.findViewById(R.id.totalOrder);
        salesLayout=view.findViewById(R.id.salesLayout);
        orderLayout=view.findViewById(R.id.orderLayout);

        SessionshowLayout=view.findViewById(R.id.siteShow);
        salesShowLayout=view.findViewById(R.id.salesShow);

        barChart = view.findViewById(R.id.lineChart);
        barChart2 = view.findViewById(R.id.lineChart2);
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
                    shopname.setText(shopName);
                    shopLink.setText("https://ashishweb-jv5n.onrender.com/" + ShopName);
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
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
        String todayDateString = dateFormat.format(currentDate);

        // Query the database for visitor count for today
        DatabaseReference visitorRef = database.getReference().child("VisitorCounts").child(ShopName).child(todayDateString);
        visitorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long visitorCount = 0;
                if (dataSnapshot.exists()) {
                    visitorCount = dataSnapshot.getValue(Long.class);
                }
                // Set visitor count
                siteVisitCount.setText(String.valueOf(visitorCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving visitor count: " + databaseError.getMessage());
            }
        });

        // Query the database for orders for today
        DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
        orderRef.orderByChild("date").equalTo(todayDateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalOrderCount = 0;
                double totalSalesAmount = 0;

                // Iterate through the retrieved data and calculate total order count and total sales amount for today
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderList order = orderSnapshot.getValue(OrderList.class);
                    if (order != null) {
                        totalOrderCount++;
                        totalSalesAmount += order.getTotalPrice();
                    }
                }
                // Set total order count and total sales amount
                totalOrder.setText(String.valueOf(totalOrderCount));
                totalSales.setText("\u20B9" + " " + String.format("%.2f", totalSalesAmount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving orders: " + databaseError.getMessage());
            }
        });

        setTodaySessionGraph();
        setTodaySalesGraph();

        enableButtons();
    }
    private void setTodaySessionGraph() {
        // Create a BarChart instance for session counts
        barChart.getDescription().setEnabled(false); // Disable chart description

        // Get today's date
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.US);
        String todayDay = dateFormat.format(currentDate);

        // Create BarEntry list for session counts
        ArrayList<BarEntry> sessionEntries = new ArrayList<>();
        // Populate the sessionEntries with data for today (you may already have this data)
        sessionEntries.add(new BarEntry(0, 100)); // Example data for today
        sessionEntries.add(new BarEntry(1, 150)); // Example data for tomorrow

        // Create BarDataSet for session data
        BarDataSet sessionDataSet = new BarDataSet(sessionEntries, "Session Counts");
        sessionDataSet.setColor(Color.BLUE);

        // Create BarData and set it to the chart
        BarData sessionData = new BarData(sessionDataSet);
        barChart.setData(sessionData);

        // Customize X-axis labels
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{todayDay, "Tomorrow"})); // Set labels for today and tomorrow
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(false);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(1f);
        xAxis.setLabelCount(2); // Number of labels to show

        // Refresh the chart
        barChart.invalidate();
    }

    private void setTodaySalesGraph() {
        // Create a BarChart instance for sales amounts
        barChart2.getDescription().setEnabled(false); // Disable chart description

        // Get today's date
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.US);
        String todayDay = dateFormat.format(currentDate);

        // Create BarEntry list for sales amounts
        ArrayList<BarEntry> salesEntries = new ArrayList<>();
        // Populate the salesEntries with data for today (you may already have this data)
        salesEntries.add(new BarEntry(0, 500)); // Example data for today
        salesEntries.add(new BarEntry(1, 700)); // Example data for tomorrow

        // Create BarDataSet for sales data
        BarDataSet salesDataSet = new BarDataSet(salesEntries, "Sales Amounts");
        salesDataSet.setColor(Color.GREEN);

        // Create BarData and set it to the chart
        BarData salesData = new BarData(salesDataSet);
        barChart2.setData(salesData);

        // Customize X-axis labels
        XAxis xAxis = barChart2.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{todayDay, "Tomorrow"})); // Set labels for today and tomorrow
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(false);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(1f);
        xAxis.setLabelCount(2); // Number of labels to show

        // Refresh the chart
        barChart2.invalidate();
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
// Get the current date
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime(); // Current date

// Subtract 7 days from the current date
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date startDate = calendar.getTime();

// Convert the dates to the format used in the database
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
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
                        totalSalesAmount += order.getTotalPrice();
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
        setLast7DaysSalesGraph();
        setLast7DaysSession();
        enableButtons();
    }
    private void setLast7DaysSession() {
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime(); // Current date

        // Subtract 7 days from the current date
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date startDate = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);

        // Initialize ArrayLists to store session counts and dates
        ArrayList<Long> sessionCounts = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();

        // Loop through each day of the previous 7 days
        while (!startDate.after(endDate)) {
            String dateString = dateFormat.format(startDate);
            dates.add(dateString);

            // Query the database for visitor counts for each day
            DatabaseReference visitorRef = database.getReference().child("VisitorCounts").child(ShopName).child(dateString);
            visitorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Long count = dataSnapshot.getValue(Long.class);
                    if (count != null) {
                        sessionCounts.add(count);
                    } else {
                        sessionCounts.add(0L); // If no data available, set count to 0
                    }

                    // Check if all queries are finished
                    if (sessionCounts.size() == 7) {
                        // Create BarEntry list for session counts
                        ArrayList<BarEntry> sessionEntries = new ArrayList<>();
                        for (int i = 0; i < sessionCounts.size(); i++) {
                            sessionEntries.add(new BarEntry(i, sessionCounts.get(i)));
                        }

                        // Create BarDataSet for session data
                        BarDataSet sessionDataSet = new BarDataSet(sessionEntries, "Session Counts");
                        sessionDataSet.setColor(Color.BLUE);

                        // Create BarData and set it to the chart
                        BarData sessionData = new BarData(sessionDataSet);

                        // Customize x-axis labels
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates)); // Set custom value formatter for x-axis
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position
                        xAxis.setLabelRotationAngle(45);
                        xAxis.setGranularity(1); // Set granularity to display labels for each entry
                        xAxis.setGranularityEnabled(true); // Enable granularity

                        // Set data to the chart
                        barChart.setData(sessionData);
                        barChart.invalidate(); // Refresh the chart
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    makeLog("Error retrieving visitor counts: " + databaseError.getMessage());
                }
            });

            // Move to the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            startDate = calendar.getTime();
        }
    }

    private void setLast7DaysSalesGraph() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime(); // Current date

        // Subtract 7 days from the current date
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date startDate = calendar.getTime();

        // Convert the dates to the format used in the database
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);

        // Initialize ArrayLists to store sales amounts and dates
        ArrayList<Double> salesAmountsList = new ArrayList<>();
        ArrayList<String> datesList = new ArrayList<>();

        // Loop through each day of the previous 7 days
        while (!startDate.after(endDate)) {
            String dateString = dateFormat.format(startDate);
            datesList.add(dateString);

            // Query the database for orders for each day
            DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
            orderRef.orderByChild("date").equalTo(dateString).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double totalSalesAmount = 0;

                    // Iterate through the retrieved data and calculate total sales amount for the day
                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                        OrderList order = orderSnapshot.getValue(OrderList.class);
                        if (order != null) {
                            totalSalesAmount += order.getTotalPrice();
                        }
                    }

                    // Add the total sales amount for the day to the list
                    salesAmountsList.add(totalSalesAmount);

                    // Check if all queries are finished
                    if (salesAmountsList.size() == 7) {
                        // Create BarEntry list for sales amounts
                        ArrayList<BarEntry> salesEntries = new ArrayList<>();
                        for (int i = 0; i < salesAmountsList.size(); i++) {
                            salesEntries.add(new BarEntry(i, salesAmountsList.get(i).floatValue()));
                        }

                        // Create BarDataSet for sales data
                        BarDataSet salesDataSet = new BarDataSet(salesEntries, "Sales Amounts");
                        salesDataSet.setColor(Color.GREEN);

                        // Create BarData and set it to the chart
                        BarData salesData = new BarData(salesDataSet);

                        // Customize x-axis labels
                        XAxis xAxis = barChart2.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(datesList)); // Set custom value formatter for x-axis
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position
                        xAxis.setLabelRotationAngle(45);
                        xAxis.setGranularity(1); // Set granularity to display labels for each entry
                        xAxis.setGranularityEnabled(true); // Enable granularity

                        // Set data to the chart
                        barChart2.setData(salesData);
                        barChart2.invalidate(); // Refresh the chart
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    makeLog("Error retrieving orders: " + databaseError.getMessage());
                }
            });

            // Move to the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            startDate = calendar.getTime();
        }
    }
    // Utility method to get the index of a day in the 7-day range
    private int getDayIndex(String startDateString, String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
        try {
            Date startDate = dateFormat.parse(startDateString);
            Date date = dateFormat.parse(dateString);
            long diffInMillies = Math.abs(date.getTime() - startDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (diff >= 0 && diff < 7) {
                return (int) diff;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
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

        DatabaseReference shopRef = database.getReference().child("Shops").child(ShopName);
        DatabaseReference visitorRef = database.getReference().child("VisitorCounts").child(ShopName);
        DatabaseReference orderRef = shopRef.child("Orders");

// Get the current date
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime(); // Current date

// Subtract 30 days from the current date
        calendar.add(Calendar.DAY_OF_MONTH, -29);
        Date startDate = calendar.getTime();

// Convert the dates to the format used in the database
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);

// Query visitor counts within the date range
        Query visitorQuery = visitorRef.orderByKey().startAt(startDateString).endAt(endDateString);
        visitorQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long totalVisitorCount = 0;

                // Iterate through visitor counts and sum up total visitor count
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

// Query orders within the date range
        Query orderQuery = orderRef.orderByChild("date").startAt(startDateString).endAt(endDateString);
        orderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalOrderCount = 0;
                double totalSalesAmount = 0;

                // Iterate through orders and sum up total order count and sales amount
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderList order = orderSnapshot.getValue(OrderList.class);
                    if (order != null) {
                        totalOrderCount++;
                        totalSalesAmount += order.getTotalPrice();
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
        set30DaysSessionGraph();
        set30DaysSalesGraph();
        enableButtons();
    }

    private void set30DaysSalesGraph() {
        DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Create a TreeMap to store sales amount for each date
                TreeMap<String, Double> salesMap = new TreeMap<>();

                // Iterate through each order and aggregate sales amount by date
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderList order = orderSnapshot.getValue(OrderList.class);
                    if (order != null) {
                        String dateString = order.getDate(); // Assuming the order object has a date field
                        double totalPrice = order.getTotalPrice();

                        // Update the salesMap with sales amount for the respective date
                        salesMap.put(dateString, salesMap.getOrDefault(dateString, 0.0) + totalPrice);
                    }
                }

                // Prepare data for chart
                ArrayList<BarEntry> dataVals = new ArrayList<>();
                ArrayList<String> xLabels = new ArrayList<>(salesMap.keySet()); // to store x-axis labels

                // Iterate through TreeMap to populate dataVals
                int i = 0;
                for (Map.Entry<String, Double> entry : salesMap.entrySet()) {
                    double salesAmount = entry.getValue();
                    // Add entry to dataVals
                    dataVals.add(new BarEntry(i++, (float) salesAmount));
                }

                // Create BarDataSet and BarData
                BarDataSet barDataSet = new BarDataSet(dataVals, "Total Sales");
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

    private void set30DaysSessionGraph() {
        // Query the database for visitor counts for the last 30 days
        DatabaseReference visitorRef = database.getReference().child("VisitorCounts").child(ShopName);

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime(); // Current date

        // Subtract 30 days from the current date
        calendar.add(Calendar.DAY_OF_MONTH, -29);
        Date startDate = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);

        visitorRef.orderByKey().startAt(startDateString).endAt(endDateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TreeMap<String, Long> visitorMap = new TreeMap<>(new DateComparator());

                // Iterate through each snapshot and aggregate visitor counts by date
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dateString = snapshot.getKey();
                    Long count = snapshot.getValue(Long.class);
                    if (dateString != null && count != null) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
                            Date dateObj = sdf.parse(dateString);
                            if (dateObj != null) {
                                visitorMap.put(dateString, count);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Prepare data for chart
                ArrayList<BarEntry> dataVals = new ArrayList<>();
                ArrayList<String> xLabels = new ArrayList<>();

                int index = 0; // Counter to keep track of x-axis index
                calendar.setTime(startDate); // Set calendar to start date
                while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
                    String date = dateFormat.format(calendar.getTime());
                    xLabels.add(date);
                    long visitorCount = visitorMap.containsKey(date) ? visitorMap.get(date) : 0;
                    dataVals.add(new BarEntry(index++, visitorCount));
                    calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
                }

                BarDataSet barDataSet = new BarDataSet(dataVals, "Site Sessions");
                BarData data = new BarData(barDataSet);

                // Set data to the chart
                barChart.setData(data);

                // Customize x-axis labels
                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels)); // Set custom value formatter for x-axis
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position
                xAxis.setLabelRotationAngle(45);
                xAxis.setGranularity(1); // Set granularity to display labels for each entry
                xAxis.setGranularityEnabled(true); // Enable granularity

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
                int totalOrderCount = (int) dataSnapshot.getChildrenCount();
                double totalSalesAmount = 0;
                // Sum up all-time total sales
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderList order = orderSnapshot.getValue(OrderList.class);
                    if (order != null) {
                        totalSalesAmount += order.getTotalPrice();
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
                    if (order != null) {
                        String dateString = order.getDate(); // Assuming the order object has a date field
                        double totalPrice = order.getTotalPrice();

                        // Extract month and year from the date string
                        String monthYearString = getMonthYearFromDate(dateString);

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

    // Helper function to extract month and year from date string
    private String getMonthYearFromDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
            Date date = sdf.parse(dateString);
            if (date != null) {
                SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMM-yyyy", Locale.US);
                return monthYearFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
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
                            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
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

    // Helper function to extract month and year from date string
    private String getMonthYearFromDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yyyy", Locale.US);
        return sdf.format(date);
    }

    // Custom Comparator to sort TreeMap by month and year
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



    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }


    private void enableButtons() {
        allTime.setEnabled(true);
        Last30.setEnabled(true);
        Last7.setEnabled(true);
        today.setEnabled(true);
        sessionLayout.setEnabled(true);
        orderLayout.setEnabled(true);
        salesLayout.setEnabled(true);
    }

    private void disableButtons() {
        allTime.setEnabled(false);
        Last30.setEnabled(false);
        Last7.setEnabled(false);
        today.setEnabled(false);

        sessionLayout.setEnabled(false);
        orderLayout.setEnabled(false);
        salesLayout.setEnabled(false);
    }
    private void makeLog(String s) {
        Log.d("AshuraDB", s);
    }
}
