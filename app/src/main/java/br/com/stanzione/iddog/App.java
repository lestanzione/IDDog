package br.com.stanzione.iddog;

import android.app.Application;

import br.com.stanzione.iddog.di.AndroidModule;
import br.com.stanzione.iddog.di.ApplicationComponent;
import br.com.stanzione.iddog.di.DaggerApplicationComponent;
import br.com.stanzione.iddog.di.NetworkModule;
import br.com.stanzione.iddog.main.MainModule;

public class App extends Application {

    private ApplicationComponent applicationComponent;

    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .networkModule(new NetworkModule())
                .mainModule(new MainModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
