package com.alabamaor.moviesapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alabamaor.moviesapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MOVIES_TABLE = "CREATE TABLE " + Util.MOVIES_TABLE + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Util.KEY_TITLE + " TEXT,"
                + Util.KEY_IMAGE + " TEXT,"
                + Util.KEY_RATING + " REAL,"
                + Util.KEY_RELEASE_YEAR + " INTEGER"
                + ")";
        db.execSQL(CREATE_MOVIES_TABLE);

        String CREATE_GENRE_TABLE = "CREATE TABLE " + Util.GENRE_TABLE + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Util.KEY_MOVIE_TITLE + " TEXT,"
                + Util.KEY_GENRE + " TEXT,"
                + " FOREIGN KEY (" + Util.KEY_MOVIE_TITLE + ") REFERENCES " + Util.MOVIES_TABLE + "(" + Util.KEY_TITLE + ")" +
                ")";
        db.execSQL(CREATE_GENRE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(Util.DROP_TABLE_EXEC_SQL, new String[]{Util.MOVIES_TABLE});
        onCreate(db);
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Util.MOVIES_TABLE);
        db.execSQL("delete from " + Util.GENRE_TABLE);
    }

    /**
     *
     * @return
     */
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        Movie movie = new Movie();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        Cursor cursor = db.rawQuery(Util.GET_ALL_FROM + Util.MOVIES_TABLE
                + " ORDER BY "+Util.KEY_RELEASE_YEAR + " ASC", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                movie.setId(cursor.getInt(cursor.getColumnIndex(Util.KEY_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(Util.KEY_TITLE)));
                movie.setImage(cursor.getString(cursor.getColumnIndex(Util.KEY_IMAGE)));
                movie.setRating(cursor.getFloat(cursor.getColumnIndex(Util.KEY_RATING)));
                movie.setReleaseYear(cursor.getInt(cursor.getColumnIndex(Util.KEY_RELEASE_YEAR)));

                getGenreList(db, movie.getTitle());

                movies.add(movie);
                cursor.moveToNext();
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return movies;
    }

    /**
     *
     * @param moviesList
     */
    public void addMovies(List<Movie> moviesList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        for (Movie movie : moviesList) {
            addSingleMovie(db, movie);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    /**
     *
     * @param movie
     */
    public void addMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        addSingleMovie(db, movie);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }


    private List<String> getGenreList(SQLiteDatabase db, String title){
        List<String> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(Util.GET_ALL_FROM + Util.GENRE_TABLE + " WHERE " + Util.KEY_MOVIE_TITLE + "="
                + title, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                list.add(cursor.getString(cursor.getColumnIndex(Util.KEY_TITLE)));
                cursor.moveToNext();
            }
        }
        return list;
    }


    private void addSingleMovie(SQLiteDatabase db, Movie movie) {
        ContentValues values = new ContentValues();
        values.put(Util.KEY_TITLE, movie.getTitle());
        values.put(Util.KEY_IMAGE, movie.getImage());
        values.put(Util.KEY_RATING, movie.getRating());
        values.put(Util.KEY_RELEASE_YEAR, movie.getReleaseYear());
        db.insert(Util.MOVIES_TABLE, null, values);

        for (String genre : movie.getGenre()) {
            values.clear();
            values.put(Util.KEY_MOVIE_TITLE, movie.getTitle());
            values.put(Util.KEY_GENRE, genre);
            db.insert(Util.GENRE_TABLE, null, values);
        }
    }


}
