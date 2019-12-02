package com.miracle.dronam.utils;

import com.miracle.dronam.model.AddressDetails;
import com.miracle.dronam.model.CartObject;
import com.miracle.dronam.model.OrderDetailsObject;
import com.miracle.dronam.model.RestaurantObject;
import com.miracle.dronam.model.SMSGatewayObject;
import com.miracle.dronam.model.UserDetails;
import com.sucho.placepicker.AddressData;

/**
 * Created by Pradeep Jadhav on 23/10/2019.
 */

public class Application extends android.app.Application {
    private static Application mInstance;

    public static UserDetails userDetails = new UserDetails();
    public static RestaurantObject restaurantObject = new RestaurantObject();
    public static CartObject cartObject = new CartObject();
    public static OrderDetailsObject orderDetailsObject = new OrderDetailsObject();

//    public static AddressDetails addressDetails = new AddressDetails();
    public static SMSGatewayObject smsGatewayObject = new SMSGatewayObject();
    public static AddressData locationAddressData;

    public Application getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

//        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
//        appSignatureHelper.getAppSignatures();
    }
}

