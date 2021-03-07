package com.project.whats_cookin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HomepageAdapter extends RecyclerView.Adapter<HomepageAdapter.ViewHolder> {

    private final Context mContext;
    private ArrayList<HomepageItems> mArrayList;

    public HomepageAdapter(ArrayList<HomepageItems> arrList, Context context) {
        mArrayList = arrList;
        mContext = context;
    }

    @NonNull
    @Override
    public HomepageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.homepage_custom_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomepageAdapter.ViewHolder holder, int position) {
        holder.text_food_catogory.setText(mArrayList.get(position).getName());
        //picasso already uses asyncTask to fetch it in background, and also provides caching.
        Picasso.with(mContext).load(mArrayList.get(position).getImgDrawable()).fit().centerCrop()
                .transform(new RoundedCornersTransformation())
                //.placeholder(R.drawable.user_placeholder)
                .into(holder.img_food_category);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //private fields_declaration
        private TextView text_food_catogory;
        private ImageView img_food_category;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //assigning child views
            text_food_catogory = (TextView)itemView.findViewById(R.id.text_food_catogory);
            img_food_category = (ImageView)itemView.findViewById(R.id.img_food_category);
        }
    }

}
