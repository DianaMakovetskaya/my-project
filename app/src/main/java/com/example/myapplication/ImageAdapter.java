package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.ViewHolder {
//    private Context mContext;
//    private List<Upload> mUploads;
    public TextView textViewName;
    public ImageView imageView;

    public ImageAdapter(@NonNull View itemView) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.text_view_name);
        imageView = itemView.findViewById(R.id.image_view_upload);
    }

//    @NonNull
//    @Override
//    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
//        return new ImageViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
//        Upload uploadCurrent = mUploads.get(position);
//        holder.textViewName.setText(uploadCurrent.getName());
//        Picasso.with(mContext)
//                .load(uploadCurrent.getImageUrl())
//                .placeholder(R.mipmap.ic_launcher)
//                .fit()
//                .centerCrop()
//                .into(holder.imageView);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mUploads.size();
//    }
//
//    public class ImageViewHolder extends RecyclerView.ViewHolder{
//        public TextView textViewName;
//        public ImageView imageView;
//        public ImageViewHolder(@NonNull View itemView) {
//            super(itemView);
//            textViewName = itemView.findViewById(R.id.text_view_name);
//            imageView = itemView.findViewById(R.id.image_view_upload);
//        }
//    }

}
