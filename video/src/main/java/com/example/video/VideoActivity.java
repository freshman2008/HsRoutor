package com.example.video;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.annotation.Route;

@Route(path = "/video/video1")
public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
    }
}
