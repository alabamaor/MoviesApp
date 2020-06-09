package com.alabamaor.moviesapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.alabamaor.moviesapp.R;
import com.alabamaor.moviesapp.model.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.alabamaor.moviesapp.model.Util.getProgressDrawable;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.JobViewHolder> {

    private ListItem mListItemListener;

    private Context mContext;
    private List<Movie> mMoviesList;


    public ListAdapter(List<Movie> mMoviesList, Context mContext) {
        this.mListItemListener = null;
        this.mMoviesList = mMoviesList;
        this.mContext = mContext;
    }

    public void update(List<Movie> newList) {
        this.mMoviesList.clear();
        this.mMoviesList.addAll(newList);
        notifyDataSetChanged();
    }


    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from
                (parent.getContext()).inflate(
                R.layout.list_tile, parent,
                false);


        return new JobViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {

        View.OnClickListener listener = v -> {
            if (mListItemListener != null)
                mListItemListener.onMovieSelected(holder.itemView, mMoviesList.get(position));
        };

        holder.mTitle.setOnClickListener(listener);
        holder.mReleaseYear.setOnClickListener(listener);
        holder.mShadeView.setOnClickListener(listener);
        holder.mImage.setOnClickListener(listener);

        holder.bind(mMoviesList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public ListItem getListItemListener() {
        return mListItemListener;
    }

    public ListAdapter setListItemListener(ListItem listItemListener) {
        this.mListItemListener = listItemListener;
        return this;
    }

    public interface ListItem {
        void onMovieSelected(View v, Movie selected);
    }

    class JobViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvMovieTitle)
        TextView mTitle;

        @BindView(R.id.tvMovieReleaseYear)
        TextView mReleaseYear;


        @BindView(R.id.shadeView)
        View mShadeView;

        @BindView(R.id.ivMovieImg)
        AppCompatImageView mImage;

        Context mContext;

        JobViewHolder(@NonNull View itemView, Context c) {
            super(itemView);
            mContext = c;
            ButterKnife.bind(this, itemView);
        }

        public void bind(Movie movie) {
            mTitle.setText(movie.getTitle());
            mReleaseYear.setText(String.valueOf(movie.getReleaseYear()));

            RequestOptions options = new RequestOptions()
                    .placeholder(getProgressDrawable(mContext))
                    .error(R.drawable.movies_background);
            Glide.with(mImage.getContext())
                    .setDefaultRequestOptions(options)
                    .load(movie.getImage())
//                    .skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(mImage);

        }



    }
}