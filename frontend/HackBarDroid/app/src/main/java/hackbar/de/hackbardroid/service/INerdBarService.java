package hackbar.de.hackbardroid.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface INerdBarService {
    //@GET("users/{user}/repos")
    //Call<List<String>> listRepos(@Path("user") String user);

    @POST("register")
    @FormUrlEncoded
    Call<Void> register(@Field("userId") String userId, @Field("deviceId") String deviceId);

    @POST("logout")
    @FormUrlEncoded
    Call<Void> logout(@Field("userId")  String userId);
}
