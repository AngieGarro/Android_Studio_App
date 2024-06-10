package com.ag.appprueba_musictest;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ag.appprueba_musictest.database.DBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btn_Playlist;
    Button btn_ListSongs;

    Button btn_bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btn_Playlist = findViewById(R.id.btn_Playlist);
        btn_ListSongs = findViewById(R.id.btn_ListSongs);


        btn_Playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PlaylistActivity.class);
                startActivity(intent);
            }
        });

        btn_ListSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SongsActivity.class);
                startActivity(intent);
            }
        });
    }

}