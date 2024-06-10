package com.ag.appprueba_musictest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ag.appprueba_musictest.adapters.SongsAdapter;
import com.ag.appprueba_musictest.database.DBPlaylist;
import com.ag.appprueba_musictest.database.DBSongs;
import com.ag.appprueba_musictest.entities.Playlist;
import com.ag.appprueba_musictest.entities.Songs;

import java.util.ArrayList;

public class ViewEditPlaylistActivity extends AppCompatActivity {

    TextView textViewName, textViewName2;
    EditText txt_playlistNameEdit;
    Button btn_Editplaylist, btnAddSong, btnDeletePlaylist;
    RecyclerView list_playlistSongs, list_allSongs;

    Playlist playlist;
    int id = 0;

    boolean correct = false;

    DBSongs dbSongs;
    DBPlaylist dbPlaylist;

    SongsAdapter allSongsAdapter;
    SongsAdapter playlistSongsAdapter;
    ArrayList<Songs> allSongs;
    ArrayList<Songs> playlistSongs;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_edit_playlist);

        textViewName = findViewById(R.id.textViewName);
        textViewName2 = findViewById(R.id.textViewName2);
        txt_playlistNameEdit = findViewById(R.id.txt_playlistNameEdit);
        btn_Editplaylist = findViewById(R.id.btn_Editplaylist);
        btnAddSong = findViewById(R.id.btn_addSong);
        btnDeletePlaylist = findViewById(R.id.btn_deletePlaylist);  // Nuevo botón de eliminación
        list_playlistSongs = findViewById(R.id.list_playlistSongs);
        list_allSongs = findViewById(R.id.list_allSongs);

        dbPlaylist = new DBPlaylist(this);
        dbSongs = new DBSongs(this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("id");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("id");
        }

        playlist = dbPlaylist.viewPlaylistId(id);

        if (playlist != null) {
            textViewName.setText(playlist.getName());
            textViewName2.setText(playlist.getName());
        }

        // Setup RecyclerView for all songs
        list_allSongs.setLayoutManager(new LinearLayoutManager(this));
        allSongs = dbSongs.viewSongs();
        allSongsAdapter = new SongsAdapter(allSongs, null);
        list_allSongs.setAdapter(allSongsAdapter);

        // Setup RecyclerView for playlist songs
        list_playlistSongs.setLayoutManager(new LinearLayoutManager(this));
        playlistSongs = dbSongs.viewSongsInPlaylist(id);
        playlistSongsAdapter = new SongsAdapter(playlistSongs, new SongsAdapter.OnSongRemoveListener() {
            @Override
            public void onSongRemove(int position) {
                int songId = playlistSongs.get(position).getId();
                correct = dbPlaylist.removeSongFromPlaylist(id, songId);

                if (correct) {
                    Toast.makeText(ViewEditPlaylistActivity.this, "Song removed from playlist", Toast.LENGTH_LONG).show();
                    // Update playlist songs
                    playlistSongs.remove(position);
                    playlistSongsAdapter.notifyItemRemoved(position);
                } else {
                    Toast.makeText(ViewEditPlaylistActivity.this, "Error removing song from playlist", Toast.LENGTH_LONG).show();
                }
            }
        });
        list_playlistSongs.setAdapter(playlistSongsAdapter);

        btn_Editplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txt_playlistNameEdit.getText().toString().equals("")) {
                    correct = dbPlaylist.editPlaylist(id, txt_playlistNameEdit.getText().toString());

                    if (correct) {
                        Toast.makeText(ViewEditPlaylistActivity.this, "Updated Playlist", Toast.LENGTH_LONG).show();
                        playlist = dbPlaylist.viewPlaylistId(id);
                        textViewName.setText(playlist.getName());
                        textViewName2.setText(playlist.getName());
                        cleanForm();
                    } else {
                        Toast.makeText(ViewEditPlaylistActivity.this, "Error Updating Playlist", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ViewEditPlaylistActivity.this, "Field Name Required", Toast.LENGTH_LONG).show();
                }
            }

            private void cleanForm() {
                txt_playlistNameEdit.setText("");
            }
        });

        btnAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = allSongsAdapter.getSelectedPosition();
                if (selectedPosition != -1) {
                    int songId = allSongs.get(selectedPosition).getId();
                    correct = dbPlaylist.addSongToPlaylist(id, songId);

                    if (correct) {
                        Toast.makeText(ViewEditPlaylistActivity.this, "Song added to playlist", Toast.LENGTH_LONG).show();
                        // Update playlist songs
                        playlistSongs.add(allSongs.get(selectedPosition));
                        playlistSongsAdapter.notifyItemInserted(playlistSongs.size() - 1);
                    } else {
                        Toast.makeText(ViewEditPlaylistActivity.this, "Error adding song to playlist", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ViewEditPlaylistActivity.this, "No song selected", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnDeletePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correct = dbPlaylist.deletePlaylist(id);

                if (correct) {
                    Toast.makeText(ViewEditPlaylistActivity.this, "Playlist deleted", Toast.LENGTH_LONG).show();
                    finish();  // Cierra la actividad y regresa a la actividad anterior
                } else {
                    Toast.makeText(ViewEditPlaylistActivity.this, "Error deleting playlist", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}



