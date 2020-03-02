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
import com.google.gson.JsonObject;
import com.miracle.dronam.R;
import com.miracle.dronam.adapter.RecycleAdapterPastOrders;
import com.miracle.dronam.adapter.RecycleAdapterRestaurantMenu;
import com.miracle.dronam.listeners.OnPastOrderOptionsClickListener;
import com.miracle.dronam.listeners.OnRecyclerViewClickListener;
import com.miracle.dronam.model.DishObject;
import com.miracle.dronam.model.OrderDetailsObject;
import com.miracle.dronam.model.RestaurantObject;
import com.miracle.dronam.service.retrofit.ApiInterface;
import com.miracle.dronam.service.retrofit.RetroClient;
import com.miracle.dronam.utils.Application;
import com.miracle.dronam.utils.InternetConnection;
import com.miracle.dronam.utils.Utils;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastOrdersFragment extends Fragment implements OnPastOrderOptionsClickListener {
    View rootView;

//    String[] restaurantName  = {"Cocobolo Poolside Bar + Grill","Palm Beach Seafood Restaurant","Shin Minori Japanese Restaurant","Shin Minori Japanese Restaurant"};
//    String[] orderAddress = {"Chembur","Ghatkopar","Thane","Sion"};
//    String[] restaurantReviews ={"238 reviews","200 reviews","556 reviews","240 reviews"};
//    String[] orderDate = {"25 Nov 2017 10 : 30 AM","27 Nov 2017 10 : 30 AM","28 Nov 2017 10 : 30 AM","29 Nov 2017 10 : 30 AM"};
//    double[] orderPrice={199.00, 249.00, 149.00, 399.00};
//    Integer[] foodImage = {R.mipmap.temp_order, R.mipmap.temp_order, R.mipmap.temp_order, R.mipmap.temp_order};

    private RecyclerView rvPastOrders;
    private RecycleAdapterPastOrders adapterPastOrders;
    private ArrayList<OrderDetailsObject> listAllOrders;

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

    private void setupRecyclerViewPastOrders() {
//        getTESTPastOrdersData();
        ArrayList<OrderDetailsObject> listPastOrders = formatPastOrderData();

        Collections.sort(listPastOrders, new Comparator<OrderDetailsObject>() {
            public int compare(OrderDetailsObject o1, OrderDetailsObject o2) {
                if (o1.getOrderDate() == null || o2.getOrderDate() == null)
                    return 0;
                return o2.getOrderDate().compareTo(o1.getOrderDate());
            }
        });

        adapterPastOrders = new RecycleAdapterPastOrders(getActivity(), listPastOrders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPastOrders.setLayoutManager(layoutManager);
        rvPastOrders.setItemAnimator(new DefaultItemAnimator());
        rvPastOrders.setAdapter(adapterPastOrders);
    }

//    private void getTESTPastOrdersData() {
//        listPastOrders = new ArrayList<>();
//
//        for (int i=0; i<restaurantName.length; i++){
////            OrderDetailsObject food7_model = new OrderDetailsObject(restaurantName[i], orderAddress[i],
////                    restaurantReviews[i], orderDate[i], orderPrice[i], foodImage[i]);
////            listPastOrders.add(food7_model);
//
//            OrderDetailsObject orderDetailsObject = new OrderDetailsObject();
//            orderDetailsObject.setRestaurantName(restaurantName[i]);
//            orderDetailsObject.setUserAddress(orderAddress[i]);
////            orderDetailsObject.setRestaurantReviews(restaurantReviews[i]);
//            orderDetailsObject.setOrderDate(orderDate[i]);
//            orderDetailsObject.setTotalAmount(orderPrice[i]);
////            orderDetailsObject.setDishImage(String.valueOf(foodImage[i]));
//
//            listPastOrders.add(orderDetailsObject);
//        }
//    }

//    private String convertJsonDate(String jsonDate) {
//        String dateTime = "";
//        try {
//            String timeString = jsonDate.substring(jsonDate.indexOf("(") + 1, jsonDate.indexOf(")"));
//            String[] timeSegments = timeString.split("\\+");
//            // May have to handle negative timezones
//            int timeZoneOffSet = Integer.valueOf(timeSegments[1]) * 36000; // (("0100" / 100) * 3600 * 1000)
//            long millis = Long.valueOf(timeSegments[0]);
//            Date date = new Date(millis + timeZoneOffSet);
//
//            //It Will Be in format 29-OCT-2014 2:26 PM
//            dateTime = new SimpleDateFormat("dd-MMM-yyyy h:mm tt").format(date);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return dateTime;
//    }

    private ArrayList<OrderDetailsObject> formatPastOrderData() {
        ArrayList<OrderDetailsObject> listPastOrders = new ArrayList<>();

        ArrayList<OrderDetailsObject> tempAllOrders = SerializationUtils.clone(listAllOrders);

        for (int i = 0; i < listAllOrders.size(); i++) {
            int orderNo = listAllOrders.get(i).getOrderNumber();
            int orderNoInList = 0;

            ArrayList<DishObject> listProducts = new ArrayList<>();
            for (int j = 0; j < tempAllOrders.size(); ) {
                orderNoInList = tempAllOrders.get(j).getOrderNumber();

                if (orderNo == orderNoInList) {
                    DishObject dishObject = new DishObject();
                    dishObject.setProductName(tempAllOrders.get(j).getProductName());
                    dishObject.setProductQuantity(tempAllOrders.get(j).getProductQuantity());

                    listProducts.add(dishObject);
                    tempAllOrders.remove(j);
                    j = 0;
                } else {
                    j++;
                }
            }

            if (listProducts != null && listProducts.size() > 0) {
                listAllOrders.get(i).setListProducts(listProducts);

                OrderDetailsObject orderDetailsObject = SerializationUtils.clone(listAllOrders.get(i));
                listPastOrders.add(orderDetailsObject);
            }
        }

        return listPastOrders;
    }

    @Override
    public void onClickReceipt(View view, int position) {

    }

    @Override
    public void onClickReorder(View view, int position) {
        OrderDetailsObject orderDetailsObject = listAllOrders.get(position);
        ArrayList<DishObject> listProducts = orderDetailsObject.getListProducts();

        for (int i = 0; i < listProducts.size(); i++) {
            DishObject dishObject = listProducts.get(i);
            addItemToCart(dishObject);
        }
    }

    private void getPastOrderDetails() {
        if (InternetConnection.checkConnection(getActivity())) {

            int userTypeID = Application.userDetails.getUserID();

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.getPastOrders(userTypeID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            listAllOrders = new ArrayList<>();
//
                            JSONArray jsonArray = new JSONArray(responseString);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);

                                double cgst = jsonObj.optDouble("CGST");
                                int restaurantID = jsonObj.optInt("Clientid");
                                String orderDate = jsonObj.optString("OrderDate");
                                int orderID = jsonObj.optInt("OrderId");
                                int orderMode = jsonObj.optInt("OrderMode");
                                int orderNumber = jsonObj.optInt("OrderNumber");
                                boolean orderPaid = jsonObj.optBoolean("OrderPaid");     // @@@@@@
                                int orderStatus = jsonObj.optInt("OrderStatus");
                                int orderType = jsonObj.optInt("OrderType");
                                int paymentID = jsonObj.optInt("PaymentId");
                                int dishID = jsonObj.optInt("ProductId");
                                String dishName = jsonObj.optString("ProductName");
                                int dishQuantity = jsonObj.optInt("ProductQnty");
                                double dishRate = jsonObj.optDouble("ProductRate");
                                String rejectReason = jsonObj.optString("RejectReason");
                                String restaurantName = jsonObj.optString("RestaurantName");
                                double sgst = jsonObj.optDouble("SGST");
                                int taxID = jsonObj.optInt("TaxId");
                                double taxableVal = jsonObj.optDouble("Taxableval");
                                double totalAmount = jsonObj.optDouble("TotalAmount");
                                String userAddress = jsonObj.optString("UserAddress");
                                int userID = jsonObj.optInt("Userid");

                                String convertedOrderDate = Utils.convertJsonDate(orderDate);

                                OrderDetailsObject orderObj = new OrderDetailsObject();
                                orderObj.setCgst(cgst);
                                orderObj.setClientID(restaurantID);
                                orderObj.setOrderDate(convertedOrderDate);
                                orderObj.setOrderID(orderID);
                                orderObj.setOrderMode(orderMode);
                                orderObj.setOrderNumber(orderNumber);
                                orderObj.setOrderStatus(orderStatus);
                                orderObj.setOrderType(orderType);
                                orderObj.setPaymentID(paymentID);
                                orderObj.setProductID(dishID);
                                orderObj.setProductName(dishName);
                                orderObj.setProductQuantity(dishQuantity);
                                orderObj.setProductRate(dishRate);
                                orderObj.setRejectReason(rejectReason);
                                orderObj.setRestaurantName(restaurantName);
                                orderObj.setSgst(sgst);
                                orderObj.setTaxID(taxID);
                                orderObj.setTaxableVal(taxableVal);
                                orderObj.setTotalAmount(totalAmount);
                                orderObj.setUserAddress(userAddress);
                                orderObj.setUserID(userID);

                                listAllOrders.add(orderObj);
                            }

//                            getTESTPastOrdersData();
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

    private JsonObject createJsonCart(DishObject dishObject) {
        RestaurantObject restaurantObject = Application.restaurantObject;

        JsonObject postParam = new JsonObject();

        try {
            postParam.addProperty("ProductId", dishObject.getProductID());
            postParam.addProperty("ProductName", dishObject.getProductName());
            postParam.addProperty("ProductRate", dishObject.getPrice());
            postParam.addProperty("ProductAmount", dishObject.getPrice());
            postParam.addProperty("ProductSize", "Regular");
            postParam.addProperty("cartId", 0);
            postParam.addProperty("ProductQnty", dishObject.getProductQuantity());
            postParam.addProperty("Taxableval", dishObject.getPrice());    // doubt
            postParam.addProperty("CGST", dishObject.getCgst());
            postParam.addProperty("SGST", dishObject.getSgst());
            postParam.addProperty("HotelName", restaurantObject.getRestaurantName());
            postParam.addProperty("IsIncludeTax", restaurantObject.getIncludeTax());
            postParam.addProperty("IsTaxApplicable", restaurantObject.getTaxable());
            postParam.addProperty("DeliveryCharge", 20);
            postParam.addProperty("Userid", Application.userDetails.getUserID());
            postParam.addProperty("Clientid", restaurantObject.getRestaurantID());
            postParam.addProperty("TotalAmount", dishObject.getPrice());
            postParam.addProperty("TaxId", 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return postParam;
    }

    public void addItemToCart(final DishObject dishObject) {
        if (InternetConnection.checkConnection(getActivity())) {

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.addItemToCart(createJsonCart(dishObject));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

//                            if (onItemAddedToCart != null) {
//                                onItemAddedToCart.onItemChangedInCart(quantity, position, incrementOrDecrement);
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

            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addItemToCart(dishObject);
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

