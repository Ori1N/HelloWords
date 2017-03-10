package com.hackathon.hellowords;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;

public class SplashScreen extends AppCompatActivity {

    private static final boolean GO_TO_CROSSWORD = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }


    @Override
    protected void onStart() {
        super.onStart();
        loadAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                proceed();
            }
        }, 3000);
    }

    private void loadAnimation() {
//        Glide.with(getApplicationContext())
//                .load(R.drawable.animalicon)
    }

    private void proceed() {
        if (GO_TO_CROSSWORD) {
            Utils.launchActivity(this, CrosswordActivity.class);
        } else {
            Utils.launchActivity(this, TopicActivity.class);
        }
        finish();
    }


}
