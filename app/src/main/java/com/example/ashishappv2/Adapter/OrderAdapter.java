package com.example.ashishappv2.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ashishappv2.Domains.OrderList;
import com.example.ashishappv2.Domains.ProductInventory;
import com.example.ashishappv2.R;

import java.util.List;
import java.util.Map;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<OrderList> itemList;
    private Context context;

    public OrderAdapter(Context context, List<OrderList> itemList) {
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
        OrderList item = itemList.get(position);
        holder.orderNumber.setText("#"+item.getOrderNumber());
        holder.dateTime.setText(item.getDate());
        holder.Accepted.setText(item.getAccepted());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumber;
        TextView dateTime;
        TextView quantity;
        TextView total;
        TextView Payment;
        ImageView dot;
        TextView Accepted;
        LinearLayout button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumber=itemView.findViewById(R.id.orderNumber);
            dateTime=itemView.findViewById(R.id.dateTime);
            quantity=itemView.findViewById(R.id.quantity);
            total=itemView.findViewById(R.id.orderPrice);
            Payment=itemView.findViewById(R.id.paymentMethod);
            dot=itemView.findViewById(R.id.dot);
            Accepted=itemView.findViewById(R.id.accepted);
            button=itemView.findViewById(R.id.Detail);




        }
    }
}

