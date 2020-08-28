package com.example.lixiulia.hsrouter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.annotation.Route;
import com.example.hsrouter.HsRouter;
//import com.example.hsrouter.HsRouter;

@Route(path="/main/main1")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jump(View view) {
        HsRouter.getInstance().navigation(this, "/main/main2");
    }
}
