package br.com.stanzione.iddog.doglist;

import br.com.stanzione.iddog.data.DogGallery;
import br.com.stanzione.iddog.data.DogType;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DogListPresenter implements DogListContract.Presenter {

    private DogListContract.View view;
    private DogListContract.Repository repository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DogListPresenter(DogListContract.Repository repository){
        this.repository = repository;
    }

    @Override
    public void getImageList(DogType dogType) {

        view.setEmptyStateVisible(false);
        view.setProgressBarVisible(true);

        compositeDisposable.clear();

        compositeDisposable.add(
                repository.getToken()
                        .switchMap(new Function<String, ObservableSource<DogGallery>>() {
                            @Override
                            public ObservableSource<DogGallery> apply(String s) throws Exception {
                                return repository.fetchImages(s, dogType);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Consumer<DogGallery>() {
                                    @Override
                                    public void accept(DogGallery dogGallery) throws Exception {
                                        System.out.println(dogGallery);

                                        view.setProgressBarVisible(false);
                                        view.showDogGallery(dogGallery.getImageUrlList());

                                        if(null == dogGallery.getImageUrlList()){
                                            view.setEmptyStateVisible(true);
                                        }
                                    }
                                },
                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        throwable.printStackTrace();

                                        view.setProgressBarVisible(false);
                                    }
                                }
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
}
