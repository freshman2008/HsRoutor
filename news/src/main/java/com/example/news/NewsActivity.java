package com.example.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.annotation.Route;
import com.example.hsrouter.HsRouter;

import java.util.HashMap;
import java.util.Map;

@Route(path = "/news/news1")
public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
    }

    public void jump(View view) {
        HsRouter.getInstance().navigation(this, "/video/video1");
    }
}
