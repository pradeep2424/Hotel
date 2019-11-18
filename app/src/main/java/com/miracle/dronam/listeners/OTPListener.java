package com.miracle.dronam.listeners;

public interface OTPListener {
    void onOtpReceived(String otp);
    void onOtpTimeout();

//    void onOTPReceived(String otp);
}
