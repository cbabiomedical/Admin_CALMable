package com.example.admincalmable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;



public class Landing extends AppCompatActivity {

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        logout=findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Landing.this, LoginUserActivity.class));
            }
        });

    }

    public void gotoMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void gotoMeditate(View view) {
        //Intent intent = new Intent(this, Meditate.class);
        //startActivity(intent);
    }

    public void gotogame(View view) {
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }

    public void gotoedit(View view) {
        //Intent intent = new Intent(this, EditUser.class);
        //startActivity(intent);
    }
}