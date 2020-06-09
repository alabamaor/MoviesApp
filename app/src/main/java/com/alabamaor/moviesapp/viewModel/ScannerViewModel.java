package com.alabamaor.moviesapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.alabamaor.moviesapp.database.DatabaseHandler;
import com.alabamaor.moviesapp.model.Movie;

public class ScannerViewModel extends AndroidViewModel {

    MutableLiveData<Boolean> mIsAdded = new MutableLiveData<>();

    private DatabaseHandler mDbHandler = DatabaseHandler.getInstance(getApplication().getApplicationContext());

    public ScannerViewModel(@NonNull Application application) {
        super(application);
    }

    public void uploadMovie(Movie movie) {
        boolean isAdded = mDbHandler.addMovie(movie);
        mIsAdded.setValue(isAdded);
    }

    public MutableLiveData<Boolean> getIsAdded() {
        return mIsAdded;
    }

}