package br.com.stanzione.iddog;

import android.app.Application;

import br.com.stanzione.iddog.di.ApplicationComponent;
import br.com.stanzione.iddog.di.DaggerApplicationComponent;
import br.com.stanzione.iddog.di.NetworkModule;

public class App extends Application {

    private ApplicationComponent applicationComponent;

    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .networkModule(new NetworkModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
