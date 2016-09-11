package com.example.pazu.indignation;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

public class MainActivity extends Activity {


    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // This code just makes the app fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Manual construction of a view
        gameView = new GameView(this);
        FrameLayout fl = new FrameLayout(this);
        fl.setLayoutParams(new LayoutParams());
        fl.addView(gameView);
        setContentView(fl);
    }
}