package com.example.lixiulia.hsrouter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.annotation.Route;
import com.example.hsrouter.HsRouter;
//import com.example.hsrouter.HsRouter;

@Route(path="/main/main2")
public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void jump2(View view) {
        HsRouter.getInstance().navigation(this, "/news/news1");
    }
}
