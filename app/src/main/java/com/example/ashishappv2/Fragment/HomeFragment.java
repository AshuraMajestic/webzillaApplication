package com.example.ashishappv2.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ashishappv2.Adapter.videoAdapter;
import com.example.ashishappv2.Domains.Product;
import com.example.ashishappv2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private DatabaseReference videoRef;
    private List<Product> videoList;
    private videoAdapter videoAdapter;
    private RecyclerView recyclerView;
    private ProgressBar loadingView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        videoRef = FirebaseDatabase.getInstance().getReference().child("productvideo");
        videoList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.vertical);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        videoAdapter = new videoAdapter(videoList);
        recyclerView.setAdapter(videoAdapter);
        loadingView = view.findViewById(R.id.loadingView);
        showLoadingScreen();

        // Set up Firebase Database listener
        videoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                videoList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("video").exists()) {
                        showMessage(dataSnapshot.child("video").toString());
                        // Ensure all necessary fields are present
                        if (dataSnapshot.child("name").exists()
                                && dataSnapshot.child("category").exists()
                                && dataSnapshot.child("price").exists()
                                && dataSnapshot.child("pieces").exists()
                                && dataSnapshot.child("userEmail").exists()) {

                            Product video = new Product();
                            video.setName(dataSnapshot.child("name").getValue(String.class));
                            video.setCategory(dataSnapshot.child("category").getValue(String.class));
                            video.setPrice(dataSnapshot.child("price").getValue(String.class));
                            video.setPieces(dataSnapshot.child("pieces").getValue(String.class));
                            video.setUserEmail(dataSnapshot.child("userEmail").getValue(String.class));
                            video.setVideoUrl(dataSnapshot.child("video").getValue(String.class));

                            videoList.add(video);
                        } else {
                            Log.e("FirebaseError", "Incomplete data for video: " + dataSnapshot.getKey());
                        }
                    } else {
                        Log.e("FirebaseError", "Video URL does not exist for: " + dataSnapshot.getKey());
                    }
                }
                videoAdapter.notifyDataSetChanged();
                hideLoadingScreen();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to load videos: " + error.getMessage());
                showToast("Failed to load videos");
                hideLoadingScreen();
            }
        });

        return view;
    }

    private void showMessage(String string) {
        Log.d("AshuraDB",string);
    }

    private void hideLoadingScreen() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    private void showLoadingScreen() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
