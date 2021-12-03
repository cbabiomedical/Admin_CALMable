package com.example.admincalmable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

//import com.example.admincalmable.Model.UploadMeditate;
import com.example.admincalmable.MainActivity;
import com.example.admincalmable.Model.UploadSong;
import com.example.admincalmable.adapter.MusicAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayUploadedSongs extends AppCompatActivity implements MusicAdapter.OnNoteListner{
    private RecyclerView recyclerView;
    private ArrayList<UploadSong> uploadedSongs;
    private MusicAdapter musicAdapter;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_uploaded_songs);
        reference = FirebaseDatabase.getInstance().getReference().child("Songs_Admin");
        recyclerView = findViewById(R.id.recyclerView);

        initData();
    }

    private void initData() {
        uploadedSongs = new ArrayList<>();

        Intent intent = getIntent();
        String selected_name = intent.getExtras().getString("selected_name");
        Log.d("selected_name------Dis",selected_name);

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Songs_Admin").child(selected_name);
        Log.d("Display Path",reference.child(selected_name).getKey());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UploadSong post = dataSnapshot.getValue(UploadSong.class);
                    Log.d("Post", String.valueOf(post));
                    uploadedSongs.add(post);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", String.valueOf(error));

            }
        });

        musicAdapter = new MusicAdapter(getApplicationContext(),uploadedSongs,this::onNoteClick);
        recyclerView.setAdapter(musicAdapter);

    }


    @Override
    public void onNoteClick(int position) {
        uploadedSongs.get(position);
        String songName = uploadedSongs.get(position).getSongName();
        String url = uploadedSongs.get(position).getUrl();
        Log.d("url",url);
        String image = uploadedSongs.get(position).getImageView();
        String id=uploadedSongs.get(position).getId();
        Log.d("Url", url);
        startActivity(new Intent(getApplicationContext(), PlayMusicActivity.class).putExtra("uri", url).putExtra("songName", songName).putExtra("image", image).putExtra("id",id));
    }
}