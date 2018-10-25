package br.com.stanzione.iddog.main;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import br.com.stanzione.iddog.api.DogApi;
import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Provides
    @Singleton
    MainContract.Presenter providesPresenter(MainContract.Repository repository){
        MainPresenter presenter = new MainPresenter(repository);
        return presenter;
    }

    @Provides
    @Singleton
    MainContract.Repository providesRepository(DogApi dogApi, SharedPreferences preferences){
        MainRepository repository = new MainRepository(dogApi, preferences);
        return repository;
    }
}
