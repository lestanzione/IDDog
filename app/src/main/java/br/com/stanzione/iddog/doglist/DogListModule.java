package br.com.stanzione.iddog.doglist;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import br.com.stanzione.iddog.api.DogApi;
import dagger.Module;
import dagger.Provides;

@Module
public class DogListModule {

    @Provides
    @Singleton
    DogListContract.Presenter providesPresenter(DogListContract.Repository repository){
        DogListPresenter presenter = new DogListPresenter(repository);
        return presenter;
    }

    @Provides
    @Singleton
    DogListContract.Repository providesRepository(DogApi dogApi, SharedPreferences preferences){
        DogListRepository repository = new DogListRepository(dogApi, preferences);
        return repository;
    }

}
