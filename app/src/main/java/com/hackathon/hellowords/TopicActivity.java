package com.hackathon.hellowords;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class TopicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        LinearLayout topicLayout = (LinearLayout) findViewById(R.id.topic_container);

        for (int i=0; i< topicLayout.getChildCount(); ++i){
            topicLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.launchActivity(TopicActivity.this, CrosswordActivity.class);
                }
            });
        }






    }

}
