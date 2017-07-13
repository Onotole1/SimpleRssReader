package com.spitchenko.simplerssreader.channelwindow.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.spitchenko.simplerssreader.R;
import com.spitchenko.simplerssreader.base.controller.ChannelRecyclerEmptyAdapter;
import com.spitchenko.simplerssreader.base.controller.ChannelRefreshDialog;
import com.spitchenko.simplerssreader.base.controller.NetworkDialogShowController;
import com.spitchenko.simplerssreader.base.controller.NoInternetDialog;
import com.spitchenko.simplerssreader.base.controller.UpdateController;
import com.spitchenko.simplerssreader.channelwindow.view.ChannelFragment;
import com.spitchenko.simplerssreader.model.Channel;
import com.spitchenko.simplerssreader.observer.FragmentAndBroadcastObserver;

import java.util.ArrayList;

import lombok.NonNull;

/**
 * Date: 31.03.17
 * Time: 19:40
 *
 * @author anatoliy
 */
public final class ChannelFragmentAndBroadcastObserver implements FragmentAndBroadcastObserver {
    private final static String CHANNEL_BROADCAST_OBSERVER
            = "com.spitchenko.focusstart.controller.channel_window."
            + "ChannelFragmentAndBroadcastObserver";
    private final static String RECYCLER_STATE = CHANNEL_BROADCAST_OBSERVER + ".recyclerState";
    private final static String UPDATE = CHANNEL_BROADCAST_OBSERVER + ".update";
    private final static String REFRESHING = CHANNEL_BROADCAST_OBSERVER + ".refreshing";
    private final static String IS_ADD_DIALOG_INTENT_RUN
            = CHANNEL_BROADCAST_OBSERVER + ".isAddDialog";

    private final ChannelBroadcastReceiver channelBroadcastReceiver = new ChannelBroadcastReceiver();
    private LocalBroadcastManager localBroadcastManager;
    private final Fragment fragment;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Parcelable recyclerState;
    private boolean isUpdate;
    private boolean isAddDialogIntentRun;
    private final ArrayList<Channel> receivedChannels = new ArrayList<>();
    private UpdateController updateController;
    private NetworkDialogShowController networkDialogShowController;
    private LinearLayout stubLayout;
    private ProgressBar progressBar;

    public ChannelFragmentAndBroadcastObserver(final Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void updateOnCreateView(final View view) {
        initToolbar();
        initFabAndRecycler(view);
        initItemTouchHelper();
        initOtherViewsAndControllers(view);
    }

    @Override
    public void updateOnCreate(@Nullable final Bundle savedInstanceState) {
        final Bundle fragmentArguments = fragment.getArguments();
        if (null != fragmentArguments) {
            final String action = fragmentArguments
                    .getString(ChannelFragment.getChannelFragmentNotificationKey());
            if (null != action && action
                    .equals(ChannelFragment.getChannelFragmentNotificationKey())) {
                isUpdate = true;
            }
        }
    }

    private void initToolbar() {
        final AppCompatActivity activity = (AppCompatActivity) fragment.getActivity();

        final Toolbar toolbar = (Toolbar) activity.findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle(R.string.app_name);
        if (null != activity.getSupportActionBar()) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        activity.setSupportActionBar(toolbar);
    }

    private void initFabAndRecycler(final View view) {
        final FloatingActionButton fab = (FloatingActionButton) view
                .findViewById(R.id.fragment_channel_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                showAddChannelDialog(null);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_channel_recycler_view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy){
                if (dy > 0 ||dy<0 && fab.isShown()) {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void initOtherViewsAndControllers(final View view) {
        swipeRefreshLayout
                = (SwipeRefreshLayout) view.findViewById(R.id.fragment_channel_swipe_refresh_layout);


        stubLayout = (LinearLayout) view.findViewById(R.id.fragment_channel_stub);
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_channel_progressBar);

        final AppCompatActivity activity = (AppCompatActivity) fragment.getActivity();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RssChannelIntentService.start(RssChannelIntentService.getRefreshAllChannelsKey()
                        , activity, null, null);
            }
        });

        updateController = new UpdateController(activity);
        updateController.turnOnUpdate();

        networkDialogShowController = new NetworkDialogShowController(activity);
    }

