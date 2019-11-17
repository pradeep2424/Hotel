package com.miracle.dronam.service.retrofit;

import com.miracle.dronam.model.UserDetails;

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

    @GET("GetUserDetails/{Username}/{Password}")
    Call<ResponseBody> getUserDetails(@Path("Username") String username,
                                      @Path("Password") String password);


    @GET("getRestaurantDetails/{Zip}")
    Call<ResponseBody> getRestaurantDetails(@Path("Zip") String zipCode);

    @GET("getProductDetails/{Usertypeid}/{ClientID}/{foodtypeid}/{CategoryID}")
    Call<ResponseBody> getProductDetailsData(@Path("Usertypeid") String userTypeID,
                                             @Path("ClientID") String clientID,
                                             @Path("foodtypeid") String foodTypeID,
                                             @Path("CategoryID") String categoryID);


    @GET("getcartItem/{userids}/{ClientIDs}")
    Call<ResponseBody> getCartItem(@Path("userids") int userID, @Path("ClientIDs") int clientID);

    @GET("getAreaDetails")
    Call<ResponseBody> getAreaDetails();

    @POST("insertUserDetails")
    Call<ResponseBody> insertUserDetails(@Body UserDetails body);


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

//    @POST("GetLastResponseDate")
//    Call<ResponseBody> getUserDetails(@Body JSONObject jsonObj);


//    @Multipart
//    @POST("UploadFile")
//    Call<ResponseBody> uploadImage(@Part List<MultipartBody.Part> file);

    @Multipart
    @POST("UploadFile")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);

    @POST("UploadFile")
    Call<ResponseBody> uploadImageString(String image);
}
