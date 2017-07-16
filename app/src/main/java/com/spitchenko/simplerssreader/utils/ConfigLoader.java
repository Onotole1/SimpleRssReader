package com.spitchenko.simplerssreader.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.google.gson.Gson;
import com.spitchenko.simplerssreader.model.Theme;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import lombok.Cleanup;

/**
 * Date: 13.07.17
 * Time: 15:43
 *
 * @author anatoliy
 */

public class ConfigLoader {
    private final static String CONFIG_LOADER = "com.spitchenko.simplerssreader.utils.ConfigLoader";

    private final static String IS_URLS_LOADED = CONFIG_LOADER + ".isUrlsLoaded";
    private final static String CONFIG_URLS_SET = CONFIG_LOADER + ".configUrlsSet";

    private final static String SETTINGS_JSON = "settings.json";

    private final static String URLS_FROM_JSON_ASSETS = "urls";

    private final static String CURRENT_URL_JSON_ASSETS = "url";

    private final static String CHARSET_UTF_8 = "UTF-8";

    private final static String IS_THEMES_LOAD = CONFIG_LOADER + "isThemesLoad";
    private final static String CUSTOM_THEMES_FROM_JSON_ASSETS = "theme";
    private final static String THEMES_FROM_PREFS_KEYS = CONFIG_LOADER + "themesJsonPrefs";

    private final static String CUSTOM_THEME_NAME_JSON = "themeName";

    private final static String MAIN_THEME_COLORS_JSON = "themeColors";

    private final static String MAIN_THEME_COLORS_EXTEND_JSON = "themeColorsExtend";

    private final static String THEME_COLOR_PRIMARY_JSON = "colorPrimary";
    private final static String THEME_COLOR_PRIMARY_DARK_JSON = "colorPrimaryDark";
    private final static String THEME_COLOR_ACCENT_FROM_JSON = "colorAccent";
    private final static String THEME_COLOR_EXTEND_TEXT_PRIMARY_JSON = "textColorPrimary";
    private final static String THEME_COLOR_EXTEND_TEXT_CONTENT_JSON = "textColorContent";
    private final static String THEME_COLOR_EXTEND_WINDOW_BACKGROUND_JSON = "windowBackground";
    private final static String THEME_COLOR_EXTEND_CONTENT_BACKGROUND_JSON = "contentBackground";

    private final Context context;

    public ConfigLoader(final Context context) {
        this.context = context;
    }

    public Set<String> loadUrlsFromConfig() {
        final SharedPreferences sharedPreferences
                = context.getSharedPreferences(CONFIG_LOADER, Context.MODE_PRIVATE);

        final boolean isUrlsLoad = sharedPreferences.getBoolean(IS_URLS_LOADED, false);

        if (isUrlsLoad) {
            return sharedPreferences.getStringSet(CONFIG_URLS_SET, null);
        } else {
                try {
                    @Cleanup
                    final InputStream inputStream = context.getAssets().open(SETTINGS_JSON);

                    final int size = inputStream.available();
                    final byte[] buffer = new byte[size];
                    final int readBytes = inputStream.read(buffer);

                    if (readBytes > 0) {
                        final String json = new String(buffer, CHARSET_UTF_8);
                        final JSONObject obj = new JSONObject(json);
                        final JSONArray jsonArray = obj.getJSONArray(URLS_FROM_JSON_ASSETS);

                        final Set<String> result = new HashSet<>();

                        for (int i = 0, length = jsonArray.length(); i < length; i++) {
                            final JSONObject jsonObject = jsonArray.getJSONObject(i);
                            final String url = jsonObject.getString(CURRENT_URL_JSON_ASSETS);
                            result.add(url);
                        }
                        return result;
                    }
                } catch (final IOException | JSONException e) {
                    e.printStackTrace();
                    return null;
                }

            return null;
        }
    }

