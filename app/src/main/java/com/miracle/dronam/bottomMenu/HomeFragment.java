package com.miracle.dronam.bottomMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miracle.dronam.R;
import com.miracle.dronam.activities.LocationGoogleMapActivity;
import com.miracle.dronam.activities.RestaurantDetailsActivity;
import com.miracle.dronam.activities.SeeMoreActivity;
import com.miracle.dronam.adapter.PagerAdapterBanner;
import com.miracle.dronam.adapter.RecycleAdapterCuisine;
import com.miracle.dronam.adapter.RecycleAdapterDish;
import com.miracle.dronam.adapter.RecycleAdapterRestaurant;
import com.miracle.dronam.listeners.OnRecyclerViewClickListener;
import com.miracle.dronam.model.CuisineObject;
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


public class HomeFragment extends Fragment implements OnRecyclerViewClickListener {
    View rootView;

    View viewToolbarLocation;
    LinearLayout llToolbarLocation;
    TextView tvToolbarTitle;

    private ArrayList<DishObject> listDishObject;
    private TextView tvSeeMoreDish;
    private RecyclerView rvDishUserLikes;
    private RecycleAdapterDish adapterDish;
    Integer image[] = {R.drawable.temp_paneer, R.drawable.temp_paratha, R.drawable.temp_paneer,
            R.drawable.temp_paratha, R.drawable.temp_paneer};
    String dish_name[] = {"Paratha", "Cheese Butter", "Paneer Handi", "Paneer Kopta", "Chiken"};
    String dish_type[] = {"Punjabi", "Maxican", "Punjabi", "Punjabi", "Non Veg"};
    String price[] = {"Rs 500 / person (app.)", "Rs 800 / person (app.)", "Rs 400 / person (app.)", "Rs 200 / person (app.)", "Rs 500 / person (app.)"};

    private ArrayList<CuisineObject> listCuisineObject;
    private TextView tvSeeMoreCuisines;
    private RecyclerView rvCuisines;
    private RecycleAdapterCuisine adapterCuisine;
    Integer image1[] = {R.drawable.temp_kesar, R.drawable.temp_ice_cream, R.drawable.temp_kesar,
            R.drawable.temp_ice_cream, R.drawable.temp_kesar};
    String price1[] = {"Rs 350", "Rs 200", "Rs 550", "Rs 400", "Rs 250"};
    String cuisineName[] = {"Thai Cusine", "Maxican", "Desert", "South Indian", "Italian"};
    String city[] = {"Chembur", "Thane", "Ghatkopar", "Bandra", "Dadar"};

    private ArrayList<RestaurantObject> listRestaurantObject;
    private TextView tvSeeMoreRestaurants;
    private RecyclerView rvRestaurants;
    private RecycleAdapterRestaurant adapterRestaurant;
    Integer image2[] = {R.mipmap.temp_img1, R.mipmap.temp_img2, R.mipmap.temp_img3,
            R.mipmap.temp_img4, R.mipmap.temp_img5, R.mipmap.temp_img6, R.mipmap.temp_img7};

    private ViewPager viewPager;
    private PagerAdapterBanner pagerAdapterForBanner;

    private final int REQUEST_CODE_LOCATION = 99;
    private final int REQUEST_CODE_SEE_MORE_DISH = 100;
    private final int REQUEST_CODE_SEE_MORE_CUISINE = 101;
    private final int REQUEST_CODE_SEE_MORE_RESTAURANT = 102;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initComponents();
        componentEvents();
        setToolbarLocation();
        setupRecyclerViewUserLikeDish();
        setupRecyclerViewCuisine();
//        setupRecyclerViewRestaurant();
        setupViewPagerBanner();

        getRestaurantData();


