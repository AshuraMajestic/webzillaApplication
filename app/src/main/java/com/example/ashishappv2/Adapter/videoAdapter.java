package com.example.ashishappv2.Adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ashishappv2.Domains.Product;
import com.example.ashishappv2.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class videoAdapter extends RecyclerView.Adapter<videoAdapter.VideoViewHolder> {
    private List<Product> videoList;
    private FirebaseStorage storage;

    public videoAdapter(List<Product> videoList) {
        this.videoList = videoList;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewvideo, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Product product = videoList.get(position);
        holder.bindData(product);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        VideoView videoView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.playVideo);
        }

        public void bindData(Product product) {
            playVideo(product.getVideoUrl());
        }

        private void playVideo(String videoPath) {
            if (videoPath != null && !videoPath.isEmpty()) {
                StorageReference videoRef = storage.getReferenceFromUrl(videoPath);

                videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Create MediaController to enable video controls
                    MediaController mediaController = new MediaController(itemView.getContext());
                    mediaController.setAnchorView(videoView);
                    videoView.setMediaController(mediaController);

                    // Set the video URI and start the video
                    videoView.setVideoURI(uri);
                    videoView.setOnPreparedListener(mp -> {
                        mp.setLooping(true);
                        mp.start();
                    });
                }).addOnFailureListener(e -> {
                    // Handle errors while getting download URL
                    Log.e("VideoAdapter", "Failed to get download URL: " + e.getMessage());
                });
            } else {
                // Handle the case where videoPath is null or empty
                Log.e("VideoAdapter", "Invalid videoPath: " + videoPath);
                // You can show a message, handle it as per your requirements
            }
        }
    }
}
