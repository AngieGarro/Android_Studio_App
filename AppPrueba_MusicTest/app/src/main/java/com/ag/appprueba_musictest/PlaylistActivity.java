package com.ag.appprueba_musictest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.ag.appprueba_musictest.database.DBPlaylist;
import com.ag.appprueba_musictest.entities.Playlist;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PlaylistActivity extends AppCompatActivity {

    Button btnBack;
    EditText txtNamePlaylist;
    Button btnAddPLaylist, btnDeletePlaylist;

    RecyclerView listPlaylist;
    ArrayList<Playlist> playlistArrayList;
    PlaylistAdapter adapter;

    DBPlaylist dbPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_playlist);

        dbPlaylist = new DBPlaylist(PlaylistActivity.this);

        listPlaylist = findViewById(R.id.list_playlist);
        listPlaylist.setLayoutManager(new LinearLayoutManager(this));

        playlistArrayList = dbPlaylist.viewPlaylists();
        adapter = new PlaylistAdapter(playlistArrayList);
        listPlaylist.setAdapter(adapter);

        btnBack = findViewById(R.id.btn_backPlay_home);
        txtNamePlaylist = findViewById(R.id.txt_playlistName);
        btnAddPLaylist = findViewById(R.id.btn_FormAdd_playlist);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaylistActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnAddPLaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = dbPlaylist.createPlaylist(txtNamePlaylist.getText().toString());

                if (id > 0) {
                    Toast.makeText(PlaylistActivity.this, "Created Playlist", Toast.LENGTH_LONG).show();
                    cleanForm();
                    refreshPlaylist();
                } else {
                    Toast.makeText(PlaylistActivity.this, "Error Creating Playlist", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cleanForm() {
        txtNamePlaylist.setText("");
    }

    void refreshPlaylist() {
        playlistArrayList.clear();
        playlistArrayList.addAll(dbPlaylist.viewPlaylists());
        adapter.notifyDataSetChanged();
    }

    private void loadPlaylists() {
        playlistArrayList = dbPlaylist.viewPlaylists();
        adapter = new PlaylistAdapter(playlistArrayList);
        listPlaylist.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPlaylists();
    }

    //REQUEST TO API -----------------------------------------------------
    private void GetToApiPlaylist() {

        String url = "https://localhost:7237/api/Playlist/RetrieveAll";

        StringRequest postResquest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String NamePlaylistApi = jsonObject.getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(postResquest);
    }

    private void PostToApiPlaylist(final String name, final Time createdDate) {

        String url = "https://localhost:7237/api/Playlist/Create";

        StringRequest postResquest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PlaylistActivity.this, "Response POST = " + response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("createDate", String.valueOf(createdDate));

                return params;
            }
        };
        Volley.newRequestQueue(this).add(postResquest);
    }

    private void PutToApiPlaylist(final String name) {

        String url = "https://localhost:7237/api/Playlist/Update";

        StringRequest postResquest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PlaylistActivity.this, "Response = " + response, Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", "1");
                params.put("title", name);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(postResquest);
    }

    private void DeleteToApi() {

        String url = "https://localhost:7237/api/Playlist/Delete/1";

        StringRequest postResquest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(PlaylistActivity.this, "Response = " + response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(postResquest);
    }
}



