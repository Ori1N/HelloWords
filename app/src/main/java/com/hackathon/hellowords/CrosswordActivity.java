package com.hackathon.hellowords;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hackathon.hellowords.data.CrosswordProvider;
import com.hackathon.hellowords.model.CrosswordObject;

public class CrosswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeUiFromCrossword(CrosswordProvider.getCrossword());
    }

    private void initializeUiFromCrossword(CrosswordObject obj) {
        
    }

}
