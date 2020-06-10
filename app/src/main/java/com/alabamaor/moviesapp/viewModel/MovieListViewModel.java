package com.alabamaor.moviesapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.alabamaor.moviesapp.database.DatabaseHandler;
import com.alabamaor.moviesapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movie>> mList = new MutableLiveData<>();
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private DatabaseHandler mDbHandler = DatabaseHandler.getInstance(getApplication().getApplicationContext());

    public MovieListViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        mDisposable.add(
                mDbHandler.getAllMovies()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<Movie>>() {

                            @Override
                            public void onSuccess(List<Movie> movies) {
                                mList.setValue(movies);
                            }

                            @Override
                            public void onError(Throwable e) {
                                mList.setValue(new ArrayList<>());
                            }
                        })

        );

    }


    public MutableLiveData<List<Movie>> getList() {
        return mList;
    }


}