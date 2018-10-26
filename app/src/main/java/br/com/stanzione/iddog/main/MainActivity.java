package br.com.stanzione.iddog.main;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import javax.inject.Inject;

import br.com.stanzione.iddog.App;
import br.com.stanzione.iddog.R;
import br.com.stanzione.iddog.doglist.DogListActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.util.Patterns.EMAIL_ADDRESS;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;

    @BindView(R.id.logoImageView)
    ImageView logoImageView;

    @BindView(R.id.emailEditText)
    EditText emailEditText;

    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUi();
        setUpInjector();
    }

    private void setUpInjector(){
        ((App) getApplicationContext())
                .getApplicationComponent()
                .inject(this);

        presenter.attachView(this);
    }

    private void setUpUi(){
        ButterKnife.bind(this);
    }

    @OnClick(R.id.loginButton)
    public void loginButtonClicked(){
        String email = emailEditText.getText().toString();
        if(isValidEmail(email)) {
            presenter.doLogin(email);
        }
        else{
            showMessage("Invalid email");
        }
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(constraintLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void navigateToDogList() {
        Intent intent = new Intent(this, DogListActivity.class);
        startActivity(intent);
    }

    @Override
    public void setProgressBarVisible(boolean visible) {
        if (visible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isValidEmail(String email){
        return EMAIL_ADDRESS.matcher(email).matches();
    }
}
