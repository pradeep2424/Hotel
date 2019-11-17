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

import com.google.android.material.snackbar.Snackbar;
import com.miracle.dronam.R;
import com.miracle.dronam.model.DishObject;
import com.miracle.dronam.model.RestaurantObject;
import com.miracle.dronam.service.retrofit.ApiInterface;
import com.miracle.dronam.service.retrofit.RetroClient;
import com.miracle.dronam.utils.Application;
import com.miracle.dronam.utils.InternetConnection;

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

        return rootView;
    }

    private void init()
    {
        llBrowseMenu = rootView.findViewById(R.id.ll_browseMenu);
    }

    private void componentEvents() {
        llBrowseMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToHomeFragment1();
            }
        });
    }

    public void switchToHomeFragment1() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, new HomeFragment());
        transaction.commit();
    }

//    private void getCartItems() {
//        if (InternetConnection.checkConnection(getActivity())) {
//
//            String userTypeID = Application.userDetails.getUserType();
//            String restaurantID = restaurantObject.getRestaurantID();
//            String foodTypeID = restaurantObject.getFoodTypeID();
//            String categoryID = restaurantObject.getCategoryID();
//
//            ApiInterface apiService = RetroClient.getApiService(getActivity());
//            Call<ResponseBody> call = apiService.getProductDetailsData(userTypeID, restaurantID, foodTypeID, categoryID);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                    try {
//                        int statusCode = response.code();
//
//                        if (response.isSuccessful()) {
//                            String responseString = response.body().string();
//                            listCartDish = new ArrayList<>();
//
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
//
//                        } else {
//                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
//                        }
//
//                        setupRecyclerViewProducts();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    try {
//                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
//                        Log.e("Error onFailure : ", t.toString());
//                        t.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } else {
////            signOutFirebaseAccounts();
//
//            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction("RETRY", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            getCartItems();
//                        }
//                    })
////                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
//                    .show();
//        }
//    }

    public void showSnackbarErrorMsg(String erroMsg) {
        Snackbar snackbar = Snackbar.make(rootView, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

}
