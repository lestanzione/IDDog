package br.com.stanzione.iddog.main;

import java.io.IOException;

import br.com.stanzione.iddog.data.LoginRequest;
import br.com.stanzione.iddog.data.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.util.Patterns.EMAIL_ADDRESS;

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

        if(!isValidEmail(email)){
            view.setProgressBarVisible(false);
            view.showMessage("Please, enter a valid email");
        }

        compositeDisposable.add(
                repository.doLogin(createLoginRequest(email))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Consumer<User.UserResponse>() {
                                    @Override
                                    public void accept(User.UserResponse userResponse) throws Exception {
                                        System.out.println("OK");
                                        System.out.println("User: " + userResponse.getUser().getId());
                                        System.out.println("Token: " + userResponse.getUser().getToken());
                                        System.out.println("Created: " + userResponse.getUser().getCreatedDate());

                                        repository.persistToken(userResponse.getUser().getToken());
                                        view.setProgressBarVisible(false);

                                    }
                                },
                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        throwable.printStackTrace();
                                        if(throwable instanceof IOException){
                                            view.showMessage("Network error!");
                                        }
                                        else{
                                            view.showMessage("API error!");
                                        }
                                        view.setProgressBarVisible(false);
                                    }
                                }
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

    private boolean isValidEmail(String email){
        return EMAIL_ADDRESS.matcher(email).matches();
    }

    private LoginRequest createLoginRequest(String email){
        return new LoginRequest(email);
    }

}
