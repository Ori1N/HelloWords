package com.hackathon.hellowords;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CrosswordActivity extends AppCompatActivity {

    private ViewGroup mCrosswordContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);

        mCrosswordContainer = (ViewGroup) findViewById(R.id.crossword_container);
        initializeCrossword(CrosswordUtils.getCrossword());
    }

    private void initializeCrossword(char[][] crossword) {

        for (int i = 0; i < crossword.length; i++) {
            for (int j = 0; j < crossword[0].length; j++) {
                char c = crossword[i][j];

                if (c == 0) {
                    getCrosswordInput(i, j).setVisibility(View.INVISIBLE);
                }
            }
        }
    }


    private static final int ROW_LENGTH = 10;

    private TextView getCrosswordInput(int i, int j) {
        return (TextView) mCrosswordContainer.getChildAt(i * ROW_LENGTH + j);
    }

    private void setCrosswordCharacter(int i, int j, char c) {
        getCrosswordInput(i, j).setText(String.valueOf(c));
    }




}
