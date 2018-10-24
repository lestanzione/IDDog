package br.com.stanzione.iddog.di;

import javax.inject.Singleton;

import br.com.stanzione.iddog.main.MainActivity;
import dagger.Component;

@Singleton
@Component(
        modules = {
                NetworkModule.class
        }
)
public interface ApplicationComponent {
    void inject(MainActivity activity);
}
