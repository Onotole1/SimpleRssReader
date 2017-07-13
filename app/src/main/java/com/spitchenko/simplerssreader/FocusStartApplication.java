package com.spitchenko.simplerssreader;

import android.app.Application;

import com.spitchenko.simplerssreader.utils.logger.LogCatHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Date: 26.03.17
 * Time: 18:10
 *
 * @author anatoliy
 */
public class FocusStartApplication extends Application {
    static {
        final Logger rootLogger = Logger.getLogger("com.spitchenko.focusstart");

        rootLogger.setUseParentHandlers(false);

        if (BuildConfig.DEBUG) {
            rootLogger.addHandler(new LogCatHandler());
        } else {
            rootLogger.setLevel(Level.OFF);
        }
    }
}
