package br.com.stanzione.iddog.doglist;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import br.com.stanzione.iddog.data.DogGallery;
import br.com.stanzione.iddog.data.DogType;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DogListPresenterTest {

    private DogListContract.View mockView;
    private DogListContract.Repository mockRepository;
    private DogListPresenter presenter;

    private DogType dogType = DogType.HUSKY;
    private String token = "token";
    private DogGallery defaultDogGallery;
    private DogGallery defaultEmptyDogGallery;

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

        mockView = mock(DogListContract.View.class);
        mockRepository = mock(DogListContract.Repository.class);

        presenter = new DogListPresenter(mockRepository);
        presenter.attachView(mockView);

        createDefaultDogGallery();
        createDefaultEmptyDogGallery();
    }

    private void createDefaultDogGallery(){
        defaultDogGallery = new DogGallery();
        defaultDogGallery.setImageUrlList(new ArrayList<>());
    }

    private void createDefaultEmptyDogGallery(){
        defaultEmptyDogGallery = new DogGallery();
    }

    @Test
    public void withTokenAndNotEmptyListShouldShowDogImageList() {

        when(mockRepository.getToken()).thenReturn(Observable.just(token));
        when(mockRepository.fetchImages(anyString(), any(DogType.class))).thenReturn(Observable.just(defaultDogGallery));

        presenter.getImageList(dogType);

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, times(1)).setEmptyStateVisible(false);
        verify(mockView, never()).setEmptyStateVisible(true);
        verify(mockView, never()).showMessage(anyString());
        verify(mockRepository, times(1)).getToken();
        verify(mockRepository, times(1)).fetchImages(token, dogType);

    }

    @Test
    public void withTokenAndEmptyListShouldShowDogImageList() {

        when(mockRepository.getToken()).thenReturn(Observable.just(token));
        when(mockRepository.fetchImages(anyString(), any(DogType.class))).thenReturn(Observable.just(defaultEmptyDogGallery));

        presenter.getImageList(dogType);

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, times(1)).setEmptyStateVisible(false);
        verify(mockView, times(1)).setEmptyStateVisible(true);
        verify(mockView, never()).showMessage(anyString());
        verify(mockRepository, times(1)).getToken();
        verify(mockRepository, times(1)).fetchImages(token, dogType);

    }

    @Test
    public void withNoTokenShouldShowErrorMessage() {

        when(mockRepository.getToken()).thenReturn(Observable.error(new Throwable()));
        when(mockRepository.fetchImages(anyString(), any(DogType.class))).thenReturn(Observable.just(defaultEmptyDogGallery));

        presenter.getImageList(dogType);

        verify(mockView, times(1)).setProgressBarVisible(true);
        verify(mockView, times(1)).setProgressBarVisible(false);
        verify(mockView, times(1)).setEmptyStateVisible(false);
        verify(mockView, never()).setEmptyStateVisible(true);
        verify(mockView, times(1)).showMessage(anyString());
        verify(mockRepository, times(1)).getToken();
        verify(mockRepository, never()).fetchImages(anyString(), any(DogType.class));

    }

}