    public Set<Theme> getThemes() {
        final SharedPreferences sharedPreferences
                = context.getSharedPreferences(CONFIG_LOADER, Context.MODE_PRIVATE);

        final boolean isThemesLoad = sharedPreferences.getBoolean(IS_THEMES_LOAD, false);
        if (isThemesLoad) {
            final Set<String> keys = sharedPreferences.getStringSet(THEMES_FROM_PREFS_KEYS, null);

            final Set<Theme> result = new HashSet<>();

            if (null != keys) {
                for (final String key:keys) {
                    final Gson gson = new Gson();
                    final Theme theme
                            = gson.fromJson(sharedPreferences.getString(key, null), Theme.class);
                    result.add(theme);
                }
            }
            return result;
        } else {
            try {
                @Cleanup
                final InputStream inputStream = context.getAssets().open(SETTINGS_JSON);

                final int size = inputStream.available();
                final byte[] buffer = new byte[size];
                final int readBytes = inputStream.read(buffer);

                if (readBytes > 0) {
                    final String json = new String(buffer, CHARSET_UTF_8);
                    final JSONObject rootObject = new JSONObject(json);

                    final Set<Theme> result = new HashSet<>();

                    //Main theme set
                    final Theme mainTheme = new Theme();

                    mainTheme.setThemeName(Constants.MAIN_THEME_NAME);

                    final JSONObject mainThemeColorsJson
                            = rootObject.getJSONObject(MAIN_THEME_COLORS_JSON);

                    mainTheme.setColorPrimary(Color.parseColor(mainThemeColorsJson
                            .getString(THEME_COLOR_PRIMARY_JSON)));

                    mainTheme.setColorPrimaryDark(Color.parseColor(mainThemeColorsJson
                            .getString(THEME_COLOR_PRIMARY_DARK_JSON)));

                    mainTheme.setColorAccent(Color.parseColor(mainThemeColorsJson
                            .getString(THEME_COLOR_ACCENT_FROM_JSON)));

                    mainTheme.setTextColorPrimary(Color.parseColor(rootObject
                            .getString(THEME_COLOR_EXTEND_TEXT_PRIMARY_JSON)));

                    mainTheme.setTextColorContent(Color.parseColor(rootObject
                            .getString(THEME_COLOR_EXTEND_TEXT_CONTENT_JSON)));

                    mainTheme.setWindowBackground(Color.parseColor(rootObject
                            .getString(THEME_COLOR_EXTEND_WINDOW_BACKGROUND_JSON)));

                    mainTheme.setContentBackground(Color.parseColor(rootObject
                            .getString(THEME_COLOR_EXTEND_CONTENT_BACKGROUND_JSON)));

                    result.add(mainTheme);

                    final JSONArray customThemesJsonArray
                            = rootObject.getJSONArray(CUSTOM_THEMES_FROM_JSON_ASSETS);

                    for (int i = 0, length = customThemesJsonArray.length(); i < length; i++) {
                        final JSONObject currentThemeJson = customThemesJsonArray.getJSONObject(i);

                        final Theme customTheme = new Theme();

                        customTheme.setThemeName(currentThemeJson.getString(CUSTOM_THEME_NAME_JSON));

                        customTheme.setColorPrimary(Color.parseColor(currentThemeJson
                                .getString(THEME_COLOR_PRIMARY_JSON)));

                        customTheme.setColorPrimaryDark(Color.parseColor(currentThemeJson
                                .getString(THEME_COLOR_PRIMARY_DARK_JSON)));

                        customTheme.setColorAccent(Color.parseColor(currentThemeJson
                                .getString(THEME_COLOR_ACCENT_FROM_JSON)));

                        customTheme.setTextColorPrimary(Color.parseColor(currentThemeJson
                                .getString(THEME_COLOR_EXTEND_TEXT_PRIMARY_JSON)));

                        customTheme.setTextColorContent(Color.parseColor(currentThemeJson
                                .getString(THEME_COLOR_EXTEND_TEXT_CONTENT_JSON)));

                        customTheme.setWindowBackground(Color.parseColor(currentThemeJson
                                .getString(THEME_COLOR_EXTEND_WINDOW_BACKGROUND_JSON)));

                        customTheme.setContentBackground(Color.parseColor(currentThemeJson
                                .getString(THEME_COLOR_EXTEND_CONTENT_BACKGROUND_JSON)));

                        result.add(customTheme);
                    }

                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(IS_THEMES_LOAD, true);

                    final Set<String> keys = new HashSet<>(customThemesJsonArray.length() + 1);

                    for (final Theme theme:result) {
                        final String themeName = theme.getThemeName();
                        keys.add(themeName);
                        editor.putString(themeName, theme.toString());
                    }

                    editor.putStringSet(THEMES_FROM_PREFS_KEYS, keys);

                    editor.apply();

                    return result;
                }
            } catch (final IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }

            return null;
        }
    }
}
