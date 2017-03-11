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

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class CrosswordActivity extends AppCompatActivity {


    private ViewGroup mCrosswordContainer;
    ViewGroup mKeyboardView;
    TextView mCurrentDrag = null;
    ImageView mOwlGuide;

    int [] mCharsAtCrossWord = new int[Constants.AlphabetSize];
    CellState [][] states;
    char[][] crossword;
    Pair words;


    /* Initialization ------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crossword);
        ButterKnife.bind(this);

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

                    states[i][j] = CellState.NOT_COMPLETED;

                    // TODO remove update counters
                    ++mCharsAtCrossWord[getCharValue(c)];

                    // show it
                    getCrosswordCellView(viewI, viewJ).setVisibility(View.VISIBLE);
                    // set onDragListener with the right character
                    getCrosswordCellView(viewI, viewJ).setOnDragListener(new OnCrosswordUnitDragListener(i, j));
                    // set text to the right answer
                    getCrosswordCellTextView(viewI, viewJ).setText(String.valueOf(c));
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

        refreshKeyboard();

    }


    // Keyboard drag and drop functionality

    private void onRightCharacterSelected(View textView, int i, int j) {
        Utils.showViewWithFadeIn(getApplicationContext(), textView);
        SoundHandler.playWinSound(getBaseContext());
        refreshStates(i, j);
        refreshKeyboard();
    }

    private void onWordCompleted(int i, int j){

        // update members
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

        if (words.completed == words.totalToComplete){
            onFinishedCrossword();
        }

    }

    private void onFinishedCrossword(){
        Utils.launchActivity(this, CongratulationsActivity.class);
        finish();
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

                        View innerView = ((ViewGroup) v).getChildAt(0);
                        onRightCharacterSelected(innerView, i, j);
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


    private void refreshStates(int i, int j) {
        if (checkIfWordWasCompleted(i, j)) {
            onWordCompleted(i, j);
        }

        // refresh state
        states[i][j] = CellState.COMPLETED;
    }

    private boolean checkIfWordWasCompleted(int i, int j) {
        final int MIN_WORD_LENGTH = 3;

        // check selected cell column
        final int maxI = states.length;

        int indexForwardI = i;
        boolean missingForwardI = false;

        while (++indexForwardI < maxI) {
            CellState cellState = states[indexForwardI][j];

            if (cellState == CellState.EMPTY) {
                // got to word end, exit loop
                break;

            } else if (cellState == CellState.NOT_COMPLETED) {
                // word not completed, mark flag and exit loop
                missingForwardI = true;
                break;
            }
        }

        int indexBackwardI = i;
        boolean missingBackwardI = false;

        while (--indexBackwardI >= 0) {
            CellState cellState = states[indexBackwardI][j];

            if (cellState == CellState.EMPTY) {
                // got to word end, exit loop
                break;

            } else if (cellState == CellState.NOT_COMPLETED) {
                // word not completed, mark flag and exit loop
                missingBackwardI = true;
                break;
            }
        }

        int countI = indexForwardI - (indexBackwardI + 1);
        if (countI >= MIN_WORD_LENGTH && !missingForwardI && !missingBackwardI) {
            return true;
        }


        // check selected cell row
        final int maxJ = states[0].length;

        // check forward
        int indexForwardJ = j;
        boolean missingForwardJ = false;

        while (++indexForwardJ < maxJ) {
            CellState cellState = states[i][indexForwardJ];

            if (cellState == CellState.EMPTY) {
                // got to word end, exit loop
                break;

            } else if (cellState == CellState.NOT_COMPLETED) {
                // word not completed, mark flag and exit loop
                missingForwardJ = true;
                break;
            }
        }

        int indexBackwardJ = j;
        boolean missingBackwardJ = false;

        while (--indexBackwardJ >= 0) {
            CellState cellState = states[i][indexBackwardJ];

            if (cellState == CellState.EMPTY) {
                // got to word end, exit loop
                break;

            } else if (cellState == CellState.NOT_COMPLETED) {
                // word not completed, mark flag and exit loop
                missingBackwardJ = true;
                break;
            }
        }

        int countJ = indexForwardJ - (indexBackwardJ + 1);
        if (countJ >= MIN_WORD_LENGTH && !missingForwardJ && !missingBackwardJ) {
            return true;
        }

        return false;
    }

    private void refreshKeyboard() {

        // update characters-counter
        int draggedIndex = getCharValue(getDraggedChar());
        // if dragged index is valid (a character was just dragged to the crossword)
        if (draggedIndex >= 0 && draggedIndex < 26) {
            --mCharsAtCrossWord[draggedIndex];
        }

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


    // views utils

    private ViewGroup getCrosswordCellView(int i, int j) {
        ViewGroup rowContainer = (ViewGroup) mCrosswordContainer.getChildAt(i);
        return (ViewGroup) rowContainer.getChildAt(j);
    }

    private TextView getCrosswordCellTextView(int i, int j) {
        return (TextView) getCrosswordCellView(i, j).getChildAt(0);
    }

    private char getDraggedChar() {
        if (mCurrentDrag == null) return 0;
        return mCurrentDrag.getText().charAt(0);
    }



    /* Extra Features ------------------------- */

    // background music

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


    // icon click functionality (say crossword word)
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

        SoundHandler.playAnimalWord(getBaseContext(), animalType);
    }


    // hint functionality

    @OnLongClick(R.id.crossword_guide)
    public boolean onOwlLongClick(View v) {
        showHint();
        return true;
    }

    private boolean mHintShown = false;
    private void showHint() {
        if (mHintShown) {
            Toast.makeText(getApplicationContext(), "Already used a hint today...", Toast.LENGTH_SHORT).show();
            return;
        }

        int hintsCounter = 0;
        // go over states array, find first uncompleted items (search by matrix order)
        for (int i = 0; i < states.length; ++i) {
            for (int j = 0; j < states[i].length; j++) {

                if (states[i][j] == CellState.NOT_COMPLETED) {
                    fillCrosswordChar(i, j);

                    // after filling
                    if (++hintsCounter >= 3) {
                        mHintShown = true;
                        return;
                    }
                }
            }
        }
    }

    private void fillCrosswordChar(int i, int j) {
        // todo!
    }

}
