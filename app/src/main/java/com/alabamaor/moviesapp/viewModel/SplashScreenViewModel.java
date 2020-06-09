package com.alabamaor.moviesapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.alabamaor.moviesapp.dataSource.MoviesService;
import com.alabamaor.moviesapp.database.DatabaseHandler;
import com.alabamaor.moviesapp.model.Movie;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SplashScreenViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> mIsLoadingDone = new MutableLiveData<>();
    private MutableLiveData<Boolean> mHasError = new MutableLiveData<>();


    private long mMoviesCount;


    private MoviesService mMoviesService = MoviesService.getInstance();
    private CompositeDisposable mDisposable = new CompositeDisposable();

    private DatabaseHandler mDbHandler = DatabaseHandler.getInstance(getApplication().getApplicationContext());


    public SplashScreenViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {

        mMoviesCount = mDbHandler.getCount();
        if (mMoviesCount <= 0) {
            getMovies();
        } else {
            mIsLoadingDone.setValue(true);
        }
    }

    private void getMovies() {

        mIsLoadingDone.setValue(false);
        mDbHandler.deleteAll();

        mDisposable.add(

                mMoviesService.getAllMovies()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<Movie>>() {
                            @Override
                            public void onSuccess(List<Movie> movies) {
                                mHasError.setValue(false);
                                mIsLoadingDone.setValue(true);
                                mMoviesCount = movies.size();
                                mDbHandler.addMovies(movies);
                            }

                            @Override
                            public void onError(Throwable e) {
                                mMoviesCount = 0;
                                mHasError.setValue(true);
                            }
                        })

        );

    }


    public MutableLiveData<Boolean> getIsLoadingDone() {
        return mIsLoadingDone;
    }


}
