package hackbar.de.hackbardroid.service;

import java.util.List;

import hackbar.de.hackbardroid.model.Drink;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface INerdBarService {
    @GET("getDrinks")
    Call<List<Drink>> getDrinks();

    @POST("register")
    @FormUrlEncoded
    Call<Void> register(@Field("userId") String userId, @Field("deviceId") String deviceId);

    @POST("logout")
    @FormUrlEncoded
    Call<Void> logout(@Field("userId")  String userId);

    @POST("orderDrink")
    @FormUrlEncoded
    Call<Void> orderDrink(@Field("userId") String userId, @Field("drinkName") String drinkName);
}
