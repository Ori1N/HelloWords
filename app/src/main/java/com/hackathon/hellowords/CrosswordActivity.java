package com.hackathon.hellowords;

import android.content.ClipData;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CrosswordActivity extends AppCompatActivity {

    private ViewGroup mCrosswordContainer;
    TextView mCurrentDrag = null;
    int [] mCharsAtCrossWord = new int[26];

    ViewGroup mKeyboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);

        initializeCrosswordViews(CrosswordUtils.getCrossword());


    }

    private void initializeCrosswordViews(char[][] crossword) {
        mKeyboardView = (ViewGroup) findViewById((R.id.gl_keys));

        mCrosswordContainer = (ViewGroup) findViewById(R.id.crossword_container);
        //mCrosswordUnits = new ViewGroup[crossword.length][crossword[0].length];

        for (int i = 0; i < crossword.length; i++) {
            for (int j = 0; j < crossword[0].length; j++) {

                char c = crossword[i][j];

            //    ++mCharsAtCrossWord[getCharValue(c)];


                // if crossword unit contains entry
                if (c != 0) {
                    // show it
                    getCrosswordUnit(i, j).setVisibility(View.VISIBLE);
                    // set onDragListener with the right character
                    getCrosswordUnit(i, j).setOnDragListener(new OnCrosswordUnitDragListener(c));
                    // set text to the right answer
                    getCrosswordUnitText(i, j).setText(String.valueOf(c));
                }
            }
        }

        for (int i = 0; i < mKeyboardView.getChildCount(); i++) {
            ViewGroup keyboardLine = (ViewGroup) mKeyboardView.getChildAt(i);
            for (int j = 0; i < keyboardLine.getChildCount(); ++j) {
                keyboardLine.getChildAt(j).setOnTouchListener(new MyTouchListener());
            }
        }

      //  RefreshKeyboard();
    }




    private ViewGroup getCrosswordUnit(int i, int j) {
        ViewGroup rowContainer = (ViewGroup) mCrosswordContainer.getChildAt(i);
        return (ViewGroup) rowContainer.getChildAt(j);
    }

    private TextView getCrosswordUnitText(int i, int j) {
        return (TextView) getCrosswordUnit(i, j).getChildAt(0);
    }

    private char getDraggedChar() {
        if (mCurrentDrag == null) return 0;
        return mCurrentDrag.getText().charAt(0);
    }


    private final class MyTouchListener implements View.OnTouchListener {

        public boolean onTouch(View view, MotionEvent motionEvent) {
//            Toast.makeText(getApplicationContext(), "Toast", Toast.LENGTH_LONG).show();
            mCurrentDrag = (TextView) view;
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                TextView tv = (TextView) view;

                //  view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    private class OnCrosswordUnitDragListener implements View.OnDragListener {

        private char mAnswer;
        public OnCrosswordUnitDragListener(char answer) {
            mAnswer = answer;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //    v.setBackgroundResource(R.drawable.shape_droptarget);
                    // TOOD add animation to button - you are here.
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackgroundResource(R.drawable.shapes);
                    break;

                case DragEvent.ACTION_DROP:
                    //Dropped, reassign View to ViewGroup

                    if (getDraggedChar() == mAnswer) {
                        View innerView = ((ViewGroup) v).getChildAt(0);
                        Utils.showViewWithFadeIn(getApplicationContext(), innerView);
                        SoundHandler.playWinSound(getBaseContext());
                  //      --mCharsAtCrossWord[getCharValue(getDraggedChar())];
                  //      RefreshKeyboard();
                        mAnswer = 0;
                    } else {
                        Toast.makeText(getApplicationContext(), "Oops.. try again :)", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    //  v.setBackgroundResource(R.drawable.shapes);
                default:
                    break;
            }
            return true;
        }
    }

    private void RefreshKeyboard() {
        for (int i = 0; i < mKeyboardView.getChildCount(); ++i){

            ViewGroup keyboardLine = (ViewGroup) mKeyboardView.getChildAt(i);
            for (int j = 0; j < keyboardLine.getChildCount(); j++) {

                TextView keyTextView = (TextView) keyboardLine.getChildAt(j);
                if (mCharsAtCrossWord[getCharValue(keyTextView.getText().charAt(0))] > 0) {
                    keyTextView.setVisibility(View.VISIBLE);
                } else {
                    keyTextView.setVisibility(View.GONE);
                }
            }
        }
    }

    private int getCharValue(char c) {
        if (Character.isLowerCase(c)){
            return c - 97;
        }
        else{
            return c - 65;
        }
    }

    private int getAsciiValue(int c) {
        return c + 65;
    }
}
