package com.dronam.manora.service.retrofit;

import com.dronam.manora.model.UserDetails;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    @GET("getAreaDetails")
    Call<ResponseBody> getAreaDetails();

    @POST("insertUserDetails")
    Call<ResponseBody> insertUserDetails(@Body UserDetails body);

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
