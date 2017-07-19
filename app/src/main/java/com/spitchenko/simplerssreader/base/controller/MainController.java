package com.spitchenko.simplerssreader.base.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.appsgeyser.sdk.AppsgeyserSDK;
import com.appsgeyser.sdk.ads.FullScreenBanner;
import com.appsgeyser.sdk.ads.IFullScreenBannerListener;
import com.appsgeyser.sdk.analytics.Analytics;
import com.spitchenko.simplerssreader.R;
import com.spitchenko.simplerssreader.base.view.BaseActivity;
import com.spitchenko.simplerssreader.channelitemwindow.view.ChannelItemFragment;
import com.spitchenko.simplerssreader.channelwindow.controller.ChannelAddDialogFragment;
import com.spitchenko.simplerssreader.channelwindow.controller.RssChannelIntentService;
import com.spitchenko.simplerssreader.channelwindow.view.ChannelFragment;
import com.spitchenko.simplerssreader.model.Channel;
import com.spitchenko.simplerssreader.settingswindow.view.SettingsFragment;
import com.spitchenko.simplerssreader.utils.ThemeController;

import java.util.ArrayList;

import lombok.NonNull;

/**
 * Date: 28.04.17
 * Time: 21:25
 *
 * @author anatoliy
 */
public class MainController {
    private final BaseActivity activity;

    public MainController(final BaseActivity activity) {
        this.activity = activity;
    }

    public void updateOnCreate(@Nullable final Bundle savedInstanceState) {
        activity.setContentView(R.layout.activity_main);

        if (null == savedInstanceState) {

            final String action = activity.getIntent().getAction();

            if (null != action && action.equals(BaseActivity
                    .getBaseActivityNotificationKey())) {
                setChannelFragment(ChannelFragment.getChannelFragmentNotificationKey());
            } else {
                setChannelFragment(null);
            }
        }

        AppsgeyserSDK.takeOff(activity.getApplication()
                , activity.getString(R.string.widgetID)
                , activity.getString(R.string.app_metrica_on_start_event)
                , activity.getString(R.string.template_version));

        final Analytics appsgeyserAnalytics = AppsgeyserSDK.getAnalytics();
        if (appsgeyserAnalytics != null) {
            appsgeyserAnalytics.activityStarted(activity);
        }

        activity.runOnUiThread(new Runnable() {
            public void run() {
                final FullScreenBanner fullScreenBanner =  AppsgeyserSDK.getFullScreenBanner(activity);
                fullScreenBanner.load();
                fullScreenBanner.setListener(new IFullScreenBannerListener() {
                    @Override
                    public void onLoadStarted() {
                        Log.d("Fullscreen", "started");
                    }

                    @Override
                    public void onLoadFinished() {
                        fullScreenBanner.show();
                    }

                    @Override
                    public void onAdFailedToLoad() {
                        Log.e("Fullscreen", "failed");
                    }

                    @Override
                    public void onAdHided() {
                        // called when ad was clicked or closed
                    }

                });
            }
        });

        AppsgeyserSDK.enablePush(BaseActivity.class
                , activity.getString(R.string.app_name)
                , activity.getApplication());

        AppsgeyserSDK.showPermissionDialog(activity);

        updateOnSetTheme();
    }

    public void updateOnSetChannelItemFragment(final Channel channel) {
        final FragmentManager manager = activity.getFragmentManager();
        final FragmentTransaction fragmentTransaction = manager.beginTransaction();

        final ChannelItemFragment fragment = new ChannelItemFragment();
        final Bundle arguments = new Bundle();
        arguments.putParcelable(ChannelItemFragment.getChannelUrlKey(), channel);
        fragment.setArguments(arguments);

        fragmentTransaction.replace(R.id.activity_main_container, fragment
                , ChannelItemFragment.getChannelUrlKey());

        fragmentTransaction.addToBackStack(ChannelItemFragment.getChannelItemFragmentKey());

        fragmentTransaction.commit();

        updateOnSetTheme();
    }

    public void updateOnSupportNavigateUp() {
        final FragmentManager manager = activity.getFragmentManager();
        manager.popBackStackImmediate();

        updateOnSetTheme();
    }

