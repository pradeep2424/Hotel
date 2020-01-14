package com.miracle.dronam.signUp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.miracle.dronam.R;
import com.miracle.dronam.activities.LocationGoogleMapActivity;
import com.miracle.dronam.model.UserDetails;
import com.miracle.dronam.utils.Application;

public class GetStartedMobileNumberActivity extends AppCompatActivity {
    //    View view;
    View viewToolbar;
    ImageView ivBack;

    LinearLayout llConfirm;
    EditText etMobileNumber;
    EditText etFName;
    EditText etLName;

    final int RESOLVE_HINT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_mobile_number);

        init();
        componentEvents();
        setNameData();
        getHintPhoneNumber();
    }

    private void init() {
        viewToolbar = findViewById(R.id.view_toolbar);
        ivBack = viewToolbar.findViewById(R.id.iv_back);

        llConfirm = findViewById(R.id.ll_confirm);
        etMobileNumber = findViewById(R.id.et_mobileNumber);
        etFName = findViewById(R.id.et_fname);
        etLName = findViewById(R.id.et_lname);

//        tvTitle = (TextView) findViewById(R.id.tv_title);
//        tvLogin = (TextView) findViewById(R.id.tv_login);
//        tvSignUp = (TextView) findViewById(R.id.tv_signUp);

//        Typeface custom_fonts = Typeface.createFromAsset(getAssets(), "fonts/ArgonPERSONAL-Regular.otf");
//        tvTitle.setTypeface(custom_fonts);
    }

    private void componentEvents() {
        llConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();

//                Intent intent = new Intent(GetStartedMobileNumberActivity.this, LocationGoogleMapActivity.class);
                Intent intent = new Intent(GetStartedMobileNumberActivity.this, GetStartedVerifyOTPActivity.class);
//                intent.putExtra("Mobile", etMobileNumber.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(GetStartedMobileNumberActivity.this, GetStartedActivity.class);
                startActivity(it);
                finish();
            }
        });
    }


    // Construct a request for phone numbers and show the picker
    private void getHintPhoneNumber() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Credentials.getClient(this).getHintPickerIntent(hintRequest);

        try {
            startIntentSenderForResult(intent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0);

        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setNameData() {
        UserDetails userDetails = Application.userDetails;
        if (userDetails.getFirstName() != null) {
            etFName.setText(userDetails.getFirstName());
        }

        if (userDetails.getLastName() != null) {
            etLName.setText(userDetails.getLastName());
        }
    }

    private void saveUserData() {
        String fname = etFName.getText().toString().trim();
        String lname = etLName.getText().toString().trim();
        String mobileNo = etMobileNumber.getText().toString().trim();
        String fullName = fname.concat(" ").concat(lname);

        Application.userDetails.setFirstName(fname);
        Application.userDetails.setLastName(lname);
        Application.userDetails.setFullName(fullName);
        Application.userDetails.setMobile(mobileNo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT) {
                if (resultCode == RESULT_OK) {
                Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
                String phoneNumber = cred.getId().substring(3);
                etMobileNumber.setText(phoneNumber);
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Intent it = new Intent(this, GetStartedActivity.class);
        startActivity(it);
        finish();

    }
}
