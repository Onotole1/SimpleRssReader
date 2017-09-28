package com.spitchenko.simplerssreader.repository;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.spitchenko.simplerssreader.database.AppDatabase;
import com.spitchenko.simplerssreader.database.dao.RssDao;
import com.spitchenko.simplerssreader.model.AbstractFeed;
import com.spitchenko.simplerssreader.model.rss.Channel;

import java.util.ArrayList;
import java.util.Arrays;

import lombok.NonNull;

/**
 * Date: 25.09.17
 * Time: 15:58
 *
 * @author anatoliy
 */

public class RssAtomLocalRepository {
    private static RssAtomLocalRepository ourInstance;
    private final RssDao rssDao;

    public static RssAtomLocalRepository getInstance(final Application application) {
        if (null == ourInstance) {
            ourInstance = new RssAtomLocalRepository(application);
        }
        return ourInstance;
    }

    private RssAtomLocalRepository(@NonNull final Application application) {
        final AppDatabase db = Room.databaseBuilder(application,
                AppDatabase.class, "rss-database").build();

        rssDao = db.getRssDao();
    }

    public ArrayList<AbstractFeed> getAllAbstractFeeds() {
        //Todo support atom
        final Channel[] allChannels = rssDao.loadAllChannels();

        for (final Channel channel:allChannels) {
            channel.setItem(new ArrayList<>(Arrays.asList(rssDao.loadChannelItemsByChannelLink(channel.getLink()))));
        }

        final ArrayList<AbstractFeed> abstractFeeds = new ArrayList<>(allChannels.length);

        if (allChannels.length != 0) {
            for (int i = 0, allChannelsLength = allChannels.length; i < allChannelsLength; i++) {
                final Channel channel = allChannels[i];
                abstractFeeds.add(new AbstractFeed(channel));
            }
        }

        return abstractFeeds;
    }
}
