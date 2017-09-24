package com.spitchenko.simplerssreader.base.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.spitchenko.simplerssreader.R;
import com.spitchenko.simplerssreader.base.view.BaseActivity;
import com.spitchenko.simplerssreader.channelwindow.controller.ChannelAddDialogFragment;
import com.spitchenko.simplerssreader.channelwindow.controller.RssChannelIntentService;
import com.spitchenko.simplerssreader.channelwindow.view.ChannelFragment;
import com.spitchenko.simplerssreader.model.rss.Rss;
import com.spitchenko.simplerssreader.settingswindow.view.SettingsFragment;
import com.spitchenko.simplerssreader.utils.ThemeController;
import com.spitchenko.simplerssreader.utils.logger.LogCatHandler;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.Cleanup;
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

        /*AppsgeyserSDK.takeOff(activity
                , activity.getString(R.string.widgetID)
                , activity.getString(R.string.app_metrica_on_start_event)
                , activity.getString(R.string.template_version));

        AppsgeyserSDK.getFullScreenBanner(activity)
                .load(com.appsgeyser.sdk.configuration.Constants.BannerLoadTags.ON_START);*/

        updateOnSetTheme();

        Single.create(new SingleOnSubscribe<Rss>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull final SingleEmitter<Rss> e) throws Exception {

                final URL url = new URL("https://martinfowler.com/feed.atom");

                @Cleanup
                final InputStream inputStream = getInputStream(url);

                if (null == inputStream) {
                    throw new NullPointerException();
                }

                @Cleanup
                final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                final XmlParserCreator parserCreator = new XmlParserCreator() {
                    @Override
                    public XmlPullParser createParser() {
                        try {
                            return XmlPullParserFactory.newInstance().newPullParser();
                        } catch (final Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                };

                final GsonXml gsonXml = new GsonXmlBuilder()
                        .setSameNameLists(true)
                        .setXmlParserCreator(parserCreator)
                        .create();

                final Rss rssModel = gsonXml.fromXml(inputStreamReader, Rss.class);

                if (null != rssModel.getChannel()) {
                    e.onSuccess(rssModel);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Rss>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull final Disposable d) {
                System.out.println();
            }

            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull final Rss rss) {
                System.out.println();
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull final Throwable e) {
                System.out.println();
            }
        });


    }

    private InputStream getInputStream(@NonNull final URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (final IOException e) {
            LogCatHandler.publishInfoRecord(e.getMessage());
            return null;
        }
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
