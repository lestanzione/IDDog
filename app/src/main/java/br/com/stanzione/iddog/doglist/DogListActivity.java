package br.com.stanzione.iddog.doglist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import br.com.stanzione.iddog.App;
import br.com.stanzione.iddog.R;
import br.com.stanzione.iddog.data.DogType;
import br.com.stanzione.iddog.doglist.adapter.DogImagesAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DogListActivity extends AppCompatActivity implements DogListContract.View,
        BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.dogRecyclerView)
    RecyclerView dogRecyclerView;

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    DogListContract.Presenter presenter;

    private DogImagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_list);

        setUpUi();
        setUpInjector();
    }

    private void setUpInjector(){
        ((App) getApplicationContext())
                .getApplicationComponent()
                .inject(this);

        presenter.attachView(this);
        presenter.getImageList(DogType.HUSKY);
    }

    private void setUpUi(){
        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        adapter = new DogImagesAdapter(this);
        dogRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        dogRecyclerView.setAdapter(adapter);

    }

    @Override
    public void showDogGallery(List<String> imageUrlList) {
        adapter.setItems(imageUrlList);
    }

    @Override
    public void setProgressBarVisible(boolean visible) {
        if (visible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigationHusky:
                presenter.getImageList(DogType.HUSKY);
                return true;
            case R.id.navigationPound:
                presenter.getImageList(DogType.POUND);
                return true;
            case R.id.navigationPug:
                presenter.getImageList(DogType.PUG);
                return true;
            case R.id.navigationLabrador:
                presenter.getImageList(DogType.LABRADOR);
                return true;
        }
        return false;
    }
}
