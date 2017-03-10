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

public class CrosswordActivity extends AppCompatActivity {

    private ViewGroup mCrosswordContainer;
    View mCurrentDrag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);

        mCrosswordContainer = (ViewGroup) findViewById(R.id.crossword_container);
        initializeCrossword(CrosswordUtils.getCrossword());


        GridLayout keysGrid = (GridLayout) findViewById(R.id.gl_keys);
        for (int i = 0; i < keysGrid.getChildCount(); ++i){
            keysGrid.getChildAt(i).setOnTouchListener(new MyTouchListener());
        }

        GridLayout crossWordGrid = (GridLayout) findViewById(R.id.crossword_container);
        for (int i = 0; i < crossWordGrid.getChildCount(); ++i){
            crossWordGrid.getChildAt(i).setOnDragListener(new MyDragListener());
        }
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



    private final class MyTouchListener implements View.OnTouchListener {

        public boolean onTouch(View view, MotionEvent motionEvent) {
//            Toast.makeText(getApplicationContext(), "Toast", Toast.LENGTH_LONG).show();
            mCurrentDrag = view;
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

    class MyDragListener implements View.OnDragListener {

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

                    TextView dest = (TextView) v;
                    TextView src = (TextView) mCurrentDrag;
                    dest.setText(src.getText());
                    if (dest.getText().equals(src.getText())){

                    }
                    //SoundHandler.playWinSound(getBaseContext());

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
