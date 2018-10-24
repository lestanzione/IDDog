package br.com.stanzione.iddog;

public interface BasePresenter<T extends BaseView> {
    void attachView(T view);
    void dispose();
}
