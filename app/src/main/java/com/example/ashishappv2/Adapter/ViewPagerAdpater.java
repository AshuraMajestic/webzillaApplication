package com.example.ashishappv2.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ashishappv2.Fragment.CategoryFragment;
import com.example.ashishappv2.Fragment.MyproductsFragment;

public class ViewPagerAdpater extends FragmentStateAdapter {
    public ViewPagerAdpater(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new MyproductsFragment();
            case 1: return new CategoryFragment();
            default: return new MyproductsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
