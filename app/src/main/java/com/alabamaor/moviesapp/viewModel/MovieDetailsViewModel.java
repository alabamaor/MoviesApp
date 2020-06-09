package com.alabamaor.moviesapp.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alabamaor.moviesapp.model.Movie;

public class MovieDetailsViewModel extends ViewModel {

   private MutableLiveData<Movie> mSelectedMovie = new MutableLiveData<>();

    public MutableLiveData<Movie> getmSelectedMovie() {
        return mSelectedMovie;
    }

    public MovieDetailsViewModel setSelectedMovie(Movie mSelectedMovie) {
        this.mSelectedMovie.setValue(mSelectedMovie);
        return this;
    }
}