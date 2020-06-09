package com.alabamaor.moviesapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alabamaor.moviesapp.R;
import com.alabamaor.moviesapp.viewModel.MovieDetailsViewModel;
import com.alabamaor.moviesapp.viewModel.MovieListViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.alabamaor.moviesapp.model.Util.getProgressDrawable;

public class MovieDetailsFragment extends Fragment {

    private MovieDetailsViewModel mViewModel;

    @BindView(R.id.ivDetailMovieImg)
    AppCompatImageView mImage;

    @BindView(R.id.tvDetailMovieTitle)
    TextView mTitle;

    @BindView(R.id.tvDetailMovieReleaseYear)
    TextView mReleaseYear;

    @BindView(R.id.tvDetailMovieGenre)
    TextView mGenre;

    @BindView(R.id.tvDetailMovieRating)
    TextView mRating;


    @BindView(R.id.shadeViewDetail)
    View mShadeViewDetail;

    @BindView(R.id.shadeViewDetailHead)
    View mShadeViewDetailHead;


    public static MovieDetailsFragment newInstance() {
        return new MovieDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_details_fragment, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);

        if (getArguments() != null) {
            mViewModel.setSelectedMovie(MovieDetailsFragmentArgs.fromBundle(getArguments()).getSelectedMovie());
        }
        observeData();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
    }

    private void observeData() {
        mViewModel.getmSelectedMovie().observe(getViewLifecycleOwner(), movie -> {
            if (movie != null){

               mTitle.setText(movie.getTitle());
               mReleaseYear.setText(String.valueOf(movie.getReleaseYear()));
               mRating.setText(String.valueOf(movie.getRating()));

                RequestOptions options = new RequestOptions()
                        .placeholder(getProgressDrawable(getContext()))
                        .error(R.drawable.movies_background);
                Glide.with(mImage.getContext())
                        .setDefaultRequestOptions(options)
                        .load(movie.getImage())
                        .into(mImage);

            }

            String genreStr = "";
            StringBuilder builder = new StringBuilder(genreStr);
            for (int i = 0 ; i < movie.getGenre().size() ; i ++) {
                builder.append(genreStr + movie.getGenre().get(i));
                if (i < movie.getGenre().size() -1){
                builder.append(genreStr + " / ");
                }
            }
            mGenre.setText(builder);

            setupBackgroundColor(getContext(), movie.getImage());
        });
    }


    public  void setupBackgroundColor(Context context, String url) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource)
                                .generate(palette -> {

                                    int intColor = palette.getDarkMutedColor(Color.rgb(20,20,20));

                                        mShadeViewDetail.setBackgroundColor(intColor);
                                        mShadeViewDetailHead.setBackgroundColor(intColor);

                                });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

}