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


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    //can implement Filterable for selecting from list.

    private ArrayList<Recipe> mRecipeList;
    private Context context;

    //constructor to initialize arraylists
    public DataAdapter(ArrayList<Recipe> arrayList) {
        mRecipeList = arrayList;
    }

    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.ViewHolder holder, int position) {
        holder.tv_recipe_name.setText(mRecipeList.get(position).getTitle());
        //picasso already uses asyncTask to fetch it in background, and also provides caching.
        Picasso.with(context).load(mRecipeList.get(position).getImage()).fit().centerCrop()
                .transform(new RoundedCornersTransformation())
                //.placeholder(R.drawable.user_placeholder)
                .into(holder.imgView_recipe_img);
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //private fields_declaration
        private TextView tv_recipe_name;
        private ImageView imgView_recipe_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //assigning child views
            tv_recipe_name = (TextView)itemView.findViewById(R.id.tv_recipe_name);
            imgView_recipe_img = (ImageView)itemView.findViewById(R.id.imgView_recipe_img);
        }
    }

}
