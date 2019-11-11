package com.example.hotel.signUp;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.hotel.R;
import com.example.hotel.activities.LocationGoogleMapActivity;
import com.example.hotel.main.MainActivity;
import com.example.hotel.utils.Utils;

public class GetStartedActivity extends AppCompatActivity {
    LinearLayout llConnectWithMobileNo;
    LinearLayout llSkip;
    TextView tvTermsNPolicy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        init();
        componentEvents();
    }

    private void init() {
        llConnectWithMobileNo  =  findViewById(R.id.ll_connectWithMobileNo);
        llSkip  =  findViewById(R.id.ll_skip);
        tvTermsNPolicy = findViewById(R.id.tv_privacyAndTerms);

        if (tvTermsNPolicy != null) {
            tvTermsNPolicy.setMovementMethod(LinkMovementMethod.getInstance());
            Utils.removeUnderlines((Spannable) tvTermsNPolicy.getText());
        }

//        tvTitle = (TextView) findViewById(R.id.tv_title);
//        tvLogin = (TextView) findViewById(R.id.tv_login);
//        tvSignUp = (TextView) findViewById(R.id.tv_signUp);

//        Typeface custom_fonts = Typeface.createFromAsset(getAssets(), "fonts/ArgonPERSONAL-Regular.otf");
//        tvTitle.setTypeface(custom_fonts);
    }

    private void componentEvents() {
        llConnectWithMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(GetStartedActivity.this, GetStartedMobileNumberActivity.class);
                startActivity(it);
                finish();
            }
        });

        llSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(GetStartedActivity.this, LocationGoogleMapActivity.class);
                startActivity(it);
                finish();
            }
        });
    }
}
