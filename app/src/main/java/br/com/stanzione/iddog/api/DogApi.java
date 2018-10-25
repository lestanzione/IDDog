package br.com.stanzione.iddog.api;

import com.google.gson.JsonObject;

import br.com.stanzione.iddog.data.User;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DogApi {

    @Headers("Content-type: application/json")
    @POST("signup")
    Observable<User.UserResponse> doLogin(@Body JsonObject email);

}
