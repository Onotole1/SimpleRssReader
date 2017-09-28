package com.spitchenko.simplerssreader.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.spitchenko.simplerssreader.model.atom.Entry;
import com.spitchenko.simplerssreader.model.atom.Feed;
import com.spitchenko.simplerssreader.model.rss.Channel;
import com.spitchenko.simplerssreader.model.rss.Item;

/**
 * Date: 24.09.2017
 * Time: 22:45
 *
 * @author Anatoliy
 */

@Dao
public interface RssDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChannel(Channel... channels);

    @Delete
    void deleteChannels(Channel... channels);

    @Update
    void updateChannels(Channel... channels);

    @Query("SELECT * FROM channel")
    Channel[] loadAllChannels();

    @Query("SELECT * FROM channel WHERE channel_link = :channelLink")
    Channel[] loadChannelByLink(String channelLink);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItems(Item... items);

    @Delete
    void deleteItems(Item... items);

    @Update
    void updateItems(Item... items);

    @Query("SELECT * FROM item WHERE channel_link = :channelLink")
    Item[] loadChannelItemsByChannelLink(String channelLink);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFeed(Feed... feeds);

    @Delete
    void deleteFeeds(Feed... feeds);

    @Update
    void updateFedds(Feed... feeds);

    @Query("SELECT * FROM feed")
    Feed[] loadAllFeeds();

    @Query("SELECT * FROM feed WHERE id = :channelLink")
    Channel[] loadFeedByLink(String channelLink);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntries(Entry... entries);

    @Delete
    void deleteEntries(Entry... entries);

    @Update
    void updateEntries(Entry... entries);

    @Query("SELECT * FROM item WHERE channel_link = :channelLink")
    Entry[] loadFeedEntriesByFeedLink(String channelLink);
}
