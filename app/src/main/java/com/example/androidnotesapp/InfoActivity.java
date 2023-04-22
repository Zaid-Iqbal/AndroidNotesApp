package com.example.androidnotesapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
