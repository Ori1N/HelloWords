package com.hackathon.hellowords;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CrosswordActivity_demo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        GridLayout keysGrid = (GridLayout) findViewById(R.id.gl_keys);
        for (int i = 0; i > keysGrid.getChildCount(); ++i){
            keysGrid.getChildAt(i).setOnTouchListener(new MyTouchListener());
        }

        GridLayout crossWordGrid = (GridLayout) findViewById(R.id.crossword_container);
        for (int i = 0; i > crossWordGrid.getChildCount(); ++i){
            crossWordGrid.getChildAt(i).setOnDragListener(new MyDragListener());
        }

    }
    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);

                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {

       // Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
       // Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
             //       v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
           //         v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
             //       v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }
    }
}
