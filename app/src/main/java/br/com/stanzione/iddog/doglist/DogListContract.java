package br.com.stanzione.iddog.doglist;

import java.util.List;

import br.com.stanzione.iddog.BasePresenter;
import br.com.stanzione.iddog.BaseView;
import br.com.stanzione.iddog.data.DogGallery;
import br.com.stanzione.iddog.data.DogType;
import io.reactivex.Observable;

public interface DogListContract {

    interface View extends BaseView {
        void showDogGallery(List<String> imageUrlList);
    }

    interface Presenter extends BasePresenter<View> {
        void getImageList(DogType dogType);
    }

    interface Repository {
        Observable<String> getToken();
        Observable<DogGallery> fetchImages(String token, DogType dogType);
    }

}