        return rootView;
    }

    private void initComponents() {
        viewToolbarLocation = rootView.findViewById(R.id.view_toolbarLocation);
        llToolbarLocation = viewToolbarLocation.findViewById(R.id.ll_toolbarLocation);
        tvToolbarTitle = viewToolbarLocation.findViewById(R.id.tv_toolbarTitle);

        rvDishUserLikes = (RecyclerView) rootView.findViewById(R.id.recyclerView_dishUserLike);
        rvCuisines = (RecyclerView) rootView.findViewById(R.id.recyclerView_cuisine);
        rvRestaurants = (RecyclerView) rootView.findViewById(R.id.rv_restaurant);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        tvSeeMoreDish = (TextView) rootView.findViewById(R.id.tv_seeMoreDish);
        tvSeeMoreCuisines = (TextView) rootView.findViewById(R.id.tv_seeMoreCuisine);
        tvSeeMoreRestaurants = (TextView) rootView.findViewById(R.id.tv_seeMoreRestaurant);

//        btnAddLogo = (Button) viewButtonAddLogo.findViewById(R.id.btn_addSurveyLogo);
    }

    private void componentEvents() {
//        viewToolbarLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), LocationGoogleMapActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_LOCATION);
//            }
//        });

        tvSeeMoreDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SeeMoreActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SEE_MORE_DISH);
            }
        });

        tvSeeMoreCuisines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SeeMoreActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SEE_MORE_CUISINE);
            }
        });

        tvSeeMoreRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SeeMoreActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SEE_MORE_RESTAURANT);
            }
        });

        llToolbarLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationGoogleMapActivity.class);
                startActivityForResult(intent, REQUEST_CODE_LOCATION);

//                Intent intent = new VanillaPlacePicker.Builder(getActivity())
//                        .with(PickerType.MAP_WITH_AUTO_COMPLETE) // Select Picker type to enable autocompelte, map or both
//                        .withLocation(23.057582, 72.534458)
//                        .setPickerLanguage(PickerLanguage.HINDI) // Apply language to picker
//                        .setLocationRestriction(new LatLng(23.0558088,72.5325067), new LatLng(23.0587592,72.5357321)) // Restrict location bounds in map and autocomplete
//                        .setCountry("IN") // Only for Autocomplete
//
//                        /*
//                         * Configuration for Map UI
//                         */
//                        .setMapType(MapType.SATELLITE) // Choose map type (Only applicable for map screen)
//                        .setMapStyle(R.raw.style_json) // Containing the JSON style declaration for night-mode styling
//                        .setMapPinDrawable(android.R.drawable.ic_menu_mylocation) // To give custom pin image for map marker
//
//            .build();
//
//                startActivityForResult(intent, 50);
            }
        });
    }

    private void setupRecyclerViewUserLikeDish() {
        getUserLikeDishData();

        adapterDish = new RecycleAdapterDish(getActivity(), listDishObject);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvDishUserLikes.setLayoutManager(mLayoutManager);
        rvDishUserLikes.setItemAnimator(new DefaultItemAnimator());
        rvDishUserLikes.setAdapter(adapterDish);
        adapterDish.setClickListener(this);
    }

    private void getUserLikeDishData() {
        listDishObject = new ArrayList<>();
        for (int i = 0; i < image.length; i++) {
//            DishObject dishObject = new DishObject(image[i], dish_name[i], dish_type[i], price[i]);
            DishObject dishObject = new DishObject();
            dishObject.setDishName(dish_name[i]);
            dishObject.setDishImage(String.valueOf(image[i]));
            dishObject.setDishCategory(dish_type[i]);
            listDishObject.add(dishObject);
        }
    }

    private void setupRecyclerViewCuisine() {
        getCuisineData();

        adapterCuisine = new RecycleAdapterCuisine(getActivity(), listCuisineObject);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvCuisines.setLayoutManager(mLayoutManager1);
        rvCuisines.setItemAnimator(new DefaultItemAnimator());
        rvCuisines.setAdapter(adapterCuisine);
        adapterCuisine.setClickListener(this);
    }

    private void getCuisineData() {
        listCuisineObject = new ArrayList<>();
        for (int i = 0; i < image.length; i++) {
            CuisineObject cuisineObject = new CuisineObject(image1[i], price1[i], cuisineName[i], city[i]);
            listCuisineObject.add(cuisineObject);
        }
    }

    private void setupRecyclerViewRestaurant() {
//        getRestaurantData();

        adapterRestaurant = new RecycleAdapterRestaurant(getActivity(), listRestaurantObject);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvRestaurants.setLayoutManager(layoutManager);
        rvRestaurants.setItemAnimator(new DefaultItemAnimator());
        rvRestaurants.setAdapter(adapterRestaurant);
        adapterRestaurant.setClickListener(this);
    }

