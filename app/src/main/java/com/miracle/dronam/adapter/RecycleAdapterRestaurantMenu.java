package com.miracle.dronam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.miracle.dronam.R;
import com.miracle.dronam.model.DishObject;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;

public class RecycleAdapterRestaurantMenu extends RecyclerView.Adapter<RecycleAdapterRestaurantMenu.ViewHolder> {

    Context context;
    private ArrayList<DishObject> modelArrayList;

    public RecycleAdapterRestaurantMenu(Context context, ArrayList<DishObject> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @Override
    public RecycleAdapterRestaurantMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_restaurant_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DishObject dishObject = modelArrayList.get(position);
        holder.tvFoodName.setText(dishObject.getDishName());
        holder.tvFoodCategory.setText(dishObject.getDishCategory());
        holder.tvFoodPrice.setText(dishObject.getDishPrice());


        holder.llAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        if (dishObject.getQuantity() > 0) {
//            showItemQuantityPicker(holder);
//        } else {
//            showAddItemButton(holder);
//        }
    }

    private void showAddItemButton(ViewHolder holder) {
        holder.numberPickerItemQuantity.setVisibility(View.VISIBLE);
        holder.llAddItem.setVisibility(View.GONE);
    }

    private void showItemQuantityPicker(ViewHolder holder) {
        holder.numberPickerItemQuantity.setVisibility(View.GONE);
        holder.llAddItem.setVisibility(View.VISIBLE);
    }


    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodName;
        TextView tvFoodCategory;
        TextView tvFoodPrice;

        LinearLayout llAddItem;
        NumberPicker numberPickerItemQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tv_foodName);
            tvFoodCategory = itemView.findViewById(R.id.tv_foodCategory);
            tvFoodPrice = itemView.findViewById(R.id.tv_foodPrice);

            llAddItem = itemView.findViewById(R.id.ll_addButton);
            numberPickerItemQuantity = itemView.findViewById(R.id.numberPicker_quantity);
        }
    }
}
