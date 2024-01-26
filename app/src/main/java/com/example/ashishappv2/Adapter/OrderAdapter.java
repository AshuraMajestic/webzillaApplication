package com.example.ashishappv2.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ashishappv2.Domains.ProductWithImage;
import com.example.ashishappv2.R;

import java.util.List;
import java.util.Map;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<ProductWithImage> itemList;
    private Context context;

    public OrderAdapter(Context context, List<ProductWithImage> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductWithImage item = itemList.get(position);

        holder.productNameTextView.setText(item.getName());
        holder.categoryTextView.setText(item.getCategory());
        holder.priceTextView.setText("₹"+item.getPrice()+" / "+item.getPieces());

        Map<String, String> images = item.getImages();
        if (images != null && !images.isEmpty()) {
            String firstImageUrl = images.values().iterator().next();
            Glide.with(context)
                    .load(firstImageUrl)
                    .centerCrop()
                    .placeholder(R.mipmap.order_image)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView productNameTextView;
        TextView categoryTextView;
        TextView priceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            productNameTextView = itemView.findViewById(R.id.productName);
            categoryTextView = itemView.findViewById(R.id.category);
            priceTextView = itemView.findViewById(R.id.price);
        }
    }
}

