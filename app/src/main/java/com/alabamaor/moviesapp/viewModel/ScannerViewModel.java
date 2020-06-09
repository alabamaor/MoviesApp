package com.alabamaor.moviesapp.viewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.alabamaor.moviesapp.database.DatabaseHandler;
import com.alabamaor.moviesapp.model.DatabaseDoneLoadingQuery;
import com.alabamaor.moviesapp.model.Movie;

public class ScannerViewModel extends AndroidViewModel implements DatabaseDoneLoadingQuery {

    MutableLiveData<Boolean> mIsAdded = new MutableLiveData<>();


    private DatabaseHandler mDbHandler = DatabaseHandler.getInstance(getApplication().getApplicationContext());

    public ScannerViewModel(@NonNull Application application) {
        super(application);
        mDbHandler.setListener(this);
    }

    public void uploadMovie(Movie movie) {
        mDbHandler.addMovie(movie);
        mIsAdded.setValue(true);

    }

    public MutableLiveData<Boolean> getIsAdded() {
        return mIsAdded;
    }

    public ScannerViewModel setIsAdded(MutableLiveData<Boolean> mIsAdded) {
        this.mIsAdded = mIsAdded;
        return this;
    }

    @Override
    public void onAddMovie(boolean isAdded) {
        Toast.makeText(getApplication().getApplicationContext(), ""+isAdded, Toast.LENGTH_SHORT).show();
    }
}