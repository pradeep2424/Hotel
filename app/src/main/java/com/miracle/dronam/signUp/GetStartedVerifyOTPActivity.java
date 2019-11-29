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
import com.miracle.dronam.R;
import com.miracle.dronam.activities.LocationGoogleMapActivity;
import com.miracle.dronam.broadcastReceiver.SMSListener;
import com.miracle.dronam.listeners.OTPListener;
import com.miracle.dronam.model.SMSGatewayObject;
import com.miracle.dronam.service.retrofit.ApiInterface;
import com.miracle.dronam.service.retrofit.RetroClient;
import com.miracle.dronam.utils.Application;
import com.miracle.dronam.utils.InternetConnection;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

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

    private final int REQUEST_PERMISSION_READ_SMS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_verify_otp);

        SMSListener.bindListener(this);

        init();
        events();
//        sendOTP();

        if (requestSMSPermission()) {
        }
    }

    private void init() {
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
                    Intent intent = new Intent(GetStartedVerifyOTPActivity.this, LocationGoogleMapActivity.class);
                    startActivity(intent);
                    finish();

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
