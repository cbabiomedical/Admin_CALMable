package com.example.admincalmable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;

import java.util.ArrayList;

public class Game extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Spinner dropdown = (Spinner) findViewById(R.id.spinner1);
        ArrayList<String> list = new ArrayList<>();


    }
}