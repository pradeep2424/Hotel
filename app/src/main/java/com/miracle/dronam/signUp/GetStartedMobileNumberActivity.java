package com.miracle.dronam.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_mobile_number);

        init();
        componentEvents();
        setNameData();
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

//                Intent it = new Intent(GetStartedMobileNumberActivity.this, LocationGoogleMapActivity.class);
                Intent intent = new Intent(GetStartedMobileNumberActivity.this, GetStartedVerifyOTPActivity.class);
                intent.putExtra("mobile", etMobileNumber.getText().toString().trim());
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
}
