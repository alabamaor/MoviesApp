package com.alabamaor.moviesapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alabamaor.moviesapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler mInstance = null;

    public static DatabaseHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHandler(context);
        }
        return mInstance;
    }


    private DatabaseHandler(Context context) {
        super(context, DatabaseUtil.DATABASE_NAME, null, DatabaseUtil.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MOVIES_TABLE = "CREATE TABLE " + DatabaseUtil.MOVIES_TABLE + "("
                + DatabaseUtil.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + DatabaseUtil.KEY_TITLE + " TEXT,"
                + DatabaseUtil.KEY_IMAGE + " TEXT,"
                + DatabaseUtil.KEY_RATING + " REAL,"
                + DatabaseUtil.KEY_RELEASE_YEAR + " INTEGER"
                + ")";
        db.execSQL(CREATE_MOVIES_TABLE);

        String CREATE_GENRE_TABLE = "CREATE TABLE " + DatabaseUtil.GENRE_TABLE + "("
                + DatabaseUtil.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + DatabaseUtil.KEY_MOVIE_TITLE + " TEXT,"
                + DatabaseUtil.KEY_GENRE + " TEXT,"
                + " FOREIGN KEY (" + DatabaseUtil.KEY_MOVIE_TITLE + ") REFERENCES " + DatabaseUtil.MOVIES_TABLE + "(" + DatabaseUtil.KEY_TITLE + ")" +
                ")";
        db.execSQL(CREATE_GENRE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseUtil.DROP_TABLE_EXEC_SQL, new String[]{DatabaseUtil.MOVIES_TABLE});
        onCreate(db);
    }


    public long getCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, DatabaseUtil.MOVIES_TABLE);
        db.close();
        return count;
    }

    /**
     *
     */
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + DatabaseUtil.MOVIES_TABLE);
        db.execSQL("delete from " + DatabaseUtil.GENRE_TABLE);
        db.close();
    }

    /**
     * @return
     */
    public Single<List<Movie>> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        Movie movie;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(DatabaseUtil.GET_ALL_FROM + DatabaseUtil.MOVIES_TABLE
                + " ORDER BY " + DatabaseUtil.KEY_RELEASE_YEAR + " DESC", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndex(DatabaseUtil.KEY_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseUtil.KEY_TITLE)));
                movie.setImage(cursor.getString(cursor.getColumnIndex(DatabaseUtil.KEY_IMAGE)));
                movie.setRating(cursor.getFloat(cursor.getColumnIndex(DatabaseUtil.KEY_RATING)));
                movie.setReleaseYear(cursor.getInt(cursor.getColumnIndex(DatabaseUtil.KEY_RELEASE_YEAR)));

                movie.setGenre(getGenreList(db, movie.getTitle()));
                movies.add(movie);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
        return Single.just(movies);
    }

    /**
     * @param moviesList
     */
    public Single<Boolean> addMovies(List<Movie> moviesList) {
        SQLiteDatabase db = this.getWritableDatabase();
        Boolean isComplete = true;

        try {
            db.beginTransaction();
            for (Movie movie : moviesList) {
                addSingleMovie(db, movie);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            isComplete = false;
        } finally {
            db.endTransaction();
            db.close();
        }
        return Single.just(isComplete);
    }

    /**
     * @param movie
     * @return
     */
    public boolean addMovie(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        String title = movie.getTitle();
        title = title.replaceAll("'", "''");

        Cursor cursor = db.rawQuery(DatabaseUtil.GET_ALL_FROM + DatabaseUtil.MOVIES_TABLE
                        + " WHERE " + DatabaseUtil.KEY_TITLE + "= ? AND " + DatabaseUtil.KEY_RELEASE_YEAR + "= ?",
                new String[]{title, String.valueOf(movie.getReleaseYear())});
        int count = cursor.getCount();
        cursor.close();

        boolean isAdded = count == 0;
        if (isAdded) {
            addSingleMovie(db, movie);

        }
        db.close();
        return isAdded;
    }


    private List<String> getGenreList(SQLiteDatabase db, String title) {
        List<String> list = new ArrayList<>();
        title = title.replaceAll("'", "''");
        Cursor cursor = db.rawQuery(DatabaseUtil.GET_ALL_FROM + DatabaseUtil.GENRE_TABLE + " WHERE "
                        + DatabaseUtil.KEY_MOVIE_TITLE + "= ?",
                new String[]{title});
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                list.add(cursor.getString(cursor.getColumnIndex(DatabaseUtil.KEY_GENRE)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return list;
    }


    private void addSingleMovie(SQLiteDatabase db, Movie movie) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.KEY_TITLE, movie.getTitle());
        values.put(DatabaseUtil.KEY_IMAGE, movie.getImage());
        values.put(DatabaseUtil.KEY_RATING, movie.getRating());
        values.put(DatabaseUtil.KEY_RELEASE_YEAR, movie.getReleaseYear());
        db.insert(DatabaseUtil.MOVIES_TABLE, null, values);

        for (String genre : movie.getGenre()) {
            values.clear();
            values.put(DatabaseUtil.KEY_MOVIE_TITLE, movie.getTitle());
            values.put(DatabaseUtil.KEY_GENRE, genre);
            db.insert(DatabaseUtil.GENRE_TABLE, null, values);
        }
    }


}
