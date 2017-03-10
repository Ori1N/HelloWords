package com.hackathon.hellowords;

import android.content.ClipData;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.OnClick;

public class CrosswordActivity extends AppCompatActivity {


    private ViewGroup mCrosswordContainer;
    ViewGroup mKeyboardView;
    TextView mCurrentDrag = null;
    ImageView mOwlGuide;

    int [] mCharsAtCrossWord = new int[Constants.AlphabetSize];
    CellState [][] states;
    char[][] crossword;
    Pair words;

    @Override
    protected void onStart() {
        super.onStart();
        SoundHandler.startBgMusic(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        SoundHandler.stopBgMusic(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossword);

        this.crossword = CrosswordUtils.getCrosswordMatrix();
        this.words = new Pair(0, CrosswordUtils.getWords());

        initializeCrosswordViews();


    }

    private void initializeCrosswordViews() {

        // find views
        mCrosswordContainer = (ViewGroup) findViewById(R.id.crossword_container);
        mKeyboardView  = (ViewGroup) findViewById((R.id.gl_keys));
        mOwlGuide = (ImageView) findViewById(R.id.crossword_guide);


        states = new CellState[crossword.length][crossword[0].length];
//        Toasty.success(this, "Success!", Toast.LENGTH_SHORT, true).show();
//        Toast.makeText(getApplicationContext(), "i size:"+crossword.length +" j size:"+crossword[0].length, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < crossword.length; i++) {
            for (int j = 0; j < crossword[0].length; j++) {

                char c = crossword[i][j];
                int viewI = i + 1, viewJ = j + 1;

                // if crossword unit contains entry
                if (c != 0) {

                    // TODO update states
                    states[i][j] = CellState.NOT_COMPLETED;

                    // TODO remove update counters
                    ++mCharsAtCrossWord[getCharValue(c)];

                    // show it
                    getCrosswordUnit(viewI, viewJ).setVisibility(View.VISIBLE);
                    // set onDragListener with the right character
                    getCrosswordUnit(viewI, viewJ).setOnDragListener(new OnCrosswordUnitDragListener(i, j));
                    // set text to the right answer
                    getCrosswordUnitText(viewI, viewJ).setText(String.valueOf(c));
                }
                else{
                    states[i][j] = CellState.EMPTY;
                }
            }
        }

        for (int i = 0; i < mKeyboardView.getChildCount(); i++) {
            ViewGroup keyboardLine = (ViewGroup) mKeyboardView.getChildAt(i);
            for (int j = 0; j < keyboardLine.getChildCount(); ++j) {
                    keyboardLine.getChildAt(j).setOnTouchListener(new MyTouchListener());
            }
        }

        RefreshKeyboard();

    }


    @OnClick({
            R.id.crossword_icon_elephant,
            R.id.crossword_icon_bear,
            R.id.crossword_icon_zebra,
            R.id.crossword_icon_hare,
            R.id.crossword_icon_tiger
    })
    public void onCrosswordIconClick(View v) {
        CrosswordAnimalType animalType = null;

        switch (v.getId()) {

            case R.id.crossword_icon_elephant:
                animalType = CrosswordAnimalType.ELEPHANT; break;

            case R.id.crossword_icon_bear:
                animalType = CrosswordAnimalType.BEAR; break;

            case R.id.crossword_icon_zebra:
                animalType = CrosswordAnimalType.ZEBRA; break;

            case R.id.crossword_icon_hare:
                animalType = CrosswordAnimalType.HARE; break;

            case R.id.crossword_icon_tiger:
                animalType = CrosswordAnimalType.TIGER; break;

        }

        SoundHandler.playAnimalWord(getApplicationContext(), animalType);
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
        private int i;
        private int j;

        public OnCrosswordUnitDragListener(int i, int j) {
            mAnswer = crossword[i][j];
            this.i = i;
            this.j = j;
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
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackgroundResource(R.drawable.shapes);
                    break;

                case DragEvent.ACTION_DROP:
                    //Dropped, reassign View to ViewGroup

                    if (getDraggedChar() == mAnswer) { // letter fits

                        Pair rowResult = checkRow(i,j);
                        Pair colResult = checkCol(i,j);

                        if (( colResult.completed > 1)  && (colResult.uncompleted == 1))  { // check there is exactly
                            WordCompleted();
                        }

                        if (( rowResult.completed > 1)  && (rowResult.uncompleted == 1))  { // check there is exactly
                            WordCompleted();
                        }
                        states[i][j] = CellState.COMPLETED; // set state to completed
                        View innerView = ((ViewGroup) v).getChildAt(0);
                        onSuccess(innerView);
                        mAnswer = 0;
                    } else {
//                        Toast.makeText(getApplicationContext(), "Oops.. try again :)", Toast.LENGTH_SHORT).show();
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
    private void WordCompleted(){
//        Toast.makeText(getApplicationContext(), "Word Completed!", Toast.LENGTH_SHORT).show();

        ++words.completed;

        // evolve owl icon

        @DrawableRes int newOwlIcon = -1;

        switch (words.completed) {
            case 1: newOwlIcon = R.drawable.owl_2; break;
            case 2: newOwlIcon = R.drawable.owl_3; break;
            case 3: newOwlIcon = R.drawable.owl_4; break;
            case 4: newOwlIcon = R.drawable.owl_5; break;
        }

        if (newOwlIcon != -1) {
            mOwlGuide.setImageResource(newOwlIcon);
        }

    }
    private Pair checkCol(int i, int j) {
        int uncompleted = 0;
        int completed = 0;
        for(int row = 0; row < states.length; ++row){
            if (states[row][j] == CellState.NOT_COMPLETED){
                ++uncompleted;
            }
            if (states[row][j] == CellState.COMPLETED){
                ++completed;
            }
        }

        return new Pair(completed,uncompleted);
    }

    private Pair checkRow(int i, int j) {
        int uncompleted = 0;
        int completed = 0;

        for(int col = 0; col < states[0].length; ++col){
            if (states[i][col] == CellState.NOT_COMPLETED){
                ++uncompleted;
            }
            if (states[i][col] == CellState.COMPLETED){
                ++completed;
            }
        }
        return new Pair(completed,uncompleted);
    }


    private void onSuccess(View textView) {
        Utils.showViewWithFadeIn(getApplicationContext(), textView);
        SoundHandler.playWinSound(getBaseContext());
        --mCharsAtCrossWord[getCharValue(getDraggedChar())];
        RefreshStates();
        RefreshKeyboard();
    }

    private void RefreshStates() { // check for
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

}
