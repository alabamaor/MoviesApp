package com.alabamaor.moviesapp.dataSource;

import com.alabamaor.moviesapp.model.Movie;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface MoviesApi {

    @GET("movies.json")
    Single<List<Movie>> getAllMovies();
}
