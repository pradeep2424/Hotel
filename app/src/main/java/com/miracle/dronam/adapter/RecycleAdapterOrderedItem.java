package com.miracle.dronam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.miracle.dronam.R;
import com.miracle.dronam.model.DishObject;

import java.util.ArrayList;

public class RecycleAdapterOrderedItem extends RecyclerView.Adapter<RecycleAdapterOrderedItem.ViewHolder> {

    Context context;
    private ArrayList<DishObject> modelArrayList;

    public RecycleAdapterOrderedItem(Context context, ArrayList<DishObject> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @Override
    public RecycleAdapterOrderedItem.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ordered_item,parent,false);
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
