package com.miracle.dronam.service.retrofit;

import com.google.gson.JsonObject;
import com.miracle.dronam.model.DishObject;
import com.miracle.dronam.model.OrderDetailsObject;
import com.miracle.dronam.model.UserDetails;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by Pradeep Jadhav.
 */

public interface ApiInterface {

    @GET("GetQuestionDetails/350/1366")
    Call<ResponseBody> testAnalyzeRespponse();


    @POST("insertUserDetails")
    Call<ResponseBody> insertUserDetails(@Body JsonObject jsonObj);

    @GET("GetUserDetails/{Username}/{Password}")
    Call<ResponseBody> getUserDetails(@Path("Username") String username,
                                      @Path("Password") String password);

    @POST("insUserAddress")
    Call<ResponseBody> insertUserAddress(@Body JsonObject jsonObj);

    @GET("getUserAddress/{mobileNo}")
    Call<ResponseBody> getUserAddress(@Path("mobileNo") String mobileNo);

    @POST("delUserAddress/{AddressID}")
    Call<ResponseBody> deleteUserAddress(@Path("AddressID") String addressID);

    @GET("getAreaDetails")
    Call<ResponseBody> getAreaDetails();


    @GET("getRestaurantDetails/{Zip}")
    Call<ResponseBody> getRestaurantDetails(@Path("Zip") String zipCode);

//    @GET("getProductDetails/{ClientID}/{foodtypeid}/{CategoryID}")
//    Call<ResponseBody> getProductDetailsData(@Path("ClientID") int clientID,
//                                             @Path("foodtypeid") int foodTypeID,
//                                             @Path("CategoryID") int categoryID);

    @GET("getProductDetails/{Usertypeid}/{ClientID}/{foodtypeid}/{CategoryID}")
    Call<ResponseBody> getProductDetailsData(@Path("Usertypeid") int userTypeID,
                                             @Path("ClientID") int clientID,
                                             @Path("foodtypeid") int foodTypeID,
                                             @Path("CategoryID") int categoryID);

    @POST("insCartItem")
    Call<ResponseBody> addItemToCart(@Body JsonObject jsonObj);

    @POST("delcartItem/{userids}/{ClientIDs}")
    Call<ResponseBody> deleteCartItem(@Path("userids") int userID, @Path("ClientIDs") int clientID);


    @GET("getcartItem/{userids}/{ClientIDs}")
    Call<ResponseBody> getCartItem(@Path("userids") int userID, @Path("ClientIDs") int clientID);

//    @POST("insCartItem")
//    Call<ResponseBody> addItemToCart(@Body DishObject dishObject);


    @POST("insorder")
    Call<ResponseBody> placeOrder(@Body JsonObject jsonObj);

//    @POST("insertUserDetails")
//    Call<ResponseBody> insertUserDetails(@Body UserDetails body);


    @GET("getorderDetails/{userids}")
    Call<ResponseBody> getPastOrders(@Path("userids") String userID);

    @POST
    Call<ResponseBody> getOtpSMS(@Url String url);

    @POST("smsapi/{user}/{pass}/{channel}/{number}/{message}/{SenderID}/{Campaign}")
    Call<ResponseBody> getOtpSMS(@Path("user") String smsUsername,
                                 @Path("pass") String smsPassword,
                                 @Path("channel") String smsChannel,
                                 @Path("number") String smsNumber,
                                 @Path("message") String smsMessage,
                                 @Path("SenderID") String senderID,
                                 @Path("Campaign") String campaign);

//    @Multipart
//    @POST("UploadFile")
//    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);

}
