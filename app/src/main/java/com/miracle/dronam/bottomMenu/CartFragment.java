package com.miracle.dronam.bottomMenu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.miracle.dronam.R;
import com.miracle.dronam.adapter.RecycleAdapterDish;
import com.miracle.dronam.adapter.RecycleAdapterOrderedItem;
import com.miracle.dronam.adapter.RecycleAdapterRestaurant;
import com.miracle.dronam.model.DishObject;
import com.miracle.dronam.model.OrderDetailsObject;
import com.miracle.dronam.model.RestaurantObject;
import com.miracle.dronam.service.retrofit.ApiInterface;
import com.miracle.dronam.service.retrofit.RetroClient;
import com.miracle.dronam.utils.Application;
import com.miracle.dronam.utils.InternetConnection;
import com.miracle.dronam.utils.SimpleDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {
    View rootView;
    LinearLayout llBrowseMenu;
    TextView tvPaymentButton;

    String dish_name[] = {"Paratha", "Cheese Butter", "Paneer Handi", "Paneer Kopta", "Chiken"};
    String dish_type[] = {"Punjabi", "Maxican", "Punjabi", "Punjabi", "Non Veg"};
    String price[] = {"Rs 500 / person (app.)", "Rs 800 / person (app.)", "Rs 400 / person (app.)", "Rs 200 / person (app.)", "Rs 500 / person (app.)"};

    Integer image[] = {R.drawable.temp_paneer, R.drawable.temp_paratha, R.drawable.temp_paneer,
            R.drawable.temp_paratha, R.drawable.temp_paneer};

    private RecyclerView rvOrderedItems;
    private RecycleAdapterOrderedItem adapterOrderedItems;

    private ArrayList<DishObject> listCartDish;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        init();
        componentEvents();
//        setupRecyclerViewOrderedItems();

        getCartItems();

        return rootView;
    }

    private void init()
    {
        llBrowseMenu = rootView.findViewById(R.id.ll_browseMenu);
        tvPaymentButton = rootView.findViewById(R.id.tv_paymentButton);
        rvOrderedItems = (RecyclerView) rootView.findViewById(R.id.recyclerView_orderedItems);
    }

    private void componentEvents() {
        llBrowseMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToHomeFragment1();
            }
        });

        tvPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
    }

    private void setupRecyclerViewOrderedItems() {
        adapterOrderedItems = new RecycleAdapterOrderedItem(getActivity(), listCartDish);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvOrderedItems.setLayoutManager(layoutManager);
        rvOrderedItems.setItemAnimator(new DefaultItemAnimator());
        rvOrderedItems.setAdapter(adapterOrderedItems);

//        rvOrderedItems.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

//        adapterOrderedItems.setClickListener(this);
    }

    private void getTESTUserLikeDishData() {
        listCartDish = new ArrayList<>();
        for (int i = 0; i < image.length; i++) {
//            DishObject dishObject = new DishObject(image[i], dish_name[i], dish_type[i], price[i]);
            DishObject dishObject = new DishObject();
            dishObject.setDishName(dish_name[i]);
            dishObject.setDishImage(String.valueOf(image[i]));
            dishObject.setDishCategory(dish_type[i]);
            listCartDish.add(dishObject);
        }
    }


    public void switchToHomeFragment1() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, new HomeFragment());
        transaction.commit();
    }

    private void getCartItems() {
        if (InternetConnection.checkConnection(getActivity())) {

            String userTypeID = Application.userDetails.getUserType();
            String restaurantID = "1";

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.getCartItem(userTypeID, restaurantID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            listCartDish = new ArrayList<>();

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
                                dishObject.setDishAmount(dishPrice);

                                listCartDish.add(dishObject);
                            }

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                        getTESTUserLikeDishData();
                        setupRecyclerViewOrderedItems();

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

            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getCartItems();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private void placeOrder() {
        if (InternetConnection.checkConnection(getActivity())) {

            String userTypeID = Application.userDetails.getUserType();
            String restaurantID = "1";

            OrderDetailsObject orderObj = new OrderDetailsObject();
            orderObj.setTotalAmount("450");
            orderObj.setUserTypeID(userTypeID);
            orderObj.setRestaurantID(restaurantID);

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.placeOrder(orderObj);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            listCartDish = new ArrayList<>();

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
//                                dishObject.setDishAmount(dishPrice);
//
//                                listCartDish.add(dishObject);
//                            }

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                        setupRecyclerViewOrderedItems();

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

            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getCartItems();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }


    public void showSnackbarErrorMsg(String erroMsg) {
        Snackbar snackbar = Snackbar.make(rootView, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

}
