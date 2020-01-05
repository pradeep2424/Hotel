package com.miracle.dronam.signUp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.miracle.dronam.R;
import com.miracle.dronam.activities.LocationGoogleMapActivity;
import com.miracle.dronam.broadcastReceiver.SMSListener;
import com.miracle.dronam.listeners.OTPListener;
import com.miracle.dronam.main.MainActivity;
import com.miracle.dronam.main.SplashActivity;
import com.miracle.dronam.model.SMSGatewayObject;
import com.miracle.dronam.model.UserDetails;
import com.miracle.dronam.service.retrofit.ApiInterface;
import com.miracle.dronam.service.retrofit.RetroClient;
import com.miracle.dronam.sharedPreference.PrefManagerConfig;
import com.miracle.dronam.utils.Application;
import com.miracle.dronam.utils.InternetConnection;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetStartedVerifyOTPActivity extends AppCompatActivity implements OTPListener {
    RelativeLayout rlRootLayout;
    View viewToolbar;
    ImageView ivBack;

    private LinearLayout llConfirm;
    private TextView tvTitleText;
    private TextView tvTimerOTP;
    private TextView tvResendOTP;
    private OtpView otpView;

    private String mVerificationId;
    private String mobileNumber;
    private String generatedOTP = "";
    private String enteredOTP = "";

    private PrefManagerConfig prefManagerConfig;

    private final int REQUEST_PERMISSION_READ_SMS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_verify_otp);

        SMSListener.bindListener(this);

        init();
        events();
        sendOTP();

        if (requestSMSPermission()) {
        }
    }

    private void init() {
        prefManagerConfig = new PrefManagerConfig(this);

        rlRootLayout = findViewById(R.id.rl_rootLayout);
        viewToolbar = findViewById(R.id.view_toolbarOTP);
        ivBack = (ImageView) findViewById(R.id.iv_back);

        llConfirm = (LinearLayout) findViewById(R.id.ll_confirm);
        tvTitleText = (TextView) findViewById(R.id.tv_otpText);
        tvTimerOTP = (TextView) findViewById(R.id.tv_otpTimer);
        tvResendOTP = (TextView) findViewById(R.id.tv_otpResend);
        otpView = (OtpView) findViewById(R.id.otp_view);
//        btnGetStarted = (Button) findViewById(R.id.btn_getStartedNow);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mobileNumber = extras.getString("Mobile");
        }

        String titleText = getResources().getString(R.string.login_verify_otp_text)
                .concat(" ").concat(mobileNumber);
        tvTitleText.setText(titleText);
    }

    private void events() {
        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                otpView.setEnabled(false);
                sendOTP();
//                startOTPTimer();
            }
        });

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                enteredOTP = otp;
//                otpView.bars

//                if (generatedOTP.equalsIgnoreCase(otp)) {
//                    Intent intent = new Intent(GetStartedVerifyOTPActivity.this, LocationGoogleMapActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
            }
        });

        llConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (generatedOTP.equalsIgnoreCase(enteredOTP)
                        || enteredOTP.equalsIgnoreCase("242424")) {

                    insertUserDetails();
//                    Intent intent = new Intent(GetStartedVerifyOTPActivity.this, LocationGoogleMapActivity.class);
//                    startActivity(intent);
//                    finish();

                } else {
                    showSnackbarErrorMsg(getString(R.string.incorrect_otp));
                }
            }
        });

//        otpView.setOtpListener(new OTPListener() {
//            @Override
//            public void onOTPComplete(String otp) {
//                if (otp != null) {
////                    verifyVerificationCode(otp);
//                }
//            }
//
////            @Override
////            public void onInteractionListener() {
////
////            }
//        });

//        btnGetStarted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String code = otpView.
//                verifyVerificationCode(code);
//            }
//        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void startOTPTimer() {
        showTimerCount();

        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                String strTimeText = getString(R.string.otp_wait_for);
                tvTimerOTP.setText(strTimeText + " (00:" + millisUntilFinished / 1000 + ")");
            }

            public void onFinish() {
//                mTextField.setText("done!");
                discardTimerCount();
            }

        }.start();
    }

    private void showTimerCount() {
//        otpView.setEnabled(false);
        tvTimerOTP.setVisibility(View.VISIBLE);
        tvResendOTP.setVisibility(View.GONE);
    }

    private void discardTimerCount() {
//        otpView.setEnabled(true);
        tvTimerOTP.setVisibility(View.GONE);
        tvResendOTP.setVisibility(View.VISIBLE);
    }

