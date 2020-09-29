package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;

    FirebaseRecyclerOptions<Upload> options;
    FirebaseRecyclerAdapter<Upload, ImageAdapter> adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        getActivity().setTitle("Home");
        mRecyclerView = view.findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProgressCircle = view.findViewById(R.id.progress_circle);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("uploads");
//        mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
////                    Upload upload = postSnapshot.getValue(Upload.class);
////                    mUploads.add(upload);
////                }
////                mAdapter = new ImageAdapter(getActivity(), mUploads);
////                mRecyclerView.setAdapter(mAdapter);
////                mProgressCircle.setVisibility(View.INVISIBLE);
//                Log.e("MainFragment","dataSnapshot count "+dataSnapshot.getChildrenCount());
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    try {
//                        Upload upload = postSnapshot.getValue(Upload.class);
//                        Log.i("MainFragment","postSnapshot => "+postSnapshot + "upload => "+upload.getName() + " * "+upload.getImageUrl());
//
//                        mUploads.add(upload);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//
//                }
//                mAdapter = new ImageAdapter(getActivity(), mUploads);
//                mRecyclerView.setAdapter(mAdapter);
//                mProgressCircle.setVisibility(View.INVISIBLE);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                mProgressCircle.setVisibility(View.INVISIBLE);
//            }
//        });
        LoadData();

        return view;
    }

    private void LoadData() {
        options = new FirebaseRecyclerOptions.Builder<Upload>().setQuery(mDatabaseRef, Upload.class).build();

        adapter = new FirebaseRecyclerAdapter<Upload, ImageAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ImageAdapter imageAdapter, int i, @NonNull Upload upload) {
                try {
                    imageAdapter.textViewName.setText(upload.getInfo() + "");
//
//                     Picasso.with(getContext()).load(upload.getImageUrl() + "").into(imageAdapter.imageView);
                    Picasso.with(getContext())
                            .load(upload.getImageUrl()) // web image url
                            .fit().centerInside()

                            .rotate(90)                    //if you want to rotate by 90 degrees

                            .into(imageAdapter.imageView);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @NonNull
            @Override
            public ImageAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
                return new ImageAdapter(v);
            }
        };
        adapter.startListening();
        mRecyclerView.setAdapter(adapter);


    }

}