    private void setChannelFragment(@Nullable final String action) {
        final FragmentManager manager = activity.getFragmentManager();
        final FragmentTransaction fragmentTransaction = manager.beginTransaction();

        final ChannelFragment channelFragment = new ChannelFragment();

        if (null != action) {
            final Bundle arguments = new Bundle();
            arguments.putString(ChannelFragment.getChannelFragmentNotificationKey(), action);
            channelFragment.setArguments(arguments);
        }

        fragmentTransaction.add(R.id.activity_main_container, channelFragment
                , ChannelFragment.getChannelFragmentKey());
        fragmentTransaction.commit();

        updateOnSetTheme();
    }

    public void updateOnSetSettingsFragment() {

        final FragmentManager manager = activity.getFragmentManager();
        final FragmentTransaction fragmentTransaction = manager.beginTransaction();

        final SettingsFragment fragment = new SettingsFragment();

        fragmentTransaction.replace(R.id.activity_main_container, fragment
                , SettingsFragment.getSettingsFragmentKey());

        fragmentTransaction.addToBackStack(SettingsFragment.getSettingsFragmentKey());

        fragmentTransaction.commit();

        updateOnSetTheme();
    }

    public void updateOnSetTheme() {

        final ThemeController themeController = new ThemeController(activity);

        final FragmentManager manager = activity.getFragmentManager();
        final Fragment channelFragment
                = manager.findFragmentByTag(ChannelFragment.getChannelFragmentKey());

        if (null != channelFragment && null != channelFragment.getView()) {
            themeController.applyThemeToChannelWindow(channelFragment.getView(), activity);
        }

        final Fragment channelItemFragment
                = manager.findFragmentByTag(ChannelItemFragment.getChannelItemFragmentKey());

        if (null != channelItemFragment && null != channelItemFragment.getView()) {
            themeController.applyThemeToChannelItemWindow(channelItemFragment.getView(), activity);
        }

        final Fragment settingsFragment
                = manager.findFragmentByTag(SettingsFragment.getSettingsFragmentKey());

        if (null != settingsFragment) {
            themeController.applyThemeToSettingsWindow(activity);
        }

        /*final Intent activityIntent = activity.getIntent();
        activity.finish();
        activity.startActivity(activityIntent);*/
    }

    public void updateOnNewIntent(final Intent intent) {
        final String action = intent.getAction();
        if (null != action && action.equals(BaseActivity.getBaseActivityNotificationKey())) {
            final ArrayList<String> channelsUrls
                    = intent.getStringArrayListExtra(BaseActivity.getBaseActivityNotificationKey());

            checkUpdates(channelsUrls);
        }
    }

    private void checkUpdates(@NonNull final ArrayList<String> receivedChannels) {
        final SharedPreferences preferences
                = RssChannelIntentService.getReadMessagesPreferences(activity);
        for (int i = 0, size = receivedChannels.size(); i < size; i++) {
            final String channelUrl = receivedChannels.get(i);
            final String message = preferences.getString(channelUrl, null);
            if (null != message) {
                showRefreshDialog(channelUrl, message);
            }
        }
    }

    private void showRefreshDialog(@NonNull final String url, @NonNull final String message) {
        final SharedPreferences preferences
                = RssChannelIntentService.getReadMessagesPreferences(activity);
        final SharedPreferences.Editor edit = preferences.edit();
        edit.putString(url, null);
        edit.apply();

        final ChannelRefreshDialog channelRefreshDialog = new ChannelRefreshDialog();
        final Bundle refreshBundle = new Bundle();
        refreshBundle.putString(ChannelRefreshDialog.getChannelUrlKey(), url);
        refreshBundle.putString(ChannelRefreshDialog.getMessageKey(), message);
        channelRefreshDialog.setArguments(refreshBundle);
        final android.app.FragmentTransaction fragmentTransaction
                = activity.getFragmentManager().beginTransaction();
        fragmentTransaction.add(channelRefreshDialog, ChannelAddDialogFragment.getDialogFragmentTag());
        fragmentTransaction.commitAllowingStateLoss();
    }
}
