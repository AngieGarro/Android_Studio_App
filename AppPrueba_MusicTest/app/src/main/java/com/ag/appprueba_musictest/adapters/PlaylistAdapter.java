package com.ag.appprueba_musictest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ag.appprueba_musictest.R;
import com.ag.appprueba_musictest.ViewEditPlaylistActivity;
import com.ag.appprueba_musictest.entities.Playlist;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    ArrayList<Playlist> RetrievePlaylist;

    public PlaylistAdapter(ArrayList<Playlist> RetrievePlaylist) {
        this.RetrievePlaylist = RetrievePlaylist;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        holder.ViewIdPlaylist.setText(String.valueOf(RetrievePlaylist.get(position).getId())); // Convertir el ID a cadena
        holder.ViewNamePlaylist.setText(RetrievePlaylist.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return RetrievePlaylist.size();
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {

        TextView ViewIdPlaylist, ViewNamePlaylist;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);

            ViewIdPlaylist = itemView.findViewById(R.id.ViewIdPlaylist);
            ViewNamePlaylist = itemView.findViewById(R.id.ViewNamePlaylist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ViewEditPlaylistActivity.class);
                    intent.putExtra("id", RetrievePlaylist.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}

