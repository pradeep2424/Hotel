package com.miracle.dronam.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.miracle.dronam.sharedPreference.PrefManagerConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class InstallReferrerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        try {

            Bundle extras = intent.getExtras();
            if (extras != null) {
                String referrerString = extras.getString("referrer");
                Log.e("########### : ", referrerString);

//                String referralCode = referrerString.substring(referrerString.indexOf("utm_source="));
                String referralCode = referrerString.replace("utm_source=", "");
                storeReferralParams(context, referralCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("########### : ", e.toString());
        }
    }

    public void storeReferralParams(Context context, String referralCode) {
        PrefManagerConfig prefManagerConfig = new PrefManagerConfig(context);
        prefManagerConfig.setReferralCode(referralCode);
    }

}