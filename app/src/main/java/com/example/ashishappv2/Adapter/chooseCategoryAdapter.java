package com.example.ashishappv2.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ashishappv2.Domains.CategoryInventory;
import com.example.ashishappv2.R;

import java.util.List;

public class chooseCategoryAdapter extends RecyclerView.Adapter<chooseCategoryAdapter.ViewHolder>{
    private List<CategoryInventory> itemList;
    private OnCategoryClickListener listener;
    private Context context;
    private Dialog dialog;

    public chooseCategoryAdapter(List<CategoryInventory> itemList, Context context, OnCategoryClickListener listener,Dialog dialog) {
        this.itemList = itemList;
        this.context = context;
        this.listener=listener;
        this.dialog=dialog;
    }
    public interface OnCategoryClickListener {
        void onCategoryClick(String categoryName);
    }
    @NonNull
    @Override
    public chooseCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_show_in_recyclerview, parent, false);
        return new chooseCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull chooseCategoryAdapter.ViewHolder holder, int position) {
        CategoryInventory item = itemList.get(position);
        holder.category.setText(item.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCategoryClick(item.getName());
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category=itemView.findViewById(R.id.category);
        }
    }
}
