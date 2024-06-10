package com.ag.appprueba_musictest.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ag.appprueba_musictest.entities.Playlist;

import java.util.ArrayList;

public class DBPlaylist extends DBHelper {

    Context context;
    private DBHelper dbHelper;

    public DBPlaylist(@Nullable Context context) {
        super(context);
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public long createPlaylist(String name) {
        long id = 0;
        SQLiteDatabase db = null;
        try {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("name", name);

            id = db.insert(TABLE_PLAYLIST, null, values);
        } catch (Exception ex) {
            Log.e("DBPlaylist", "Error creating playlist", ex);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return id;
    }

    public boolean editPlaylist(int id, String name) {
        boolean correct = false;
        SQLiteDatabase db = null;
        DBHelper dbHelper = new DBHelper(context);

        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);

            int rowsAffected = db.update(TABLE_PLAYLIST, values, "id = ?", new String[]{String.valueOf(id)});
            if (rowsAffected > 0) {
                correct = true;
            }
        } catch (Exception ex) {
            correct = false;
            Log.e("DBPlaylist", "Error updating playlist", ex);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return correct;
    }


    @SuppressLint("Range")
    public ArrayList<Playlist> viewPlaylists() {
        ArrayList<Playlist> list = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursorPlaylist = null;

        try {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getReadableDatabase();

            cursorPlaylist = db.rawQuery("SELECT * FROM " + TABLE_PLAYLIST, null);

            if (cursorPlaylist.moveToFirst()) {
                do {
                    Playlist playlist = new Playlist();
                    playlist.setId(cursorPlaylist.getInt(cursorPlaylist.getColumnIndex("id")));
                    playlist.setName(cursorPlaylist.getString(cursorPlaylist.getColumnIndex("name")));
                    list.add(playlist);
                    Log.d("DBPlaylist", "Added playlist: " + playlist.getName());
                } while (cursorPlaylist.moveToNext());
            }
        } catch (Exception ex) {
            Log.e("DBPlaylist", "Error viewing playlists", ex);
        } finally {
            if (cursorPlaylist != null) {
                cursorPlaylist.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return list;
    }

    @SuppressLint("Range")
    public Playlist viewPlaylistId(int id) {

        SQLiteDatabase db = null;
        Cursor cursorPlaylist = null;

        Playlist playlist = null;
        try {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getReadableDatabase();

            cursorPlaylist = db.rawQuery("SELECT * FROM " + TABLE_PLAYLIST + " WHERE id =" + id +
                    " LIMIT 1", null);

            if (cursorPlaylist.moveToFirst()) {

                playlist = new Playlist();
                playlist.setId(cursorPlaylist.getInt(cursorPlaylist.getColumnIndex("id")));
                playlist.setName(cursorPlaylist.getString(cursorPlaylist.getColumnIndex("name")));
                Log.d("DBPlaylist", "Added playlist: " + playlist.getName());
            }

        } catch (Exception ex) {
            Log.e("DBPlaylist", "Error viewing playlist", ex);
        } finally {
            if (cursorPlaylist != null) {
                cursorPlaylist.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return playlist;
    }

    public boolean addSongToPlaylist(int playlistId, int songId) {
        SQLiteDatabase db = null;
        boolean correct = false;

        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id_playlist", playlistId);
            values.put("id_song", songId);
            long result = db.insert(TABLE_PLAYLIST_SONGS, null, values);
            if (result != -1) {
                correct = true;
            }
        } catch (Exception ex) {
            correct = false;
            Log.e("DBPlaylist", "Error adding song to playlist", ex);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return correct;
    }

    public boolean removeSongFromPlaylist(int playlistId, int songId) {
        boolean correct = false;
        SQLiteDatabase db = null;

        try {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();

            int result = db.delete(TABLE_PLAYLIST_SONGS, "id_playlist=? AND id_song=?", new String[]{String.valueOf(playlistId), String.valueOf(songId)});
            correct = result > 0;
        } catch (Exception ex) {
            Log.e("DBPlaylist", "Error removing song from playlist", ex);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return correct;
    }

    public boolean deletePlaylist(int playlistId) {
        boolean correct = false;
        SQLiteDatabase db = null;

        try {
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();

            // Start a transaction
            db.beginTransaction();

            // Delete songs from the playlist_songs table
            db.delete(TABLE_PLAYLIST_SONGS, "id_playlist=?", new String[]{String.valueOf(playlistId)});

            // Delete the playlist from the playlists table
            db.delete(TABLE_PLAYLIST, "id=?", new String[]{String.valueOf(playlistId)});

            // Mark the transaction as successful
            db.setTransactionSuccessful();
            correct = true;
        } catch (Exception ex) {
            Log.e("DBPlaylist", "Error deleting playlist", ex);
        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                if (db.isOpen()) {
                    db.close();
                }
            }
        }

        return correct;
    }


}


