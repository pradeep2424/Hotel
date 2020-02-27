package com.miracle.dronam.bottomMenu;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.miracle.dronam.R;
import com.miracle.dronam.adapter.RecycleAdapterOrderedItem;
import com.miracle.dronam.listeners.OnItemAddedToCart;
import com.miracle.dronam.listeners.TriggerTabChangeListener;
import com.miracle.dronam.model.CartObject;
import com.miracle.dronam.model.OrderDetailsObject;
import com.miracle.dronam.model.RestaurantObject;
import com.miracle.dronam.model.UserDetails;
import com.miracle.dronam.service.retrofit.ApiInterface;
import com.miracle.dronam.service.retrofit.RetroClient;
import com.miracle.dronam.utils.Application;
import com.miracle.dronam.utils.InternetConnection;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;
import com.suke.widget.SwitchButton;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment implements OnItemAddedToCart {
    View rootView;
    LinearLayout llBrowseMenu;
    TextView tvPaymentButton;

    RelativeLayout rlCartItemDetails;
    View viewEmptyCart;

    LinearLayout llReferralPointsLayout;
    SwitchButton switchButtonApplyPoints;
    TextView tvBalancePoints;
    TextView tvSaveReferralPointsMessage;

    private RecyclerView rvOrderedItems;
    private RecycleAdapterOrderedItem adapterOrderedItems;

    private TextView tvItemTotal;
    private TextView tvRestaurantCharges;
    private TextView tvDeliveryFee;
    private TextView tvDeliveryFreeText;
    private TextView tvTotalPaymentAmount;
    private TextView tvPaymentButtonAmount;

    TriggerTabChangeListener triggerTabChangeListener;
    private ArrayList<CartObject> listCartDish;

    double totalPayment;
    int userID;
    double referralPoints;
    int restaurantID;
    int orderNumber;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        triggerTabChangeListener = (TriggerTabChangeListener) context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = Application.userDetails.getUserID();
        restaurantID = Application.restaurantObject.getRestaurantID();
        referralPoints = Application.userDetails.getTotalReferralPoints();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        init();
        componentEvents();
        setupReferralPointsLayout();

        getOrderNumber();
        getCartItems();

        return rootView;
    }

    private void init() {
        rlCartItemDetails = rootView.findViewById(R.id.rl_cartItemLayout);
        viewEmptyCart = rootView.findViewById(R.id.view_emptyCart);
        llBrowseMenu = rootView.findViewById(R.id.ll_browseMenu);
        tvPaymentButton = rootView.findViewById(R.id.tv_paymentButton);
        rvOrderedItems = (RecyclerView) rootView.findViewById(R.id.recyclerView_orderedItems);

        llReferralPointsLayout = rootView.findViewById(R.id.ll_referralPointsLayout);
        switchButtonApplyPoints = rootView.findViewById(R.id.switchButton_applyPoints);
        tvBalancePoints = rootView.findViewById(R.id.tv_balanceReferralPoints);
        tvSaveReferralPointsMessage = rootView.findViewById(R.id.tv_referralPointsSaveMessage);

        tvItemTotal = rootView.findViewById(R.id.tv_itemTotalText);
        tvRestaurantCharges = rootView.findViewById(R.id.tv_restaurantChargesText);
        tvDeliveryFee = rootView.findViewById(R.id.tv_deliveryFeeText);
        tvDeliveryFreeText = rootView.findViewById(R.id.tv_deliveryFeeTextFree);
        tvTotalPaymentAmount = rootView.findViewById(R.id.tv_totalPayText);
        tvPaymentButtonAmount = rootView.findViewById(R.id.tv_paymentButtonAmount);


    }

    private void componentEvents() {
        llBrowseMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerTabChangeListener.setTab(0);
            }
        });

        tvPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
    }

    private void setupReferralPointsLayout() {
        String formattedPoints = getFormattedNumberDouble(referralPoints);

        if (referralPoints == 0) {
            llReferralPointsLayout.setVisibility(View.GONE);

        } else {
            llReferralPointsLayout.setVisibility(View.VISIBLE);

            String balanceText = getString(R.string.referral_points_you_will_save)
                    + " " + getString(R.string.rupees)
                    + " " + formattedPoints
                    + " " + getString(R.string.using)
                    + " " + formattedPoints
                    + " " + getString(R.string.referral_points);

            tvBalancePoints.setText(formattedPoints + " " + getString(R.string.rupees));
            tvSaveReferralPointsMessage.setText(balanceText);
        }
    }

    private void setupRecyclerViewOrderedItems() {
        adapterOrderedItems = new RecycleAdapterOrderedItem(getActivity(), listCartDish);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvOrderedItems.setLayoutManager(layoutManager);
        rvOrderedItems.setItemAnimator(new DefaultItemAnimator());
        rvOrderedItems.setAdapter(adapterOrderedItems);

        adapterOrderedItems.setOnItemAddedToCart(this);

//        rvOrderedItems.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

//        adapterOrderedItems.setClickListener(this);
    }

