package com.hackathon.hellowords;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Ori on 09/03/2017.
 */

public class Utils {

    public static void launchActivity(Activity activityFrom, Class<? extends Activity> activityTo) {
        Intent activityIntent = new Intent(activityFrom, activityTo);
        activityFrom.startActivity(activityIntent);
    }

}
