package com.ag.appprueba_musictest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Clase que va a manejar las diferentes consultas en SQLite.
 */
public class DBHelper extends SQLiteOpenHelper {

    //Control de versiones y nombre de la base de datos.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "musicTest.db";

    //Tablas
    public static final String TABLE_PLAYLIST = "t_playlist";
    public static final String TABLE_SONGS = "t_songs";
    public static final String TABLE_PLAYLIST_SONGS = "t_playlist_songs";

    //Constructor
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_PLAYLIST + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL )");

        db.execSQL(" CREATE TABLE " + TABLE_SONGS + "(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " title TEXT NOT NULL," +
                " artistName TEXT," +
                " album TEXT NOT NULL," +
                " duration TIME)");

        db.execSQL("CREATE TABLE " + TABLE_PLAYLIST_SONGS + "(" +
                " id_playlist INTEGER," +
                " id_song INTEGER," +
                " PRIMARY KEY (id_playlist, id_song)," +
                " FOREIGN KEY (id_playlist) REFERENCES t_playlist(id)," +
                " FOREIGN KEY (id_song) REFERENCES t_songs(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_PLAYLIST);
        db.execSQL("DROP TABLE " + TABLE_SONGS);
        db.execSQL("DROP TABLE " + TABLE_PLAYLIST_SONGS);
        onCreate(db);

    }
}
