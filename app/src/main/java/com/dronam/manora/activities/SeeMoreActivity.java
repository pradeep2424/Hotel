package com.dronam.manora.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dronam.manora.R;
import com.dronam.manora.adapter.RecycleAdapterRestaurant;
import com.dronam.manora.listeners.OnRecyclerViewClickListener;
import com.dronam.manora.model.RestaurantObject;

import java.util.ArrayList;

public class SeeMoreActivity extends AppCompatActivity implements OnRecyclerViewClickListener {
    private RecyclerView rvSeeMoreRestaurant;
    private RecycleAdapterRestaurant adapterRestaurant;

    private ArrayList<RestaurantObject> listRestaurantObject;

    Integer image2[] ={R.mipmap.temp_img1, R.mipmap.temp_img2, R.mipmap.temp_img3,
            R.mipmap.temp_img4, R.mipmap.temp_img5, R.mipmap.temp_img6 , R.mipmap.temp_img7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more);

        initComponents();
        setupRecyclerViewRestaurant();
    }

    private void initComponents() {
        rvSeeMoreRestaurant = (RecyclerView) findViewById(R.id.recyclerView_seeMore);
    }

    private void setupRecyclerViewRestaurant()
    {
        getRestaurantData();

        adapterRestaurant = new RecycleAdapterRestaurant(this, listRestaurantObject);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvSeeMoreRestaurant.setLayoutManager(layoutManager);
        rvSeeMoreRestaurant.setItemAnimator(new DefaultItemAnimator());
        rvSeeMoreRestaurant.setAdapter(adapterRestaurant);
        adapterRestaurant.setClickListener(this);
    }

    private void getRestaurantData() {
        listRestaurantObject = new ArrayList<>();
        for (int i=0; i < image2.length; i++) {
            RestaurantObject restaurantObject = new RestaurantObject(image2[i]);
            listRestaurantObject.add(restaurantObject);
        }
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(this, RestaurantDetailsActivity.class);
        startActivity(intent);
    }
}
