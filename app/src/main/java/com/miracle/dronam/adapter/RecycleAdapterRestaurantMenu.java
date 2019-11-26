package com.miracle.dronam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.miracle.dronam.R;
import com.miracle.dronam.activities.RestaurantDetailsActivity;
import com.miracle.dronam.model.DishObject;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;

public class RecycleAdapterRestaurantMenu extends RecyclerView.Adapter<RecycleAdapterRestaurantMenu.ViewHolder> {

    RestaurantDetailsActivity activity;
    private ArrayList<DishObject> modelArrayList;

    private ViewHolder viewHolderClickedItem;

    public RecycleAdapterRestaurantMenu(RestaurantDetailsActivity activity, ArrayList<DishObject> modelArrayList) {
        this.activity = activity;
        this.modelArrayList = modelArrayList;
    }

    @Override
    public RecycleAdapterRestaurantMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_restaurant_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DishObject dishObject = modelArrayList.get(position);
        holder.tvFoodName.setText(dishObject.getDishName());
        holder.tvFoodCategory.setText(dishObject.getDishCategory());
        holder.tvFoodPrice.setText(dishObject.getDishAmount());


        holder.llAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolderClickedItem = holder;
                activity.addItemToCart(1);
            }
        });

        holder.numberPickerItemQuantity.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                String actionText = action == ActionEnum.MANUAL ? "manually set" : (action == ActionEnum.INCREMENT ? "incremented" : "decremented");
                String message = String.format("NumberPicker is %s to %d", actionText, value);

                activity.addItemToCart(value);
            }
        });

//        if (dishObject.getQuantity() > 0) {
//            showItemQuantityPicker(holder);
//        } else {
//            showAddItemButton(holder);
//        }
    }

    public void showHideQuantityAndAddItemButton() {
        if (viewHolderClickedItem.numberPickerItemQuantity.getValue() == 0) {
            showAddItemButton();
        }
        else {
            showItemQuantityPicker();
        }
    }

    private void showAddItemButton() {
        viewHolderClickedItem.numberPickerItemQuantity.setVisibility(View.GONE);
        viewHolderClickedItem.llAddItem.setVisibility(View.VISIBLE);
    }

    private void showItemQuantityPicker() {
        viewHolderClickedItem.numberPickerItemQuantity.setVisibility(View.VISIBLE);
        viewHolderClickedItem.llAddItem.setVisibility(View.GONE);
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
