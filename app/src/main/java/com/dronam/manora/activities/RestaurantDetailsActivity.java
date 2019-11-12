package com.dronam.manora.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dronam.manora.R;
import com.dronam.manora.adapter.RecycleAdapterRestaurantFoodPhotos;
import com.dronam.manora.adapter.RecycleAdapterRestaurantMenu;
import com.dronam.manora.model.DishObject;
import com.dronam.manora.model.RestaurantObject;
import com.dronam.manora.service.retrofit.ApiInterface;
import com.dronam.manora.service.retrofit.RetroClient;
import com.dronam.manora.utils.InternetConnection;
import com.google.android.material.snackbar.Snackbar;

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
    private ArrayList<DishObject> listMenuProducts;

//    private FoodPagerAdapter loginPagerAdapter;
//    private ViewPager viewPager;
//    private CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

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
            DishObject dishObject = new DishObject(foodImage[i], "", "", "");
            listPhotos.add(dishObject);
        }
    }

    private void setupRecyclerViewProducts() {
        adapterRestaurantMenu = new RecycleAdapterRestaurantMenu(this, listMenuProducts);
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

            ApiInterface apiService = RetroClient.getApiService(this);
//            Call<ResponseBody> call = apiService.getUserDetails(createJsonUserDetails());
            Call<ResponseBody> call = apiService.getProductDetailsData("416004", "416004", "416004", "416004");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

                            JSONObject jsonObj = new JSONObject(responseString);
                            String categoryID = jsonObj.optString("CategoryId");
                            String categoryName = jsonObj.optString("CategoryName");
                            String clientID = jsonObj.optString("ClientId");
                            String restaurantName = jsonObj.optString("RestaurantName");
                            String foodTypeID = jsonObj.optString("FoodTypeId");
                            String foodTypeName = jsonObj.optString("FoodTypeName");
                            String logo = jsonObj.optString("Logo");
                            String taxID = jsonObj.optString("TaxId");
                            String taxable = jsonObj.optString("Taxable");
                            String includeTax = jsonObj.optString("IncludeTax");

                            DishObject dishObject = new DishObject();
                            dishObject.setCategoryID(categoryID);
                            dishObject.setCategoryName(categoryName);
                            dishObject.setClientID(clientID);
                            dishObject.setRestaurantName(restaurantName);
                            dishObject.setFoodTypeID(foodTypeID);
                            dishObject.setFoodTypeName(foodTypeName);
                            dishObject.setLogo(logo);

//                            listMenuProducts.add(restaurantObject);

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
                            getRestaurantData();
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
