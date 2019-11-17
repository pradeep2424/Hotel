package com.miracle.dronam.signUp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.miracle.dronam.R;
import com.miracle.dronam.model.SMSGatewayObject;
import com.miracle.dronam.service.retrofit.ApiInterface;
import com.miracle.dronam.service.retrofit.RetroClient;
import com.miracle.dronam.utils.Application;
import com.miracle.dronam.utils.ConstantValues;
import com.miracle.dronam.utils.InternetConnection;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import swarajsaaj.smscodereader.interfaces.OTPListener;
import swarajsaaj.smscodereader.receivers.OtpReader;


public class GetStartedVerifyOTPActivity extends AppCompatActivity implements OTPListener {
    private RelativeLayout rlRootLayout;
    View viewToolbar;
    ImageView ivBack;

    private TextView tvTitleText;
    private TextView tvTimerOTP;
    private TextView tvResendOTP;
    private OtpView otpView;

    private String mVerificationId;
    private String mobileNumber;
    private String generatedOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_verify_otp);

        OtpReader.bind(this,"DRONAM");

        init();
        events();
        sendOTP();
    }

    private void init() {
        rlRootLayout = findViewById(R.id.rl_rootLayout);
        viewToolbar = findViewById(R.id.view_toolbarOTP);
        ivBack = (ImageView) findViewById(R.id.iv_back);

        tvTitleText = (TextView) findViewById(R.id.tv_otpText);
        tvTimerOTP = (TextView) findViewById(R.id.tv_otpTimer);
        tvResendOTP = (TextView) findViewById(R.id.tv_otpResend);
        otpView = (OtpView) findViewById(R.id.otp_view);
//        btnGetStarted = (Button) findViewById(R.id.btn_getStartedNow);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mobileNumber = extras.getString("mobile");
        }

        String titleText = getResources().getString(R.string.login_verify_otp_text)
                .concat(" ").concat(mobileNumber);
        tvTitleText.setText(titleText);
    }

    private void events() {
        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpView.setEnabled(false);
                sendOTP();
//                startOTPTimer();
            }
        });

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {


                // do Stuff
                Log.d("onOtpCompleted=>", otp);
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
        otpView.setEnabled(false);
        tvTimerOTP.setVisibility(View.VISIBLE);
        tvResendOTP.setVisibility(View.GONE);
    }

    private void discardTimerCount() {
        otpView.setEnabled(true);
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
                            startOTPTimer();

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

    public void showSnackbarErrorMsg(String erroMsg) {
        Snackbar snackbar = Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

    private String generateRandomOTP() {
        Random random = new Random();
        String otp = String.format("%04d", random.nextInt(10000));
        return otp;
    }

    @Override
    public void otpReceived(String smsText) {
        //Do whatever you want to do with the text
        otpView.setText(smsText);

        Toast.makeText(this,"Got "+smsText,Toast.LENGTH_LONG).show();
        Log.d("Otp",smsText);
    }

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
}
