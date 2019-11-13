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

public class GetStartedMobileNumberActivity extends AppCompatActivity {
//    View view;
    View viewToolbar;
    ImageView ivBack;

    LinearLayout llConfirm;
    EditText etMobileNumber;
    EditText fName;
    EditText lName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_mobile_number);

        init();
        componentEvents();
    }

    private void init() {
        viewToolbar = findViewById(R.id.view_toolbar);
        ivBack = viewToolbar.findViewById(R.id.iv_back);

        llConfirm = findViewById(R.id.ll_confirm);
        etMobileNumber = findViewById(R.id.et_mobileNumber);
        fName = findViewById(R.id.et_fname);
        lName = findViewById(R.id.et_lname);

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
                Intent it = new Intent(GetStartedMobileNumberActivity.this, LocationGoogleMapActivity.class);
                startActivity(it);
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
}
