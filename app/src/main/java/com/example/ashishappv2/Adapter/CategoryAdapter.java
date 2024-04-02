package com.example.ashishappv2.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ashishappv2.Domains.CategoryInventory;
import com.example.ashishappv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<CategoryInventory> itemList;
    private Context context;
    private OnSwitchChangeListener2 switchChangeListener;
    public interface OnSwitchChangeListener2 {
        void onSwitchChanged(String categoryName, boolean isChecked);
    }

    public CategoryAdapter(List<CategoryInventory> itemList, Context context,OnSwitchChangeListener2 switchChangeListener) {
        this.itemList = itemList;
        this.context = context;
        this.switchChangeListener=switchChangeListener;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryInventory item = itemList.get(position);

        holder.categoryName.setText(item.getName());
            holder.listed.setText(item.getItemCount() + " products listed");
        holder.mySwitch.setChecked(item.isActive());

        // Set switch change listener
        holder.mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update database when switch is toggled
            switchChangeListener.onSwitchChanged(item.getName(), isChecked);
        });

        // Set custom attributes for switch based on its state
        if (item.isActive()) {
            holder.mySwitch.setText("Active");
            holder.mySwitch.setTextColor(ContextCompat.getColor(context, R.color.btngreen));
        } else {
            holder.mySwitch.setText("Inactive");
            holder.mySwitch.setTextColor(ContextCompat.getColor(context, R.color.red));
        }


        String imageUrl = item.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_crop)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(android.R.drawable.ic_menu_crop);
        }
    }

    private void showToast(String userDataNotFound) {
        Toast.makeText(context, userDataNotFound, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView categoryName, listed;
        SwitchCompat mySwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
        categoryName=itemView.findViewById(R.id.categoryName);
        listed=itemView.findViewById(R.id.listed);
        mySwitch=itemView.findViewById(R.id.switch1);
        }
    }
}

