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

    //ViewGroup[][]  mCrosswordUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);

        initializeCrosswordViews(CrosswordUtils.getCrossword());
    }

    private void initializeCrosswordViews(char[][] crossword) {

        mCrosswordContainer = (ViewGroup) findViewById(R.id.crossword_container);
        //mCrosswordUnits = new ViewGroup[crossword.length][crossword[0].length];

        for (int i = 0; i < crossword.length; i++) {
            for (int j = 0; j < crossword[0].length; j++) {

                ViewGroup rowContainer = (ViewGroup) mCrosswordContainer.getChildAt(i);
                //mCrosswordUnits[i][j] = (ViewGroup) rowContainer.getChildAt(j);

                char c = crossword[i][j];

                // if crossword unit doesn't contain entry
                if (c == 0) {
                    // hide crossword unit
                    getCrosswordUnit(i, j).setVisibility(View.INVISIBLE);
                    // else
                } else {
                    // set onDragListener with the right character
                    getCrosswordUnit(i, j).setOnDragListener(new OnCrosswordUnitDragListener(c));
                    // set text to the right answer
                    getCrosswordUnitText(i, j).setText(String.valueOf(c));
                }
            }
        }


        GridLayout keysGrid = (GridLayout) findViewById(R.id.gl_keys);
        for (int i = 0; i < keysGrid.getChildCount(); ++i){
            keysGrid.getChildAt(i).setOnTouchListener(new MyTouchListener());
        }
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

                //  view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class OnCrosswordUnitDragListener implements View.OnDragListener {

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

}
