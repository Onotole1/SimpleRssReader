package com.spitchenko.simplerssreader.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Date: 13.07.17
 * Time: 16:14
 *
 * @author anatoliy
 */

public class FirstLaunchController {
    private final static String FIRST_LAUNCH_CONTROLLER
            = "com.spitchenko.simplerssreader.utils.FirstLaunchController";
    private final static String IS_FIRST_LAUNCH = FIRST_LAUNCH_CONTROLLER + ".isFirstLaunch";

    private final Context context;

    public FirstLaunchController(final Context context) {
        this.context = context;
    }

    public boolean isFirstLaunch() {
        final SharedPreferences sharedPreferences
                = context.getSharedPreferences(FIRST_LAUNCH_CONTROLLER, Context.MODE_PRIVATE);

        final boolean isFirstLaunch = sharedPreferences.getBoolean(IS_FIRST_LAUNCH, true);

        if (isFirstLaunch) {
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_FIRST_LAUNCH, false);
            editor.apply();
        }

        return isFirstLaunch;
    }
}
