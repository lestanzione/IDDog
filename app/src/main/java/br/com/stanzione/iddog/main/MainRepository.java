package br.com.stanzione.iddog.main;

import android.content.SharedPreferences;

import br.com.stanzione.iddog.api.DogApi;
import br.com.stanzione.iddog.data.LoginRequest;
import br.com.stanzione.iddog.data.User;
import io.reactivex.Observable;

public class MainRepository implements MainContract.Repository {

    private DogApi dogApi;
    private SharedPreferences preferences;

    public MainRepository(DogApi dogApi, SharedPreferences preferences){
        this.dogApi = dogApi;
        this.preferences = preferences;
    }

    @Override
    public Observable<User.UserResponse> doLogin(String email) {
        return dogApi.doLogin(createLoginRequest(email).toJson());
    }

    @Override
    public void persistToken(String token) {
        preferences.edit()
                .putString("token", token)
                .apply();
    }

    private LoginRequest createLoginRequest(String email){
        return new LoginRequest(email);
    }
}
