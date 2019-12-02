package com.miracle.dronam.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.miracle.dronam.R;
import com.miracle.dronam.adapter.RecycleAdapterPastOrders;
import com.miracle.dronam.model.DishObject;
import com.miracle.dronam.model.OrderDetailsObject;
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

public class PastOrdersFragment extends Fragment {
    View rootView;

    String[] restaurantName  = {"Cocobolo Poolside Bar + Grill","Palm Beach Seafood Restaurant","Shin Minori Japanese Restaurant","Shin Minori Japanese Restaurant"};
    String[] orderAddress = {"Chembur","Ghatkopar","Thane","Sion"};
    String[] restaurantReviews ={"238 reviews","200 reviews","556 reviews","240 reviews"};
    String[] orderDate = {"25 Nov 2017 10 : 30 AM","27 Nov 2017 10 : 30 AM","28 Nov 2017 10 : 30 AM","29 Nov 2017 10 : 30 AM"};
    double[] orderPrice={199.00, 249.00, 149.00, 399.00};
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
//        setupRecyclerViewPastOrders();

        getPastOrderDetails();

        return rootView;
    }

    private void initComponents() {
        rvPastOrders = rootView.findViewById(R.id.recyclerView_pastOrders);
    }

    private void setupRecyclerViewPastOrders()
    {
//        getTESTPastOrdersData();

        adapterPastOrders = new RecycleAdapterPastOrders(getActivity(), listPastOrders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPastOrders.setLayoutManager(layoutManager);
        rvPastOrders.setItemAnimator(new DefaultItemAnimator());
        rvPastOrders.setAdapter(adapterPastOrders);
    }

    private void getTESTPastOrdersData() {
        listPastOrders = new ArrayList<>();

        for (int i=0; i<restaurantName.length; i++){
//            OrderDetailsObject food7_model = new OrderDetailsObject(restaurantName[i], orderAddress[i],
//                    restaurantReviews[i], orderDate[i], orderPrice[i], foodImage[i]);
//            listPastOrders.add(food7_model);

            OrderDetailsObject orderDetailsObject = new OrderDetailsObject();
            orderDetailsObject.setRestaurantName(restaurantName[i]);
            orderDetailsObject.setUserAddress(orderAddress[i]);
//            orderDetailsObject.setRestaurantReviews(restaurantReviews[i]);
            orderDetailsObject.setOrderDate(orderDate[i]);
            orderDetailsObject.setTotalAmount(orderPrice[i]);
//            orderDetailsObject.setDishImage(String.valueOf(foodImage[i]));

            listPastOrders.add(orderDetailsObject);
        }
    }

    private void getPastOrderDetails() {
        if (InternetConnection.checkConnection(getActivity())) {

            String userTypeID = Application.userDetails.getUserType();

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.getPastOrders(userTypeID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            listPastOrders = new ArrayList<>();

                            JSONArray jsonArray = new JSONArray(responseString);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);

//                                String cgst = jsonObj.optString("CGST");
//                                String cgstVal = jsonObj.optString("CGSTval");
//                                String restaurantID = jsonObj.optString("Clientid");
//                                String discount = jsonObj.optString("Discount");
//                                String foodType = jsonObj.optString("FoodType");
//                                String includeTax = jsonObj.optString("IncludeTax");
//                                String orderDate = jsonObj.optString("OrderDate");
//                                String orderID = jsonObj.optString("OrderId");
//                                String orderMode = jsonObj.optString("OrderMode");
//                                String orderNumber = jsonObj.optString("OrderNumber");
//                                String orderStatus = jsonObj.optString("OrderStatus");
//                                String orderType = jsonObj.optString("OrderType");
//                                String paymentID = jsonObj.optString("PaymentId");
//                                String dishAmount = jsonObj.optString("ProductAmount");
//                                String dishID = jsonObj.optString("ProductId");
//                                String dishName = jsonObj.optString("ProductName");
//                                String dishQuantity = jsonObj.optString("ProductQnty");
//                                String dishRate = jsonObj.optString("ProductRate");
//                                String dishSize = jsonObj.optString("ProductSize");
//                                String rejectReason = jsonObj.optString("RejectReason");
//                                String restaurantName = jsonObj.optString("RestaurantName");
//                                String sgst = jsonObj.optString("SGST");
//                                String sgstVal = jsonObj.optString("SGSTval");
//                                String subtotal = jsonObj.optString("Subtotal");
//                                String taxID = jsonObj.optString("TaxId");
//                                String taxable = jsonObj.optString("Taxable");
//                                String taxableVal = jsonObj.optString("Taxableval");
//                                String totalAmount = jsonObj.optString("TotalAmount");
//                                String userAddress = jsonObj.optString("UserAddress");
//                                String userRole = jsonObj.optString("UserRole");
//                                String userTypeId = jsonObj.optString("UserTypeId");
//                                String userID = jsonObj.optString("Userid");
//                                String voucher = jsonObj.optString("Voucher");
//
//                                OrderDetailsObject orderObj = new OrderDetailsObject();
//                                orderObj.setCGST(cgst);
//                                orderObj.setCGSTVal(cgstVal);
//                                orderObj.setRestaurantID(restaurantID);
//                                orderObj.setDiscount(discount);
//                                orderObj.setFoodType(foodType);
//                                orderObj.setIncludeTax(includeTax);
//                                orderObj.setOrderDate(orderDate);
//                                orderObj.setOrderID(orderID);
//                                orderObj.setOrderMode(orderMode);
//                                orderObj.setOrderNumber(orderNumber);
//                                orderObj.setOrderStatus(orderStatus);
//
//                                orderObj.setOrderType(orderType);
//                                orderObj.setPaymentID(paymentID);
//                                orderObj.setDishAmount(dishAmount);
//                                orderObj.setDishID(dishID);
//                                orderObj.setDishName(dishName);
//                                orderObj.setDishQuantity(dishQuantity);
//                                orderObj.setDishRate(dishRate);
//                                orderObj.setDishSize(dishSize);
//                                orderObj.setRejectReason(rejectReason);
//                                orderObj.setRestaurantName(restaurantName);
//                                orderObj.setSGST(sgst);
//                                orderObj.setSGSTVal(sgstVal);
//                                orderObj.setSubtotal(subtotal);
//                                orderObj.setTaxID(taxID);
//                                orderObj.setTaxable(taxable);
//                                orderObj.setTaxableVal(taxableVal);
//                                orderObj.setTotalAmount(totalAmount);
//                                orderObj.setUserAddress(userAddress);
//                                orderObj.setUserRole(userRole);
//                                orderObj.setUserTypeID(userTypeId);
//                                orderObj.setUserID(userID);
//                                orderObj.setVoucher(voucher);
//
//                                listPastOrders.add(orderObj);
                            }

                            getTESTPastOrdersData();
                            setupRecyclerViewPastOrders();

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

            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getPastOrderDetails();
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

