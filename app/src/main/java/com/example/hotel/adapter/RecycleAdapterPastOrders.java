package com.example.hotel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.hotel.R;
import com.example.hotel.model.OrderDetailsObject;

import java.util.ArrayList;
import java.util.Random;

public class RecycleAdapterPastOrders extends RecyclerView.Adapter<RecycleAdapterPastOrders.ViewHolder> {

    private Context context;
    private ArrayList<OrderDetailsObject> modelArrayList;

    public RecycleAdapterPastOrders(Context context, ArrayList<OrderDetailsObject> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_past_orders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderDetailsObject orderDetailsObject = modelArrayList.get(position);

        holder.tvRestaurantName.setText(orderDetailsObject.getRestaurantName());
        holder.tvRestaurantAddress.setText(orderDetailsObject.getOrderAddress());
        holder.tvRestaurantReviews.setText(orderDetailsObject.getRestaurantReviews());
        holder.tvOrderDate.setText(orderDetailsObject.getOrderDate());
        holder.tvOrderPrice.setText(orderDetailsObject.getOrderPrice());
        holder.ivFoodImage.setImageResource(orderDetailsObject.getFoodImage());

//        TextDrawable drawable = TextDrawable.builder()
//                .beginConfig()
//                .withBorder(4) /* thickness in px */
//                .endConfig()
//                .buildRoundRect(String.valueOf(new Random().nextInt(5 + 1 - 1) + 1), ContextCompat.getColor(context, R.color.light_gray_hint), 10);
//
//        holder.ivFoodQuantity.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvRestaurantName;
        TextView tvRestaurantAddress;
        TextView tvRestaurantReviews;
        TextView tvOrderDate;
        TextView tvOrderPrice;
        ImageView ivFoodImage;

        ImageView ivFoodQuantity;

        public ViewHolder(View itemView) {
            super(itemView);

            tvRestaurantName = itemView.findViewById(R.id.tv_restaurantName);
            tvRestaurantAddress = itemView.findViewById(R.id.tv_restaurantAddress);
            tvRestaurantReviews = itemView.findViewById(R.id.tv_restaurantReview);
            tvOrderDate = itemView.findViewById(R.id.tv_date);
            tvOrderPrice = itemView.findViewById(R.id.tv_price);
            ivFoodImage = itemView.findViewById(R.id.iv_foodImage);

            ivFoodQuantity = itemView.findViewById(R.id.iv_item0);
        }
    }
}
