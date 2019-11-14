package com.miracle.dronam.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.miracle.dronam.R;
import com.miracle.dronam.adapter.PlacesAutoCompleteAdapter;
import com.miracle.dronam.main.MainActivity;
import com.miracle.dronam.signUp.GetStartedMobileNumberActivity;
import com.miracle.dronam.utils.Application;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.PlacePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import com.vanillaplacepicker.data.VanillaAddress;
//import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker;
//import com.vanillaplacepicker.utils.KeyUtils;
//import com.vanillaplacepicker.utils.PickerLanguage;
//import com.vanillaplacepicker.utils.PickerType;
//import com.vanillaplacepicker.data.VanillaAddress;
//import com.vanillaplacepicker.extenstion.ViewExtsKt;
//import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker;
//import com.vanillaplacepicker.utils.MapType;
//import com.vanillaplacepicker.utils.PickerLanguage;
//import com.vanillaplacepicker.utils.PickerType;


public class LocationGoogleMapActivity extends AppCompatActivity implements PlacesAutoCompleteAdapter.ClickListener {
    private View viewCurrentLocation;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;
    private EditText etSearchText;

    double latitude = 0;
    double longitude = 0;

    private final int REQUEST_PERMISSION_LOCATION = 1001;
    private final int REQUEST_CODE_MAP = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_google_map);

        init();
        setupSearchLocation();
        componentEvents();

//        searchLocationOnMap();
//        openGoogleMaps(23.057582, 72.534458);
    }

    private void init() {
        viewCurrentLocation = findViewById(R.id.view_currentLocation);
        recyclerView = findViewById(R.id.places_recycler_view);
        etSearchText = findViewById(R.id.et_searchAddress);
        etSearchText.addTextChangedListener(filterTextWatcher);
    }

    private void componentEvents() {
        viewCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestLocationPermission()) {
                    openGoogleMaps(0, 0);
                }
            }
        });
    }

    private void setupSearchLocation() {
        Places.initialize(this, getResources().getString(R.string.google_maps_key));
        PlacesClient placesClient = Places.createClient(this);

        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAutoCompleteAdapter);

        mAutoCompleteAdapter.setClickListener(this);
        mAutoCompleteAdapter.notifyDataSetChanged();

