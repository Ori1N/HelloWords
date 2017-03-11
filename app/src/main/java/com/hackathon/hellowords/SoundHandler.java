package com.hackathon.hellowords;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.RawRes;

/**
 * Created by roman on 3/10/17.
 */

public class SoundHandler {

    static MediaPlayer backgroundMusic;
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
            case ELEPHANT:
                soundRes = R.raw.elephant;
                break;
            case BEAR:
                soundRes = R.raw.bear;
                break;
            case ZEBRA:
                soundRes = R.raw.zebra;
                break;
            case HARE:
                soundRes = R.raw.hare;
                break;
            case TIGER:
                soundRes = R.raw.tiger;
                break;
        }

        if (soundRes != -1) {
            MediaPlayer winSound = MediaPlayer.create(context, soundRes);
            winSound.start();
        }

    }

    public static void playWordCompletion(Context context) {// TOOD change to owlSound
        MediaPlayer wordCompleted = MediaPlayer.create(context, R.raw.win_sound);
        wordCompleted.start();
    }

    public static void startBgMusic(Context context) {
        backgroundMusic = MediaPlayer.create(context, R.raw.bg_music);
        backgroundMusic.start();
    }

    public static void stopBgMusic(Context context) {
        backgroundMusic.stop();
    }

}
