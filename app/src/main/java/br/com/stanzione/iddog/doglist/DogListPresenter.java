package br.com.stanzione.iddog.doglist;

import br.com.stanzione.iddog.data.DogGallery;
import br.com.stanzione.iddog.data.DogType;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DogListPresenter implements DogListContract.Presenter {

    private DogListContract.View view;
    private DogListContract.Repository repository;

    private DogType dogType;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DogListPresenter(DogListContract.Repository repository){
        this.repository = repository;
    }

    @Override
    public void getImageList(DogType dogType) {

        this.dogType = dogType;

        view.setEmptyStateVisible(false);
        view.setProgressBarVisible(true);

        compositeDisposable.clear();

        compositeDisposable.add(
                repository.getToken()
                        .switchMap(this::mapToDogImageList)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::onDogGalleryReceived,
                                this::onDogGalleryError
                        )
        );

    }

    @Override
    public void attachView(DogListContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {
        if(!compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }

    private ObservableSource<DogGallery> mapToDogImageList(String s) {
        return repository.fetchImages(s, dogType);
    }

    private void onDogGalleryReceived(DogGallery dogGallery) {
        view.setProgressBarVisible(false);
        view.showDogGallery(dogGallery.getImageUrlList());

        if(null == dogGallery.getImageUrlList()){
            view.setEmptyStateVisible(true);
        }
    }

    private void onDogGalleryError(Throwable throwable) {
        view.setProgressBarVisible(false);
        view.showMessage();
    }

}
