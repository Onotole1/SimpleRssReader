package com.spitchenko.simplerssreader;

import android.app.Application;

import com.spitchenko.simplerssreader.utils.logger.LogCatHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Date: 16.07.2017
 * Time: 19:49
 *
 * @author Anatoliy
 */

public class SimpleRssReaderApplication extends Application {
    static {
        final Logger rootLogger = Logger.getLogger("com.spitchenko.simplerssreader");

        rootLogger.setUseParentHandlers(false);

        if (BuildConfig.DEBUG) {
            rootLogger.addHandler(new LogCatHandler());
        } else {
            rootLogger.setLevel(Level.OFF);
        }
    }
}
