package com.example.hotel.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hotel.R;
import com.example.hotel.adapter.RecycleAdapterCuisine;
import com.example.hotel.adapter.RecycleAdapterPastOrders;
import com.example.hotel.model.CuisineObject;
import com.example.hotel.model.OrderDetailsObject;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class PastOrdersFragment extends Fragment {
    View rootView;

    String[] restaurantName  = {"Cocobolo Poolside Bar + Grill","Palm Beach Seafood Restaurant","Shin Minori Japanese Restaurant","Shin Minori Japanese Restaurant"};
    String[] orderAddress = {"Chembur","Ghatkopar","Thane","Sion"};
    String[] restaurantReviews ={"238 reviews","200 reviews","556 reviews","240 reviews"};
    String[] orderDate = {"25 Nov 2017 10 : 30 AM","27 Nov 2017 10 : 30 AM","28 Nov 2017 10 : 30 AM","29 Nov 2017 10 : 30 AM"};
    String[] orderPrice={"₹ 199.00","₹ 249.00","₹ 149.00","₹ 399.00"};
    Integer[] foodImage = {R.mipmap.temp_order, R.mipmap.temp_order, R.mipmap.temp_order, R.mipmap.temp_order};

    private RecyclerView rvPastOrders;
    private RecycleAdapterPastOrders adapterPastOrders;
    private ArrayList<OrderDetailsObject> listPastOrders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_past_orders, container, false);

        initComponents();
        setupRecyclerViewPastOrders();
        return rootView;
    }

    private void initComponents() {
        rvPastOrders = rootView.findViewById(R.id.recyclerView_pastOrders);
    }

    private void setupRecyclerViewPastOrders()
    {
        getPastOrdersData();

        adapterPastOrders = new RecycleAdapterPastOrders(getActivity(), listPastOrders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPastOrders.setLayoutManager(layoutManager);
        rvPastOrders.setItemAnimator(new DefaultItemAnimator());
        rvPastOrders.setAdapter(adapterPastOrders);
    }

    private void getPastOrdersData() {
        listPastOrders = new ArrayList<>();

        for (int i=0; i<restaurantName.length; i++){
            OrderDetailsObject food7_model = new OrderDetailsObject(restaurantName[i], orderAddress[i],
                    restaurantReviews[i], orderDate[i], orderPrice[i], foodImage[i]);
            listPastOrders.add(food7_model);
        }
    }
}

