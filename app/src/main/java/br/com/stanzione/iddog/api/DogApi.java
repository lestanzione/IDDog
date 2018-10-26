package br.com.stanzione.iddog.api;

import com.google.gson.JsonObject;

import br.com.stanzione.iddog.data.DogGallery;
import br.com.stanzione.iddog.data.User;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DogApi {

    @Headers("Content-type: application/json")
    @POST("signup")
    Observable<User.UserResponse> doLogin(@Body JsonObject email);

    @Headers("Content-type: application/json")
    @GET("feed")
    Observable<DogGallery> getDogs(@Header("Authorization") String token, @Query("category") String category);

}
