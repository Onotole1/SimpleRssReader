package com.spitchenko.simplerssreader.base.controller;

import android.content.Context;
import android.content.SharedPreferences;

import lombok.NonNull;

/**
 * Date: 09.04.17
 * Time: 21:56
 *
 * @author anatoliy
 */
public class UpdateController {
    private final static String UPDATE_CONTROLLER
            = "com.spitchenko.simplerssreader.controller.updateController";
    private final SharedPreferences preferences;

    public UpdateController(@NonNull final Context context) {
        preferences = context.getSharedPreferences(UPDATE_CONTROLLER, Context.MODE_PRIVATE);
    }

    public void turnOnUpdate() {
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(UPDATE_CONTROLLER, true);
        editor.apply();
    }

    public void turnOffUpdate() {
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(UPDATE_CONTROLLER, false);
        editor.apply();
    }

    public boolean isUpdate() {
        return preferences.getBoolean(UPDATE_CONTROLLER, false);
    }
}
