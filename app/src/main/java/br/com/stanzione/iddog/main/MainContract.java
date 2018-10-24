package br.com.stanzione.iddog.main;

import br.com.stanzione.iddog.BasePresenter;
import br.com.stanzione.iddog.BaseView;

public interface MainContract {

    interface View extends BaseView {
        void showMessage(String message);
    }

    interface Presenter extends BasePresenter<View>{
        void doLogin();
    }

    interface Repository {

    }

}
