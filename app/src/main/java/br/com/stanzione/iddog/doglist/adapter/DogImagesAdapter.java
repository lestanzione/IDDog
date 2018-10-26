package br.com.stanzione.iddog.doglist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.stanzione.iddog.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DogImagesAdapter extends RecyclerView.Adapter<DogImagesAdapter.ViewHolder> {

    private Context context;
    private List<String> imageUrlList = new ArrayList<>();

    public DogImagesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_dog_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imageUrl = imageUrlList.get(position);

        Picasso.with(context)
                .load(imageUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholder_dog_image)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return (null != imageUrlList ? imageUrlList.size() : 0);
    }

    public void setItems(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemDogImageView)
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}