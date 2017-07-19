package com.spitchenko.simplerssreader.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionButton;
import com.spitchenko.simplerssreader.R;
import com.spitchenko.simplerssreader.base.view.BaseActivity;
import com.spitchenko.simplerssreader.model.Theme;

import java.util.Set;

/**
 * Date: 16.07.2017
 * Time: 19:00
 *
 * @author Anatoliy
 */

public class ThemeController {
    private final static String THEME_CONTROLLER
            = "com.spitchenko.simplerssreader.utils.ThemeController";
    private final static String CURRENT_THEME = THEME_CONTROLLER + ".currentTheme";

    private final Context context;

    public ThemeController(final Context activity) {
        this.context = activity;
    }

    @Nullable
    public Theme getCurrentTheme() {
        final ConfigLoader configLoader = new ConfigLoader(context);
        final Set<Theme> themes = configLoader.getThemes();

        Theme currentTheme = null;
        if (null != themes) {
            for (final Theme theme : themes) {
                if (theme.getThemeName().equals(getCurrentThemeName())) {
                    currentTheme = theme;
                }
            }
        }

        return currentTheme;
    }

    private String getCurrentThemeName() {
        final SharedPreferences sharedPreferences
                = context.getSharedPreferences(THEME_CONTROLLER, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CURRENT_THEME, Constants.MAIN_THEME_NAME);
    }

    public void setCurrentTheme(@NonNull final String themeName) {
        final SharedPreferences sharedPreferences
                = context.getSharedPreferences(THEME_CONTROLLER, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENT_THEME, themeName);
        editor.apply();
    }

    public void applyThemeToChannelWindow(@NonNull final View view, @NonNull final BaseActivity activity) {
        final ConfigLoader configLoader = new ConfigLoader(context);
        final Set<Theme> themes = configLoader.getThemes();
        if (null != themes) {
            Theme currentTheme = null;
            for (final Theme theme : themes) {
                if (theme.getThemeName().equals(getCurrentThemeName())) {
                    currentTheme = theme;
                }
            }

            if (null != currentTheme) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    final Window window = activity.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(currentTheme.getColorPrimaryDark());
                }

                final AppBarLayout appBarLayout
                        = (AppBarLayout) activity.findViewById(R.id.activity_main_app_bar);
                appBarLayout.setBackgroundColor(currentTheme.getColorPrimary());

                final Toolbar toolbar = (Toolbar) activity.findViewById(R.id.activity_main_toolbar);
                toolbar.setTitleTextColor(currentTheme.getTextColorPrimary());

                final RecyclerView recyclerView = view.findViewById(R.id.fragment_channel_recycler_view);
                recyclerView.setBackgroundColor(currentTheme.getContentBackground());

                final ProgressBar progressBar = view.findViewById(R.id.fragment_channel_progressBar);
                progressBar.getIndeterminateDrawable().setColorFilter(currentTheme.getColorAccent()
                        , PorterDuff.Mode.MULTIPLY);

                final FloatingActionButton floatingActionButton = view.findViewById(R.id.fragment_channel_fab);
                floatingActionButton.setColorNormal(currentTheme.getColorAccent());
                floatingActionButton.setColorPressed(currentTheme.getColorAccent());
                floatingActionButton.setImageResource(android.R.drawable.ic_input_add);
            }
        }
    }

    public void applyThemeToChannelItemWindow(@NonNull final View view
            , @NonNull final BaseActivity activity) {
        final ConfigLoader configLoader = new ConfigLoader(context);
        final Set<Theme> themes = configLoader.getThemes();

        if (null != themes) {

            Theme currentTheme = null;
            for (final Theme theme : themes) {
                if (theme.getThemeName().equals(getCurrentThemeName())) {
                    currentTheme = theme;
                }
            }

            if (null != currentTheme) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    final Window window = activity.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(currentTheme.getColorPrimaryDark());
                }

                final AppBarLayout appBarLayout
                        = (AppBarLayout) activity.findViewById(R.id.activity_main_app_bar);
                appBarLayout.setBackgroundColor(currentTheme.getColorPrimary());

                final Toolbar toolbar = (Toolbar) activity.findViewById(R.id.activity_main_toolbar);
                toolbar.setTitleTextColor(currentTheme.getTextColorPrimary());

                final RecyclerView recyclerView = view.findViewById(R.id.fragment_channel_item_recycler_view);
                recyclerView.setBackgroundColor(currentTheme.getContentBackground());
            }
        }
    }

    public void applyThemeToSettingsWindow(@NonNull final BaseActivity activity) {
        final ConfigLoader configLoader = new ConfigLoader(context);
        final Set<Theme> themes = configLoader.getThemes();

        if (null != themes) {

            Theme currentTheme = null;
            for (final Theme theme : themes) {
                if (theme.getThemeName().equals(getCurrentThemeName())) {
                    currentTheme = theme;
                }
            }

            if (null != currentTheme) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    final Window window = activity.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(currentTheme.getColorPrimaryDark());
                }

                final AppBarLayout appBarLayout
                        = (AppBarLayout) activity.findViewById(R.id.activity_main_app_bar);
                appBarLayout.setBackgroundColor(currentTheme.getColorPrimary());

                final Toolbar toolbar = (Toolbar) activity.findViewById(R.id.activity_main_toolbar);
                toolbar.setTitleTextColor(currentTheme.getTextColorPrimary());
            }
        }
    }
}
