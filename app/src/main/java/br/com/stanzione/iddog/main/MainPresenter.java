package br.com.stanzione.iddog.main;

import java.io.IOException;

import br.com.stanzione.iddog.data.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;
    private MainContract.Repository repository;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainPresenter(MainContract.Repository repository){
        this.repository = repository;
    }

    @Override
    public void doLogin(String email){

        view.setProgressBarVisible(true);

        compositeDisposable.add(
                repository.doLogin(email)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::onTokenReceived,
                                this::onTokenGenerationError
                        )
        );
    }

    @Override
    public void attachView(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void dispose() {
        if(!compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }

    private void onTokenReceived(User.UserResponse userResponse){
        repository.persistToken(userResponse.getUser().getToken());
        view.setProgressBarVisible(false);
        view.navigateToDogList();
    }

    private void onTokenGenerationError(Throwable throwable){
        if(throwable instanceof IOException){
            view.showNetworkMessage();
        }
        else{
            view.showApiMessage();
        }
        view.setProgressBarVisible(false);
    }

}
