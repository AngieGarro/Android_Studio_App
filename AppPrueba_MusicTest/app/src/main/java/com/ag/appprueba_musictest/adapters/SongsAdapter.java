package com.ag.appprueba_musictest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ag.appprueba_musictest.R;
import com.ag.appprueba_musictest.entities.Songs;

import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder>{

    ArrayList<Songs> RetrieveSongs;
    private int selectedPosition = -1;
    private OnSongRemoveListener removeListener;

    public SongsAdapter(ArrayList<Songs> songsArrayList) {
        this.RetrieveSongs = songsArrayList;
    }

    public interface OnSongRemoveListener {
        void onSongRemove(int position);
    }

    public SongsAdapter(ArrayList<Songs> RetrieveSongs, OnSongRemoveListener removeListener) {
        this.RetrieveSongs = RetrieveSongs;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public SongsAdapter.SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_songs, parent, false);
        return new SongsAdapter.SongsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsAdapter.SongsViewHolder holder, int position) {
        Songs song = RetrieveSongs.get(position);
        holder.ViewTitleSong.setText(song.getTitle());
        holder.ViewArtistNameSongs.setText(song.getArtistName());
        holder.ViewAlbumSongs.setText(song.getAlbum());
        holder.ViewDurationSongs.setText(song.getDuration().toString());

        holder.itemView.setSelected(selectedPosition == position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });

        holder.btnRemoveSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (removeListener != null) {
                    removeListener.onSongRemove(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return RetrieveSongs.size();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public class SongsViewHolder extends RecyclerView.ViewHolder {
        TextView ViewTitleSong, ViewArtistNameSongs, ViewAlbumSongs, ViewDurationSongs;
        Button btnRemoveSong;

        public SongsViewHolder(@NonNull View itemView) {
            super(itemView);

            ViewTitleSong = itemView.findViewById(R.id.ViewTitleSong);
            ViewArtistNameSongs = itemView.findViewById(R.id.ViewArtistNameSongs);
            ViewAlbumSongs = itemView.findViewById(R.id.ViewAlbumSongs);
            ViewDurationSongs = itemView.findViewById(R.id.ViewDurationSongs);
            btnRemoveSong = itemView.findViewById(R.id.btn_removeSong);
        }
    }
}


