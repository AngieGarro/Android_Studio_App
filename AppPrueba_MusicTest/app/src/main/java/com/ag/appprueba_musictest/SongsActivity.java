package com.ag.appprueba_musictest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ag.appprueba_musictest.adapters.PlaylistAdapter;
import com.ag.appprueba_musictest.adapters.SongsAdapter;
import com.ag.appprueba_musictest.database.DBPlaylist;
import com.ag.appprueba_musictest.database.DBSongs;
import com.ag.appprueba_musictest.entities.Playlist;
import com.ag.appprueba_musictest.entities.Songs;

import java.sql.Time;
import java.util.ArrayList;

public class SongsActivity extends AppCompatActivity {

    Button btnBack;
    RecyclerView listSongs;
    ArrayList<Songs> SongsArrayList;
    SongsAdapter adapter;

    DBSongs dbSongs;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_songs);

        dbSongs = new DBSongs(SongsActivity.this);

        listSongs = findViewById(R.id.list_songs);
        listSongs.setLayoutManager(new LinearLayoutManager(this));

        SongsArrayList = dbSongs.viewSongs();
        adapter = new SongsAdapter(SongsArrayList);
        listSongs.setAdapter(adapter);

        btnBack = findViewById(R.id.btn_backSong_home);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SongsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void refreshSongs() {
        SongsArrayList.clear();
        SongsArrayList.addAll(dbSongs.viewSongs());
        adapter.notifyDataSetChanged();
    }
}