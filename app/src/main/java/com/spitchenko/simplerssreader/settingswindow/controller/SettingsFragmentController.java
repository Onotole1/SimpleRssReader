package com.spitchenko.simplerssreader.settingswindow.controller;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.spitchenko.simplerssreader.R;
import com.spitchenko.simplerssreader.base.controller.AlarmController;
import com.spitchenko.simplerssreader.base.view.BaseActivity;
import com.spitchenko.simplerssreader.model.Theme;
import com.spitchenko.simplerssreader.utils.ConfigLoader;
import com.spitchenko.simplerssreader.utils.Constants;
import com.spitchenko.simplerssreader.utils.ThemeController;

import java.util.Set;

import lombok.NonNull;

/**
 * Date: 03.04.17
 * Time: 16:15
 *
 * @author anatoliy
 */
public class SettingsFragmentController {
    private final PreferenceFragment fragment;

    public SettingsFragmentController(@NonNull final PreferenceFragment fragment) {
        this.fragment = fragment;
    }

    public void updateOnCreate() {
        fragment.setHasOptionsMenu(true);

        fragment.addPreferencesFromResource(R.xml.settings);

        final BaseActivity activity = (BaseActivity) fragment.getActivity();

        final AlarmController alarmController = new AlarmController(activity);

        final ListPreference timeList = (ListPreference) fragment.findPreference(activity.getResources()
                .getString(R.string.settings_fragment_notifications_list));

        alarmController.saveTimeSecondsToPreferences(Integer.parseInt(
                timeList.getValue()), activity);

        timeList.setSummary(timeList.getEntry());

        timeList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull final Preference preference
                    , @NonNull final Object newValue) {
                final int index = timeList.findIndexOfValue(newValue.toString());
                if (index != -1) {
                    alarmController.saveTimeSecondsToPreferences(Integer.parseInt(newValue
                                    .toString())
                            , activity);
                    alarmController.restartAlarm();
                    timeList.setSummary(timeList.getEntries()[index]);
                    timeList.setValueIndex(index);
                }
                return false;
            }
        });

        final Preference timeCheckBox = fragment.findPreference(fragment.getResources()
                .getString(R.string.settings_fragment_notification_checkbox));

        timeCheckBox.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull final Preference preference) {
                final SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(activity);
                alarmController.saveTimeSecondsToPreferences(Integer.parseInt(timeList.getValue())
                        , activity);
                if (!sharedPreferences.getBoolean(fragment.getActivity().getResources()
                        .getString(R.string.settings_fragment_notification_checkbox), false)) {
                    alarmController.stopAlarm();
                } else {
                    alarmController.startAlarm();
                }
                return true;
            }
        });

        final ListPreference themeList = (ListPreference) fragment.findPreference(fragment
                .getActivity().getResources().getString(R.string.settings_fragment_theme_list));

        setThemeListPreferenceData(themeList);

        themeList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull final Preference preference
                    , @NonNull final Object newValue) {

                final String theme = newValue.toString();

                final String previousValue = themeList.getValue();

                if (null != previousValue && !previousValue.equals(theme)) {
                    final int index = themeList.findIndexOfValue(theme);
                    if (index != -1) {
                        themeList.setSummary(themeList.getEntries()[index]);
                        themeList.setValueIndex(index);
                        final ThemeController themeController = new ThemeController(activity);
                        themeController.setCurrentTheme(theme);
                        activity.setTheme();
                    }
                }
                return false;
            }
        });
    }

    private void setThemeListPreferenceData(final ListPreference listPreference) {
        final ConfigLoader configLoader = new ConfigLoader(fragment.getActivity());

        final Set<Theme> themes = configLoader.getThemes();

        final String[] themeNames = new String[themes.size()];

        int i = 0;
        for (final Theme theme:themes) {
            themeNames[i] = theme.getThemeName();
            i++;
        }

        listPreference.setEntries(themeNames);
        listPreference.setDefaultValue(Constants.MAIN_THEME_NAME);
        listPreference.setEntryValues(themeNames);

        if (null == listPreference.getValue()) {
            listPreference.setValue(Constants.MAIN_THEME_NAME);
            listPreference.setSummary(Constants.MAIN_THEME_NAME);
        } else {
            listPreference.setSummary(listPreference.getValue());
        }
    }

    private void initToolbar() {
        final android.support.v7.widget.Toolbar toolbar
                = fragment.getActivity()
                .findViewById(R.id.activity_main_toolbar);

        toolbar.setTitle(fragment.getString(R.string.settings_activity_title));

        final AppCompatActivity activity = (AppCompatActivity) fragment.getActivity();
        activity.setSupportActionBar(toolbar);
        if (null != activity.getSupportActionBar()) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private int findIndexOfEntry(final ListPreference listPreference) {
        for (int i = 0; i < listPreference.getEntries().length; i++) {
            if (listPreference.getEntries()[i].toString().equals(listPreference.getEntry())) {
                return i;
            }
        }

        return -1;
    }

    public void updateOnCreateView() {
        initToolbar();
    }

    public void updateOnCreateOptionsMenu(final Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
    }
}
