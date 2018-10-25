package br.com.stanzione.iddog.main;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import br.com.stanzione.iddog.data.LoginRequest;
import br.com.stanzione.iddog.data.User;
import br.com.stanzione.iddog.data.User.UserResponse;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {


    private MainContract.View mockView;
    private MainContract.Repository mockRepository;
    private MainPresenter presenter;
    private User.UserResponse mockUserResponse;

    private String validEmail = "test@mail.com";
    private String token = "token";
    private User defaultUser;

    @BeforeClass
    public static void setupRxSchedulers() {

        Scheduler immediate = new Scheduler() {

            @Override
            public Disposable scheduleDirect(Runnable run, long delay, TimeUnit unit) {
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);

    }

    @AfterClass
    public static void tearDownRxSchedulers(){
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    @Before
    public void setup() {

        mockView = mock(MainContract.View.class);
        mockRepository = mock(MainContract.Repository.class);
        mockUserResponse = mock(UserResponse.class);

        presenter = new MainPresenter(mockRepository);
        presenter.attachView(mockView);

        createDefaultUser();
    }

    private void createDefaultUser(){
        defaultUser = new User();
        defaultUser.setEmail(validEmail);
        defaultUser.setToken(token);
    }


    @Test
    public void withValidEmailShouldDoLogin(){
        when(mockUserResponse.getUser()).thenReturn(defaultUser);
        when(mockRepository.doLogin(anyString())).thenReturn(Observable.just(mockUserResponse));

        presenter.doLogin(validEmail);

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, never()).showMessage(anyString());
        verify(mockRepository, times(1)).doLogin(validEmail);
        verify(mockRepository, times(1)).persistToken(token);
    }

    @Test
    public void withNoInternetShouldShowMessage(){

        when(mockRepository.doLogin(anyString())).thenReturn(Observable.error(new IOException()));

        presenter.doLogin(validEmail);

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, times(1)).showMessage(anyString());
        verify(mockRepository, times(1)).doLogin(validEmail);
        verify(mockRepository, never()).persistToken(anyString());

    }

    @Test
    public void withErrorShouldShowMessage(){

        when(mockRepository.doLogin(anyString())).thenReturn(Observable.error(new Throwable()));

        presenter.doLogin(validEmail);

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, times(1)).showMessage(anyString());
        verify(mockRepository, times(1)).doLogin(validEmail);
        verify(mockRepository, never()).persistToken(anyString());

    }

}