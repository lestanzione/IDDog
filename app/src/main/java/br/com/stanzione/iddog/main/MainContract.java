package br.com.stanzione.iddog.main;

import br.com.stanzione.iddog.BasePresenter;
import br.com.stanzione.iddog.BaseView;
import br.com.stanzione.iddog.data.User;
import io.reactivex.Observable;

public interface MainContract {

    interface View extends BaseView {
        void showMessage(String message);
        void navigateToDogList();
    }

    interface Presenter extends BasePresenter<View>{
        void doLogin(String email);
    }

    interface Repository {
        Observable<User.UserResponse> doLogin(String email);
        void persistToken(String token);
    }

}