//        String locationName = "Nha Hang restaurant";
//        Geocoder gc = new Geocoder(this);
//        try {
//            List<Address> addressList = gc.getFromLocationName(locationName, 5);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());

                if (recyclerView.getVisibility() == View.GONE) {
                    recyclerView.setVisibility(View.VISIBLE);
                    viewCurrentLocation.setVisibility(View.GONE);
                }
            } else {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                    viewCurrentLocation.setVisibility(View.VISIBLE);
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    @Override
    public void click(Place place) {
        Toast.makeText(this, place.getAddress() + ", " + place.getLatLng().latitude + place.getLatLng().longitude, Toast.LENGTH_SHORT).show();

        latitude = place.getLatLng().latitude;
        longitude = place.getLatLng().longitude;
        openGoogleMaps(latitude, longitude);
    }

//    private void searchLocationOnMap() {
//        Intent intent = new VanillaPlacePicker.Builder(this)
//                .with(PickerType.AUTO_COMPLETE) // Select Picker type to enable autocompelte, map or both
//                .withLocation(23.057582, 72.534458)
//                .setPickerLanguage(PickerLanguage.ENGLISH) // Apply language to picker
////                .setLocationRestriction(new LatLng(23.0558088, 72.5325067), new LatLng(23.0587592, 72.5357321)) // Restrict location bounds in map and autocomplete
//                .setCountry("IN") // Only for Autocomplete
//
//                /*
//                 * Configuration for Map UI
//                 */
////                .setMapType(MapType.NORMAL) // Choose map type (Only applicable for map screen)
////                .setMapStyle(R.raw.style_json) // Containing the JSON style declaration for night-mode styling
////                .setMapPinDrawable(R.drawable.ic_location_on_red_24dp) // To give custom pin image for map marker
//
//                .build();
//
//        startActivityForResult(intent, KeyUtils.REQUEST_PLACE_PICKER);
//    }

    private void openGoogleMaps(double latitude, double longitude) {
//        Intent intent = new PlacePicker.IntentBuilder()
//                .setLatLong(40.748672, -73.985628)
//                .showLatLong(true)
//                .setMapRawResourceStyle(R.raw.map_style)
//                .setMapType(MapType.NORMAL)
//                .build(LocationGoogleMapActivity.this);
//        startActivityForResult(intent, REQUEST_CODE_PLACE_PICKER);

        Intent intent = new PlacePicker.IntentBuilder()
                .setLatLong(latitude, longitude)  // Initial Latitude and Longitude the Map will load into
//                .setLatLong(40.748672, -73.985628)
                .showLatLong(false)  // Show Coordinates in the Activity
                .setMapZoom(18.0f)  // Map Zoom Level. Default: 14.0
                .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                .hideMarkerShadow(true) // Hides the shadow under the map marker. Default: False
//                .setMarkerDrawable(R.drawable.marker) // Change the default Marker Image
//                .setMarkerImageImageColor(R.color.dark_orange)
//                .setFabColor(R.color.white)
                .setPrimaryTextColor(R.color.main_text) // Change text color of Shortened Address
//                .setSecondaryTextColor(R.color.main_text) // Change text color of full Address
                .setMapRawResourceStyle(R.raw.map_style)  //Set Map Style (https://mapstyle.withgoogle.com/)
                .setMapType(com.sucho.placepicker.MapType.NORMAL)
//                .onlyCoordinates(true)  //Get only Coordinates from Place Picker
                .build(LocationGoogleMapActivity.this);
        startActivityForResult(intent, REQUEST_CODE_MAP);
    }

    private boolean requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_PERMISSION_LOCATION);
                return false;
            }
        }
        return true;

    }

    private void showDialogOK(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.location_permission_title))
                .setMessage(getResources().getString(R.string.location_permission_text))
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == KeyUtils.REQUEST_PLACE_PICKER) {
//            if (resultCode == RESULT_OK && data != null) {
//                VanillaAddress vanillaAddress = VanillaPlacePicker.Companion.onActivityResult(requestCode, resultCode, data);
//
//                if (vanillaAddress != null) {
//                    double latitude = vanillaAddress.getLatitude();
//                    double longitude = vanillaAddress.getLongitude();
//
//                    String countryName = vanillaAddress.getCountryName();
//                    String countryCode = vanillaAddress.getCountryCode();
//                    String postalCode = vanillaAddress.getPostalCode();
//                    String formattedAddress = vanillaAddress.getFormattedAddress();
//
//
//
////                    boolean var6 = false;
////                    boolean var7 = false;
////                    int var9 = false;
////                    CardView var10000 = (CardView) this._$_findCachedViewById(id.cardviewSelectedPlace);
////                    Intrinsics.checkExpressionValueIsNotNull(var10000, "cardviewSelectedPlace");
////                    ViewExtsKt.showView((View) var10000);
////                    TextView var10 = (TextView) this._$_findCachedViewById(id.tvSelectedPlace);
////                    Intrinsics.checkExpressionValueIsNotNull(var10, "tvSelectedPlace");
////                    var10.setText((CharSequence) vanillaAddress.getFormattedAddress());
//                }
//            }
//        } else
        if (requestCode == REQUEST_CODE_MAP) {
            if (resultCode == RESULT_OK && data != null) {
                AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
                Application.locationAddressData = addressData;

                Intent it = new Intent(LocationGoogleMapActivity.this, MainActivity.class);
                startActivity(it);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:

                Map<String, Integer> perms1 = new HashMap<>();
                perms1.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
//                perms1.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                perms1.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        perms1.put(permissions[i], grantResults[i]);
                    }

                    // Check for both permissions
//                    if (perms1.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
////                            && perms1.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
////                            && perms1.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    if (perms1.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        openGoogleMaps(latitude, longitude);

                    } else {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                            showDialogOK(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            requestLocationPermission();

                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:

                                            break;
                                    }
                                }
                            });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable app permissions",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }

                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent it = new Intent(LocationGoogleMapActivity.this, GetStartedMobileNumberActivity.class);
        startActivity(it);
        finish();
    }
}
