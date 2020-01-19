package com.miracle.dronam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miracle.dronam.R;
import com.miracle.dronam.model.DishObject;
import com.miracle.dronam.model.OrderDetailsObject;
import com.miracle.dronam.model.OrderDetailsObject;

import java.util.ArrayList;

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
        holder.tvRestaurantAddress.setText(orderDetailsObject.getUserAddress());
//        holder.tvRestaurantReviews.setText(orderDetailsObject.getRestaurantReviews());
        holder.tvOrderDate.setText(orderDetailsObject.getOrderDate());
        holder.tvOrderPrice.setText("₹ " + orderDetailsObject.getTotalAmount());
//        holder.ivFoodImage.setImageResource(Integer.parseInt(orderDetailsObject.getDishImage()));

        setupRecyclerViewPastOrders(holder.rvProductList, orderDetailsObject.getListProducts());
    }

    private void setupRecyclerViewPastOrders(RecyclerView recyclerView, ArrayList<DishObject> listDish) {
        RecycleAdapterPastOrderProductView  adapter = new RecycleAdapterPastOrderProductView(context, listDish);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
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
        RecyclerView rvProductList;

        public ViewHolder(View itemView) {
            super(itemView);

            tvRestaurantName = itemView.findViewById(R.id.tv_restaurantName);
            tvRestaurantAddress = itemView.findViewById(R.id.tv_restaurantAddress);
            tvRestaurantReviews = itemView.findViewById(R.id.tv_restaurantReview);
            tvOrderDate = itemView.findViewById(R.id.tv_date);
            tvOrderPrice = itemView.findViewById(R.id.tv_price);
            ivFoodImage = itemView.findViewById(R.id.iv_foodImage);
            rvProductList = itemView.findViewById(R.id.recyclerView_productView);
        }
    }
}
