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
import com.example.ashishappv2.Domains.OrderList;
import com.example.ashishappv2.R;
import java.util.List;
import java.util.Objects;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<OrderList> itemList;
    private Context context;
    private OrderClickListener orderClickListener;

    // Constructor
    public OrderAdapter(Context context, List<OrderList> itemList, OrderClickListener orderClickListener) {
        this.context = context;
        this.itemList = itemList;
        this.orderClickListener = orderClickListener;
    }

    // Interface to handle button click events
    public interface OrderClickListener {
        void onOrderClick(OrderList order);
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
        holder.orderNumber.setText("#" + item.getOrderNumber());
        holder.dateTime.setText(item.getDate());
        holder.quantity.setText(item.getTotalQuantity() + " ITEM");
        holder.total.setText("₹ " + item.getTotalPrice());
        if (item.getPaymentOption().equals("Cash On Delivery")) {
            holder.Payment.setText("COD");
        } else {
            holder.Payment.setText("UPI");
        }
        holder.Accepted.setText(item.getAccepted());
        if (Objects.equals(item.getAccepted(), "accepted")) {
            holder.dot.setImageResource(R.drawable.green_dot);
        } else {
            holder.dot.setImageResource(R.drawable.orange_dot);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderList clickedOrder = item;
                orderClickListener.onOrderClick(clickedOrder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ViewHolder class
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
            orderNumber = itemView.findViewById(R.id.orderNumber);
            dateTime = itemView.findViewById(R.id.dateTime);
            quantity = itemView.findViewById(R.id.quantity);
            total = itemView.findViewById(R.id.orderPrice);
            Payment = itemView.findViewById(R.id.paymentMethod);
            dot = itemView.findViewById(R.id.dot);
            Accepted = itemView.findViewById(R.id.accepted);
            button = itemView.findViewById(R.id.Detail);
        }
    }
}
