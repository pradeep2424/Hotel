package com.example.hotel.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.hotel.R;
import com.example.hotel.adapter.RecycleAdapterRestaurantFoodPhotos;
import com.example.hotel.adapter.RecycleAdapterRestaurantMenu;
import com.example.hotel.model.DishObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class RestaurantDetailsActivity extends AppCompatActivity {

    String[] foodName = {"Navgrah Veg Restaurant", "Saroj Hotel", "Hotel Jewel of Chembur"};
    Integer[] foodImage = {R.mipmap.temp_order, R.mipmap.temp_order,
            R.mipmap.temp_order, R.mipmap.temp_order, R.mipmap.temp_order};

    private RecyclerView rvPhotos;
    private RecycleAdapterRestaurantFoodPhotos adapterRestaurantPhotos;
    private ArrayList<DishObject> listPhotos;

    private RecyclerView rvMenu;
    private RecycleAdapterRestaurantMenu adapterRestaurantMenu;
    private ArrayList<DishObject> listMenu;

//    private FoodPagerAdapter loginPagerAdapter;
//    private ViewPager viewPager;
//    private CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        initComponents();
        setupRecyclerViewPhotos();
        setupRecyclerViewMenu();
//        setupViewPagerSlidingRestaurantImages();
    }

    private void initComponents() {
        rvPhotos = findViewById(R.id.recyclerView_photos);
        rvMenu = findViewById(R.id.recyclerView_menu);
//        viewPager = (ViewPager) findViewById(R.id.viewPager_slidingRestaurantImages);
    }

    private void setupRecyclerViewPhotos() {
        getPhotosData();

        adapterRestaurantPhotos = new RecycleAdapterRestaurantFoodPhotos(this, listPhotos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvPhotos.setLayoutManager(layoutManager);
        rvPhotos.setItemAnimator(new DefaultItemAnimator());
        rvPhotos.setAdapter(adapterRestaurantPhotos);
    }

    private void getPhotosData() {
        listPhotos = new ArrayList<>();

        for (int i = 0; i < foodImage.length; i++) {
            DishObject dishObject = new DishObject(foodImage[i], "", "", "");
            listPhotos.add(dishObject);
        }
    }

    private void setupRecyclerViewMenu() {
        getMenuData();

        adapterRestaurantMenu = new RecycleAdapterRestaurantMenu(this, listMenu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvMenu.setLayoutManager(layoutManager);
        rvMenu.setItemAnimator(new DefaultItemAnimator());
        rvMenu.setAdapter(adapterRestaurantMenu);
    }

    private void getMenuData() {
        listMenu = new ArrayList<>();

        for (int i = 0; i < foodName.length; i++) {
            DishObject dishObject = new DishObject(foodImage[i], foodName[i], "", "");
            listMenu.add(dishObject);
        }
    }

//    private void setupViewPagerSlidingRestaurantImages()
//    {
//        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
//        loginPagerAdapter = new FoodPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(loginPagerAdapter);
//        indicator.setViewPager(viewPager);
//        loginPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
//    }
}
