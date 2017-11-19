package hackbar.de.hackbardroid.service;

import java.util.List;

import hackbar.de.hackbardroid.model.Drink;
import hackbar.de.hackbardroid.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface INerdBarService {
    @GET("getDrinks")
    Call<List<Drink>> getDrinks();

    @POST("register")
    @FormUrlEncoded
    Call<User> register(@Field("userId") String userId, @Field("deviceId") String deviceId);

    @POST("logout")
    @FormUrlEncoded
    Call<Void> logout(@Field("userId")  String userId);

    @GET("getUser")
    Call<User> getUser(@Query("userId") String userId);

    @POST("orderDrink")
    @FormUrlEncoded
    Call<Void> orderDrink(@Field("userId") String userId, @Field("drinkName") String drinkName);

    @POST("findMyDrink")
    @FormUrlEncoded
    Call<User> findMyDrink(@Field("userId") String userId);

    @POST("needAssistance")
    @FormUrlEncoded
    Call<User> needAssistance(@Field("userId") String userId);

    @POST("resetSipCount")
    @FormUrlEncoded
    Call<User> resetSipCount(@Field("userId")  String userId);
}
