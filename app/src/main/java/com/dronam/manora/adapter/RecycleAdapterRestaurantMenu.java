package com.dronam.manora.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dronam.manora.R;
import com.dronam.manora.model.DishObject;

import java.util.ArrayList;

public class RecycleAdapterRestaurantMenu extends RecyclerView.Adapter<RecycleAdapterRestaurantMenu.ViewHolder> {

    Context context;
    private ArrayList<DishObject> modelArrayList;

    public RecycleAdapterRestaurantMenu(Context context, ArrayList<DishObject> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }


    @Override
    public RecycleAdapterRestaurantMenu.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_restaurant_menu,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        DishObject dishObject = modelArrayList.get(position);
        holder.tvFoodName.setText(dishObject.getDishName());
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tv_foodName);
        }
    }
}
