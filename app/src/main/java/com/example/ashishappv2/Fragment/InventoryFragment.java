package com.example.ashishappv2.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ashishappv2.Adapter.ViewPagerAdpater;
import com.example.ashishappv2.R;
import com.google.android.material.tabs.TabLayout;

public class InventoryFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdpater viewPagerAdpater;

    public InventoryFragment() {
        // Required empty public constructor
    }
    public static InventoryFragment newInstance() {
        return new InventoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_inventory, container, false);
        tabLayout=view.findViewById(R.id.UpperTab);
        viewPager2=view.findViewById(R.id.viewPager);
        viewPagerAdpater=new ViewPagerAdpater(getActivity());
        viewPager2.setAdapter(viewPagerAdpater);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                    case 1:
                        tabLayout.getTabAt(position).select();
                }
                super.onPageSelected(position);
            }
        });
        return view;
    }
}