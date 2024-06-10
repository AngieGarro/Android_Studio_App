package com.ag.appprueba_musictest.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ag.appprueba_musictest.entities.Playlist;
import com.ag.appprueba_musictest.entities.Songs;

import java.sql.Time;
import java.util.ArrayList;

public class DBSongs extends DBHelper{

    Context context;
    public DBSongs(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long createSong(String Title, String ArtisName, String Album, Time Duration) {
        long id = 0;
        SQLiteDatabase db = null;
        try {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("title", Title);
            values.put("artistName", ArtisName);
            values.put("album", Album);
            values.put("duration", Duration.getTime());

            id = db.insert(TABLE_SONGS, null, values);
        } catch (Exception ex) {
            Log.e("DBSongs", "Error creating song", ex);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return id;
    }
    @SuppressLint("Range")
    public ArrayList<Songs> viewSongs() {
        ArrayList<Songs> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursorSong = null;

        try {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getReadableDatabase();

            cursorSong = db.rawQuery("SELECT * FROM " + TABLE_SONGS, null);

            if (cursorSong.moveToFirst()) {
                do {
                    Songs song = new Songs();
                    song.setId(cursorSong.getInt(cursorSong.getColumnIndex("id")));
                    song.setTitle(cursorSong.getString(cursorSong.getColumnIndex("title")));
                    song.setArtistName(cursorSong.getString(cursorSong.getColumnIndex("artistName")));
                    song.setAlbum(cursorSong.getString(cursorSong.getColumnIndex("album")));

                    String durationString = cursorSong.getString(cursorSong.getColumnIndex("duration"));
                    Time duration = Time.valueOf(durationString); // Convertir a Time
                    song.setDuration(duration);

                    list.add(song);
                    Log.d("DBSongs", "Added song: " + song.getTitle());
                } while (cursorSong.moveToNext());
            }
        } catch (Exception ex) {
            Log.e("DBSongs", "Error viewing songs", ex);
        } finally {
            if (cursorSong != null) {
                cursorSong.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return list;
    }

    @SuppressLint("Range")
    public ArrayList<Songs> viewSongsInPlaylist(int playlistId) {
        ArrayList<Songs> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursorSong = null;

        try {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getReadableDatabase();

            cursorSong = db.rawQuery("SELECT s.* FROM " + TABLE_SONGS + " s " +
                    "INNER JOIN " + TABLE_PLAYLIST_SONGS + " ps ON s.id = ps.id_song " +
                    "WHERE ps.id_playlist = ?", new String[]{String.valueOf(playlistId)});

            if (cursorSong.moveToFirst()) {
                do {
                    Songs song = new Songs();
                    song.setId(cursorSong.getInt(cursorSong.getColumnIndex("id")));
                    song.setTitle(cursorSong.getString(cursorSong.getColumnIndex("title")));
                    song.setArtistName(cursorSong.getString(cursorSong.getColumnIndex("artistName")));
                    song.setAlbum(cursorSong.getString(cursorSong.getColumnIndex("album")));

                    int durationInMillis = cursorSong.getInt(cursorSong.getColumnIndex("duration"));
                    song.setDuration(new Time(durationInMillis));

                    list.add(song);
                } while (cursorSong.moveToNext());
            }
        } catch (Exception ex) {
            Log.e("DBSongs", "Error viewing songs in playlist", ex);
        } finally {
            if (cursorSong != null) {
                cursorSong.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return list;
    }

}
