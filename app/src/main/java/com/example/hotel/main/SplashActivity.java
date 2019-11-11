package com.example.hotel.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hotel.R;
import com.example.hotel.model.UserDetails;
import com.example.hotel.service.retrofit.ApiInterface;
import com.example.hotel.service.retrofit.RetroClient;
import com.example.hotel.signUp.LoginActivity;
import com.example.hotel.utils.Application;
import com.example.hotel.utils.InternetConnection;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    RelativeLayout rlRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
        getUserDetails();
        getAreaDetails();

        insertUserDetails();

        loadNextPage();
    }

    private void init()
    {
        rlRootLayout = findViewById(R.id.rl_rootLayout);
    }

    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

//    private JSONObject createJsonUserDetails() {
//        JSONObject postParam = new JSONObject();
//
//        try {
//            postParam.put("Username", "test");
//            postParam.put("Password", "test");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return postParam;
//    }

    private void getUserDetails() {
        if (InternetConnection.checkConnection(this)) {

            ApiInterface apiService = RetroClient.getApiService(this);
//            Call<ResponseBody> call = apiService.getUserDetails(createJsonUserDetails());
            Call<ResponseBody> call = apiService.getUserDetails("abc1", "123456");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();
                        if (response.isSuccessful()) {

                            String responseString = response.body().string();

                            JSONObject jsonObj = new JSONObject(responseString);
                            String status = jsonObj.optString("Status");

                            String name = jsonObj.optString("Name");
                            String gender = jsonObj.optString("Gender");
                            String email = jsonObj.optString("Email");
                            String mobile = jsonObj.optString("Mobile");
                            String telephone = jsonObj.optString("Telephone");
                            String facebookId = jsonObj.optString("FacebookId");

                            String userID = jsonObj.optString("UserID");
                            String username = jsonObj.optString("Username");
                            String userPhoto = jsonObj.optString("UserPhoto");
                            String userRole = jsonObj.optString("UserRole");
                            String userType = jsonObj.optString("UserType");

                            String address = jsonObj.optString("Address");
                            String area = jsonObj.optString("Area");
                            String cityName = jsonObj.optString("CityName");
                            String stateName = jsonObj.optString("StateName");
                            String zipCode = jsonObj.optString("ZipCode");

                            UserDetails userDetails = new UserDetails();
                            userDetails.setName(name);
                            userDetails.setGender(gender);
                            userDetails.setEmail(email);
                            userDetails.setMobile(mobile);
                            userDetails.setTelephone(telephone);
                            userDetails.setFacebookId(facebookId);
                            userDetails.setUserID(userID);
                            userDetails.setUsername(username);
                            userDetails.setUserPhoto(userPhoto);
                            userDetails.setUserRole(userRole);
                            userDetails.setUserType(userType);
                            userDetails.setAddress(address);
                            userDetails.setArea(area);
                            userDetails.setCityName(cityName);
                            userDetails.setStateName(stateName);
                            userDetails.setZipCode(zipCode);
                            Application.userDetails = userDetails;

//                            if (status.equalsIgnoreCase("Success")) {
//                                FirebaseUser user = mAuth.getCurrentUser();
//
//                                String accessToken = jsonObj.getString("AccessToken");
//                                int accountType = jsonObj.getInt("AccountType");
//                                String mainCorpNo = jsonObj.getString("MainCorpNo");
//                                String corpID = jsonObj.getString("CorpID");
//                                String emailID = jsonObj.getString("EmailID");
//                                String firstName = jsonObj.getString("FirstName");
//                                String lastName = jsonObj.getString("LastName");
//
//                                String hibernate = jsonObj.getString("Hibernate");
//                                boolean isGracePeriod = jsonObj.getBoolean("IsGracePeriod");
//                                boolean isTrial = jsonObj.getBoolean("IsTrial");
//                                msgHeader = jsonObj.getString("MessageHeader");
//                                message = jsonObj.getString("Message");
//
//                                int maxQuestionsAllowed = jsonObj.getInt("MaxQuestionsAllowed");
//                                int maxSurveysAllowed = jsonObj.getInt("MaxSurveysAllowed");
//
//                            } else if (status.equalsIgnoreCase("LoginFailed")) {
//
//                                String msg = jsonObj.getString("Message");
//                                String msgHeader = jsonObj.getString("MessageHeader");
//
//                                prefs.edit().clear().apply();
//                                signOutFirebaseAccounts();
//
//
//                                if (msgHeader.trim().equalsIgnoreCase("")) {
//                                    showSnackbarErrorMsg(msg);
//                                    callSignUpPage();
//                                } else {
//                                    accountBlockedDialog(msgHeader, msg);
//                                }
//
//                            } else if (status.equalsIgnoreCase("Invalid AccessToken")) {
//                                showSnackbarErrorMsg(getResources().getString(R.string.invalid_access_token));
//
//                            } else if (status.equalsIgnoreCase("Error")) {
//                                String msg = jsonObj.getString("Message");
//                                showSnackbarErrorMsg(msg);
//
//                            } else {
//                                showSnackbarErrorMsg("Unmatched response, Please try again.");
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
                            getUserDetails();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private void getAreaDetails() {
        if (InternetConnection.checkConnection(this)) {

            ApiInterface apiService = RetroClient.getApiService(this);
//            Call<ResponseBody> call = apiService.getUserDetails(createJsonUserDetails());
            Call<ResponseBody> call = apiService.getAreaDetails();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        int statusCode = response.code();
                        if (response.isSuccessful()) {

                            String responseString = response.body().string();
                            JSONArray jsonArray = new JSONArray(responseString);

                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);

                                String area = jsonObj.optString("Area");
                                String areaId = jsonObj.optString("AreaId");
                                String cityId = jsonObj.optString("CityId");
                                String cityName = jsonObj.optString("CityName");
                                String country = jsonObj.optString("Country");
                                String countryId = jsonObj.optString("CountryId");
                                String stateId = jsonObj.optString("StateId");
                                String stateName = jsonObj.optString("StateName");
                            }


//                            if (status.equalsIgnoreCase("Success")) {
//                                FirebaseUser user = mAuth.getCurrentUser();
//
//                                String accessToken = jsonObj.getString("AccessToken");
//                                int accountType = jsonObj.getInt("AccountType");
//                                String mainCorpNo = jsonObj.getString("MainCorpNo");
//                                String corpID = jsonObj.getString("CorpID");
//                                String emailID = jsonObj.getString("EmailID");
//                                String firstName = jsonObj.getString("FirstName");
//                                String lastName = jsonObj.getString("LastName");
//
//                                String hibernate = jsonObj.getString("Hibernate");
//                                boolean isGracePeriod = jsonObj.getBoolean("IsGracePeriod");
//                                boolean isTrial = jsonObj.getBoolean("IsTrial");
//                                msgHeader = jsonObj.getString("MessageHeader");
//                                message = jsonObj.getString("Message");
//
//                                int maxQuestionsAllowed = jsonObj.getInt("MaxQuestionsAllowed");
//                                int maxSurveysAllowed = jsonObj.getInt("MaxSurveysAllowed");
//
//                            } else if (status.equalsIgnoreCase("LoginFailed")) {
//
//                                String msg = jsonObj.getString("Message");
//                                String msgHeader = jsonObj.getString("MessageHeader");
//
//                                prefs.edit().clear().apply();
//                                signOutFirebaseAccounts();
//
//
//                                if (msgHeader.trim().equalsIgnoreCase("")) {
//                                    showSnackbarErrorMsg(msg);
//                                    callSignUpPage();
//                                } else {
//                                    accountBlockedDialog(msgHeader, msg);
//                                }
//
//                            } else if (status.equalsIgnoreCase("Invalid AccessToken")) {
//                                showSnackbarErrorMsg(getResources().getString(R.string.invalid_access_token));
//
//                            } else if (status.equalsIgnoreCase("Error")) {
//                                String msg = jsonObj.getString("Message");
//                                showSnackbarErrorMsg(msg);
//
//                            } else {
//                                showSnackbarErrorMsg("Unmatched response, Please try again.");
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
                            getUserDetails();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }


    private void insertUserDetails(){
        if (InternetConnection.checkConnection(this)) {

            UserDetails userDetails = new UserDetails();
            userDetails.setUsername("Pradeep");
            userDetails.setPassword("PradeepPass");

            ApiInterface apiService = RetroClient.getApiService(this);
//            Call<ResponseBody> call = apiService.getUserDetails(createJsonUserDetails());
            Call<ResponseBody> call = apiService.insertUserDetails(userDetails);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        int statusCode = response.code();
                        if (response.isSuccessful()) {

                            String responseString = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseString);


//                            if (status.equalsIgnoreCase("Success")) {
//                                FirebaseUser user = mAuth.getCurrentUser();
//
//                                String accessToken = jsonObj.getString("AccessToken");
//                                int accountType = jsonObj.getInt("AccountType");
//                                String mainCorpNo = jsonObj.getString("MainCorpNo");
//                                String corpID = jsonObj.getString("CorpID");
//                                String emailID = jsonObj.getString("EmailID");
//                                String firstName = jsonObj.getString("FirstName");
//                                String lastName = jsonObj.getString("LastName");
//
//                                String hibernate = jsonObj.getString("Hibernate");
//                                boolean isGracePeriod = jsonObj.getBoolean("IsGracePeriod");
//                                boolean isTrial = jsonObj.getBoolean("IsTrial");
//                                msgHeader = jsonObj.getString("MessageHeader");
//                                message = jsonObj.getString("Message");
//
//                                int maxQuestionsAllowed = jsonObj.getInt("MaxQuestionsAllowed");
//                                int maxSurveysAllowed = jsonObj.getInt("MaxSurveysAllowed");
//
//                            } else if (status.equalsIgnoreCase("LoginFailed")) {
//
//                                String msg = jsonObj.getString("Message");
//                                String msgHeader = jsonObj.getString("MessageHeader");
//
//                                prefs.edit().clear().apply();
//                                signOutFirebaseAccounts();
//
//
//                                if (msgHeader.trim().equalsIgnoreCase("")) {
//                                    showSnackbarErrorMsg(msg);
//                                    callSignUpPage();
//                                } else {
//                                    accountBlockedDialog(msgHeader, msg);
//                                }
//
//                            } else if (status.equalsIgnoreCase("Invalid AccessToken")) {
//                                showSnackbarErrorMsg(getResources().getString(R.string.invalid_access_token));
//
//                            } else if (status.equalsIgnoreCase("Error")) {
//                                String msg = jsonObj.getString("Message");
//                                showSnackbarErrorMsg(msg);
//
//                            } else {
//                                showSnackbarErrorMsg("Unmatched response, Please try again.");
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
                            getUserDetails();
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
}
