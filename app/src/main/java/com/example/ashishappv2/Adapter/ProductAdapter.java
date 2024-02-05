package com.example.ashishappv2.Adapter;

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
import com.example.ashishappv2.Domains.ProductInventory;
import com.example.ashishappv2.R;

import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<ProductInventory> itemList;
    private Context context;

    public ProductAdapter(List<ProductInventory> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductInventory item = itemList.get(position);

        holder.productName.setText(item.getName());
        holder.piece.setText(item.getPieces() + " pieces");
        holder.price.setText("₹" + item.getPrice());
        holder.mySwitch.setChecked(item.isAvailable());

        Map<String, String> images = item.getImages();
        if (images != null && !images.isEmpty()) {
            String firstImageUrl = images.values().iterator().next();
            Glide.with(context)
                    .load(firstImageUrl)
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_crop)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, shareImageView;
        TextView productName, piece, price;
        SwitchCompat mySwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            piece = itemView.findViewById(R.id.pieces);
            price = itemView.findViewById(R.id.price);
            mySwitch = itemView.findViewById(R.id.switch1);
            imageView = itemView.findViewById(R.id.imageView);

            // Views in the ConstraintLayout
            shareImageView = itemView.findViewById(R.id.imageView2);
        }
    }
}