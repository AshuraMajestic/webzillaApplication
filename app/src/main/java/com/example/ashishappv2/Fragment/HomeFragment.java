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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private LinearLayout sessionLayout,salesLayout,orderLayout;
    private LinearLayout salesShowLayout,SessionshowLayout;
    private FirebaseDatabase database;
    private String ShopName;

    private TextView shopname, shopLink, siteVisitCount, totalSales, totalOrder;

    private AppCompatButton allTime, Last30, Last7, today;
    LineChart lineChart,lineChart2;

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
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
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

        lineChart = view.findViewById(R.id.lineChart);
        lineChart2 = view.findViewById(R.id.lineChart2);
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
                    enableButtons();
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
                allTime.setBackgroundResource(R.drawable.background_last_normal);
                allTime.setTextColor(Color.GRAY);
                Last30.setBackgroundResource(R.drawable.background_last_pressed);
                Last30.setTextColor(Color.BLACK);
                Last7.setBackgroundResource(R.drawable.background_last_normal);
                Last7.setTextColor(Color.GRAY);
                today.setBackgroundResource(R.drawable.background_last_normal);
                today.setTextColor(Color.GRAY);

                // Calculate the start date for the last 30 days
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -30); // Go back 30 days from today
                String startDate = getFormattedDate(calendar.getTimeInMillis());
                updateVisitorCountLast30Days();

                // Query the database for orders within the last 30 days
                DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
                orderRef.orderByChild("date").startAt(startDate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int orderCount = 0;
                        double totalSalesAmount = 0;
                        // Iterate through the orders and calculate total sales and order count
                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            OrderList order = orderSnapshot.getValue(OrderList.class);
                            if (order != null) {
                                // Check if the order date is within the last 30 days
                                if (isWithinLast30Days(order.getDate())) {
                                    orderCount++;
                                    totalSalesAmount += order.getTotalPrice();
                                }
                            }
                        }
                        totalOrder.setText(String.valueOf(orderCount));
                        totalSales.setText("\u20B9" + " " + String.format("%.2f", totalSalesAmount));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        makeLog("Error retrieving orders: " + databaseError.getMessage());
                    }
                });
            }
        });
        Last7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allTime.setBackgroundResource(R.drawable.background_last_normal);
                allTime.setTextColor(Color.GRAY);
                Last30.setBackgroundResource(R.drawable.background_last_normal);
                Last30.setTextColor(Color.GRAY);
                Last7.setBackgroundResource(R.drawable.background_last_pressed);
                Last7.setTextColor(Color.BLACK);
                today.setBackgroundResource(R.drawable.background_last_normal);
                today.setTextColor(Color.GRAY);
                updateVisitorCountLast7Days();
                // Calculate the start date for the last 7 days
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -7); // Go back 7 days from today
                String startDate = getFormattedDate(calendar.getTimeInMillis());

                // Query the database for orders within the last 7 days
                DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
                orderRef.orderByChild("date").startAt(startDate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int orderCount = 0;
                        double totalSalesAmount = 0;
                        // Iterate through the orders and calculate total sales and order count
                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            OrderList order = orderSnapshot.getValue(OrderList.class);
                            if (order != null) {
                                // Check if the order date is within the last 7 days
                                if (isWithinLast7Days(order.getDate())) {
                                    orderCount++;
                                    totalSalesAmount += order.getTotalPrice();
                                }
                            }
                        }
                        totalOrder.setText(String.valueOf(orderCount));
                        totalSales.setText("\u20B9" + " " + String.format("%.2f", totalSalesAmount));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        makeLog("Error retrieving orders: " + databaseError.getMessage());
                    }
                });
            }
        });


        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allTime.setBackgroundResource(R.drawable.background_last_normal);
                allTime.setTextColor(Color.GRAY);
                Last30.setBackgroundResource(R.drawable.background_last_normal);
                Last30.setTextColor(Color.GRAY);
                Last7.setBackgroundResource(R.drawable.background_last_normal);
                Last7.setTextColor(Color.GRAY);
                today.setBackgroundResource(R.drawable.background_last_pressed);
                today.setTextColor(Color.BLACK);

                // Get today's date
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault());
                String todayDate = dateFormat.format(calendar.getTime());
                // Update visitor count
                updateVisitorCount(todayDate);
                // Query the database for orders placed today
                DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
                orderRef.orderByChild("date").equalTo(todayDate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int orderCount = 0;
                        double totalSalesAmount = 0;
                        // Iterate through the orders and calculate total sales and order count
                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            OrderList order = orderSnapshot.getValue(OrderList.class);
                            if (order != null) {
                                orderCount++;
                                totalSalesAmount += order.getTotalPrice();
                            }
                        }
                        totalOrder.setText(String.valueOf(orderCount));
                        totalSales.setText("\u20B9" + " " + String.format("%.2f", totalSalesAmount));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        makeLog("Error retrieving orders: " + databaseError.getMessage());
                    }
                });
            }
        });
    }

    private void updateVisitorCountLast7Days() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault());

        // Create a list to hold visitor counts for the last 7 days
        List<Long> visitorCounts = new ArrayList<>();

        // Iterate over the last 7 days
        for (int i = 0; i < 7; i++) {
            // Calculate the date for the current iteration
            String date = dateFormat.format(calendar.getTime());

            // Retrieve the visitor count for the current date and shop
            DatabaseReference visitorRef = database.getReference()
                    .child("VisitorCounts")
                    .child(ShopName) // Change to your shop name
                    .child(date);

            visitorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long visitorCount = 0; // Default visitor count to 0
                    if (dataSnapshot.exists()) {
                        visitorCount = dataSnapshot.getValue(Long.class);
                    }
                    visitorCounts.add(visitorCount);

                    // Check if all visitor counts for the last 7 days have been retrieved
                    if (visitorCounts.size() == 7) {
                        // Calculate the total visitor count for the last 7 days
                        long totalVisitorCount = 0;
                        for (Long count : visitorCounts) {
                            totalVisitorCount += count;
                        }
                        // Update UI with the total visitor count for the last 7 days
                        siteVisitCount.setText(String.valueOf(totalVisitorCount));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    makeLog("Error retrieving visitor count: " + databaseError.getMessage());
                }
            });

            // Move to the previous day
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
    }

    private void updateVisitorCountLast30Days() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault());

        // Create a list to hold visitor counts for the last 30 days
        List<Long> visitorCounts = new ArrayList<>();

        // Iterate over the last 30 days
        for (int i = 0; i < 30; i++) {
            // Calculate the date for the current iteration
            String date = dateFormat.format(calendar.getTime());

            // Retrieve the visitor count for the current date and shop
            DatabaseReference visitorRef = database.getReference()
                    .child("VisitorCounts")
                    .child(ShopName) // Change to your shop name
                    .child(date);

            final int finalI = i; // To access within inner class

            visitorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long visitorCount = 0; // Default visitor count to 0
                    if (dataSnapshot.exists()) {
                        visitorCount = dataSnapshot.getValue(Long.class);
                    }
                    visitorCounts.add(visitorCount);

                    // Check if all visitor counts for the last 30 days have been retrieved
                    if (finalI == 29) {
                        // Calculate the total visitor count for the last 30 days
                        long totalVisitorCount = 0;
                        for (Long count : visitorCounts) {
                            totalVisitorCount += count;
                        }
                        // Update UI with the total visitor count for the last 30 days
                        siteVisitCount.setText(String.valueOf(totalVisitorCount));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    makeLog("Error retrieving visitor count: " + databaseError.getMessage());
                }
            });

            // Move to the previous day
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
    }


    private void setAllTime() {
        allTime.setBackgroundResource(R.drawable.background_last_pressed);
        allTime.setTextColor(Color.BLACK); // Set text color of pressed button to black
        Last30.setBackgroundResource(R.drawable.background_last_normal);
        Last30.setTextColor(Color.GRAY); // Set text color of other buttons to grey
        Last7.setBackgroundResource(R.drawable.background_last_normal);
        Last7.setTextColor(Color.GRAY); // Set text color of other buttons to grey
        today.setBackgroundResource(R.drawable.background_last_normal);
        today.setTextColor(Color.GRAY); // Set text color of other buttons to grey
        updateVisitorCountAllTime();
        DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("Orders");
        DatabaseReference orderCountRef = database.getReference().child("Shops").child(ShopName).child("orderCount");
        orderCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String orderCount = String.valueOf(dataSnapshot.getValue(Integer.class));
                    if (orderCount != null) {
                        totalOrder.setText(orderCount);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving order count: " + databaseError.getMessage());
            }
        });

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalSalesAmount = 0;
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderList order = orderSnapshot.getValue(OrderList.class);
                    if (order != null) {
                        totalSalesAmount += order.getTotalPrice();
                    }
                }

                totalSales.setText("\u20B9" + " " + String.format("%.2f", totalSalesAmount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving total sales: " + databaseError.getMessage());
            }
        });

        DatabaseReference orderRef2 = database.getReference().child("counter").child(ShopName);
        orderRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String siteCount = String.valueOf(dataSnapshot.getValue(Integer.class));
                    if (siteCount != null) {
                        siteVisitCount.setText(siteCount);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving order count: " + databaseError.getMessage());
            }
        });
        fetchDataFromFirebase();
    }

    private void fetchDataFromFirebase() {
        FirebaseDatabase.getInstance().getReference("VisitorCounts").child(ShopName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Entry> dataVals = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<>(); // to store x-axis labels
                int index = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String date = snapshot.getKey();
                    Long count = snapshot.getValue(Long.class); // Correctly retrieve Long value
                    dataVals.add(new Entry(index++, count));
                    labels.add(date); // Adding date as a label
                }

                setupLineChart(dataVals, labels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
        DatabaseReference salesRef = database.getReference().child("Shops").child(ShopName).child("Orders");
        salesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double[] monthlySales = new double[12]; // Array to store monthly sales
                ArrayList<String> labels = new ArrayList<>(); // to store x-axis labels
                int index = 0;

                // Initialize monthly sales array to 0
                Arrays.fill(monthlySales, 0);

                // Iterate through orders and accumulate total sales for each month
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderList order = orderSnapshot.getValue(OrderList.class);
                    if (order != null) {
                        // Parse the string date to a Date object
                        Date date = parseDate(order.getDate());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        int month = cal.get(Calendar.MONTH); // Get month index (0-11)
                        double salesAmount = order.getTotalPrice();
                        monthlySales[month] += salesAmount; // Add sales to corresponding month
                    }
                }

                // Add month labels (Jan, Feb, ..., Dec) to the chart
                String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                labels.addAll(Arrays.asList(monthNames));

                // Setup line chart
                setupLineChart2(monthlySales, labels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving total sales: " + databaseError.getMessage());
            }
        });

    }

    private void setupLineChart2(double[] monthlySales, ArrayList<String> labels) {
        ArrayList<Entry> dataVals = new ArrayList<>();
        for (int i = 0; i < monthlySales.length; i++) {
            dataVals.add(new Entry(i, (float) monthlySales[i]));
        }

        LineDataSet lineDataSet = new LineDataSet(dataVals, "Monthly Sales");
        LineData data = new LineData(lineDataSet);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(5f); // Set line width

        lineChart2.setData(data);

        // Customize x-axis labels
        XAxis xAxis = lineChart2.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); // Set custom value formatter for x-axis
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position
        xAxis.setDrawGridLines(false); // Hide grid lines
        xAxis.setGranularity(1f); // Set granularity to display all months
        xAxis.setLabelCount(labels.size()); // Set label count to display all months
        xAxis.setLabelRotationAngle(-45); // Rotate labels to avoid overlap

        // Hide y-axis labels and grid lines
        YAxis yAxis = lineChart2.getAxisLeft();
        yAxis.setDrawLabels(false);
        yAxis.setDrawGridLines(false);

        // Hide right y-axis
        YAxis rightYAxis = lineChart2.getAxisRight();
        rightYAxis.setEnabled(false);

        lineChart2.getDescription().setEnabled(false); // Hide description

        lineChart2.invalidate(); // Refresh chart
    }


    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault());
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void setupLineChart(ArrayList<Entry> dataVals, ArrayList<String> labels) {
        LineDataSet lineDataSet = new LineDataSet(dataVals, "Visitor Counts");
        LineData data = new LineData(lineDataSet);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(5f); // Set line width

        lineChart.setData(data);

        // Hide y-axis labels
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawLabels(false);

        // Customize x-axis labels
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); // Set custom value formatter for x-axis
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position
        yAxis.setDrawGridLines(false); // Show horizontal grid lines
        yAxis.setGridColor(Color.GRAY); // Set grid line color
        yAxis.setGranularity(5f);
        lineChart.invalidate();
    }
    private void updateVisitorCount(String date) {
        DatabaseReference visitorRef = database.getReference().child("VisitorCounts").child(ShopName).child(date);
        visitorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long visitorCount = dataSnapshot.getValue(Long.class);
                    siteVisitCount.setText(String.valueOf(visitorCount));
                } else {
                    // Handle case when visitor count data for the given date does not exist
                    siteVisitCount.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving visitor count: " + databaseError.getMessage());
            }
        });
    }
    private boolean isWithinLast7Days(String orderDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault());
            Date orderDateTime = dateFormat.parse(orderDate);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -7); // Go back 7 days from today
            Date last7DaysDate = calendar.getTime();
            return orderDateTime.after(last7DaysDate) || orderDateTime.equals(last7DaysDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean isWithinLast30Days(String orderDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault());
            Date orderDateTime = dateFormat.parse(orderDate);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -30); // Go back 30 days from today
            Date last30DaysDate = calendar.getTime();
            return orderDateTime.after(last30DaysDate) || orderDateTime.equals(last30DaysDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    private String getFormattedDate(long timestamp) {
        // Format the timestamp to match the date format in the database
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault());
        return dateFormat.format(new Date(timestamp));
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
    private void updateVisitorCountAllTime() {
        DatabaseReference visitorRef = database.getReference().child("VisitorCounts").child(ShopName);
        visitorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long totalVisitorCount = 0;
                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    long visitorCount = dateSnapshot.getValue(Long.class);
                    totalVisitorCount += visitorCount;
                }
                siteVisitCount.setText(String.valueOf(totalVisitorCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving visitor count: " + databaseError.getMessage());
            }
        });
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
