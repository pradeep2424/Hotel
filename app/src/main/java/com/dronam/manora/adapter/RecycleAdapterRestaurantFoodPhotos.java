package com.dronam.manora.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.dronam.manora.R;
import com.dronam.manora.model.DishObject;

import java.util.ArrayList;

public class RecycleAdapterRestaurantFoodPhotos extends RecyclerView.Adapter<RecycleAdapterRestaurantFoodPhotos.ViewHolder> {

    Context context;
    private ArrayList<DishObject> modelArrayList;

    public RecycleAdapterRestaurantFoodPhotos(Context context, ArrayList<DishObject> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @Override
    public RecycleAdapterRestaurantFoodPhotos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_restaurant_food_photos,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecycleAdapterRestaurantFoodPhotos.ViewHolder holder, int position) {
        DishObject dishObject = modelArrayList.get(position);
        holder.ivFood.setImageResource(dishObject.getImage());
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFood;

        public ViewHolder(View itemView) {
            super(itemView);

            ivFood = itemView.findViewById(R.id.iv_foodImage);
        }
    }
}
