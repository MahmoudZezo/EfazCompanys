package com.taj51.efazcompany.interfcaes;

import com.taj51.efazcompany.pojo.CategoryPojo;
import com.taj51.efazcompany.pojo.GetProfilePojo;
import com.taj51.efazcompany.pojo.LoginDetailsPOJO;
import com.taj51.efazcompany.pojo.LoginPOJO;
import com.taj51.efazcompany.pojo.ProfilePOJO;
import com.taj51.efazcompany.pojo.SignUpPOJO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

//    @FormUrlEncoded // annotation used in POST type requests
    @Headers("Content-Type: application/json")
    @POST("/register/add")
        // API's endpoints
    Call<Void> registration(@Body SignUpPOJO signUpPOJO);

    @GET("/register/getAll")
    Call<List<SignUpPOJO>> getAllRegisters();

    @Headers("Content-Type: application/json")
    @POST("/login/loginUser")
    Call<LoginPOJO>login(@Body LoginPOJO loginPOJO);

    @Headers("Content-Type: application/json")
    @POST("/profile/addProfile")
    Call<Integer>AddUserProfile(@Body ProfilePOJO profilePOJO);

    @GET("/cat/getCategories")
    Call<List<CategoryPojo>> getCategories();

    @Headers("Content-Type: application/json")
    @POST("/details/add")
    Call<Void>addLoginDetails(@Body LoginDetailsPOJO pojo);

    @GET("/details/getAll")
    Call<List<LoginDetailsPOJO>> getLoginDetails();

    @POST("/login/isLogged")
    Call<Boolean> isLogged(@Body LoginPOJO pojo);

    @POST("/login/getLoginId")
    Call<LoginPOJO> getLoggedId(@Body LoginPOJO pojo);

    @GET("/profile/getProfile/{id}")
    Call<GetProfilePojo> getProfile(@Path("id") int id);

}