    private void initItemTouchHelper() {
        final ItemTouchHelper.SimpleCallback simpleItemTouchCallback
                = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder
                    , final RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int swipeDir) {
                final ChannelRecyclerAdapter channelAdapter
                        = (ChannelRecyclerAdapter) recyclerView.getAdapter();
                final Channel channelFromRecycler
                        = channelAdapter.getChannels().get(viewHolder.getAdapterPosition());
                channelAdapter.removeItem(viewHolder.getAdapterPosition());

                RssChannelIntentService.start(RssChannelIntentService.getRemoveChannelKey()
                        , fragment.getActivity(), channelFromRecycler, null);
            }
        };

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void updateOnResume() {
        final AppCompatActivity activity = (AppCompatActivity) fragment.getActivity();

        if (activity.getIntent().getAction().equals(Intent.ACTION_VIEW) && !isAddDialogIntentRun) {
            isAddDialogIntentRun = true;
            showAddChannelDialog(activity.getIntent().getData().toString());
        }

        subscribe();

        if (updateController.isUpdate()) {

            RssChannelIntentService.start(RssChannelIntentService.getReadChannelsKey(), activity, null
                    , null);

            updateController.turnOffUpdate();
        }
    }

    private void subscribe() {
        localBroadcastManager = LocalBroadcastManager.getInstance(fragment.getActivity());
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ChannelBroadcastReceiver.getReceiveChannelsKey());
        intentFilter.addAction(ChannelBroadcastReceiver.getRefreshDialogKey());
        intentFilter.addAction(ChannelBroadcastReceiver.getRemoveAction());
        intentFilter.addAction(ChannelBroadcastReceiver.getIoExceptionAction());
        intentFilter.addAction(ChannelBroadcastReceiver.getNoInternetAction());
        intentFilter.addAction(ChannelBroadcastReceiver.getLoadingAction());
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        localBroadcastManager.registerReceiver(channelBroadcastReceiver, intentFilter);

        channelBroadcastReceiver.addObserver(this);
    }

    @Override
    public void updateOnPause() {
        unSubscribe();
    }

    private void unSubscribe() {
        if (null != localBroadcastManager) {
            localBroadcastManager.unregisterReceiver(channelBroadcastReceiver);
            channelBroadcastReceiver.removeObserver(this);
        }
        isUpdate = false;
    }

    @Override
    public void updateOnReceiveItems(@NonNull final ArrayList<?> items
            , @Nullable final String action) {
        receivedChannels.clear();
        receivedChannels.addAll(convertArrayListToChannelList(items));
        if (null == recyclerView.getLayoutManager()) {
            final RecyclerView.LayoutManager layoutManager
                    = new LinearLayoutManager(fragment.getActivity());
            recyclerView.setLayoutManager(layoutManager);
        }
        if (!receivedChannels.isEmpty()) {
            stopLoading();
            final ChannelRecyclerAdapter channelAdapter
                    = new ChannelRecyclerAdapter(receivedChannels);
            recyclerView.setAdapter(channelAdapter);

            if (null != recyclerState) {
                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerState);
            }
            if (isUpdate) {
                checkUpdates(receivedChannels);
            }

        } else {
            stopLoading();
            recyclerView.setAdapter(new ChannelRecyclerEmptyAdapter());
        }
        if (null != action) {
            if (action.equals(ChannelBroadcastReceiver.getNoInternetAction())) {
                stopLoading();
                showNetworkDialog();
            } else if (action.equals(ChannelBroadcastReceiver.getIoExceptionAction())) {
                stopLoading();
                final String toastContent
                        = fragment.getResources()
                        .getString(R.string.activity_channel_controller_io_exception);
                Toast.makeText(fragment.getActivity(), toastContent, Toast.LENGTH_LONG).show();
            } else if (action.equals(ChannelBroadcastReceiver.getLoadingAction())) {
                startLoading();
            }
        }
        swipeRefreshLayout.setRefreshing(false);
        updateController.turnOffUpdate();
    }

    private void startLoading() {
        stubLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        stubLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void checkUpdates(@NonNull final ArrayList<Channel> receivedChannels) {
        final SharedPreferences preferences
                = RssChannelIntentService.getReadMessagesPreferences(fragment.getActivity());
        for (int i = 0, size = receivedChannels.size(); i < size; i++) {
            final Channel channel = receivedChannels.get(i);
            final String message = preferences.getString(channel.getLink(), null);
            if (null != message) {
                showRefreshDialog(channel.getLink(), message);
            }
        }
    }

    void updateOnReceiveNotification() {
        checkUpdates(receivedChannels);
    }

    @Override
    public void updateOnRestoreInstanceState(@Nullable final Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            recyclerState = savedInstanceState.getParcelable(RECYCLER_STATE);
            if (savedInstanceState.getBoolean(UPDATE)) {
                swipeRefreshLayout.setRefreshing(true);
            }
            if (savedInstanceState.getBoolean(REFRESHING)) {
                startLoading();
            }
            if (savedInstanceState.getBoolean(IS_ADD_DIALOG_INTENT_RUN)) {
                isAddDialogIntentRun = true;
            }
        }
    }

    @Override
    public void updateOnSavedInstanceState(@NonNull final Bundle outState) {
        if (null != outState) {
            if (null != recyclerView && null != recyclerView.getLayoutManager()) {
                outState.putParcelable(RECYCLER_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
                outState.putBoolean(UPDATE, swipeRefreshLayout.isRefreshing());
            }
            if (null != progressBar && progressBar.isShown()) {
                outState.putBoolean(REFRESHING, true);
            }
            outState.putBoolean(IS_ADD_DIALOG_INTENT_RUN, isAddDialogIntentRun);
        }
    }

    private ArrayList<Channel> convertArrayListToChannelList(@NonNull final ArrayList<?> list) {
        final ArrayList<Channel> result = new ArrayList<>();
        for (int i = 0, size = list.size(); i < size; i++) {
            final Object object = list.get(i);
            if (object instanceof Channel) {
                result.add((Channel) object);
            }
        }
        return result;
    }

    private void showAddChannelDialog(@Nullable final String url) {
        final ChannelAddDialogFragment dialogFragment = new ChannelAddDialogFragment();
        if (null != url) {
            final Bundle addDialogBundle = new Bundle();
            addDialogBundle.putString(ChannelAddDialogFragment.getChannelUrlKey(), url);
            dialogFragment.setArguments(addDialogBundle);
        }
        final android.app.FragmentTransaction fragmentTransaction = fragment.getFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(dialogFragment, ChannelAddDialogFragment.getDialogFragmentTag());
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void showNetworkDialog() {
        if (!networkDialogShowController.isNetworkDialogShow()) {
            final NoInternetDialog noInternetDialog = new NoInternetDialog();
            final FragmentManager fragmentManager = fragment.getFragmentManager();
            final android.app.FragmentTransaction fragmentTransaction
                    = fragmentManager.beginTransaction();
            fragmentTransaction.add(noInternetDialog, NoInternetDialog.getNoInternetDialogKey());
            fragmentTransaction.commitAllowingStateLoss();
            networkDialogShowController.turnOnNetworkDialog();
        }
    }

    private void showRefreshDialog(@NonNull final String url, @NonNull final String message) {
        final SharedPreferences preferences
                = RssChannelIntentService.getReadMessagesPreferences(fragment.getActivity());
        final SharedPreferences.Editor edit = preferences.edit();
        edit.putString(url, null);
        edit.apply();

        final ChannelRefreshDialog channelRefreshDialog = new ChannelRefreshDialog();
        final Bundle refreshBundle = new Bundle();
        refreshBundle.putString(ChannelRefreshDialog.getChannelUrlKey(), url);
        refreshBundle.putString(ChannelRefreshDialog.getMessageKey(), message);
        channelRefreshDialog.setArguments(refreshBundle);
        final android.app.FragmentTransaction fragmentTransaction
                = fragment.getFragmentManager().beginTransaction();
        fragmentTransaction.add(channelRefreshDialog, ChannelAddDialogFragment.getDialogFragmentTag());
        fragmentTransaction.commitAllowingStateLoss();
    }

}
