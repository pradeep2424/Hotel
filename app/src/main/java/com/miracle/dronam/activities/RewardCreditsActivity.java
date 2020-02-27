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

import com.google.android.material.snackbar.Snackbar;
import com.miracle.dronam.R;
import com.miracle.dronam.adapter.RecycleAdapterCuisine;
import com.miracle.dronam.adapter.RecycleAdapterReferralPoints;
import com.miracle.dronam.dialog.DialogLoadingIndicator;
import com.miracle.dronam.model.CartObject;
import com.miracle.dronam.model.DishObject;
import com.miracle.dronam.model.ReferralDetails;
import com.miracle.dronam.service.retrofit.ApiInterface;
import com.miracle.dronam.service.retrofit.RetroClient;
import com.miracle.dronam.utils.Application;
import com.miracle.dronam.utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RewardCreditsActivity extends AppCompatActivity {
    private RelativeLayout rlRootLayout;
    private DialogLoadingIndicator progressIndicator;

    private TextView tvBalanceReferralPoints;
    private RecyclerView rvReferralPoints;
    private RecycleAdapterReferralPoints adapterReferralPoints;

    private ArrayList<ReferralDetails> listReferralDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_credits);

        initComponents();
        getReferralDetails();

        setupRecyclerViewReferralPoints();

    }

    private void initComponents() {
        progressIndicator = DialogLoadingIndicator.getInstance();

        rlRootLayout = findViewById(R.id.rl_rootLayout);
        tvBalanceReferralPoints = findViewById(R.id.tv_balanceReferralPoints);
        rvReferralPoints = findViewById(R.id.rv_referredUsers);

        double totalReferralPoints = Application.userDetails.getTotalReferralPoints();
        String formattedPoints = getFormattedNumberDouble(totalReferralPoints);
        tvBalanceReferralPoints.setText(formattedPoints + " " + getString(R.string.rupees));
    }

    private void setupRecyclerViewReferralPoints() {
        getDummyReferralData();

        adapterReferralPoints = new RecycleAdapterReferralPoints(this, listReferralDetails);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        rvReferralPoints.setLayoutManager(mLayoutManager1);
        rvReferralPoints.setItemAnimator(new DefaultItemAnimator());
        rvReferralPoints.setAdapter(adapterReferralPoints);
    }

    private void getDummyReferralData() {
        listReferralDetails = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            int amount = random.nextInt(900) + 100;

            ReferralDetails referralDetails = new ReferralDetails();
            referralDetails.setFirstName("FName");
            referralDetails.setLastName("LName");
            referralDetails.setTotalAmount(amount);

            listReferralDetails.add(referralDetails);
        }
    }

    private String getFormattedNumberDouble(double amount) {
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    private void getReferralDetails() {
        if (InternetConnection.checkConnection(this)) {

            int userTypeID = Application.userDetails.getUserID();

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.getReferralDetails(0);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();
                            listReferralDetails = new ArrayList<>();

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
                                double price = jsonObj.optDouble("Price");
                                String productDesc = jsonObj.optString("ProductDesc");
                                int productID = jsonObj.optInt("ProductId");
                                String productImage = jsonObj.optString("ProductImage");
                                String productName = jsonObj.optString("ProductName");
                                double sgst = jsonObj.optDouble("SGST");
                                int taxID = jsonObj.optInt("TaxID");
                                String taxName = jsonObj.optString("TaxName");

                            }

//                            setupRecyclerViewProducts();

                        } else {
                            showSnackBarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        showSnackBarErrorMsg(getResources().getString(R.string.server_conn_lost));
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
                            getReferralDetails();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    public void showSnackBarErrorMsg(String erroMsg) {
//        Snackbar.make(fragmentRootView, erroMsg, Snackbar.LENGTH_LONG).show();

        Snackbar snackbar = Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

    public void showSnackBarErrorMsgWithButton(String erroMsg) {
        Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();
    }

    public void showDialog() {
        progressIndicator.showProgress(RewardCreditsActivity.this);
    }

    public void dismissDialog() {
        if (progressIndicator != null) {
            progressIndicator.hideProgress();
        }
    }
}