//    private void setupBillingDetails() {
//        CartObject cartObject = Application.listCartItems.get(0);
//        double itemTotal = getUpdateItemPrice(cartObject);
//        double deliveryCharges = cartObject.getDeliveryCharge();
//
//        double sgst = cartObject.getSgst();
//        double cgst = cartObject.getCgst();
//        sgst = itemTotal * (sgst / 100);
//        cgst = itemTotal * (cgst / 100);
//        double totalGST = sgst + cgst;
//
//        double totalPayment = itemTotal + totalGST + deliveryCharges;
//
//        tvItemTotal.setText("₹ " + itemTotal);
//        tvRestaurantCharges.setText("₹ " + totalGST);
//        tvDeliveryFee.setText("₹ " + deliveryCharges);
//        tvTotalPaymentAmount.setText("₹ " + totalPayment);
//        tvPaymentButtonAmount.setText("₹ " + totalPayment);
//    }

    private void setupBillingDetails() {
        double itemTotal = 0;
        double deliveryCharges = 0;
        double sgst = 0;
        double cgst = 0;

        for (int i = 0; i < Application.listCartItems.size(); i++) {
            CartObject cartObject = SerializationUtils.clone(Application.listCartItems.get(i));

            itemTotal = itemTotal + getUpdateItemPrice(cartObject);
            deliveryCharges = cartObject.getDeliveryCharge();
            sgst = cartObject.getSgst();
            cgst = cartObject.getCgst();
        }

        sgst = itemTotal * (sgst / 100);
        cgst = itemTotal * (cgst / 100);
        double totalGST = sgst + cgst;


        if (itemTotal > 200) {
            totalPayment = itemTotal + totalGST;

            tvDeliveryFreeText.setVisibility(View.VISIBLE);
            tvDeliveryFee.setPaintFlags(tvDeliveryFee.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } else {
            totalPayment = itemTotal + totalGST + deliveryCharges;

            tvDeliveryFreeText.setVisibility(View.GONE);
            tvDeliveryFee.setPaintFlags(tvDeliveryFee.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        tvItemTotal.setText("₹ " + itemTotal);
        tvRestaurantCharges.setText("₹ " + totalGST);
        tvDeliveryFee.setText("₹ " + deliveryCharges);
        tvTotalPaymentAmount.setText("₹ " + totalPayment);
        tvPaymentButtonAmount.setText("₹ " + totalPayment);

    }

    private double getUpdateItemPrice(CartObject cartObject) {
        double updatedPrice = cartObject.getProductRate() * cartObject.getProductQuantity();
        return updatedPrice;
    }

//    private void getTESTUserLikeDishData() {
//        listCartDish = new ArrayList<>();
//        for (int i = 0; i < image.length; i++) {
////            DishObject dishObject = new DishObject(image[i], dish_name[i], dish_type[i], price[i]);
//            CartObject cartObject = new CartObject();
//            cartObject.setProductName(dish_name[i]);
//            cartObject.setProductImage(String.valueOf(image[i]));
//            cartObject.setCategoryName(dish_type[i]);
//            listCartDish.add(dishObject);
//        }
//    }

    @Override
    public void onItemChangedInCart(int quantity, int position, String incrementOrDecrement) {
        updateItemQuantityInCart(quantity, position);

//        if (quantity == 0) {
//            deleteCartItem(quantity, position);
//
//        } else {
//            addItemToCart(quantity, position);
//        }
    }

    private void showEmptyCart() {
        viewEmptyCart.setVisibility(View.VISIBLE);
        rlCartItemDetails.setVisibility(View.GONE);
    }

    private void showCartItemDetails() {
        viewEmptyCart.setVisibility(View.GONE);
        rlCartItemDetails.setVisibility(View.VISIBLE);
    }

    private void getOrderNumber() {
        if (InternetConnection.checkConnection(getActivity())) {

            String strRestaurantID = String.valueOf(restaurantID);

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.getOrderNo(strRestaurantID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();
                        if (response.isSuccessful()) {

                            String strOrderNumber = response.body().string();
                            if (strOrderNumber != null) {
                                orderNumber = Integer.parseInt(strOrderNumber);
                            }

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
                            getOrderNumber();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }


    private void getCartItems() {
        if (InternetConnection.checkConnection(getActivity())) {

//            String userTypeID = "0";
//            String restaurantID = "1";

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.getCartItem(userID, restaurantID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            listCartDish = new ArrayList<>();

                            JSONArray jsonArray = new JSONArray(responseString);

                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObj = jsonArray.getJSONObject(i);

                                    double cgst = jsonObj.optDouble("CGST");
                                    int restaurantID = jsonObj.optInt("Clientid");
                                    double deliveryCharge = jsonObj.optDouble("DeliveryCharge");
                                    String restaurantName = jsonObj.optString("HotelName");
                                    boolean isIncludeTax = jsonObj.optBoolean("IsIncludeTax");
                                    boolean isTaxApplicable = jsonObj.optBoolean("IsTaxApplicable");
                                    double productAmount = jsonObj.optDouble("ProductAmount");
                                    int productID = jsonObj.optInt("ProductId");
                                    String productName = jsonObj.optString("ProductName");
                                    int productQuantity = jsonObj.optInt("ProductQnty");
                                    double productRate = jsonObj.optDouble("ProductRate");
                                    String productSize = jsonObj.optString("ProductSize");
                                    double sgst = jsonObj.optDouble("SGST");
                                    int taxID = jsonObj.optInt("TaxId");
                                    double taxableVal = jsonObj.optDouble("Taxableval");
                                    double totalAmount = jsonObj.optDouble("TotalAmount");
                                    int userID = jsonObj.optInt("Userid");
                                    int cartID = jsonObj.optInt("cartId");


                                    CartObject cartObject = new CartObject();
                                    cartObject.setCgst(cgst);
                                    cartObject.setRestaurantID(restaurantID);
                                    cartObject.setDeliveryCharge(deliveryCharge);
                                    cartObject.setRestaurantName(restaurantName);
                                    cartObject.setIsIncludeTax(isIncludeTax);
                                    cartObject.setIsTaxApplicable(isTaxApplicable);
                                    cartObject.setProductAmount(productAmount);
                                    cartObject.setProductID(productID);
                                    cartObject.setProductName(productName);
                                    cartObject.setProductQuantity(productQuantity);
                                    cartObject.setProductRate(productRate);
                                    cartObject.setProductSize(productSize);
                                    cartObject.setSgst(sgst);
                                    cartObject.setTaxID(taxID);
                                    cartObject.setTaxableVal(taxableVal);
                                    cartObject.setTotalAmount(totalAmount);
                                    cartObject.setUserID(userID);
                                    cartObject.setCartID(cartID);


                                    listCartDish.add(cartObject);
                                }

                                Application.listCartItems = SerializationUtils.clone(listCartDish);
                                int noOfCartItems = Application.listCartItems.size();
                                triggerTabChangeListener.setBadgeCount(noOfCartItems);

                                showCartItemDetails();
                                setupRecyclerViewOrderedItems();
                                setupBillingDetails();

                            } else {
                                showEmptyCart();
                            }

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

//                        getTESTUserLikeDishData();

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

    private JsonObject createJsonCart(CartObject cartObject) {
        double totalPrice;

        RestaurantObject restaurantObject = Application.restaurantObject;

        if (restaurantObject.getTaxable()) {
            double productPrice = cartObject.getProductAmount();
            double cgst = cartObject.getCgst();
            double sgst = cartObject.getCgst();

//            totalPrice = productPrice * ()
        }

        JsonObject postParam = new JsonObject();

        try {
            postParam.addProperty("ProductId", cartObject.getProductID());
            postParam.addProperty("ProductName", cartObject.getProductName());
            postParam.addProperty("ProductRate", cartObject.getProductAmount());
            postParam.addProperty("ProductAmount", cartObject.getProductAmount());
            postParam.addProperty("ProductSize", "Regular");
            postParam.addProperty("cartId", cartObject.getCartID());
            postParam.addProperty("ProductQnty", cartObject.getProductQuantity());
            postParam.addProperty("Taxableval", cartObject.getProductAmount());    // doubt
            postParam.addProperty("CGST", cartObject.getCgst());
            postParam.addProperty("SGST", cartObject.getSgst());
            postParam.addProperty("HotelName", restaurantObject.getRestaurantName());
            postParam.addProperty("IsIncludeTax", restaurantObject.getIncludeTax());
            postParam.addProperty("IsTaxApplicable", restaurantObject.getTaxable());
            postParam.addProperty("DeliveryCharge", 30.00);
            postParam.addProperty("Userid", Application.userDetails.getUserID());
            postParam.addProperty("Clientid", restaurantObject.getRestaurantID());
            postParam.addProperty("TotalAmount", cartObject.getTotalAmount());
            postParam.addProperty("TaxId", 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return postParam;
    }


    public void updateItemQuantityInCart(final int quantity, final int position) {
        if (InternetConnection.checkConnection(getActivity())) {

//            final CartObject cartObjectOld = SerializationUtils.clone(listCartDish.get(position));
            final CartObject cartObjectUpdated = SerializationUtils.clone(listCartDish.get(position));
            cartObjectUpdated.setProductQuantity(quantity);

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.addItemToCart(createJsonCart(cartObjectUpdated));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

                            if (quantity != 0) {
                                double price = getUpdateItemPrice(cartObjectUpdated);

                                adapterOrderedItems.updateCartItemQuantity(quantity);
                                adapterOrderedItems.updateCartItemPrice(price);
                                showCartItemDetails();

                                Application.listCartItems.set(position, cartObjectUpdated);
//                                Application.listCartItems.add(cartObject);

                            } else {
                                adapterOrderedItems.removeAt(position);
                                Application.listCartItems.remove(position);

                                int noOfCartItems = Application.listCartItems.size();
                                triggerTabChangeListener.setBadgeCount(noOfCartItems);

                                if (Application.listCartItems != null && Application.listCartItems.size() == 0) {
                                    showEmptyCart();
                                }
                            }

                            setupBillingDetails();

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

            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateItemQuantityInCart(quantity, position);
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    public void deleteCartItem() {
        if (InternetConnection.checkConnection(getActivity())) {

//            CartObject cartObject = listCartDish.get(position);

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.deleteCartItem(userID, restaurantID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

                            showEmptyCart();

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

            Snackbar.make(rootView, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteCartItem();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private JsonObject createJsonPlaceOrder(OrderDetailsObject orderDetailsObject) {
        JsonObject postParam = new JsonObject();

        try {
//                postParam.addProperty("OrderId", orderDetailsObject.getOrderID());
            postParam.addProperty("OrderNumber", orderDetailsObject.getOrderNumber());
//                postParam.addProperty("OrderDate", orderDetailsObject.getOrderDate());
            postParam.addProperty("OrderType", orderDetailsObject.getOrderType());
            postParam.addProperty("OrderStatus", 0);       // order placed
            postParam.addProperty("OrderMode", orderDetailsObject.getOrderMode());
//                postParam.addProperty("PaymentId", orderDetailsObject.getPaymentID());    // doubt
            postParam.addProperty("PaymentId", 1);     // cash
            postParam.addProperty("ProductId", orderDetailsObject.getProductID());
            postParam.addProperty("ProductName", orderDetailsObject.getProductName());
            postParam.addProperty("ProductRate", orderDetailsObject.getProductRate());
            postParam.addProperty("ProductQnty", orderDetailsObject.getProductQuantity());
            postParam.addProperty("Taxableval", orderDetailsObject.getTaxableVal());
            postParam.addProperty("CGST", orderDetailsObject.getCgst());
            postParam.addProperty("SGST", orderDetailsObject.getSgst());
            postParam.addProperty("UserAddress", orderDetailsObject.getUserAddress());
//            postParam.addProperty("UserAddress", "ABCD");
            postParam.addProperty("Userid", orderDetailsObject.getUserID());
            postParam.addProperty("Clientid", orderDetailsObject.getClientID());
//                postParam.addProperty("RestaurantName", orderDetailsObject.getRestaurantName());
            postParam.addProperty("TotalAmount", orderDetailsObject.getTotalAmount());
            postParam.addProperty("TaxId", orderDetailsObject.getTaxID());
            postParam.addProperty("OrderPaid", orderDetailsObject.getOrderPaid());
            postParam.addProperty("RejectReason", orderDetailsObject.getRejectReason());

////                JsonObject postParam = new JsonObject();
////                postParam.addProperty("orderID", orderDetailsObject.getOrderID());
//                postParam.addProperty("orderNumber", orderDetailsObject.getOrderNumber());
////                postParam.addProperty("orderDate", orderDetailsObject.getOrderDate());
//                postParam.addProperty("orderType", orderDetailsObject.getOrderType());
//                postParam.addProperty("orderStatus", orderDetailsObject.getOrderStatus());
//                postParam.addProperty("orderMode", orderDetailsObject.getOrderMode());
////                postParam.addProperty("paymentID", orderDetailsObject.getPaymentID());    // doubt
//                postParam.addProperty("productID", orderDetailsObject.getProductID());
//                postParam.addProperty("productName", orderDetailsObject.getProductName());
//                postParam.addProperty("productRate", orderDetailsObject.getProductRate());
//                postParam.addProperty("ProductQnty", orderDetailsObject.getProductQuantity());
//                postParam.addProperty("taxableVal", orderDetailsObject.getTaxableVal());
//                postParam.addProperty("cgst", orderDetailsObject.getCgst());
//                postParam.addProperty("sgst", orderDetailsObject.getSgst());
//                postParam.addProperty("UserAddress", "ABCD");
//                postParam.addProperty("userID", orderDetailsObject.getUserID());
//                postParam.addProperty("restaurantID", orderDetailsObject.getClientID());
////                postParam.addProperty("restaurantName", orderDetailsObject.getRestaurantName());
//                postParam.addProperty("totalAmount", orderDetailsObject.getTotalAmount());
//                postParam.addProperty("taxID", orderDetailsObject.getTaxID());
//                postParam.addProperty("orderPaid", orderDetailsObject.getOrderPaid());
//                postParam.addProperty("rejectReason", orderDetailsObject.getRejectReason());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return postParam;
    }

    private void placeOrder() {
        if (InternetConnection.checkConnection(getActivity())) {
            try {

                UserDetails userDetails = Application.userDetails;
                final ArrayList<CartObject> listCartItems = Application.listCartItems;


                for (int i = 0; i < listCartItems.size(); i++) {
                    CartObject cartObject = listCartItems.get(i);

                    OrderDetailsObject orderObj = new OrderDetailsObject();
//                    orderObj.setOrderID(i + 1);
                    orderObj.setOrderNumber(orderNumber);
                    orderObj.setOrderType(i + 1);
                    orderObj.setOrderStatus(0);     // order placed
                    orderObj.setOrderMode(i + 1);
                    orderObj.setPaymentID(1);      //  cash
                    orderObj.setProductID(cartObject.getProductID());
                    orderObj.setProductName(cartObject.getProductName());
                    orderObj.setProductRate(cartObject.getProductRate());
                    orderObj.setProductQuantity(cartObject.getProductQuantity());
                    orderObj.setTaxableVal(cartObject.getTaxableVal());
                    orderObj.setCgst(cartObject.getCgst());
                    orderObj.setSgst(cartObject.getSgst());
                    orderObj.setTotalAmount(totalPayment);
                    orderObj.setUserAddress(userDetails.getAddress());
//                    orderObj.setUserAddress("ABCD");
                    orderObj.setUserID(userDetails.getUserID());
                    orderObj.setClientID(cartObject.getRestaurantID());
//                    orderObj.setRestaurantName(cartObject.getRestaurantName());
                    orderObj.setTaxID(cartObject.getTaxID());
                    orderObj.setOrderPaid(false);
                    orderObj.setRejectReason("NO");
//                    orderObj.setOrderDate(getCurrentDate());

//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

                    final int currentIndex = i;

                    ApiInterface apiService = RetroClient.getApiService(getActivity());
                    Call<ResponseBody> call = apiService.placeOrder(createJsonPlaceOrder(orderObj));
//                    Call<ResponseBody> call = apiService.placeOrder(orderObj);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {
                                int statusCode = response.code();

                                if (response.isSuccessful()) {
                                    String responseString = response.body().string();

                                    if (currentIndex == listCartItems.size() - 1) {
                                        triggerTabChangeListener.setBadgeCount(0);
                                        deleteCartItem();
                                        sendAppliedReferrelPoints();
                                        showSuccessOrderMsg();
                                    }

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


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

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

    public void sendAppliedReferrelPoints() {
        if (InternetConnection.checkConnection(getActivity())) {

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.setReferrelPoint(userID, referralPoints);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

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
                            deleteCartItem();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private String getFormattedNumberDouble(double amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }


//    private void placeOrder() {
//        if (InternetConnection.checkConnection(getActivity())) {
//            ArrayList<OrderDetailsObject> listOrderDetails = new ArrayList<>();
//            try {
//
//                UserDetails userDetails = Application.userDetails;
//                ArrayList<CartObject> listCartItems = Application.listCartItems;
//
////                RestaurantObject restaurantObj = Application.restaurantObject;
////                String userTypeID = Application.userDetails.getUserType();
////                String restaurantID = "1";
//
//                for (int i = 0; i < listCartItems.size(); i++) {
//                    CartObject cartObject = listCartItems.get(i);
//
//                    OrderDetailsObject orderObj = new OrderDetailsObject();
//                    orderObj.setOrderID(i + 1);
//                    orderObj.setOrderNumber(i + 1);
//                    orderObj.setOrderType(i + 1);
//                    orderObj.setOrderStatus(i + 1);
//                    orderObj.setOrderMode(i + 1);
//                    orderObj.setPaymentID(i + 1);
//                    orderObj.setProductID(cartObject.getProductID());
//                    orderObj.setProductName(cartObject.getProductName());
//                    orderObj.setProductRate(cartObject.getProductRate());
//                    orderObj.setProductQuantity(cartObject.getProductQuantity());
//                    orderObj.setTaxableVal(cartObject.getTaxableVal());
//                    orderObj.setCgst(cartObject.getCgst());
//                    orderObj.setSgst(cartObject.getSgst());
//                    orderObj.setUserAddress(userDetails.getAddress());
//                    orderObj.setUserID(userDetails.getUserID());
//                    orderObj.setClientID(cartObject.getRestaurantID());
//                    orderObj.setRestaurantName(cartObject.getRestaurantName());
//                    orderObj.setTaxID(cartObject.getTaxID());
//                    orderObj.setOrderPaid(false);
//                    orderObj.setRejectReason("NO");
//                    orderObj.setOrderDate(getCurrentDate());
//
//                    listOrderDetails.add(orderObj);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            ApiInterface apiService = RetroClient.getApiService(getActivity());
////          Call<ResponseBody> call = apiService.placeOrder(createJsonPlaceOrder(listOrderDetails));
//            Call<ResponseBody> call = apiService.placeOrder(listOrderDetails);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                    try {
//                        int statusCode = response.code();
//
//                        if (response.isSuccessful()) {
//                            String responseString = response.body().string();
//
////                            if (responseString.equalsIgnoreCase(getString(R.string.success))) {
////                                setupRecyclerViewOrderedItems();
//
////                                showSnackBarErrorMsgWithButton(getString(R.string.order_placed_successfully));
//
//                            showSuccessOrderMsg();
//                            showEmptyCart();
////                            }
//
//                        } else {
//                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
//                        }
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

    private String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

//    private JsonObject createJsonPlaceOrder(OrderDetailsObject orderDetailsObject) {
////        RestaurantObject restaurantObject = Application.restaurantObject;
//
//        JsonObject postParam = new JsonObject();
//
//        try {
//
//            postParam.addProperty("orderID", orderDetailsObject.getProductID());
//            postParam.addProperty("orderNumber", orderDetailsObject.getProductName());
//            postParam.addProperty("orderDate", orderDetailsObject.getOrderDate());
//            postParam.addProperty("orderType", orderDetailsObject.getOrderType());
//            postParam.addProperty("orderStatus", orderDetailsObject.getOrderStatus());
//            postParam.addProperty("orderMode", orderDetailsObject.getOrderMode());
//            postParam.addProperty("paymentID", orderDetailsObject.getPaymentID());    // doubt
//            postParam.addProperty("productID", orderDetailsObject.getProductID());
//            postParam.addProperty("productName", orderDetailsObject.getProductName());
//            postParam.addProperty("productRate", orderDetailsObject.getProductRate());
//            postParam.addProperty("ProductQnty", orderDetailsObject.getProductQuantity());
//            postParam.addProperty("taxableVal", orderDetailsObject.getTaxableVal());
//            postParam.addProperty("cgst", orderDetailsObject.getCgst());
//            postParam.addProperty("sgst", orderDetailsObject.getSgst());
//            postParam.addProperty("UserAddress", orderDetailsObject.getUserAddress());
//            postParam.addProperty("userID", orderDetailsObject.getUserID());
//            postParam.addProperty("restaurantID", orderDetailsObject.getRestaurantID());
//            postParam.addProperty("restaurantName", orderDetailsObject.getRestaurantName());
//            postParam.addProperty("totalAmount", orderDetailsObject.getTotalAmount());
//            postParam.addProperty("taxID", orderDetailsObject.getTaxID());
//            postParam.addProperty("orderPaid", orderDetailsObject.getOrderPaid());
//            postParam.addProperty("rejectReason", orderDetailsObject.getRejectReason());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return postParam;
//    }
//
//    private void placeOrder() {
//        if (InternetConnection.checkConnection(getActivity())) {
//
//            OrderDetailsObject orderObj = new OrderDetailsObject();
//            try {
//
//                UserDetails userDetails = Application.userDetails;
//                RestaurantObject restaurantObj = Application.restaurantObject;
//
//                CartObject cartObject = Application.listCartItems.get(0);
//
//                String userTypeID = Application.userDetails.getUserType();
//                String restaurantID = "1";
//
//                orderObj.setOrderID(1);
//                orderObj.setOrderNumber(1);
//                orderObj.setOrderType(1);
//                orderObj.setOrderStatus(1);
//                orderObj.setOrderMode(1);
//                orderObj.setPaymentID(1);
//                orderObj.setProductID(cartObject.getProductID());
//                orderObj.setProductName(cartObject.getProductName());
//                orderObj.setProductRate(cartObject.getProductRate());
//                orderObj.setProductQuantity(cartObject.getProductQuantity());
//                orderObj.setTaxableVal(cartObject.getTaxableVal());
//                orderObj.setCgst(cartObject.getCgst());
//                orderObj.setSgst(cartObject.getSgst());
//                orderObj.setUserAddress(userDetails.getAddress());
//                orderObj.setUserID(userDetails.getUserID());
//                orderObj.setRestaurantID(cartObject.getRestaurantID());
//                orderObj.setRestaurantName(cartObject.getRestaurantName());
//                orderObj.setTaxID(cartObject.getTaxID());
//                orderObj.setOrderPaid(true);
//                orderObj.setRejectReason("NO");
//                orderObj.setOrderDate("28-11-2019");
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            ApiInterface apiService = RetroClient.getApiService(getActivity());
//            Call<ResponseBody> call = apiService.placeOrder(createJsonPlaceOrder(orderObj));
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
////                            for (int i = 0; i < jsonArray.length(); i++) {
////                                JSONObject jsonObj = jsonArray.getJSONObject(i);
////
////                                String dishID = jsonObj.optString("ProductId");
////                                String dishName = jsonObj.optString("ProductName");
////                                String dishDescription = jsonObj.optString("ProductDesc");
////                                String dishImage = jsonObj.optString("ProductImage");
////                                String dishPrice = jsonObj.optString("Price");
////
////                                DishObject dishObject = new DishObject();
////                                dishObject.setDishID(dishID);
////                                dishObject.setDishName(dishName);
////                                dishObject.setDishDescription(dishDescription);
////                                dishObject.setDishImage(dishImage);
////                                dishObject.setDishAmount(dishPrice);
////
////                                listCartDish.add(dishObject);
////                            }
//
//                            setupRecyclerViewOrderedItems();
//
//                        } else {
//                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
//                        }
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

    private void showSuccessOrderMsg() {
        new FancyGifDialog.Builder(getActivity())
                .setTitle(getString(R.string.order_placed_title))
                .setMessage(getString(R.string.order_placed_message))
                .setPositiveBtnText("Ok")
                .setPositiveBtnBackground("#ff3d00")
                .setGifResource(R.drawable.gif1)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
//                        Toast.makeText(MainActivity.this,"Ok",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }

    public void showSnackbarErrorMsg(String erroMsg) {
        Snackbar snackbar = Snackbar.make(rootView, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

    public void showSnackBarErrorMsgWithButton(String erroMsg) {
        Snackbar snackbar = Snackbar.make(rootView, erroMsg, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);

        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }
}
