package br.com.stanzione.iddog.doglist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.stanzione.iddog.R;
import butterknife.BindView;

public class DogListActivity extends AppCompatActivity {

    @BindView(R.id.dogRecyclerView)
    RecyclerView dogRecyclerView;

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigationHusky:
                    return true;
                case R.id.navigationPound:
                    return true;
                case R.id.navigationPug:
                    return true;
                case R.id.navigationLabrador:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_list);
    }

}
