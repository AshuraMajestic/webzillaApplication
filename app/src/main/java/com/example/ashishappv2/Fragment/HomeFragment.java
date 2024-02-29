package com.example.ashishappv2.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashishappv2.Adapter.videoAdapter;
import com.example.ashishappv2.Domains.Product;
import com.example.ashishappv2.Domains.userData;
import com.example.ashishappv2.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String ShopName;
    private DatabaseReference userRef;
    private TextView shopname,shopLink,siteVisitCount,totalSales,totalOrder;
    private LineChart visitorChart;
    private AppCompatButton allTime,Last30,Last7,today;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        shopname = view.findViewById(R.id.name);
        shopLink=view.findViewById(R.id.link);
        siteVisitCount=view.findViewById(R.id.SiteSession);
        totalSales=view.findViewById(R.id.TotalSales);
        totalOrder=view.findViewById(R.id.totalOrder);
        allTime=view.findViewById(R.id.alltime);
        Last30=view.findViewById(R.id.last30days);
        Last7=view.findViewById(R.id.last7days);
        today=view.findViewById(R.id.today);
        visitorChart=view.findViewById(R.id.visitor_chart);
        visitorChart.setDrawGridBackground(false);
        visitorChart.setBackgroundColor(Color.TRANSPARENT);
updateGraph(0);

        userRef = FirebaseDatabase.getInstance().getReference().child("UserData").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userData user = dataSnapshot.getValue(userData.class);
                    if (user != null) {
                        String shopName = user.getShopname();
                        String link = user.getLink();
                        ShopName=link;
                        setAllTime();
                        // Set shop name in the TextView
                        shopname.setText(shopName);
                        shopLink.setText("https://ashishweb-jv5n.onrender.com/"+link);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving data: " + databaseError.getMessage());
            }
        });

        return  view;
    }
    private void updateGraph(long count) {
        // Create entries for the line chart
        ArrayList<Entry> entries = new ArrayList<>();

        // Add a point at (0, 0) to draw a straight line
        entries.add(new Entry(0, 0));

        // Create a dataset with entries
        LineDataSet dataSet = new LineDataSet(entries, "Visitor Count");
        dataSet.setColor(Color.BLUE); // Set line color

        LineData lineData = new LineData(dataSet);
        // Customize the line chart
        Description desc = new Description();
        desc.setEnabled(false); // Hide description
        visitorChart.setDescription(desc);
        visitorChart.setData(lineData);
        visitorChart.setGridBackgroundColor(Color.TRANSPARENT);
        visitorChart.setDrawBorders(false);
        visitorChart.setDrawGridBackground(false);
        visitorChart.getLegend().setEnabled(false); // Hide legend
        visitorChart.getAxisLeft().setDrawLabels(false); // Hide left axis labels
        visitorChart.getAxisRight().setDrawLabels(false); // Hide right axis labels
        visitorChart.getXAxis().setDrawLabels(false); // Hide x-axis labels
        visitorChart.setDrawBorders(false); // Hide chart borders
        visitorChart.invalidate(); // Refresh the chart
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
                siteVisitCount.setText("30");
                totalSales.setText("\u20B9"+" "+"30.00");
                totalOrder.setText("50");
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
                siteVisitCount.setText("30");
                totalSales.setText("\u20B9"+" "+"30.00");
                totalOrder.setText("1100");

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
                siteVisitCount.setText("30");
                totalSales.setText("\u20B9"+" "+"30.00");
                totalOrder.setText("1");

            }
        });
    }

    private void setAllTime() {
        allTime.setBackgroundResource(R.drawable.background_last_pressed);
        allTime.setTextColor(Color.BLACK); // Set text color of pressed button to black
        Last30.setBackgroundResource(R.drawable.background_last_normal);
        Last30.setTextColor(Color.GRAY); // Set text color of other buttons to grey
        Last7.setBackgroundResource(R.drawable.background_last_normal);
        Last7.setTextColor(Color.GRAY); // Set text color of other buttons to grey
        today.setBackgroundResource(R.drawable.background_last_normal);
        today.setTextColor(Color.GRAY); // Set text color of other buttons to gre
        DatabaseReference orderRef = database.getReference().child("Shops").child(ShopName).child("orderCount");
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String orderCount = String.valueOf(dataSnapshot.getValue(Integer.class));
                    if (orderCount != null) {
                        totalSales.setText("\u20B9" + " " + 00 + ".00"); // Set total sales as order count
                        totalOrder.setText(orderCount); // Set total order as order count
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeLog("Error retrieving order count: " + databaseError.getMessage());
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

    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    private void makeLog(String s) {
        Log.d("AshuraDB", s);
    }
}
