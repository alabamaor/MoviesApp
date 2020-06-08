package com.alabamaor.moviesapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    int id;

    @SerializedName("title")
    String title;

    @SerializedName("image")
    String image;

    @SerializedName("rating")
    float rating;

    @SerializedName("releaseYear")
    int releaseYear;

    @SerializedName("genre")
    List<String> genre;

    /*
     "title": "Dawn of the Planet of the Apes",
        "image": "https://api.androidhive.info/json/movies/1.jpg",
        "rating": 8.3,
        "releaseYear": 2014,
        "genre": ["Action", "Drama", "Sci-Fi"]

     */

    public Movie() {
    }


    public Movie(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.image = movie.getImage();
        this.rating = movie.getRating();
        this.releaseYear = movie.getReleaseYear();
        this.genre = new ArrayList<>();
        this.genre.addAll(movie.getGenre());
    }

    public Movie(int id, String title, String image, float rating, int releaseYear, List<String> genre) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.rating = rating;
        this.releaseYear = releaseYear;
        this.genre = new ArrayList<>();
        this.genre.addAll(genre);
    }

    public int getId() {
        return id;
    }

    public Movie setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Movie setImage(String image) {
        this.image = image;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public Movie setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public Movie setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
        return this;
    }

    public List<String> getGenre() {
        return genre;
    }

    public Movie setGenre(List<String> genre) {
        this.genre = genre;
        return this;
    }
}