//    private void getRestaurantData() {
//        listRestaurantObject = new ArrayList<>();
//        for (int i = 0; i < image2.length; i++) {
//            RestaurantObject restaurantObject = new RestaurantObject(image2[i]);
//            listRestaurantObject.add(restaurantObject);
//        }
//    }

    private void setupViewPagerBanner() {
        pagerAdapterForBanner = new PagerAdapterBanner(getFragmentManager());
        viewPager.setAdapter(pagerAdapterForBanner);
    }

    private void setToolbarLocation() {
        if (Application.locationAddressData != null) {
            tvToolbarTitle.setText(Application.locationAddressData.getAddressList().get(0).getSubLocality());
        }
    }

    @Override
    public void onClick(View view, int position) {
        RestaurantObject restaurantObject = listRestaurantObject.get(position);

        Intent intent = new Intent(getActivity(), RestaurantDetailsActivity.class);
        intent.putExtra("RestaurantObject", restaurantObject);
        startActivity(intent);
    }

    public void showSnackbarErrorMsg(String erroMsg) {
//        Snackbar.make(fragmentRootView, erroMsg, Snackbar.LENGTH_LONG).show();

        Snackbar snackbar = Snackbar.make(rootView, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

    private void getRestaurantData() {
        if (InternetConnection.checkConnection(getActivity())) {

            ApiInterface apiService = RetroClient.getApiService(getActivity());
//            Call<ResponseBody> call = apiService.getUserDetails(createJsonUserDetails());
            Call<ResponseBody> call = apiService.getRestaurantDetails("416004");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            listRestaurantObject = new ArrayList<>();

                            JSONArray jsonArray = new JSONArray(responseString);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);

                                String categoryID = jsonObj.optString("CategoryId");
                                String categoryName = jsonObj.optString("CategoryName");
                                String restaurantID = jsonObj.optString("ClientId");
                                String restaurantName = jsonObj.optString("RestaurantName");
                                String restaurantAddress = jsonObj.optString("ClientAddress");
                                String openTime = jsonObj.optString("OpentTime");
                                String closeTime = jsonObj.optString("CloseTime");
                                String contact = jsonObj.optString("Contact");
                                String description = jsonObj.optString("Description");
                                String longitude = jsonObj.optString("Langitude");
                                String latitude = jsonObj.optString("Latitude");
                                String rating = jsonObj.optString("Rating");
                                String foodTypeID = jsonObj.optString("FoodTypeId");
                                String foodTypeName = jsonObj.optString("FoodTypeName");
                                String logo = jsonObj.optString("Logo");
                                String taxID = jsonObj.optString("TaxId");
                                String taxable = jsonObj.optString("Taxable");
                                String includeTax = jsonObj.optString("IncludeTax");

                                RestaurantObject restaurantObject = new RestaurantObject();
                                restaurantObject.setCategoryID(categoryID);
                                restaurantObject.setCategoryName(categoryName);
                                restaurantObject.setRestaurantID(restaurantID);
                                restaurantObject.setRestaurantName(restaurantName);
                                restaurantObject.setRestaurantAddress(restaurantAddress);
                                restaurantObject.setOpenTime(openTime);
                                restaurantObject.setCloseTime(closeTime);
                                restaurantObject.setContact(contact);
                                restaurantObject.setDescription(description);
                                restaurantObject.setLongitude(longitude);
                                restaurantObject.setLatitude(latitude);
                                restaurantObject.setRating(rating);
                                restaurantObject.setFoodTypeID(foodTypeID);
                                restaurantObject.setFoodTypeName(foodTypeName);
                                restaurantObject.setLogo(logo);
                                restaurantObject.setTaxID(taxID);
                                restaurantObject.setTaxable(taxable);
                                restaurantObject.setIncludeTax(includeTax);

                                listRestaurantObject.add(restaurantObject);
                            }

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                        setupRecyclerViewRestaurant();

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
                            getRestaurantData();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (resultCode == Activity.RESULT_OK && data != null) {

            }

        } else if (requestCode == REQUEST_CODE_SEE_MORE_CUISINE) {
            if (resultCode == Activity.RESULT_OK && data != null) {

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