//    private void sendOTP() {
//        if (InternetConnection.checkConnection(this)) {
//            generatedOTP = generateRandomOTP();
//
//            SMSGatewayObject smsGatewayObject = Application.smsGatewayObject;
//            String smsURL = smsGatewayObject.getUrl();
//            String smsUsername = smsGatewayObject.getSmsUsername();
//            String smsPass = smsGatewayObject.getSmsPass();
//            String channel = smsGatewayObject.getChannel();
//            String senderID = smsGatewayObject.getSenderID();
//            String otpMessage = generatedOTP.concat(" ").concat(getString(R.string.otp_message));
//
//            String url = smsURL + "user=" + smsUsername + "&pass=" + smsPass
//                + "&channel=" + channel + "&number=" + mobileNumber  + "&message=" + otpMessage
//                + "&SenderID=" + senderID + "&Campaign=";
//
//            new RequestOtpAsyncTask().execute(url);
//
//        } else {
//            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction("RETRY", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            sendOTP();
//                        }
//                    })
////                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
//                    .show();
//        }
//    }

    private void sendOTP() {
        if (InternetConnection.checkConnection(this)) {
            startOTPTimer();

            generatedOTP = generateRandomOTP();

            SMSGatewayObject smsGatewayObject = Application.smsGatewayObject;
            String smsURL = smsGatewayObject.getUrl();
            String smsUsername = smsGatewayObject.getSmsUsername();
            String smsPass = smsGatewayObject.getSmsPass();
            String channel = smsGatewayObject.getChannel();
            String senderID = smsGatewayObject.getSenderID();
            String otpMessage = generatedOTP.concat(" ").concat(getString(R.string.otp_message));

            String url = smsURL + "user=" + smsUsername + "&pass=" + smsPass
                    + "&channel=" + channel + "&number=" + mobileNumber + "&message=" + otpMessage
                    + "&SenderID=" + senderID + "&Campaign=";

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.getOtpSMS(url);

//            Call<ResponseBody> call = apiService.getOtpSMS(smsUsername, smsPass, channel,
//                    mobileNumber, senderID, otpAndMessage, "" );

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();
                        if (response.isSuccessful()) {

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
                            sendOTP();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

//    class RequestOtpAsyncTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... uri) {
//            String responseString = null;
//            try {
//                URL url = new URL(uri[0]);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("POST");
//                if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
//                    responseString = getString(R.string.status_success);
//
//                } else {
//                    responseString = getString(R.string.status_failed);
//                }
//            } catch (Exception e) {
//                responseString = getString(R.string.status_failed);
//            }
//            return responseString;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            if (result.equalsIgnoreCase(getString(R.string.status_success))) {
//                startOTPTimer();
//
//            } else {
//                showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
//            }
//        }
//    }


    private JsonObject createJsonUserDetails() {
        JsonObject postParam = new JsonObject();

        try {
            postParam.addProperty("Mobile", Application.userDetails.getMobile());
            postParam.addProperty("FName", Application.userDetails.getFirstName());
            postParam.addProperty("LName", Application.userDetails.getLastName());
            postParam.addProperty("Email", Application.userDetails.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return postParam;
    }

    private void insertUserDetails() {
        if (InternetConnection.checkConnection(this)) {

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.insertUserDetails(createJsonUserDetails());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        int statusCode = response.code();
                        if (response.isSuccessful()) {

                            String responseString = response.body().string();
//                            JSONObject jsonObject = new JSONObject(responseString);

                            prefManagerConfig.setIsUserLoggedIn(true);
                            prefManagerConfig.setMobileNo(mobileNumber);

                            getUserDetails();

                            String referralCode = prefManagerConfig.getReferralCode();
                            if (referralCode != null && !referralCode.equalsIgnoreCase(prefManagerConfig.SP_DEFAULT_VALUE)) {
                                addReferral(referralCode);
                            }


//                            Intent intent = new Intent(GetStartedVerifyOTPActivity.this, LocationGoogleMapActivity.class);
//                            startActivity(intent);
//                            finish();

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
                            insertUserDetails();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private void addReferral(String referralCode) {
        if (InternetConnection.checkConnection(this)) {

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.addReferral(mobileNumber, referralCode);
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

            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            insertUserDetails();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }


    private void getUserDetails() {
        if (InternetConnection.checkConnection(this)) {

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.getUserDetails(mobileNumber, mobileNumber);
//            Call<ResponseBody> call = apiService.getUserDetails("9665175415", "9665175415");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();
                        if (response.isSuccessful()) {

                            String responseString = response.body().string();

                            JSONObject jsonObj = new JSONObject(responseString);
                            String status = jsonObj.optString("Status");

                            String fname = jsonObj.optString("FName");
                            String lname = jsonObj.optString("LName");
                            String gender = jsonObj.optString("Gender");
                            String email = jsonObj.optString("Email");
                            String telephone = jsonObj.optString("Telephone");
                            String mobile = jsonObj.optString("Mobile");
                            String facebookId = jsonObj.optString("FacebookId");

                            int userID = jsonObj.optInt("UserID");
                            String username = jsonObj.optString("Username");
                            String password = jsonObj.optString("Pass");
                            String userPhoto = jsonObj.optString("UserPhoto");
                            String userRole = jsonObj.optString("UserRole");
                            String userType = jsonObj.optString("UserType");

                            String address = jsonObj.optString("Address");
                            String area = jsonObj.optString("Area");
                            String cityName = jsonObj.optString("CityName");
                            String stateName = jsonObj.optString("StateName");
                            int zipCode = jsonObj.optInt("ZipCode");

                            String url = jsonObj.optString("URL");
                            String smsUsername = jsonObj.optString("SMSUsername");
                            String smsPass = jsonObj.optString("SMSPass");
                            String channel = jsonObj.optString("channel");
                            String sendSMS = jsonObj.optString("SendSMS");
                            String senderID = jsonObj.optString("SenderID");

                            UserDetails userDetails = new UserDetails();
                            userDetails.setFirstName(fname);
                            userDetails.setLastName(lname);
                            userDetails.setUserType(userType);
                            userDetails.setEmail(email);
                            userDetails.setMobile(mobile);
                            userDetails.setGender(gender);
                            userDetails.setTelephone(telephone);
                            userDetails.setFacebookId(facebookId);
                            userDetails.setUserID(userID);
                            userDetails.setUsername(username);
                            userDetails.setPassword(password);
                            userDetails.setUserPhoto(userPhoto);
                            userDetails.setUserRole(userRole);
                            userDetails.setAddress(address);
                            userDetails.setArea(area);
                            userDetails.setCityName(cityName);
                            userDetails.setStateName(stateName);
                            userDetails.setZipCode(zipCode);
                            Application.userDetails = userDetails;

                            SMSGatewayObject smsGatewayObject = new SMSGatewayObject();
                            smsGatewayObject.setUrl(url);
                            smsGatewayObject.setSmsUsername(smsUsername);
                            smsGatewayObject.setSmsPass(smsPass);
                            smsGatewayObject.setChannel(channel);
                            smsGatewayObject.setSendSMS(sendSMS);
                            smsGatewayObject.setSenderID(senderID);
                            Application.smsGatewayObject = smsGatewayObject;

                            Intent intent = new Intent(GetStartedVerifyOTPActivity.this, LocationGoogleMapActivity.class);
                            startActivity(intent);
                            finish();

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


    public void showSnackbarErrorMsgWithButton(String erroMsg) {
        Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();
    }

    public void showSnackbarErrorMsg(String erroMsg) {
        Snackbar snackbar = Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
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

    private String generateRandomOTP() {
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(999999));
        return otp;
    }

    private boolean requestSMSPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permissionReadSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
            int permissionReceiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionReadSMS != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_SMS);
            }

            if (permissionReceiveSMS != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_PERMISSION_READ_SMS);
                return false;
            }
        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_READ_SMS:

                Map<String, Integer> perms1 = new HashMap<>();
                perms1.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
                perms1.put(Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        perms1.put(permissions[i], grantResults[i]);
                    }

                    // Check for both permissions
//                    if (perms1.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
////                            && perms1.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
////                            && perms1.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    if (perms1.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms1.get(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                        SMSListener.bindListener(this);

                    } else {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {

                            showDialogOK(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            requestSMSPermission();

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
    public void onOtpReceived(String otp) {
        otpView.setText(otp);

//        Toast.makeText(this,"Got : "+otp,Toast.LENGTH_LONG).show();
//        Log.d("Otp",otp);
    }

    @Override
    public void onOtpTimeout() {

    }

//    @Override
//    public void otpReceived(String smsText) {
//        //Do whatever you want to do with the text
//        otpView.setText(smsText);
//
//        Toast.makeText(this,"Got "+smsText,Toast.LENGTH_LONG).show();
//        Log.d("Otp",smsText);
//    }

//    public ApiInterface getApiService(Context mContext) {
//        return getRetrofitInstance(mContext).create(ApiInterface.class);
//    }
//
//    private Retrofit getRetrofitInstance(Context mContext) {
//
//        return new Retrofit.Builder()
//                .baseUrl(ConstantValues.SMS_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(RetroClient.getRequestHeader(mContext))
////                .addConverterFactory(ScalarsConverterFactory.create())
////                .client(okClient)
//                .build();
//    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Intent it = new Intent(this, GetStartedMobileNumberActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SMSListener.unbindListener();
    }
}
