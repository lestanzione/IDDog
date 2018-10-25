package br.com.stanzione.iddog.di;

import javax.inject.Singleton;

import br.com.stanzione.iddog.main.MainActivity;
import br.com.stanzione.iddog.main.MainModule;
import dagger.Component;

@Singleton
@Component(
        modules = {
                AndroidModule.class,
                NetworkModule.class,
                MainModule.class
        }
)
public interface ApplicationComponent {
    void inject(MainActivity activity);
}
