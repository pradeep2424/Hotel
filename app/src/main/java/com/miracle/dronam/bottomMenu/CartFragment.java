package com.miracle.dronam.bottomMenu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.miracle.dronam.R;
import com.miracle.dronam.adapter.RecycleAdapterDish;
import com.miracle.dronam.adapter.RecycleAdapterOrderedItem;
import com.miracle.dronam.adapter.RecycleAdapterRestaurant;
import com.miracle.dronam.listeners.OnItemAddedToCart;
import com.miracle.dronam.model.CartObject;
import com.miracle.dronam.model.DishObject;
import com.miracle.dronam.model.OrderDetailsObject;
import com.miracle.dronam.model.RestaurantObject;
import com.miracle.dronam.model.UserDetails;
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


public class CartFragment extends Fragment implements OnItemAddedToCart {
    View rootView;
    LinearLayout llBrowseMenu;
    TextView tvPaymentButton;

    RelativeLayout rlCartItemDetails;
    View viewEmptyCart;

    String dish_name[] = {"Paratha", "Cheese Butter", "Paneer Handi", "Paneer Kopta", "Chiken"};
    String dish_type[] = {"Punjabi", "Maxican", "Punjabi", "Punjabi", "Non Veg"};
    String price[] = {"Rs 500 / person (app.)", "Rs 800 / person (app.)", "Rs 400 / person (app.)", "Rs 200 / person (app.)", "Rs 500 / person (app.)"};

    Integer image[] = {R.drawable.temp_paneer, R.drawable.temp_paratha, R.drawable.temp_paneer,
            R.drawable.temp_paratha, R.drawable.temp_paneer};

    private RecyclerView rvOrderedItems;
    private RecycleAdapterOrderedItem adapterOrderedItems;

    private ArrayList<CartObject> listCartDish;

    int userID;
    int restaurantID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = Application.userDetails.getUserID();
        restaurantID = Application.restaurantObject.getRestaurantID();
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

    private void init() {
        rlCartItemDetails = rootView.findViewById(R.id.rl_cartItemLayout);
        viewEmptyCart = rootView.findViewById(R.id.view_emptyCart);
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

        adapterOrderedItems.setOnItemAddedToCart(this);

//        rvOrderedItems.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

//        adapterOrderedItems.setClickListener(this);
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


    public void switchToHomeFragment1() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, new HomeFragment());
        transaction.commit();
    }

    @Override
    public void onItemChangedInCart(int quantity, int position) {

        if (quantity == 0) {
            deleteCartItem(quantity, position);

        } else {
            addItemToCart(quantity, position);
        }
    }

    private void showEmptyCart() {
        viewEmptyCart.setVisibility(View.VISIBLE);
        rlCartItemDetails.setVisibility(View.GONE);
    }

    private void showCartItemDetails() {
        viewEmptyCart.setVisibility(View.GONE);
        rlCartItemDetails.setVisibility(View.VISIBLE);
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

                            setupRecyclerViewOrderedItems();

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

    private JsonObject createJsonCart(CartObject cartObject, int quantity) {
        double totalPrice;

        RestaurantObject restaurantObject = Application.restaurantObject;

        if(restaurantObject.getTaxable())
        {
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
            postParam.addProperty("ProductQnty", quantity);
            postParam.addProperty("Taxableval",  cartObject.getProductAmount());    // doubt
            postParam.addProperty("CGST", cartObject.getCgst());
            postParam.addProperty("SGST",cartObject.getSgst());
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


    public void addItemToCart(final int quantity, final int position) {
        if (InternetConnection.checkConnection(getActivity())) {

            CartObject cartObject = listCartDish.get(position);

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.addItemToCart(createJsonCart(cartObject, quantity));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

                            adapterOrderedItems.updateCartItemQuantity(quantity);
                            showCartItemDetails();

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
                            addItemToCart(quantity, position);
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    public void deleteCartItem(final int quantity, final int position) {
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

                            adapterOrderedItems.removeAt(position);
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
                            addItemToCart(quantity, position);
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private JsonObject createJsonPlaceOrder(CartObject cartObject) {
        RestaurantObject restaurantObject = Application.restaurantObject;

        JsonObject postParam = new JsonObject();

        try {
            postParam.addProperty("orderID", cartObject.getProductID());
            postParam.addProperty("orderNumber", cartObject.getProductName());
            postParam.addProperty("orderDate", cartObject.getProductAmount());
            postParam.addProperty("orderType", cartObject.getProductAmount());
            postParam.addProperty("orderStatus", "Regular");
            postParam.addProperty("orderMode", cartObject.getCartID());
            postParam.addProperty("ProductQnty", 2);
            postParam.addProperty("paymentID",  cartObject.getProductAmount());    // doubt
            postParam.addProperty("productID", cartObject.getCgst());
            postParam.addProperty("productName",cartObject.getSgst());
            postParam.addProperty("productRate", restaurantObject.getRestaurantName());
            postParam.addProperty("productQuantity", restaurantObject.getIncludeTax());
            postParam.addProperty("taxableVal", restaurantObject.getTaxable());
            postParam.addProperty("cgst", 30.00);
            postParam.addProperty("sgst", Application.userDetails.getUserID());
            postParam.addProperty("userAddress", restaurantObject.getRestaurantID());
            postParam.addProperty("userID", cartObject.getTotalAmount());
            postParam.addProperty("restaurantID", 0);
            postParam.addProperty("restaurantName", 0);
            postParam.addProperty("totalAmount", 0);
            postParam.addProperty("taxID", 0);
            postParam.addProperty("orderPaid", 0);
            postParam.addProperty("rejectReason", 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return postParam;
    }


    private void placeOrder() {
        if (InternetConnection.checkConnection(getActivity())) {

            UserDetails userDetails = Application.userDetails;
            RestaurantObject restaurantObj = Application.restaurantObject;
            CartObject cartObject = Application.cartObject;

            String userTypeID = Application.userDetails.getUserType();
            String restaurantID = "1";

            OrderDetailsObject orderObj = new OrderDetailsObject();
            orderObj.setOrderID(1);
            orderObj.setOrderNumber(1);
            orderObj.setOrderType(1);
            orderObj.setOrderStatus(1);
            orderObj.setOrderMode(1);
            orderObj.setPaymentID(1);
            orderObj.setProductID(1);
            orderObj.setProductName(cartObject.getProductName());
            orderObj.setProductRate(cartObject.getProductRate());
            orderObj.setProductQuantity(cartObject.getProductQuantity());
            orderObj.setTaxableVal(cartObject.getTaxableVal());
            orderObj.setCgst(cartObject.getCgst());
            orderObj.setSgst(cartObject.getSgst());
            orderObj.setUserAddress(userDetails.getAddress());
            orderObj.setUserID(userDetails.getUserID());
            orderObj.setRestaurantID(restaurantObj.getRestaurantID());
            orderObj.setRestaurantName(restaurantObj.getRestaurantName());
            orderObj.setTaxID(cartObject.getTaxID());
            orderObj.setOrderPaid(true);
            orderObj.setRejectReason("NO");
            orderObj.setOrderDate("28-11-2019");

            ApiInterface apiService = RetroClient.getApiService(getActivity());
            Call<ResponseBody> call = apiService.placeOrder(createJsonPlaceOrder(cartObject));
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

                            setupRecyclerViewOrderedItems();

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
