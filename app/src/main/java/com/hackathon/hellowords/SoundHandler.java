package com.hackathon.hellowords;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.RawRes;

/**
 * Created by roman on 3/10/17.
 */

public class SoundHandler {


    public static void playLoseSound(Context context) {
        MediaPlayer loseSound = MediaPlayer.create(context, R.raw.lose_sound);
        loseSound.start();
    }

    public static void playWinSound(Context context) {
        MediaPlayer winSound = MediaPlayer.create(context, R.raw.win_sound);
        winSound.start();
    }


    public static void playAnimalWord(Context context, CrosswordAnimalType animalType) {
        if (animalType == null) return;

        @RawRes int soundRes = -1;

        switch (animalType) {
            // todo: apply resource by enum
        }

        if (soundRes != -1) {
            MediaPlayer winSound = MediaPlayer.create(context, soundRes);
            winSound.start();
        }

    }


}
