package com.example.admincalmable.adapter;

import com.example.admincalmable.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.admincalmable.EditMeditation;
import com.example.admincalmable.EditMusicPage;
import com.example.admincalmable.Model.UploadSong;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UploadSong> uploadedSongs;
    private OnNoteListner onNoteListner;
    DatabaseReference reference1;


    public MusicAdapter(Context context, ArrayList<UploadSong> uploadedSongs,OnNoteListner onNoteListner) {
        this.context = context;
        this.uploadedSongs = uploadedSongs;
        this.onNoteListner=onNoteListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_upload_music,parent,false);
        return new ViewHolder(view,onNoteListner);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.songTitle.setText(uploadedSongs.get(position).getSongName());
        holder.songId.setText(uploadedSongs.get(position).getId());
        Picasso.get().load(uploadedSongs.get(position).getImageView()).into(holder.imageView);
        holder.songUrl.setText(uploadedSongs.get(position).getUrl());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Songs_Admin");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            UploadSong post=dataSnapshot.getValue(UploadSong.class);
                            if(post.getId().equals(uploadedSongs.get(position).getId())){
                                reference1 = databaseReference.child(dataSnapshot.getRef().getKey());
                                Log.d("ReferencePath", String.valueOf(reference1));
                                reference1.removeValue();
                                notifyItemRemoved(position);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.editMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myactivity = new Intent(context.getApplicationContext(), EditMusicPage.class).putExtra("songName",uploadedSongs.get(position).getSongName())
                        .putExtra("image",uploadedSongs.get(position).getImageView()).putExtra("id",uploadedSongs.get(position).getId()).putExtra("uri",uploadedSongs.get(position).getUrl());
                myactivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(myactivity);
            }
        });
    }


    @Override
    public int getItemCount() {
        return uploadedSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView songTitle;
        TextView songId;
        TextView songUrl;
        ImageView deleteBtn,editMusic;
        OnNoteListner onNoteListner;


        public ViewHolder(@NonNull View itemView, OnNoteListner onNoteListner) {
            super(itemView);
            imageView = itemView.findViewById(R.id.musicImage);
            songId = itemView.findViewById(R.id.musicId);
            songTitle = itemView.findViewById(R.id.musicTitle);
            songUrl=itemView.findViewById(R.id.url);
            deleteBtn=itemView.findViewById(R.id.delete);
            this.onNoteListner=onNoteListner;
            itemView.setOnClickListener(this);
            editMusic=itemView.findViewById(R.id.editMusic);

        }

        @Override
        public void onClick(View view) {
            onNoteListner.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListner {
        void onNoteClick(int position);
    }

}