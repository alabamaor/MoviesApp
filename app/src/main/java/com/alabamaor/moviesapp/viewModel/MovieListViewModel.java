package com.alabamaor.moviesapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alabamaor.moviesapp.database.DatabaseHandler;
import com.alabamaor.moviesapp.model.Movie;
import com.alabamaor.moviesapp.view.ListAdapter;

import java.util.List;

public class MovieListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movie>> mList = new MutableLiveData<>();

    private DatabaseHandler mDbHandler = DatabaseHandler.getInstance(getApplication().getApplicationContext());

    public MovieListViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        mList.setValue(mDbHandler.getAllMovies());
    }


    public MutableLiveData<List<Movie>> getList() {
        return mList;
    }

    public MovieListViewModel setList(MutableLiveData<List<Movie>> mList) {
        this.mList = mList;
        return this;
    }
}