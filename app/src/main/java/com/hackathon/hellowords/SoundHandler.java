package com.hackathon.hellowords;

import android.content.Context;
import android.media.MediaPlayer;

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


}
