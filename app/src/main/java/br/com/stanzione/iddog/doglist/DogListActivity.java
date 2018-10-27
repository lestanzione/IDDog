package br.com.stanzione.iddog.doglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.stanzione.iddog.App;
import br.com.stanzione.iddog.R;
import br.com.stanzione.iddog.data.DogType;
import br.com.stanzione.iddog.doglist.adapter.DogImagesAdapter;
import br.com.stanzione.iddog.imagedetail.ImageDetailActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DogListActivity extends AppCompatActivity implements DogListContract.View,
        BottomNavigationView.OnNavigationItemSelectedListener, DogImagesAdapter.OnDogImageSelectedListener {

    private static final String SAVED_IMAGE_URL_LIST = "SavedImageUrlList";

    @BindView(R.id.dogRecyclerView)
    RecyclerView dogRecyclerView;

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.emptyListTextView)
    TextView emptyListTextView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    DogListContract.Presenter presenter;

    private DogImagesAdapter adapter;
    private List<String> imageUrlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_list);

        setUpUi();
        setUpInjector();

        if(savedInstanceState == null) {
            presenter.getImageList(DogType.HUSKY);
        }
        else{
            imageUrlList = (List<String>) savedInstanceState.getSerializable(SAVED_IMAGE_URL_LIST);
            showDogGallery(imageUrlList);
            if(null == imageUrlList){
                setEmptyStateVisible(true);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVED_IMAGE_URL_LIST, (Serializable) imageUrlList);
    }

    @Override
    protected void onDestroy() {
        presenter.dispose();
        super.onDestroy();
    }

    private void setUpInjector(){
        ((App) getApplicationContext())
                .getApplicationComponent()
                .inject(this);

        presenter.attachView(this);
    }

    private void setUpUi(){
        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        adapter = new DogImagesAdapter(this, this);
        dogRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        dogRecyclerView.setAdapter(adapter);

    }

    @Override
    public void showMessage() {
        Snackbar.make(dogRecyclerView, getResources().getString(R.string.message_general_error), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showDogGallery(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
        adapter.setItems(imageUrlList);
    }

    @Override
    public void setEmptyStateVisible(boolean visible) {
        if (visible) {
            emptyListTextView.setVisibility(View.VISIBLE);
        } else {
            emptyListTextView.setVisibility(View.INVISIBLE);
        }
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

    @Override
    public void onDogImageSelected(String imageUrl) {
        Intent intent = new Intent(this, ImageDetailActivity.class);
        intent.putExtra("imageUrl", imageUrl);
        startActivity(intent);
    }
}
