package com.example.ashishappv2.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ashishappv2.Activity.DukanDeliveryActivity;
import com.example.ashishappv2.R;


public class ManageFragment extends Fragment {

    private LinearLayout delivery,payout;


    public ManageFragment() {
        // Required empty public constructor
    }


    public static ManageFragment newInstance(String param1, String param2) {
        ManageFragment fragment = new ManageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_manage, container, false);
        delivery=view.findViewById(R.id.delivery);
        payout=view.findViewById(R.id.payout);
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DukanDeliveryActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}