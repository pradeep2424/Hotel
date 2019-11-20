package com.miracle.dronam.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miracle.dronam.R;
import com.miracle.dronam.adapter.RecycleAdapterRestaurantFoodPhotos;
import com.miracle.dronam.adapter.RecycleAdapterRestaurantMenu;
import com.miracle.dronam.model.DishObject;
import com.miracle.dronam.model.RestaurantObject;
import com.miracle.dronam.service.retrofit.ApiInterface;
import com.miracle.dronam.service.retrofit.RetroClient;
import com.miracle.dronam.utils.Application;
import com.miracle.dronam.utils.InternetConnection;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailsActivity extends AppCompatActivity {
    RelativeLayout rlRootLayout;
    String[] foodName = {"Navgrah Veg Restaurant", "Saroj Hotel", "Hotel Jewel of Chembur"};
    Integer[] foodImage = {R.mipmap.temp_order, R.mipmap.temp_order,
            R.mipmap.temp_order, R.mipmap.temp_order, R.mipmap.temp_order};

    private RecyclerView rvPhotos;
    private RecycleAdapterRestaurantFoodPhotos adapterRestaurantPhotos;
    private ArrayList<DishObject> listPhotos;

    private RecyclerView rvMenu;
    private RecycleAdapterRestaurantMenu adapterRestaurantMenu;
    private ArrayList<DishObject> listDishProducts;

    RestaurantObject restaurantObject;

//    private FoodPagerAdapter loginPagerAdapter;
//    private ViewPager viewPager;
//    private CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            restaurantObject = (RestaurantObject) bundle.getSerializable("RestaurantObject");
        }

        initComponents();
        setupRecyclerViewPhotos();
//        setupRecyclerViewMenu();

        getProductDetailsData();
    }

    private void initComponents() {
        rlRootLayout = findViewById(R.id.rl_rootLayout);
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
            DishObject dishObject = new DishObject();
            dishObject.setDishImage(String.valueOf(foodImage[i]));
            listPhotos.add(dishObject);
        }

//        for (int i = 0; i < foodImage.length; i++) {
//            DishObject dishObject = new DishObject(foodImage[i], "", "", "");
//            listPhotos.add(dishObject);
//        }
    }

    private void setupRecyclerViewProducts() {
        adapterRestaurantMenu = new RecycleAdapterRestaurantMenu(this, listDishProducts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvMenu.setLayoutManager(layoutManager);
        rvMenu.setItemAnimator(new DefaultItemAnimator());
        rvMenu.setAdapter(adapterRestaurantMenu);
    }

//    private void getMenuData() {
//        listMenu = new ArrayList<>();
//
//        for (int i = 0; i < foodName.length; i++) {
//            DishObject dishObject = new DishObject(foodImage[i], foodName[i], "", "");
//            listMenu.add(dishObject);
//        }
//    }

    private void getProductDetailsData() {
        if (InternetConnection.checkConnection(this)) {

            String userTypeID = Application.userDetails.getUserType();
            String restaurantID = restaurantObject.getRestaurantID();
            String foodTypeID = restaurantObject.getFoodTypeID();
            String categoryID = restaurantObject.getCategoryID();

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.getProductDetailsData(userTypeID, restaurantID, foodTypeID, categoryID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            listDishProducts = new ArrayList<>();

                            JSONArray jsonArray = new JSONArray(responseString);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);

                                String dishID = jsonObj.optString("ProductId");
                                String dishName = jsonObj.optString("ProductName");
                                String dishDescription = jsonObj.optString("ProductDesc");
                                String dishImage = jsonObj.optString("ProductImage");
                                String dishPrice = jsonObj.optString("Price");

                                DishObject dishObject = new DishObject();
                                dishObject.setDishID(dishID);
                                dishObject.setDishName(dishName);
                                dishObject.setDishDescription(dishDescription);
                                dishObject.setDishImage(dishImage);
                                dishObject.setDishPrice(dishPrice);

                                listDishProducts.add(dishObject);
                            }

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                        setupRecyclerViewProducts();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
                        Log.e("Error onFailure : ", t.toString());
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
//            signOutFirebaseAccounts();

            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getProductDetailsData();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private void addItemToCart() {
        if (InternetConnection.checkConnection(this)) {

            String userTypeID = Application.userDetails.getUserType();
            String restaurantID = "1";

            DishObject dishObject = new DishObject();
            dishObject.setDishID("1");
            dishObject.setDishName("Test Name Paneer");
            dishObject.setDishDescription("Test Desc Paneer");
            dishObject.setDishImage("");
            dishObject.setDishPrice("10000");


            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.addItemToCart(dishObject);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
//                            listCartDish = new ArrayList<>();

                            JSONArray jsonArray = new JSONArray(responseString);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObj = jsonArray.getJSONObject(i);
//
//                                String dishID = jsonObj.optString("ProductId");
//                                String dishName = jsonObj.optString("ProductName");
//                                String dishDescription = jsonObj.optString("ProductDesc");
//                                String dishImage = jsonObj.optString("ProductImage");
//                                String dishPrice = jsonObj.optString("Price");
//
//                                DishObject dishObject = new DishObject();
//                                dishObject.setDishID(dishID);
//                                dishObject.setDishName(dishName);
//                                dishObject.setDishDescription(dishDescription);
//                                dishObject.setDishImage(dishImage);
//                                dishObject.setDishPrice(dishPrice);
//
//                                listCartDish.add(dishObject);
//                            }

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

//                        setupRecyclerViewOrderedItems();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
                        Log.e("Error onFailure : ", t.toString());
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
//            signOutFirebaseAccounts();

            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addItemToCart();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }


    public void showSnackbarErrorMsg(String erroMsg) {
//        Snackbar.make(fragmentRootView, erroMsg, Snackbar.LENGTH_LONG).show();

        Snackbar snackbar = Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
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
