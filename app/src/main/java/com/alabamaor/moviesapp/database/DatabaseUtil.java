package com.alabamaor.moviesapp.database;

public class DatabaseUtil {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movies_db";
    public static final String MOVIES_TABLE = "movies_table";
    public static final String GENRE_TABLE = "genre_table";


    public static String GET_ALL_FROM = "select * from ";
    public static String DROP_TABLE_EXEC_SQL = "DROP TABLE IF EXISTS";

    public static final String KEY_ID = "id";
    public static final String KEY_MOVIE_TITLE = "movie_title";
    public static final String KEY_TITLE = "title";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_RATING = "rating";
    public static final String KEY_RELEASE_YEAR = "releaseYear";
    public static final String KEY_GENRE = "genre";



}
