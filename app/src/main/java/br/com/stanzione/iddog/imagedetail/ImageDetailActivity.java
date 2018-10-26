package br.com.stanzione.iddog.imagedetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.stanzione.iddog.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageDetailActivity extends AppCompatActivity {

    @BindView(R.id.imageDetailImageView)
    ImageView imageDetailImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ButterKnife.bind(this);

        String imageUrl = getIntent().getStringExtra("imageUrl");

        Picasso.with(this)
                .load(imageUrl)
                .into(imageDetailImageView);

    }

}
