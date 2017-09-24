package com.spitchenko.simplerssreader.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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
    public void insertChannel(Channel... channels);

    @Delete
    public void deleteChannels(Channel... channels);

    @Update
    public void updateChannels(Channel... channels);

    @Query("SELECT * FROM channel")
    public Channel[] loadAllChannels();

    @Query("SELECT * FROM channel WHERE channel_link = :channelLink")
    public Channel[] loadChannelByLink(String channelLink);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertItems(Item... items);

    @Delete
    public void deleteItems(Item... items);

    @Update
    public void updateItems(Item... items);

    @Query("SELECT * FROM item WHERE channel_link = :channelLink")
    public Item[] loadChannelItemsByChannelLink(String channelLink);
}
