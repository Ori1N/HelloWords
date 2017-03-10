package com.hackathon.hellowords;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class CongratulationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }


    @Override
    protected void onStart() {
        super.onStart();
       /* loadAnimation();*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                proceed();
            }
        }, 5000);
    }

    private void loadAnimation() {
        ImageView image = (ImageView) findViewById(R.id.splash_image);
      /*  Glide.with(getApplicationContext())
                .load(R.drawable.splashed)
                .asGif()
                .placeholder(R.drawable.splashed_frozen)
                .into(image);*/
    }

    private void proceed() {
        Utils.launchActivity(this, TopicActivity.class);
        finish();
    }


}
