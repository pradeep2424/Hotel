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

import com.google.gson.JsonObject;
import com.miracle.dronam.R;
import com.miracle.dronam.adapter.RecycleAdapterRestaurantFoodPhotos;
import com.miracle.dronam.adapter.RecycleAdapterRestaurantMenu;
import com.miracle.dronam.dialog.DialogLoadingIndicator;
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

    DialogLoadingIndicator progressIndicator;

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
        progressIndicator = DialogLoadingIndicator.getInstance();

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
            dishObject.setProductImage(String.valueOf(foodImage[i]));
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

            int userTypeID = 0;
            int restaurantID = restaurantObject.getRestaurantID();
            int foodTypeID = restaurantObject.getFoodTypeID();
            int categoryID = restaurantObject.getCategoryID();

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.getProductDetailsData(userTypeID, restaurantID, foodTypeID, categoryID);
//            Call<ResponseBody> call = apiService.getProductDetailsData(userTypeID, restaurantID, foodTypeID, categoryID);
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

                                double cgst = jsonObj.optDouble("CGST");
                                int categoryID = jsonObj.optInt("CategoryID");
                                String categoryName = jsonObj.optString("CategoryName");
                                String foodType = jsonObj.optString("FoodType");
                                int foodTypeID = jsonObj.optInt("FoodTypeId");
                                int dishID = jsonObj.optInt("HaveRuntimeRate");
                                String isDiscounted = jsonObj.optString("IsDiscounted");
                                String price = jsonObj.optString("Price");
                                String productDesc = jsonObj.optString("ProductDesc");
                                int productID = jsonObj.optInt("ProductId");
                                String productImage = jsonObj.optString("ProductImage");
                                String productName = jsonObj.optString("ProductName");
                                double sgst = jsonObj.optDouble("SGST");
                                int taxID = jsonObj.optInt("TaxID");
                                String taxName = jsonObj.optString("TaxName");

//                                String dishID = jsonObj.optString("ProductId");
//                                String dishName = jsonObj.optString("ProductName");
//                                String dishDescription = jsonObj.optString("ProductDesc");
//                                String dishImage = jsonObj.optString("ProductImage");
//                                String dishPrice = jsonObj.optString("Price");

                                DishObject dishObject = new DishObject();
                                dishObject.setCgst(cgst);
                                dishObject.setCategoryID(categoryID);
                                dishObject.setCategoryName(categoryName);
                                dishObject.setFoodType(foodType);
                                dishObject.setFoodTypeID(foodTypeID);
                                dishObject.setDishID(dishID);
                                dishObject.setIsDiscounted(isDiscounted);
                                dishObject.setPrice(price);
                                dishObject.setProductDesc(productDesc);
                                dishObject.setProductID(productID);
                                dishObject.setProductImage(productImage);
                                dishObject.setProductName(productName);
                                dishObject.setSgst(sgst);
                                dishObject.setTaxID(taxID);
                                dishObject.setTaxName(taxName);

                                listDishProducts.add(dishObject);
                            }

                            setupRecyclerViewProducts();

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

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

    private JsonObject createJsonCart(DishObject dishObject, int quantity) {
        JsonObject postParam = new JsonObject();

        try {
            postParam.addProperty("ProductId", dishObject.getProductID());
            postParam.addProperty("ProductName", dishObject.getProductName());
            postParam.addProperty("ProductRate", 80.00);
            postParam.addProperty("ProductAmount", dishObject.getPrice());
            postParam.addProperty("ProductSize", "Regular");
            postParam.addProperty("cartId", 1);
            postParam.addProperty("ProductQnty", quantity);
            postParam.addProperty("Taxableval", 20.00);
            postParam.addProperty("CGST", 10.00);
            postParam.addProperty("SGST",10.00);
            postParam.addProperty("HotelName", "Hotel Manora");
            postParam.addProperty("IsIncludeTax", false);
            postParam.addProperty("IsTaxApplicable", false);
            postParam.addProperty("DeliveryCharge", 30.00);
            postParam.addProperty("Userid", 0);
            postParam.addProperty("Clientid", 1);
            postParam.addProperty("TotalAmount", dishObject.getPrice());
            postParam.addProperty("TaxId", 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return postParam;
    }


    public void addItemToCart(final int quantity, final int position) {
        if (InternetConnection.checkConnection(this)) {

//            String userTypeID = Application.userDetails.getUserType();
//            String restaurantID = "1";
//
//            DishObject dishObject = new DishObject();
//            dishObject.setDishID("1");
//            dishObject.setDishQuantity(quantity);
//            dishObject.setDishName("Test Name Paneer");
//            dishObject.setDishDescription("Test Desc Paneer");
//            dishObject.setDishImage("");
//            dishObject.setDishAmount("10000");

            DishObject dishObject = listDishProducts.get(position);

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.addItemToCart(createJsonCart(dishObject, quantity));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

                            adapterRestaurantMenu.showHideQuantityAndAddItemButton();
//                            listCartDish = new ArrayList<>();

//                            ada

//                            JSONArray jsonArray = new JSONArray(responseString);
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
                            addItemToCart(quantity, position);
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    public void showDialog() {
        progressIndicator.showProgress(RestaurantDetailsActivity.this);
    }

    public void dismissDialog() {
        if (progressIndicator != null) {
            progressIndicator.hideProgress();
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
