package br.com.stanzione.iddog.doglist;

import android.content.SharedPreferences;
import android.text.TextUtils;

import br.com.stanzione.iddog.api.DogApi;
import br.com.stanzione.iddog.data.DogGallery;
import br.com.stanzione.iddog.data.DogType;
import io.reactivex.Observable;

public class DogListRepository implements DogListContract.Repository{

    private DogApi dogApi;
    private SharedPreferences preferences;

    public DogListRepository(DogApi dogApi, SharedPreferences preferences){
        this.dogApi = dogApi;
        this.preferences = preferences;
    }

    @Override
    public Observable<String> getToken() {
        String token = preferences.getString("token", null);

        if(!TextUtils.isEmpty(token)) {
            return Observable.just(token);
        }
        else{
            return Observable.error(new Throwable());
        }
    }

    @Override
    public Observable<DogGallery> fetchImages(String token, DogType dogType) {
        return dogApi.getDogs(token, dogType.name().toLowerCase());
    }
}
