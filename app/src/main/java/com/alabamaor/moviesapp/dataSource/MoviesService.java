package com.alabamaor.moviesapp.dataSource;

import com.alabamaor.moviesapp.model.Movie;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesService {
        private static final String BASE_URL = "https://api.androidhive.info/json/";

        private static MoviesService mInstance;

        private MoviesApi mApi = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
                .create(MoviesApi.class);

        private MoviesService() {

        }

        public static MoviesService getInstance() {
            if (mInstance == null)
                mInstance = new MoviesService();

            return mInstance;
        }

        public Single<List<Movie>> getAllMovies() {
            return mApi.getAllMovies();
        }
    }

