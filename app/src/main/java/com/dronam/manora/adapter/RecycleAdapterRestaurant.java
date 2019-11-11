package com.dronam.manora.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dronam.manora.R;
import com.dronam.manora.listeners.OnRecyclerViewClickListener;
import com.dronam.manora.model.RestaurantObject;
import java.util.ArrayList;

public class RecycleAdapterRestaurant extends RecyclerView.Adapter<RecycleAdapterRestaurant.ViewHolder> {
    Context context;
    private OnRecyclerViewClickListener clickListener;

    ArrayList<RestaurantObject> restaurantObjects;

    public RecycleAdapterRestaurant(Context context, ArrayList<RestaurantObject> restaurantObjects) {
        this.context = context;
        this.restaurantObjects = restaurantObjects;
    }

    public void setClickListener(OnRecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_cover);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view, getAdapterPosition());
            }
        }
    }

    @Override
    public RecycleAdapterRestaurant.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_restaurant,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterRestaurant.ViewHolder holder, int position) {
//        holder.imageView.setImageResource(restaurantObjects.get(position).getImage1());

                Glide.with(context)
                .load(restaurantObjects.get(position).getImage1())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return restaurantObjects.size();
    }
}
